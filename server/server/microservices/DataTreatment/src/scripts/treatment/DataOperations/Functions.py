from abc import ABC, abstractmethod
import numpy as np
from scipy import stats

# All classes defined in this file are singletons


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


class Function(Singleton, ABC):
    """
    This Function class is equivalent to the Command of the Command pattern
    and declares a method for executing a command.
    """

    @abstractmethod
    def execute(self, data) -> None:
        pass


class Sum(metaclass=Function):
    """
    Function for sum a value to all array elements or two arrays of the same
    length.
    """

    @staticmethod
    def execute(data):
        return data[0] + data[1]


class Subtract(metaclass=Function):
    """
    Function for subtract a value to all array elements or two arrays of the same
    length.
    """

    @staticmethod
    def execute(data):
        return data[0] - data[1]


class Multiply(metaclass=Function):
    """
    Function for multiply a value to all array elements or two arrays of the same
    length.
    """

    @staticmethod
    def execute(data):
        return data[0] * data[1]


class Divide(metaclass=Function):
    """
    Function for divide a value to all array elements or two arrays of the same
    length.
    """

    @staticmethod
    def execute(data):
        return data[0] / data[1]


class Module(metaclass=Function):
    """
    Function for obtain the module between two values.
    """

    @staticmethod
    def execute(data):
        return data[0] % data[1]


class WholeDivide(metaclass=Function):
    """
    Function for obtain the whole divide between two arrays of the same
    length.
    """

    @staticmethod
    def execute(data):
        return data[0] // data[1]


class SquareRoot(metaclass=Function):
    """
    Function for obtain the square root of a value or array.
    """

    @staticmethod
    def execute(data):
        return np.sqrt(data[0])


class Raise(metaclass=Function):
    """
    Function for raise a value or array to a number length.
    """

    @staticmethod
    def execute(data):
        return data[0] ** data[1]


class StandardDeviation(metaclass=Function):
    """
    Function for obtain the standard deviation of an array.
    """

    @staticmethod
    def execute(data):
        return [[np.std(measure)] for measure in data[0]]


class Variance(metaclass=Function):
    """
    Function for obtain the variance of an array.
    """

    @staticmethod
    def execute(data):
        return [[np.var(measure)] for measure in data[0]]


class Mean(metaclass=Function):
    """
    Function for obtain the mean of an array.
    """

    @staticmethod
    def execute(data):
        return [[np.mean(measure)] for measure in data[0]]


class Median(metaclass=Function):
    """
    Function for obtain the median of an array.
    """

    @staticmethod
    def execute(data):
        return [[np.median(measure)] for measure in data[0]]


class Mode(metaclass=Function):
    """
    Function for obtain the mean of an array.
    """

    @staticmethod
    def execute(data):
        return [[stats.mode(measure)] for measure in data[0]]


class SumAll(metaclass=Function):
    """
    Function for sum all values of an array.
    """

    @staticmethod
    def execute(data):
        return [[sum(measure)] for measure in data[0]]


class Minimum(metaclass=Function):
    """
    Function for obtain the minimum value of an array.
    """

    @staticmethod
    def execute(data):
        return [[np.min(measure)] for measure in data[0]]


class Maximum(metaclass=Function):
    """
    Function for obtain the maximum value of an array.
    """

    @staticmethod
    def execute(data):
        return [[np.max(measure)] for measure in data[0]]


class MinMaxNormalization(metaclass=Function):
    """
    Function for normalize the values of an array using min max normalization.
    """

    @staticmethod
    def execute(data):
        return [[float(m)/(np.max(data[0]) - np.min(data[0])) for m in measure] for measure in data[0]] \
            if (np.max(data[0]) - np.min(data[0])) != 0 else data


class MaxNormalization(metaclass=Function):
    """
    Function for normalize the values of an array using min max normalization.
    """

    @staticmethod
    def execute(data):
        return [[float(m)/np.max(data[0]) for m in measure] for measure in data[0]] if np.max(data[0]) != 0 else data


class Normalization(metaclass=Function):
    """
    Function to normalize a number
    """

    @staticmethod
    def execute(data):
        return [[float(m) / np.linalg.norm(measure) for m in measure] for measure in data[0]] \
            if np.linalg.norm(data[0]) != 0 else data


class ValueAt(metaclass=Function):
    """
    Function for obtain the value at a point.
    """

    @staticmethod
    def execute(data):
        return [[float(measure[int(data[1])])] for measure in data[0]]


class Operations:
    """
    The Invoker class is associated with all commands and represents a request to
    the command.
    It declares a method for each kind of operation for make the required operation.
    """
    def __init__(self):
        self._operate = {}
        self._add_operations()

    def _add_operations(self):
        """
        Method used to add operations to the _operate member.
        @input:
            * None
        @return:
            * None - operations added
        """
        self._operate["sum"] = Sum()
        self._operate["subtract"] = Subtract()
        self._operate["multiply"] = Multiply()
        self._operate["divide"] = Divide()
        self._operate["module"] = Module()
        self._operate["whole_divide"] = WholeDivide()
        self._operate["square_root"] = SquareRoot()
        self._operate["raise_x"] = Raise()
        self._operate["standard_deviation"] = StandardDeviation()
        self._operate["mean"] = Mean()
        self._operate["median"] = Median()
        self._operate["mode"] = Mode()
        self._operate["sum_all"] = SumAll()
        self._operate["minimum"] = Minimum()
        self._operate["maximum"] = Maximum()
        self._operate["min_max_normalization"] = MinMaxNormalization()
        self._operate["normalize"] = Normalization()
        self._operate["variance"] = Variance()
        self._operate["max_abs_normalization"] = MaxNormalization()
        self._operate["value_at"] = ValueAt()

    def operate(self, data):
        """
        Function to perform an operation
        @input:
            * data - operation (data[0]) and data to pass to operation (data[1:])
        @return:
            * result - result of the operation
        """
        return self._operate[data[0]].execute(data[1:])

