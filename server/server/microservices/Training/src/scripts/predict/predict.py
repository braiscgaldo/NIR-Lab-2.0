import tensorflow as tf
from src.scripts.database.load.Information import Information
import numpy as np


class Prediction:
    """
    Class simulating a prediction
    """
    def __init__(self, data_file, model_name):
        self.data_file, self.model_name, self.__info, self.data, self.model = data_file, model_name, Information(data_file), None, None

    def _load_data(self):
        """
        Method to load data
        :return: self with loaded data
        """
        self.data = self.__info.get_data()

    def _prepare_data(self):
        """
        Method to prepare the correspondent data
        :return: self with data prepared
        """
        self.data.pop('Output')
        self.data.pop('Labels')
        for element in self.data.keys():
            self.data.get(element).pop('Hour', None)
            self.data.get(element).pop('Labels', None)
        data = []
        for element in self.data.keys():
            data.append(np.array([np.array(self.data[element][char]) for char in self.data.get(element).keys()]))
        self.data = np.array(data)
        print(self.data)
        print(self.data.shape)

    def _load_model(self):
        """
        Method to load the model
        :return: self with model loaded
        """
        self.model = tf.keras.models.load_model(self.model_name, custom_objects=None, compile=True, options=None)

    def predict(self):
        """
        Function to realise predictions
        :return: predictions
        """
        self._load_data()
        self._prepare_data()
        self._load_model()
        return self.model.predict(self.data)

