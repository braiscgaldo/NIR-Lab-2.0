import sys
import threading
import requests

sys.path.append('../../')
from src.scripts.data_info.read_data import DataInfo
from src.scripts.authentication.auth import Auth
from flask import Flask, request

app = Flask(__name__)
app.config['FLASK_ENV'] = 'development'
app.config['HOST'] = '0.0.0.0'
app.config['BUNDLE_ERRORS'] = True


# Parse petition
def parse_request():
    return request.get_json(force=True)


# Creates a session
def login():
    pass


# Delete a session
def sign_out():
    pass


def create_user(data):
    if Auth.create_user(username=data['username_auth'], password=data['password_auth'], name=data['name_auth'],
                        surname=data['surname_auth'], email=data['email_auth']):
        print({'message': 'User ' + data['username_auth'] + ' created', 'token': 'KJAHSDC432H4RHKFE98CASDIJC'}, 200)
        return {'message': 'User ' + data['username_auth'] + ' created', 'token': 'KJAHSDC432H4RHKFE98CASDIJC'}, 200

    return {'message': 'Internal server error'}, 500


def edit_user(data):
    if Auth.edit_user(username=data['username_auth'], password=data['password_auth'], name=data['name_auth'],
                      surname=data['surname_auth'], email=data['email_auth'], new_password=data['new_password_auth']):
        return {'message': 'User created', 'token': 'KJAHSDC432H4RHKFE98CASDIJC'}, 200

    return {'message': 'Internal server error'}, 500


def drop_user(data):
    if Auth.drop_user(username=data['username_auth'], password=data['password_auth']):
        return {'message': 'User ' + data['username_auth'] + ' dropped'}, 200

    return {'message': 'Internal server error'}, 500


def info_user(data):
    info = Auth.info_user(username=data['username_auth'], password=data['password_auth'])
    if info:
        print(info)
        return info, 200

    return {'message': 'Internal server error'}, 500


def list_users(data):
    users = Auth.list_users()
    print({'users': users})
    if users:
        return {'message': 'users returned', 'users': users}, 200

    return {'message': 'No users in database'}, 200


def list_files(data):
    files = DataInfo(data['username']).list_files()
    print({'message': 'files returned', 'files': files})
    if files:
        return {'message': 'files returned', 'files': files}, 200

    return {'message': 'Internal server error'}, 500


def add_file(data):
    if DataInfo(data['username']).add_file(data['filename'], data['directory']):
        print(data['filename'])
        return {'message': 'Added file'}, 200

    return {'message', 'Internal server error'}, 500


def delete_file(data):
    if DataInfo(data['username']).delete_file(data['filename'], data['directory']):
        print(data['filename'])
        return {'message': 'File ' + data['filename'] + ' deleted successfully'}, 200

    return {'message': 'Internal server error'}, 500


def list_characteristics(data):
    chars = DataInfo(data['username']).list_characteristics(data['filename'])
    if chars:
        print(chars)
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

    thread = threading.Thread(target=treat_data_back, kwargs=data)
    thread.start()

    return {"message": "Executing background task..."}, 202


operations = {
    'DataTreatment': treat_data,
    'Training': training,
    'ListFiles': list_files,
    'AddFile': add_file,
    'DeleteFile': delete_file,
    'ListCharacteristics': list_characteristics,
    'Login': login,
    'CreateUser': create_user,
    'EditUser': edit_user,
    'DropUser': drop_user,
    'InfoUser': info_user,
    'ListUsers': list_users
}


@app.route('/', methods=['GET', 'PUT', 'DELETE', 'POST'])
def facade_operation():
    data = parse_request()
    return operations[data['type']](data=data)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=4000)
