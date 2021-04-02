from src.scripts.authentication.user import User
from src.scripts.authentication.sql_connection import SQLConnection
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
        login_successful = connection.login(username, password)
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
        if Auth.remove_dirs(username):
            connection = SQLConnection()
            connection.connect()
            dropped_user = connection.drop_user(username, password)
            connection.disconnect()
            return dropped_user
        else:
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
        info_user = connection.info_user(username, password)
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
            user = User(username, name, surname, password, email)
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
        user = User(username, name, surname, new_password, email)
        connection = SQLConnection(user)
        connection.connect()
        created_user = connection.edit_user(username, password, user)
        connection.disconnect()
        return created_user
