import numpy as np


class Operate:
    """
    Class for deciding which operation apply
    """

    def __init__(self, functions):
        self._functions = functions

    def operate(self, data):
        """
        Method for realise the needed operation.
        @input:
            * data - data[0] contains the operation and data[1:] contains the parameters
        @return:
            * result - result of the operation
        """
        result = 0
        try:
            result = self._functions.operate(data)
            if type(result) == np.ndarray:
                result = result.tolist()
        except Exception as e:
            print(e)
            print('error occurs...', data[0])

        return result
