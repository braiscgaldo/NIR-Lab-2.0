from src.scripts.database.load.Information import Information


class ObtainedData:
    """
    Class that represents all data joined. It use 3 objects of type Information to achieve the correspondent objective.
    It needs 3 parameters, one for each kind of data:
        * preprocessed_db_name - the preprocessed data
        * original_db_name - for obtaining the labels of the data
        * model_parameters_name - for obtaining the model parameters
    """

    def __init__(self, preprocessed_db_name, model_parameters_name, target_label):
        self.__preprocessed_db_name = preprocessed_db_name
        self.__model_parameters_name = model_parameters_name
        self.__target_label = target_label
        self.preprocessed_db = self.labels_db = self.model_parameters = None

    def load_all_data(self):
        """
        Function to load all required data
        @input:
            * None - takes information from itself
        :return:
            * None - information is loaded into the object
        """
        self.preprocessed_db = Information(self.__preprocessed_db_name).get_data()
        self.preprocessed_db.pop('Output')
        self.labels_db = [self.preprocessed_db[info]['Labels'][self.__target_label] for info in self.preprocessed_db if info != 'Labels']
        self.preprocessed_db.pop('Labels')
        [self.preprocessed_db[element].pop('Labels') for element in self.preprocessed_db.keys()]
        self.model_parameters = Information(self.__model_parameters_name).get_data()


if __name__ == '__main__':
    rute_p_db = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/HadaBeer_config_hadamard.json'
    rute_o_db = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/HadaBeer.json'
    rute_m_db = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/model.json'
    target_label = 'Graduacion'
    od = ObtainedData(rute_p_db, rute_o_db, rute_m_db, target_label)
    od.load_all_data()
    p = False
    if p:
        print('pdb:', od.preprocessed_db)
        print('odb', od.original_db)
        print('mdb:', od.model_parameters)
