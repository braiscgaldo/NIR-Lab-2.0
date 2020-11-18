import json


class JSONFiles:
    def __init__(self, username, measures_db, configuration_file):
        self._username = username
        self._measures_db_filename = measures_db
        self._configuration_file = configuration_file
        self._measures_db = None
        self._configuration = None

    def load_data(self):
        """
        Method for connect to the database:
        @input:
            * None
        @return:
            * None
        """
        with open(self._measures_db_filename) as mdb:
            self._measures_db = json.load(mdb)
        with open(self._configuration_file) as cdb:
            self._configuration = json.load(cdb)

    def obtain_measures(self):
        """
        Method for obtain the measures from the database:
        @input:
            * None
        @return:
            * measures_db - measures data
        """
        return self._measures_db

    def obtain_configuration(self):
        """
        Method for obtain the configuration file that is required
        @input:
            * None
        @return
            * configuration - configuration file required
        """
        return self._configuration

    def generate_database(self, data):
        """
        Method for generate a new database parting from data.
        @input:
            * configuration_filename - configuration filename for calling the database
            * data - data to introduce in the database
        @return:
            * None - new collection in database
        """
        with open(self._measures_db_filename[:self._measures_db_filename.rindex('.')] + '_' +
                  self._configuration_file[self._configuration_file.rindex('/')+1:], 'w') as new_db:
            json.dump(data, new_db)


if __name__ == '__main__':
    c = JSONFiles('brais', 'password1', 'mongodb://localhost:27017/', 'measures')
    c.connect()
    c.obtain_measures()
    c.disconnect()
