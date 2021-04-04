from src.scripts.authentication.user import User
from src.scripts.authentication.sql_connection import SQLConnection
import hashlib
import os
import shutil


class Auth:
    """
    Class that represents the authentication
    """

    @staticmethod
    def create_dirs(username):
        """
        Method for creating correspondent directories
        :param: username: username for create directories
        :return: create_successful: if directories where correctly created
        """
        try:
            os.mkdir('/home/' + username)
            os.mkdir('/home/' + username + '/databases')
            os.mkdir('/home/' + username + '/databases_config')
            os.mkdir('/home/' + username + '/models')
            os.mkdir('/home/' + username + '/models_config')
        except:
            print('error creating directories')
            return False
        return True

    @staticmethod
    def remove_dirs(username):
        """
        Method for creating correspondent directories
        :param: username: username for create directories
        :return: create_successful: if directories where correctly created
        """
        try:
            shutil.rmtree('/home/' + username)
        except:
            print('error deleting directories')
            return False
        return True

    @staticmethod
    def login(username, password):
        """
        Method for realise a login
        :param: username: username of a database
        :param: password: password of the user
        :return login_successful
        """
        connection = SQLConnection()
        connection.connect()
        login_successful = connection.login(username, hashlib.sha256(password.encode('utf-8')).hexdigest())
        connection.disconnect()
        return login_successful

    @staticmethod
    def list_users():
        """
        Method for list all usernames
        :return users
        """
        connection = SQLConnection()
        connection.connect()
        users = connection.list_users()
        connection.disconnect()
        return users

    @staticmethod
    def drop_user(username, password):
        """
        Method for drop an user
        :param: username: username of a database
        :param: password: password of the user
        :return dropped_user
        """
        try:
            connection = SQLConnection()
            connection.connect()
            dropped_user = connection.drop_user(username, hashlib.sha256(password.encode('utf-8')).hexdigest())
            connection.disconnect()
            Auth.remove_dirs(username)
            return dropped_user
        except:
            return None

    @staticmethod
    def info_user(username, password):
        """
        Method for realise a login
        :param: username: username of a database
        :param: password: password of the user
        :return info_user
        """
        connection = SQLConnection()
        connection.connect()
        info_user = connection.info_user(username, hashlib.sha256(password.encode('utf-8')).hexdigest())
        connection.disconnect()
        return info_user

    @staticmethod
    def create_user(username, name, surname, password, email):
        """
        Method for realise a login
        :param: username: username of a database
        :param: password: password of the user
        :param: name: name of the user
        :param: surname: surname of the user
        :param: email: email of the user
        :return created_user
        """
        if Auth.create_dirs(username):
            user = User(username, name, surname, hashlib.sha256(password.encode('utf-8')).hexdigest(), email)
            connection = SQLConnection(user)
            connection.connect()
            created_user = connection.create_user()
            connection.disconnect()
            return created_user
        else:
            return None

    @staticmethod
    def edit_user(username, name, surname, password, email, new_password):
        """
        Method for realise a login
        :param: username: username of a database
        :param: password: password of the user
        :param: name: name of the user
        :param: surname: surname of the user
        :param: email: email of the user
        :param: new_password: new password for the user
        :return edited_user
        """
        user = User(username, name, surname, hashlib.sha256(new_password.encode('utf-8')).hexdigest(), email)
        connection = SQLConnection(user)
        connection.connect()
        created_user = connection.edit_user(username, hashlib.sha256(password.encode('utf-8')).hexdigest(), user)
        connection.disconnect()
        return created_user

    @staticmethod
    def get_user(username, password):
        """
        Method for obtain all info of an user
        :param: username: user name
        :param: password: password of the user
        return user
        """
        user = User(username=username, name=username, surname=username, password=password, email=username)
        connection = SQLConnection(user)
        connection.connect()
        user_info = connection.get_user(username, hashlib.sha256(password.encode('utf-8')).hexdigest())[0]
        connection.disconnect()
        return User(username, user_info[1], user_info[2], user_info[3], user_info[4])

    @staticmethod
    def get_user_by_name(username):
        """
        Method for obtain all info of an user
        :param: username: user name
        return user
        """
        user = User(username=username, name=username, surname=username, password=username, email=username)
        connection = SQLConnection(user)
        connection.connect()
        user_info = connection.get_user_by_name(username)[0]
        connection.disconnect()
        return User(username, user_info[1], user_info[2], user_info[3], user_info[4])
