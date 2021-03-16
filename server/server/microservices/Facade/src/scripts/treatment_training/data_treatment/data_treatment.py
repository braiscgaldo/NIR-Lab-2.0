class DataTreatment:
    """
    Class representing the data treatment parameters objects
    """

    def __init__(self, username_sql, password_sql_user, host_sql, database_name, configuration_name, file_data_name):
        self.image = 'data_treatment:v1'
        self.links = {'dockers_database_1': 'database'}
        self.volumes = {'/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB': {'bind': '/home/brais'}}
        self.username_sql, self.password_sql_user, self.host_sql = username_sql, password_sql_user, host_sql
        self.database_name, self.configuration_name = database_name, configuration_name
        self.file_data_name = file_data_name

    def __str__(self):
        string = 'python3 main.py --username_sql ' + self.username_sql + ' --password_sql_user ' + \
                 self.password_sql_user + ' --host_sql ' + self.host_sql + ' --database_name ' + self.database_name + \
                 ' --configuration_name ' + self.configuration_name + ' --file_data_name ' + self.file_data_name
        return string
