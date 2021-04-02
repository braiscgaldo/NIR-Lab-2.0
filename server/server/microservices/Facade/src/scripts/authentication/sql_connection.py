import mysql.connector


class SQLConnection:
    """
    Class for connect to SQL database for authentication
    """

    def __init__(self, user='brais', database='nirlab', host='nirlab_database', name='nirlab', generic_user='root',
                 generic_password='password'):
        self.__user, self.__database, self.__host, self.__name = user, database, host, name
        self.__generic_user, self.__generic_password = generic_user, generic_password
        self._connection, self._cursor = None, None

    def connect(self):
        """
        Function to connect to users database
        :return: None - established connection
        """
        self._connection = mysql.connector.connect(user=self.__generic_user, password=self.__generic_password,
                                                   host=self.__host,
                                                   database=self.__database)
        self._cursor = self._connection.cursor(buffered=True)

    def disconnect(self):
        """
        Function to disconnect to users database
        :return: None - established connection
        """
        self._cursor.close()
        self._connection.disconnect()

    def __valid_user(self, username, password):
        """
        Method to validate an user (for security)
        :param: username: username of the db
        :param: password: password for the user
        :return: valid_user
        """
        valid_user_query = 'select username, password from USERS where username = \'' + username + \
                           '\' and password = ' + password
        self._cursor.execute(valid_user_query)
        if len(self._cursor.fetchall()) > 0:
            return True
        return False

    def login(self, username, password):
        """
        Method for login into system
        :param: username: username of the db
        :param: password: password for the user
        :return: valid_user
        """
        return self.__valid_user(username, password)

    def list_users(self):
        """
        Function use to list all users in database
        :return: users
        """
        obtain_users_query = 'select username from USERS'
        self._cursor.execute(obtain_users_query)
        return [user[0] for user in self._cursor.fetchall()]

    def drop_user(self, username, password):
        """
        Method for drop an user from system
        :param: username: username of the db
        :param: password: password for the user
        :return: userDropped
        """
        if self.__valid_user(username, password):
            drop_user_query = 'delete from USERS where username = \'' + username + '\' and password = ' + password
            try:
                self._cursor.execute(drop_user_query)
                self._connection.commit()
                return True
            except Exception as e:
                print(e)

        return False

    def create_user(self):
        """
        Function to create an user
        :return: created_user
        """
        if self.list_users() is None or self.__name not in self.list_users():
            add_user_query = 'insert into USERS (username, name, surname, password, email) values (\'' + \
                             self.__user.username + '\', \'' + self.__user.name + '\', \'' + self.__user.surname + \
                             '\', ' + self.__user.password + ', \'' + self.__user.email + '\')'
            try:
                self._cursor.execute(add_user_query)
                self._connection.commit()
                return True
            except Exception as error:
                print(error)
                return False
        return False

    def edit_user(self, username, password, new_user):
        """
        Method for edit the user
        :param: username: username
        :param: password: last password
        :param: new_user: the username edited
        :return: edited_user
        """
        if self.__valid_user(username, password):
            self.__user = new_user
            edit_user_query = 'update USERS SET name=\'' + self.__user.name + '\', surname=\'' + self.__user.surname + \
                              '\', password=' + self.__user.password + ', email=\'' + self.__user.email + \
                              '\' where username=\'' + username + '\' and password=' + password
            print(edit_user_query)
            self._cursor.execute(edit_user_query)
            self._connection.commit()
            return True
        return False

    def info_user(self, username, password):
        """
        Method for obtain the info of the user
        :param: username: username
        :param: password: password
        :return: user_info
        """
        info_user_query = 'select name, surname, email from USERS where username = \'' + username + \
                          '\' and password = ' + password
        self._cursor.execute(info_user_query)
        return self._cursor.fetchall()
