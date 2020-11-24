from abc import ABC, abstractmethod
import tensorflow.keras.layers as layers


class Singleton(type):
    """
    Class used to make singletons in this file
    """
    _instances = {}

    def __call__(cls, *args, **kwargs):
        """
        Possible changes to the value of the `__init__` argument do not affect
        the returned instance.
        """
        if cls not in cls._instances:
            instance = super().__call__(*args, **kwargs)
            cls._instances[cls] = instance
        return cls._instances[cls]


class Layer(Singleton, ABC):
    """
    This Layer class is equivalent to the Command of the Command pattern
    and declares a method for executing a command.
    """

    @abstractmethod
    def generate_layer(self, params) -> None:
        pass


class DenseLayer(metaclass=Layer):
    """
    Layer of type Dense to add to the model. The params received by the function are:
        * number_neurons
        * activation_function
    """

    @staticmethod
    def generate_layer(params):
        if 'input_shape' in params.keys():
            return layers.Dense(input_shape=params['input_shape'], units=params['units'],
                                activation=params['activation'])
        return layers.Dense(units=params['units'], activation=params['activation'])


class DropoutLayer(metaclass=Layer):
    """
    Layer of type Dense to add to the model. The params received by the function are:
        * rate
    """

    @staticmethod
    def generate_layer(params):
        if 'input_shape' in params.keys():
            return layers.Dropout(input_shape=params['input_shape'], rate=params['rate'])
        return layers.Dropout(rate=params['rate'])


class SoftmaxLayer(metaclass=Layer):
    """
    Layer of type Dense to add to the model. The params received by the function are:
        * None
    """

    @staticmethod
    def generate_layer(params):
        if 'input_shape' in params.keys():
            return layers.Softmax(input_shape=params['input_shape'])
        return layers.Softmax()


class MaxPool1DLayer(metaclass=Layer):
    """
    Layer of type Dense to add to the model. The params received by the function are:
        * pool_size
    """

    @staticmethod
    def generate_layer(params):
        if 'input_shape' in params.keys():
            return layers.MaxPool1D(input_shape=params['input_shape'], rate=params['pool_size'])
        return layers.MaxPool1D(pool_size=params['pool_size'])


class AveragePooling1DLayer(metaclass=Layer):
    """
    Layer of type Dense to add to the model. The params received by the function are:
        * pool_size
    """

    @staticmethod
    def generate_layer(params):
        if 'input_shape' in params.keys():
            return layers.AveragePooling1D(input_shape=params['input_shape'], rate=params['pool_size'])
        return layers.AveragePooling1D(pool_size=params['pool_size'])


class AddLayers:
    """
    Class representing the interface for add several layers to the model.
    """

    def __init__(self):
        self._layers = {}
        self.__add_layers()

    def __add_layers(self):
        """
        Method for generate a new layer for add to the model
        :return: None
        """
        self._layers['Dense'] = DenseLayer()
        self._layers['Dropout'] = DropoutLayer()
        self._layers['Softmax'] = SoftmaxLayer()
        self._layers['MaxPool1D'] = MaxPool1DLayer()
        self._layers['AveragePooling1D'] = AveragePooling1DLayer()

    def generate_layer(self, params):
        """
        Method for decide what layer must be generated with the correspondent params
        :param params: parameters for treat the layer including the correspondent layer name
        :return: generated layer
        """
        return self._layers[params['name']].generate_layer(params)
