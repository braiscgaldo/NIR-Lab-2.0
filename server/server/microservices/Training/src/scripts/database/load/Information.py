import json


class Information:
    """
    Class for obtain the correspondent database.
    Since thy are the same type of file, it is able to treat all data like all was the same.
    """

    def __init__(self, preprocessed_db_name):
        self.__preprocessed_db_name = preprocessed_db_name
        self.data = None

    def _load_database(self):
        """
        Function used to obtain the data from correspondent file
        @input:
            * None - take the parameters from itself
        :return:
            * None - data into self.data
        """
        with open(self.__preprocessed_db_name) as db:
            self.data = json.load(db)

    def get_data(self):
        """
        Method for obtaining the correspondent data
        @input:
            * None - take data from itself
        :return:
            * data - data loaded
        """
        self._load_database()
        return self.data


if __name__ == '__main__':
    rute = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/HadaBeer_config_hadamard.json'
    db = Information(rute)
    print(db.get_data())

