import sys
sys.path.append('../..')

from src.scripts.database.db_connection.DataBaseConnection import DataBaseConnection

import pymongo


class DataBaseConnectionMongoDB(DataBaseConnection):
    def __init__(self, username, password, host, uri_database):
        super().__init__(username, password, host, uri_database)
        self._connection = None
        self._measures_db = None

    def connect(self):
        """
        Method for connect to the database:
        @input:
            * None
        @return:
            * None
        """
        self._connection = pymongo.MongoClient(self._host)

    def obtain_measures(self):
        self._measures_db = self._connection[self._uri_database]['medition_data']
        return self._measures_db

    def disconnect(self):
        """
        Method for disconnect the connection with database. It must exist a connection, otherwise it will fail.
        @input:
            * None
        @return:
            * None
        """
        self._connection.close()


if __name__ == '__main__':
    c = DataBaseConnectionMongoDB('brais', 'password1', 'mongodb://localhost:27017/', 'meditions')
    c.connect()
    c.obtain_measures()
    c.disconnect()
