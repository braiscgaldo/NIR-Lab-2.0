from src.scripts.database.db_connection.SQL.DataBaseConnectionSQL import DataBaseConnectionSQL
from src.scripts.database.db_connection.MongoDB.DataBaseConnectionMongoDB import DataBaseConnectionMongoDB


class ObtainDataFromDB:
    def __init__(self, username_sql, username_nosql, password_sql, password_nosql, host_sql, host_nosql,
                 uri_database_sql):
        self._username_sql, self._username_nosql = username_sql, username_nosql
        self._password_sql, self._password_nosql = password_sql, password_nosql
        self._host_sql, self._host_nosql, self._uri_database_sql = host_sql, host_nosql, uri_database_sql

        self._connection_sql = DataBaseConnectionSQL(self._username_sql, self._password_sql, self._host_sql,
                                                     self._uri_database_sql)

        self._connection_nosql = DataBaseConnectionMongoDB(self._username_nosql, self._password_nosql, self._host_nosql,
                                                           'meditions')

    def connect_to_dbs(self):
        """
        Method to connect to both dbs
        @inputs:
            * None
        @return:
            * None
        """
        self._connection_sql.connect()
        self._connection_nosql.connect()

    def obtain_measures(self):
        """
        Method for obtaining the correspondent measures.
        @input:
            * None
        @return:
            * None
        """
        print(self._connection_nosql.obtain_measures().find_one())


    def disconnect_from_both_dbs(self):
        """
        Method for disconnect from both dbs
        @inputs:
            * None
        @return:
            * None
        """
        self._connection_sql.disconnect()
        self._connection_nosql.disconnect()


if __name__ == '__main__':
    c = ObtainDataFromDB('brais', 'brais', 'password1', 'password1', '127.0.0.1', 'mongodb://localhost:27017/',
                         'brais_data_problems')
    c.connect_to_dbs()
    c.obtain_measures()
    c.disconnect_from_both_dbs()
