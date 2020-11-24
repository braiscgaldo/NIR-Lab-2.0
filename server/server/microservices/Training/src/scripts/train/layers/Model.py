import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import tensorflow.keras as keras
from src.scripts.train.layers.DevelopLayers import AddLayers


class Model:
    """
    Class that represents the model with the correspondent layers
    """

    def __init__(self):
        self._model = keras.Sequential()
        self.__layers = AddLayers()

    def add_layer(self, params):
        """
        Method for add a layer with the correspondent params to the model
        :param params: dict with parameters of the layer including the name
        :return: None
        """
        self._model.add(self.__layers.generate_layer(params))

    def get_model(self):
        return self._model

