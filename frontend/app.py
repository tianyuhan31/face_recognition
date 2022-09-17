from flask import Flask
from flask import request
import os
import requests

BACKEND_HOST = os.getenv('BACKEND_HOST')
SERVER_PORT = os.getenv('SERVER_PORT')
app = Flask(__name__, static_folder='static', static_url_path='/static')

@app.route('/')
def homepage():
    return app.send_static_file('index.html')

@app.route('/greeting', methods=['GET'])
def greeting():
    response = requests.get(BACKEND_HOST + '/greeting')
    return response.content

@app.route('/images', methods=['POST'])
def image():
    response = requests.post(BACKEND_HOST + '/images', data = request.data)
    return response.content

@app.route('/validate', methods=['POST'])
def validate():
    response = requests.post(BACKEND_HOST + '/validate', data = request.data)
    return response.content

if __name__ == '__main__': 
    #export FLASK_RUN_PORT=8000
    app.run(host="0.0.0.0", port = SERVER_PORT, debug=True)