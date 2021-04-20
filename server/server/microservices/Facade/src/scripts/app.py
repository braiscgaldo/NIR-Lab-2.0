import sys
import threading
import requests
import datetime

sys.path.append('../../')
from src.scripts.data_info.read_data import DataInfo
from src.scripts.authentication.auth import Auth
from flask import Flask, request
from flask_login import LoginManager, login_user, current_user, logout_user
from flask_jwt_extended import JWTManager, create_access_token
from flask_cors import cross_origin, CORS

app = Flask(__name__)
app.config['FLASK_ENV'] = 'development'
app.config['HOST'] = '0.0.0.0'
app.config['BUNDLE_ERRORS'] = True
app.config['SECRET_KEY'] = '7110c8ae51a4b5af97be6534caef90e4bb9bdcb3380af008f90b23a5d1616bf319bc298105da20fe'

jwt = JWTManager(app)
CORS(app, support_credentials=True)

# login manager
login_manager = LoginManager(app)


# Parse petition
def parse_request():
    return request.get_json(force=True)


# Loads an user
@login_manager.user_loader
def load_user(username):
    return Auth.get_user_by_name(username)


@app.route('/login', methods=['POST'])
@cross_origin(supports_credentials=True)
def login():
    if current_user.is_authenticated:
        return {'message': 'Already authenticated'}
    data = parse_request()
    response = {'message': 'login failed'}
    if Auth.login(data['username'], data['password']):
        login_user(Auth.get_user(data['username'], data['password']), remember=True, duration=datetime.timedelta(days=0, minutes=15))
        return {'status': 'success', 'message': 'login successful', 'data': {'id': data['username'],
                'token': create_access_token(identity=data['username'])}}
    return response


# Delete a session
def logout(data):
    logout_user()
    return {'message': 'logged out'}, 200


@app.route('/create_user', methods=['PUT'])
@cross_origin()
def create_user():
    data = parse_request()
    if Auth.create_user(username=data['username_auth'], password=data['password_auth'], name=data['name_auth'],
                        surname=data['surname_auth'], email=data['email_auth']):
        return {'message': 'User ' + data['username_auth'] + ' created', 'token': 'KJAHSDC432H4RHKFE98CASDIJC'}, 200

    return {'message': 'Internal server error'}, 500


@app.route('/edit_user', methods=['POST'])
@cross_origin()
def edit_user():
    data = parse_request()
    if Auth.edit_user(username=data['username_auth'], password=data['password_auth'], name=data['name_auth'],
                      surname=data['surname_auth'], email=data['email_auth'], new_password=data['new_password_auth']):
        return {'message': 'User created', 'token': 'KJAHSDC432H4RHKFE98CASDIJC'}, 200

    return {'message': 'Not updated'}, 403


def drop_user(data):
    if Auth.drop_user(username=data['username_auth'], password=data['password_auth']):
        logout(None) # Automatically logout when dropping user
        return {'message': 'User ' + data['username_auth'] + ' dropped'}, 200

    return {'message': 'Internal server error'}, 500


@app.route('/edit_user', methods=['GET'])
@cross_origin()
def info_user():
    data = parse_request()
    info = Auth.info_user(username=data['username_auth'], password=data['password_auth'])
    if info:
        return info, 200

    return {'message': 'Internal server error'}, 500


@app.route('/login', methods=['GET'])
@cross_origin()
def list_users():
    users = Auth.list_users()
    if users:
        return {'message': 'users returned', 'users': users}, 200

    return {'message': 'No users in database'}, 200


@app.route('/list_files', methods=['GET'])
@cross_origin()
def list_files():
    files = DataInfo(request.args.get('username')).list_files()
    if files:
        return {'message': 'files returned', 'files': files}, 200

    return {'message': 'Internal server error'}, 500


def add_file(data):
    if DataInfo(data['username']).add_file(data['filedata'], data['path']):
        return {'message': 'Added file'}, 200

    return {'message', 'Internal server error'}, 500


@app.route('/delete_file', methods=['DELETE'])
def delete_file():
    if DataInfo(request.args.get('username')).delete_file(request.args.get('path')):
        return {'message': 'File ' + request.args.get('path') + ' deleted successfully'}, 200

    return {'message': 'Internal server error'}, 500


@app.route('/list_characteristics', methods=['GET'])
@cross_origin()
def list_characteristics():
    chars = DataInfo(request.args.get('username')).list_characteristics(request.args.get('filename'))
    if chars:
        return {'characteristics': chars}, 200

    return {'message': 'Internal server error'}, 500


def training(data):
    def train_back(**kwargs):
        requests.post('http://nirlab_training:5050/training', json=kwargs)

    thread = threading.Thread(target=train_back, kwargs=data)
    thread.start()

    return {"message": "Executing background task..."}, 202


def treat_data(data):
    def treat_data_back(**kwargs):
        requests.post('http://nirlab_data_treatment:5000/data_treatment', json=kwargs)

    data['username_sql'] = 'brais'
    data['password_sql_user'] = 'password1'
    data['host_sql'] = 'nirlab_database'
    data['database_name'] = 'nirlab'
    thread = threading.Thread(target=treat_data_back, kwargs=data)
    thread.start()

    return {"message": "Executing background task"}, 202


def treat_data_prediction(data):
    def treat_data_back(**kwargs):
        requests.post('http://nirlab_data_treatment:5000/data_treatment', json=kwargs)

    data['username_sql'] = 'brais'
    data['password_sql_user'] = 'password1'
    data['host_sql'] = 'nirlab_database'
    data['database_name'] = 'nirlab'
    thread = threading.Thread(target=treat_data_back, kwargs=data)
    thread.start()
    thread.join()

    return {"message": "Data treated"}, 200


@app.route('/download', methods=['GET'])
def get_file_content():
    file_content, file_type = DataInfo(request.args.get('username')).get_file_content(request.args.get('path'))
    if file_content is not None:
        return {'message': 'Obtained file', 'file_content': file_content, 'file_type': file_type}, 200

    return {'message', 'Internal server error'}, 500


def predict(data):
    result = requests.post('http://nirlab_training:5050/predict', json=data).content
    return {"message": "Predictions returned", "results": result.decode('utf-8')}, 200


# Only permitted when logged


operations = {
    'Logout': logout,
    'DataTreatment': treat_data,
    'DataTreatmentPrediction': treat_data_prediction,
    'Training': training,
    'ListFiles': list_files,
    'AddFile': add_file,
    'ListCharacteristics': list_characteristics,
    'DropUser': drop_user,
    'InfoUser': info_user,
    'Predict': predict
}


@app.route('/', methods=['GET', 'PUT', 'DELETE', 'POST'])
@cross_origin()
def facade_operation():
    data = parse_request()
    return operations[data['type']](data=data)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4000)
