from flask import Flask, render_template, jsonify, request
import requests

app = Flask(__name__)

# 메인 화면
@app.route('/')
def main():
    return render_template("/index.html")


# 일기 업로드
@app.route('/', methods=['post'])
def hi():
    # 전달된 값 처리 후 백앤드 서버로 전송
    result = request.form['title']
    test1 = requests.get('http://localhost:5000/send')

    # 백엔드 서버에 이동해 가공된 결과를 이용하여 출력
    return render_template("/pass.html", res = test1.json())

# 일기 전체 목록 날짜 내림차순으로 로드
@app.route('/list')
def test1():
    return {"key":"value"}

# 특정한 일련번호 일기 로드
@app.route('/<seq>')
def detail(seq):
    print(seq)
    return "/detailView.html"

# 일기 삭제


# 모든 외부 접속을 허용함 (포트는 기본 5000)
if __name__ == "__main__":
    app.run(host='0.0.0.0')

# 화면
# 1. 메인화면 - 전체 목록
# 2. 일기 작성 화면
# 3. 하나의 일기 내용 상세 보기 화면
# 삭제 버튼은 전체와 개별 화면 둘 다