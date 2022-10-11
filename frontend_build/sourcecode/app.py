import json
import socket

import requests
from flask import Flask, render_template, request, redirect, url_for, flash

url = "http://backend:8081"

app = Flask(__name__)
app.secret_key = 'some_secret'


# 메인 화면
@app.route('/')
def main():
    return render_template("/index.html")


# 일기 작성 화면
@app.route('/write')
def write():
    return render_template("/writeDiary.html")


# 일기 업로드
@app.route('/', methods=['post'])
def save():
    # 전달된 값 처리 후 백앤드 서버로 전송
    result = request.form
    diary = {
        'title': result['title'],
        'content': result['content'],
        'writer': result['writer'],
        'password': result['password'],
        'status': result['status']
    }

    # 백엔드 서버로 전송
    test11 = requests.post(url + '/', json=diary)
    mySeq = json.loads(test11.text)
    seq = mySeq['data']['seq']
    return redirect('/' + str(seq))


# 일기 전체 목록 로드
@app.route('/list')
def list():
    sort = request.args['sort']
    if sort == 'seq':
        lists = requests.get(url + '/list?sort=seq').json()
    else:
        lists = requests.get(url + '/list?sort=time').json()
    return render_template('/list.html', lists=lists['data']['results'], sort=sort)


# 특정한 일련번호 일기 로드
@app.route('/<seq>')
def detail(seq):
    diarylist = requests.get(url + '/' + seq).json()
    # frontHost = socket.gethostname()
    print(socket.gethostname())
    return render_template("/detailView.html",
                           seq=seq,
                           diary=diarylist['data']['result'])


# 일기 삭제
@app.route('/del/<seq>', methods=['post'])
def delDiary(seq):
    result = request.form
    password = {
        'password': result['password']
    }

    # # 백엔드 서버로 전송
    delResult = requests.delete(url + '/' + seq, json=password).json()
    httpStatus = delResult['httpStatus']
    print(httpStatus)
    #
    # # list 화면으로 이동
    print(delResult)
    if httpStatus == 'OK':
        return redirect(url_for('list',sort='time'))
    else:
        msg = delResult['message']
        print(msg)
        diarylist = requests.get(url + '/' + seq).json()
        flash(msg)
        return render_template("/detailView.html",
                               seq=seq,
                               diary=diarylist['data']['result'])


# 일기 수정을 위한 유효성 검사
@app.route('/auth/<seq>', methods=['post'])
def authenticateDiary(seq):
    result = request.form
    password = {
        'password': result['password']
    }

    # 백엔드 서버로 전송
    authResult = requests.get(url + '/auth/' + seq, json=password).json()
    httpStatus = authResult['httpStatus']
    print(httpStatus)

    if httpStatus == 'OK':
        # 수정 창으로 redirect
        return redirect(url_for('updateDiary', seq=seq))
    else:
        # 모달 화면으로 다시 이동
        msg = authResult['message']
        print(msg)
        diarylist = requests.get(url + '/' + seq).json()
        flash(msg)
        return render_template("/detailView.html",
                               seq=seq,
                               diary=diarylist['data']['result'])


# 일기 수정 창
@app.route('/update/<seq>')
def updateDiaryView(seq):
    result = requests.get(url + '/' + seq).json()
    print(result)
    print(result['data'])
    return render_template("/updateDiary.html",
                           seq=seq,
                           diary=result['data']['result'])


# 일기 수정 내용 저장
@app.route('/update/<seq>', methods=['post'])
def updateDiary(seq):
    result = request.form
    diary = {
        'title': result['title'],
        'content': result['content'],
        'writer': result['writer'],
        'password': result['password'],
        'status': result['status']
    }
    print(diary['title'])
    print(diary['content'])
    updateResult = requests.put(url + '/' + seq, json=diary).json()
    return redirect(url_for('list',sort='time'))


# Node IP 출력
@app.route('/info')
def showInfo():
    result = requests.get(url + '/info').json()
    frontHost = socket.gethostname()
    frontIp = socket.gethostbyname(frontHost)
    return render_template("/showInfo.html",
                           backHost=result['data']['hostname'],
                           backIp=result['data']['hostaddress'],
                           frontHost=frontHost,
                           frontIp=frontIp)


# 모든 외부 접속을 허용함 (포트는 기본 5000)
if __name__ == "__main__":
    app.run(host='0.0.0.0', port=35001)
