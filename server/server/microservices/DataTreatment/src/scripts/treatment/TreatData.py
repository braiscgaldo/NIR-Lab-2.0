from src.scripts.database.ObtainDataFromDB import ObtainDataFromDB
from src.scripts.treatment.DataOperations.Operate import Operate
from src.scripts.treatment.DataOperations.Functions import Operations
import numpy as np

INTENSITY_KEY = 'Intensity'
ABSORBANCE_KEY = 'Absorbance'
REFLECTANCE_KEY = 'Reflectance'
NAME_KEY = 'name'
NOT_DATA_KEYS = ["Wavelength", "Hour", "Labels"]


class TreatData:

    def __init__(self, connections, config_file_name):
        self._connections = connections
        self._config_filename = config_file_name
        self._operations = Operate(Operations())
        self._measures_names = []
        self._measures_data = {}
        self._config_file = None
        self._data_treated = {}

    def establish_connections(self):
        """
        Method to establish connections.
        @input:
            * None
        @return
            * None
        """
        self._connections.connect_to_dbs()

    def disconnect_connections(self):
        """
        Method to establish connections.
        @input:
            * None
        @return
            * None
        """
        self._connections.disconnect_from_both_dbs()

    def obtain_data(self):
        """
        Method for obtain the correspondent data.
        @input:
            * None
        @return:
            * None
        """
        measures = self._connections.obtain_measures()
        self._measures_names = [key for key, i in zip(measures.keys(), range(len(measures.keys()))) if i > 2]
        for measure in measures[self._measures_names[0]].keys():
            if measure not in NOT_DATA_KEYS:
                self._measures_data[measure.lower()] = []

        for key in self._measures_names:
            for measure in measures[key].keys():
                if measure not in NOT_DATA_KEYS:
                    self._measures_data[measure.lower()].append(measures[key][measure])
        
        self._config_file = self._connections.obtain_config_file()

    def operate(self):
        """
        Method to perform the operations indicated in the configuration json file.
        The results are stored into _measures_data attribute.
        @input:
            * None
        @return:
            * None
        """
        for operation in self._config_file['data_operations']:
            data = [self._config_file['data_operations'][operation][0]]
            for element in self._config_file['data_operations'][operation][1:]:
                if element in self._measures_data.keys():
                    data.append(np.array(self._measures_data[element]))
                else:
                    data.append(float(element))
            self._measures_data[operation] = self._operations.operate(data)

    def obtain_results(self):
        """
        Method to obtain the required results stored in _measures_data
        @input:
            * None
        @return:
            * data - data from
        """
        data = {}
        for key in self._measures_data.keys():
            if key in self._config_file['output_chars']:
                data[key] = self._measures_data[key]

        data_from_sample, data_from_name = {}, {}
        for name_idx in range(len(self._measures_names)):
            for measure_key in data:
                data_from_name[measure_key] = self._measures_data[measure_key][name_idx]
            data_from_sample[self._measures_names[name_idx]] = data_from_name
            data_from_name = {}
        return data_from_sample

    def generate_database(self, config_filename, data):
        """
        Method to generate a database with the new data.
        @input:
            * data - dictionary with the correspondent data and key values
        @return:
            * None
        """
        self._connections.generate_database(data)

    def main(self):
        """
        Method for execute the main operations to perform a TreatData operation. It pretends be the main algorithm to
        perform an operation of this type. Otherwise, the rest of the operations will have public access for other kinds
        of data manipulation.
        @input:
            * None
        @return:
            * None - database actualized with new treated data
        """
        print('Establishing connections...')
        self.establish_connections()
        print('Obtaining data... [this may take time]')
        self.obtain_data()
        print('Operating with data...[this may take time]')
        self.operate()
        print('Obtaining results...')
        output = self.obtain_results()
        print('Generating new databases...')
        self.generate_database(self._config_filename, output)
        print('Disconnecting connections...')
        self.disconnect_connections()


if __name__ == '__main__':
    odd = ObtainDataFromDB('brais', 'brais', 'password1', 'password1', '127.0.0.1', 'mongodb://localhost:27017/',
                           'brais_data_problems')
    cf = 'config_hadamard_1'
    treat_data = TreatData(odd, cf)
    treat_data.main()
