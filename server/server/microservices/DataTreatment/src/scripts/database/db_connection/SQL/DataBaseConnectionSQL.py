import sys

sys.path.append('../..')

from mysql.connector import MySQLConnection

from src.scripts.database.db_connection.DataBaseConnection import DataBaseConnection
import mysql.connector


class DataBaseConnectionSQL(DataBaseConnection):

    def __init__(self, username, password, host, uri_database):
        super().__init__(username, password, host, uri_database)
        self._connection = None

    def connect(self):
        """
        Method for connect to the database:
        @input:
            * None
        @return:
            * None
        """
        self._connection = mysql.connector.connect(user=self._username, password=self._password,
                                                   host=self._host,
                                                   database=self._uri_database)

    def disconnect(self):
        """
        Method for disconnect the connection with database. It must exist a connection, otherwise it will fail.
        @input:
            * None
        @return:
            * None
        """
        self._connection.close()


if __name__ == "__main__":
    c = DataBaseConnectionSQL('brais', 'password1', '127.0.0.1', 'brais_data_problems')
    c.connect()
    c.disconnect()
