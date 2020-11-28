from src.scripts.database.ObtainedData import ObtainedData
from src.scripts.train.layers.Model import Model
from sklearn.model_selection import train_test_split
import numpy as np
import tensorflow.keras as keras


class DevelopModel:
    """
    Class for develop a model parting from the information obtained in config files and database files.
    """

    def __init__(self, information):
        self.__information = information
        # must divide information into labels and data
        self.__model = None
        self.__data_train = self.__labels_train = None
        self.__data_test = self.__labels_test = None

    def __develop_model(self):
        """
        Method for develop the correspondent model
        :return: developed model
        """
        model = Model()
        self.__information.model_parameters['layers']['1']['input_shape'] = self.__get_input_shape(self.__data_test)
        for layer_idx in self.__information.model_parameters['layers'].keys():
            layer = self.__information.model_parameters['layers'][layer_idx]
            print('adding layer:', layer)
            model.add_layer(layer)

        self.__model = model

    def __compile_model(self):
        """
        Method for compile the model
        :return: model compiled
        """
        optimizer = self.__information.model_parameters['optimizer']
        loss = self.__information.model_parameters['loss_function']
        learning_rate = self.__information.model_parameters['learning_rate']
        epsilon = self.__information.model_parameters['epsilon']
        metrics = self.__information.model_parameters['metrics']

        if optimizer == 'adam':
            opt = keras.optimizers.Adam(learning_rate=learning_rate, epsilon=epsilon)
        elif optimizer == 'sgd':
            opt = keras.optimizers.SGD(learning_rate=learning_rate, epsilon=epsilon)
        elif optimizer == 'adadelta':
            opt = keras.optimizers.Adadelta(learning_rate=learning_rate, epsilon=epsilon)
        elif optimizer == 'rmsprop':
            opt = keras.optimizers.RMSprop(learning_rate=learning_rate, epsilon=epsilon)
        elif optimizer == 'adagrad':
            opt = keras.optimizers.Adagrad(learning_rate=learning_rate, epsilon=epsilon)
        elif optimizer == 'adamax':
            opt = keras.optimizers.Adamax(learning_rate=learning_rate, epsilon=epsilon)
        elif optimizer == 'ftrl':
            opt = keras.optimizers.Ftrl(learning_rate=learning_rate, epsilon=epsilon)
        elif optimizer == 'nadam':
            opt = keras.optimizers.Nadam(learning_rate=learning_rate, epsilon=epsilon)
        self.__model.get_model().compile(optimizer=opt, loss=loss, metrics=metrics)

    def __divide_data(self):
        """
        Method to divide the data
        :return: Data divided
        """
        self.__data_train, self.__data_test, self.__labels_train, self.__labels_test = \
        train_test_split([(k, v) for k, v in self.__information.preprocessed_db.items()],
                         self.__information.labels_db,
                         test_size=self.__information.model_parameters['test_size'])
        aux_dict = {self.__data_train[i][0] : self.__data_train[i][1] for i, _ in enumerate(self.__data_train)}
        self.__data_train = aux_dict
        aux_dict = {self.__data_test[i][0] : self.__data_test[i][1] for i, _ in enumerate(self.__data_test)}
        self.__data_test = aux_dict

    @staticmethod
    def __get_only_data(data_dict):
        """
        Method for obtain only a matrix of data parting from a pair of
        :param data_dict: dictionary of data
        :return: data: only data in np arrays
        """
        data = []
        for sample in data_dict.keys():
            data.append([np.array(data_dict[sample][char]) for char in data_dict[sample]])
        return np.array(data)

    @staticmethod
    def __get_input_shape(data):
        """
        Method for obtain the input shape for the first layer
        :param data: data input
        :return: shape
        """
        input_shape = np.array(list(data[list(data.keys())[0]].values())).shape
        print('Input shape: ', input_shape)
        return input_shape

    @staticmethod
    def __labels_modify(data):
        """
        Method for modify the labels
        :param data: labels list
        :return: labels list in numeric value
        """
        labels = []
        for label in data:
            labels.append(1) if float(label) > 5 else labels.append(0)
        return np.array(labels)

    def __train_model(self):
        """
        Method to train the model
        :return:
        """
        data_train = self.__get_only_data(self.__data_train)
        data_labels = self.__labels_modify(self.__labels_train)
        #model_checkpoint_callback = keras.callbacks.ModelCheckpoint("best_Model.h5", save_best_only=True)
        self.__model.get_model().fit(data_train, data_labels,
                                     epochs=self.__information.model_parameters['num_epochs'],
                                     batch_size=self.__information.model_parameters['batch_size'],
                                     validation_split=self.__information.model_parameters['validation_size'])
                                     #callbacks=[model_checkpoint_callback])

    def __evaluate_model(self):
        """
        Method to evaluate the model
        :return:
        """
        data_test = self.__get_only_data(self.__data_test)
        data_labels = self.__labels_modify(self.__labels_test)
        test_loss, test_acc = self.__model.get_model().\
            evaluate(data_test, data_labels, batch_size=self.__information.model_parameters['batch_size'])

        print('Test accuracy:', test_acc)
        print('Test loss:', test_loss)

    def __save_model(self):
        """
        Method to save the model in JSON format
        :return: a file with the correspondents weights
        """
        self.__model.get_model().save('/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/' + self.__information.model_parameters['name'] + '.h5')
        with open('/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/' + self.__information.model_parameters['name'] + '.json', "w") as json_model:
            json_model.write(self.__model.get_model().to_json())

    def run(self):
        """
        Method to run all pipeline
        :return: trained model saved in json format
        """
        print('dividing data...')
        self.__divide_data()
        print('developing model...')
        self.__develop_model()
        print('compiling model...')
        self.__compile_model()
        print('training model...')
        self.__train_model()
        print('evaluating model...')
        self.__evaluate_model()
        print('saving model into json...')
        self.__save_model()


if __name__ == '__main__':
    rute_p_db = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/HadaBeer_config_hadamard.json'
    rute_o_db = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/HadaBeer.json'
    rute_m_db = '/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/model_test.json'
    target_label = 'Graduacion'
    od = ObtainedData(rute_p_db, rute_o_db, rute_m_db, target_label)
    od.load_all_data()
    dm = DevelopModel(od)
    dm.run()
