from src.scripts.database.db_connection.SQL.DataBaseConnectionSQL import DataBaseConnectionSQL
from src.scripts.database.db_connection.Files.JSONFiles import JSONFiles

mdb = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/HadaBeer.json'
cdb = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/config_hadamard.json'


class ObtainDataFromDB:
    def __init__(self, username_sql, password_sql, host_sql,
                 uri_database_sql, database_name, configuration_name):
        self._username_sql = username_sql
        self._password_sql = password_sql
        self._database_name, self._configuration_name = database_name, configuration_name
        self._host_sql, self._uri_database_sql = host_sql, uri_database_sql

        self._connection_sql = DataBaseConnectionSQL(self._username_sql, self._password_sql, self._host_sql,
                                                     self._uri_database_sql)

        self._connection_files = JSONFiles(self._username_sql, self._database_name, self._configuration_name)

    def connect_to_dbs(self):
        """
        Method to connect to both dbs
        @inputs:
            * None
        @return:
            * None
        """
        self._connection_sql.connect()
        self._connection_files.load_data()

    def obtain_measures(self):
        """
        Method for obtaining the correspondent measures.
        @input:
            * None
        @return:
            * data - the measures data
        """
        data = self._connection_files.obtain_measures()
        return data

    def obtain_config_file(self):
        """
        Method for obtaining the configuration file that is asked for.
        @input:
            * None
        @output:
            * data - configuration file required
        """
        '''
        data = self._connection_nosql.obtain_configuration().find_one()
        if data['name'] == config_filename:
            return data

        print('no data')
        '''
        data = self._connection_files.obtain_configuration()
        return data

    def generate_database(self, data):
        """
        Method for generate a new database parting from data.
        @input:
            * data - data to introduce in the database
        @return:
            * None - new collection in database
        """
        self._connection_files.generate_database(data)

    def disconnect_from_both_dbs(self):
        """
        Method for disconnect from both dbs
        @inputs:
            * None
        @return:
            * None
        """
        self._connection_sql.disconnect()


if __name__ == '__main__':
    c = ObtainDataFromDB('brais', 'brais', 'password1', 'password1', '127.0.0.1', 'mongodb://localhost:27017/',
                         'brais_data_problems')
    c.connect_to_dbs()
    c.obtain_measures()
    c.obtain_config_file('config_hadamard_1')
    c.disconnect_from_both_dbs()
