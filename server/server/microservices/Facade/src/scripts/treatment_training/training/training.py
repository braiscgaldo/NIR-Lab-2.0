class Training:
    """
    Class representing a training
    """

    def __init__(self, path_preprocessed_database, path_configuration_model, target_label):
        self.image = 'training:v1'
        self.links = {'database': 'database'}
        self.volumes = {'/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB': {'bind': '/home/brais'}}
        self.path_preprocessed_database = path_preprocessed_database
        self.path_configuration_model, self.target_label = path_configuration_model, target_label

    def __str__(self):
        string = 'python3 main.py --path_preprocessed_database ' + self.path_preprocessed_database + \
                 ' --path_configuration_model ' + self.path_configuration_model + ' --target_label ' + self.target_label
        return string
