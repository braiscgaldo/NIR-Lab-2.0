class FacadeInterface:
    """
    Class representing a interface to the facade
    """
    def __init__(self):
        self.facade_command = {}
        self.__init_facade_commands()

    def __init_facade_commands(self):
        """
        Method for initialise the facade commands
        :return: None - dict created
        """
        self.facade_command['Login'] = '--type Login --username_auth {username_auth} --password_auth {password_auth}'
        self.facade_command['CreateUser'] = '--type CreateUser --username_auth {username_auth} --password_auth ' \
                                            '{password_auth} --name_auth {name_auth} --surname_auth {surname_auth} ' \
                                            '--email_auth {email_auth}'
        self.facade_command['DataTreatment'] = '--type DataTreatment --username_sql {username_sql} ' \
                                               '--password_sql_user {password_sql_user} --host_sql {host_sql} ' \
                                               '--database_name {database_name} ' \
                                               '--configuration_name {configuration_name} ' \
                                               '--file_data_name {file_data_name}'
        self.facade_command['DropUser'] = '--type DropUser --username_auth {username_auth} ' \
                                          '--password_auth {password_auth}'
        self.facade_command['EditUser'] = '--type EditUser --username_auth {username_auth} ' \
                                          '--password_auth {password_auth} --name_auth {name_auth} ' \
                                          '--surname_auth {surname_auth} --email_auth {email_auth} ' \
                                          '--new_password_auth {new_password_auth}'
        self.facade_command['InfoUser'] = '--type InfoUser --username_auth {username_auth} ' \
                                          '--password_auth {password_auth}'
        self.facade_command['ListUser'] = '--type ListUsers'
        self.facade_command['Training'] = '--type Training --path_preprocessed_database {path_preprocessed_database} ' \
                                          '--path_configuration_model {path_configuration_model} ' \
                                          '--target_label {target_label}'
        self.facade_command['ListFiles'] = '--type ListFiles --username_auth {username_auth}'
        self.facade_command['AddFile'] = '--type AddFile --username_auth {username_auth} --name_file {name_file}'
        self.facade_command['DeleteFile'] = '--type DeleteFile --username_auth {username_auth} --name_file {name_file}'
        self.facade_command['ListCharacteristics'] = '--type ListCharacteristics --username_auth {username_auth} ' \
                                                      '--name_file {name_file}'

    def exec_command(self, command_dict):
        """
        Method to execute a command with appropriated data
        :param: command_dict - dictionary with the command to exec and the data required
        :return: execution of command
        """
        return self.facade_command[command_dict.pop('command', 'not valid command')].format(**command_dict)

