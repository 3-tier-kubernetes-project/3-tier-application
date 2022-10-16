# <img style="weight:40px;height:40px;margin-right:20px" src="https://user-images.githubusercontent.com/54930365/195153805-2460bddc-a0f1-49e7-8a43-df5ede83bac2.png"> 춘식 일기 <img style="weight:80px;height:80px" src="https://user-images.githubusercontent.com/54930365/195151501-5a62e682-7c91-40bf-91d3-a9d8c34583ed.png">

_카카오클라우드스쿨에서 진행한 외부 etcd 클러스터 아키텍처 기반의 3-Tier 쿠버네티스 어플리케이션 구축 프로젝트_   
<br>  

### 🔶 프로젝트 목적  
- __외부 etcd 클러스터 아키텍처 기반의 3-Tier 쿠버네티스 어플리케이션 구축해보기__    
- 쿠버네티스 아키텍처 이해하고 응용하기   
- 기존의 도커 기반의 3-Tier container Application 기능 및 성능 개선하기   
<br>  

### 🔶 프로젝트 주제   
- __"춘식 일기"__ 일기 어플리케이션  
- 기존에 소나기 팀이 진행했던 [도커 기반의 3-Tier container Application 프로젝트](https://github.com/KCS-S1-2nd-3team/dcompose-v2.git)를 개선  
<br>   

### 🔶 DEV 개선사항
- UI 개선
  - 사용자 편의를 위한 UI 개선
- 기능 개선  
  - 감정 표현 기능 추가 
  - 일기 수정 기능 추가 
  - 닉네임과 비밀번호 입력 받도록 개선   
  - 일기 수정, 삭제 시 비밀번호 인증 기능 추가 
  - 일기 목록 정렬 기능 추가  
  
    <img width="900" alt="스크린샷 2022-10-12 오전 2 39 07" src="https://user-images.githubusercontent.com/54930365/195161679-dcd246a7-cff0-4c3a-8dc8-4f1f3a52114c.png">
<br>  

### 🔶 프로젝트 진행 과정
__1. 쿠버네티스 서버 구축하기__
<img width="900" alt="쿠버네티스 서버 구성도" src="https://user-images.githubusercontent.com/54930365/196036305-435ac7f5-f77e-46f9-8f20-774fda86d878.png">  

1. image를 안전하게 공유하기 위한 private repository인 Nexus 구축  
   > - 방화벽을 해제하고 외부에서 접속 가능하도록 인바운드, 아웃바운드 규칙을 추가  
   > - http에서 접속 가능하도록 설정한 다음, docker run 명령어를 통해 nexus를 실행  
2. 프로젝트 환경 구성
   > - dns 파일에 노드의 hostname 등록, 방화벽과 swap 해제, 시간 동기화, ip_forward 설정을 수행
   > - docker 설치
3. 쿠버네티스를 설치
   > - 쿠버네티스 설치
   > - 쿠버네티스를 설치한 노드를 필요한 wortker node, loadbalancer, etcd의 개수만큼 복제
4. etcd cluster를 구축했습니다.
   > - etcd 노드에서 kubelet을 etcd에 대한 서비스 관리자로 구성
   > - 각 etcd host를 등록하고 etcd host에 대한 configuration을 진행
   > - etcd1에서 CA를 생성하고 각 etcd의 인증서를 생성. etcd1의 인증서 및 kubeadm 구성을 etcd2, etcd3에 복사
   > - 각 etcd host에서 정적 etcd pod를 생성
5. loadbalancer를 구축
   > - 로드밸런서 노드에 haproxy를 설치
6. master,etcd join with LB
   > - master1에서 kubeadm-config.yaml을 작성하고 init 실행
   > - master2,3를 LB에 조인
   > - worter node 1~5를 로드밸런서에 조인
   > - master1에서 calic를 통한 네트워크 연결을 수행
7. metric 수집을 위해 prometheus 설치
   > - kubernetes 패키지 매니저인 helm을 설치
   > - prometheus helm chart를 설치
   > - etcd metric을 수집하기 위한 etcd node의 ip 주소와 port 주소 등을 설정
   > - prometheus 배포 → prometheus 관련 pod와 service를 생성
8. 수집된 metric의 시각화를 위해 grafana 설치
   > - grafana helm chart를 설치
   > - grafana 접속을 위한 계정 및 서비스타입 등을 설정
   > - grafana 배포 → grafana 관련 pod와 서비스를 생성
   
   <br>  

__2. 3-Tier Application 이미지 배포__  
1. 3-Tier Application 기능 개선을 위한 개발 진행
   > - 위의 DEV 개선사항대로 개발 진행
2. 3-Tier Application의 이미지 build
   > - 작성된 Dockerfile을 이용하여 데이터베이스,백엔드,프론트엔드 image를 각각 build
3. 이미지 Tag 및 Push
   > - 빌드된 이미지들에 tag를 추가
   > - public/private repository에 이미지를 push

<br>  

__3. kubernetes object 생성__

<img width="900" alt="kubernetes object 구성도" src="https://user-images.githubusercontent.com/54930365/196035677-1e77f279-7b7b-4bc1-84d0-c17e84864d75.png">


1. database pod의 데이터 보존 및 마운트를 위한 `NFS volume` 설정
   > - NFS 서버 구축
   > - PV 오브젝트 생성
   >   ```yaml
   >   # db-pv.yaml
   >   apiVersion: v1
   >   kind: PersistentVolume
   >   metadata:
   >     name: db-pv
   >   spec:
   >     capacity:
   >       storage: 20Gi
   >     accessModes:
   >     - ReadWriteMany
   >     persistentVolumeReclaimPolicy: Retain
   >     nfs:
   >       path: /data_dir
   >       server: 192.168.56.111
   >   ```
   > - PVC 오브젝트 생성
   >   ```yaml
   >   # db-pvc.yaml  
   >   apiVersion: v1 
   >   kind: PersistentVolumeClaim 
   >   metadata:
   >     labels:
   >       service: db-pvc
   >     name: db-pvc
   >   spec:
   >     accessModes:
   >     - ReadWriteMany
   >     resources:
   >       requests:
   >         storage: 20Gi
   >   ```
2. database pod의 실행 및 관리를 위한 `deployment`, `service` manifest 파일 작성  
   >- 기밀 정보인 데이터베이스 비밀번호를 secret 오브젝트에 담아 환경변수 값으로 전달  
   >  `kubectl create secret generic database-pass --from-literal=password=qwerty1234`
   >- mariadb 데이터 경로와 nfs pvc를 마운트  
   >  ```yaml
   >  # database-deployment.yaml
   >  apiVersion: apps/v1
   >  kind: Deployment
   >  metadata:
   >    labels:
   >      service: database
   >    name: database
   >  spec:
   >    replicas: 1
   >    selector:
   >      matchLabels:
   >        service: database
   >    template:
   >      metadata:
   >        labels:
   >          service: database
   >      spec:
   >        containers:
   >        - env:
   >          - name: MYSQL_DATABASE
   >            value: DOCKERTEST
   >          - name: MYSQL_ROOT_PASSWORD
   >            valueFrom:
   >              secretKeyRef:
   >                name: database-pass
   >                key: password
   >          image: mariadb:10.9
   >          name: database
   >          ports:
   >          - containerPort: 3306
   >          volumeMounts:
   >            - mountPath: /var/lib/mysql
   >              name: db-pvc
   >        restartPolicy: Always
   >        volumes:
   >          - name: db-pvc
   >            persistentVolumeClaim:
   >              claimName: db-pvc
   >  ```
   >  ```yaml
   >  # database-service.yaml
   >  apiVersion: v1
   >  kind: Service
   >  metadata:
   >    labels:
   >      service: database
   >    name: database
   >  spec:
   >    ports:
   >    - name: "3306"
   >      port: 3306
   >      targetPort: 3306
   >    selector:
   >      service: database
   >  ```
3. backend pod의 실행 및 관리를 위한 `deployment`와 `service` manifest 파일 작성  
   > ```yaml
   > # backend-deployment.yaml
   > apiVersion: apps/v1
   > kind: Deployment
   > metadata:
   >   labels:
   >     service: backend
   >   name: backend
   > spec:
   >   replicas: 3
   >   selector:
   >     matchLabels:
   >       service: backend
   >   template:
   >     metadata:
   >       labels:
   >         service: backend
   >     spec:
   >       containers:
   >       - image: 2214yj/3-tier-application-backend:nolb
   >         imagePullPolicy: Always
   >         name: backend
   >         ports:
   >         - containerPort: 8081
   >       restartPolicy: Always
   > ```
   > ```yaml
   > # backend-service.yaml
   > apiVersion: v1
   > kind: Service
   > metadata:
   >   labels:
   >     service: backend
   >   name: backend
   > spec:
   >   ports:
   >   - name: "8081"
   >     port: 8081
   >     targetPort: 8081
   >   selector:
   >     service: backend
   > ```
4. frontend pod의 실행 및 관리를 위한 `deployment`와 `service` manifest 파일 작성  
   > ```yaml
   > # frontend-deployment.yaml
   > apiVersion: apps/v1
   > kind: Deployment
   > metadata:
   >   labels:
   >     service: frontend
   >   name: frontend
   > spec:
   >   replicas: 3
   >   selector:
   >     matchLabels:
   >       service: frontend
   >   template:
   >     metadata:
   >       labels:
   >         service: frontend
   >     spec:
   >       containers:
   >       - image: 2214yj/3-tier-application-frontend:nolb
   >         imagePullPolicy: Always
   >         name: frontend
   >         ports:
   >         - containerPort: 35001
   >       restartPolicy: Always
   > ```
   > ```yaml
   > # frontend-service.yaml
   > apiVersion: v1
   > kind: Service
   > metadata:
   >   labels:
   >     service: frontend
   >   name: frontend
   > spec:
   >   type: NodePort
   >   ports:
   >   - name: "80"
   >     port: 80
   >     targetPort: 35001
   >   selector:
   >     service: frontend
   > ```
5. `kubectl apply -f [manifest 파일명]` 명령어를 통해, 위에서 manifest file로 설정한 쿠버네티스 오브젝트들을 생성  

<br>  

### 🔶 팀원

| 서동주(팀장) |김예지|이기빈|이재원|
|:-------:|:---:|:---:|:---:|




