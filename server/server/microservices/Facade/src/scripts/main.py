import argparse
from src.scripts.data_info.read_data import DataInfo
from src.scripts.treatment_training.data_treatment.data_treatment import DataTreatment
from src.scripts.treatment_training.training.training import Training
from src.scripts.treatment_training.docker_interface import DockerInterface
from src.scripts.authentication.auth import Auth


TYPES = ['datatreatment', 'training', 'listfiles', 'addfile', 'deletefile', 'listcharacteristics', 'joinfiles', 'login',
         'createuser', 'listusers', 'edituser', 'dropuser', 'infouser']


def parse_args():
    """
    Function used to parse arguments for facade
    """
    parser = argparse.ArgumentParser(description='This file works as an interface for the Facade service. It '
                                                 'takes several arguments to make the action required by the user')
    parser.add_argument('-t', '--type', type=str, required=True, help='Type of the microservice to start. The possible '
                                                                      'values are:\n * DataTreatment\n * Training\n'
                                                                      ' * ListFiles\n * AddFile\n * DeleteFile\n'
                                                                      ' * ListCharacteristics\n * JoinFiles\n'
                                                                      ' * Login\n * CreateUser\n * ListUsers\n'
                                                                      ' * EditUser\n * DropUser\n * InfoUser')
    parser.add_argument('-f', '--filename', type=str, help='Filename for add, delete, join or list characteristics')
    parser.add_argument('-f2', '--filename2', type=str, help='Filename for join')
    parser.add_argument('-f3', '--new_filename', type=str, help='Filename for join others into it')
    parser.add_argument('-d', '--directory', type=str, help='Directory to treat the file')
    parser.add_argument('-pp', '--path_preprocessed_database', type=str, help='Path to preprocessed database. Required '
                                                                              'if type Training')
    parser.add_argument('-pm', '--path_configuration_model', type=str, help='Path to configuration model file. '
                                                                            'Required if type Training')
    parser.add_argument('-tl', '--target_label', type=str, help='Target label to infer. Required if type Training')
    parser.add_argument('-us', '--username_sql', type=str, help='Username for access to SQL database server. '
                                                                'Required if type DataTreatment')
    parser.add_argument('-ps', '--password_sql_user', help='Password for the user of the SQL server. Required if type '
                                                           'DataTreatment')
    parser.add_argument('-hs', '--host_sql', type=str, help='Direction of the SQL database server. Required if type '
                                                            'DataTreatment')
    parser.add_argument('-dn', '--database_name', type=str, help='Name of the database to take data from. Required if '
                                                                 'type DataTreatment')
    parser.add_argument('-cn', '--configuration_name', type=str, help='Name of the configuration to use. Required if '
                                                                      'type DataTreatment')
    parser.add_argument('-fdn', '--file_data_name', type=str, help='Name of the file where is the measures database. '
                                                                   'Required if type DataTreatment')
    parser.add_argument('-ua', '--username_auth', type=str, help='Username for authentication. Required for '
                                                                 'authentication')
    parser.add_argument('-pa', '--password_auth', type=str, help='Password for authentication. Required for '
                                                                 'authentication')
    parser.add_argument('-na', '--name_auth', type=str, help='Name for authentication. Required for authentication')
    parser.add_argument('-sa', '--surname_auth', type=str, help='Surname for authentication. Required for '
                                                                'authentication')
    parser.add_argument('-ea', '--email_auth', type=str, help='Email for authentication. Required for authentication')
    parser.add_argument('-npa', '--new_password_auth', type=str, help='New password for authentication. Required for '
                                                                       'authentication')
    return parser


def require(parser, args):
    """
    Function to require args if one is pressent
    :param parser: parser
    :param args: arguments passed to program
    :return: throws exception
    """
    if args.type.lower() not in TYPES:
        parser.error('--args invalid value')
    if args.type.lower() == 'datatreatment' and (args.username_sql is None or args.password_sql_user is None or
                                                 args.host_sql is None or args.database_name is None or
                                                 args.configuration_name is None or args.file_data_name is None):
        parser.error('type DataTreatment invalid arguments: arguments lack')
    if args.type.lower() == 'training' and (args.path_preprocessed_database is None or
                                            args.path_configuration_model is None or args.target_label is None):
        parser.error('type Training invalid arguments: argument lack')
    if args.type.lower() == 'addfile' and (args.filename is None or args.directory is None):
        parser.error('type AddFile invalid arguments: argument lack')
    if args.type.lower() == 'deletefile' and (args.filename is None or args.directory is None):
        parser.error('type DeleteFile invalid arguments: argument lack')
    if args.type.lower() == 'listcharacteristics' and args.filename is None:
        parser.error('type ListCharacteristics invalid arguments: argument lack')
    if args.type.lower() == 'joinfiles' and (args.filename is None or args.filename2 is None or
                                             args.new_filename is None):
        parser.error('type ListCharacteristics invalid arguments: argument lack')
    if args.type.lower() == 'createuser' and (args.username_auth is None or args.password_auth is None or
                                              args.name_auth is None or args.surname_auth is None or args.email_auth is
                                              None):
        parser.error('type ListCharacteristics invalid arguments: argument lack')
    if args.type.lower() == 'edituser' and (args.username_auth is None or args.password_auth is None or args.name_auth
                                            is None or args.surname_auth is None or args.email_auth is None or
                                            args.new_password_auth is None):
        parser.error('type ListCharacteristics invalid arguments: argument lack')
    if (args.type.lower() == 'login' or args.type.lower() == 'dropuser' or args.type.lower() == 'infouser') and \
            (args.username_auth is None or args.password_auth is None):
        parser.error('type ListCharacteristics invalid arguments: argument lack')


def main(args):
    type_operation = args.type.lower()
    # authentication
    if type_operation == 'login':
        return Auth.login(username=args.username_auth, password=args.password_auth)
    elif type_operation == 'createuser':
        return Auth.create_user(username=args.username_auth, password=args.password_auth, name=args.name_auth,
                                surname=args.surname_auth, email=args.email_auth)
    elif type_operation == 'dropuser':
        return Auth.drop_user(username=args.username_auth, password=args.password_auth)
    elif type_operation == 'infouser':
        return Auth.info_user(username=args.username_auth, password=args.password_auth)
    elif type_operation == 'edituser':
        return Auth.edit_user(username=args.username_auth, password=args.password_auth, name=args.name_auth,
                              surname=args.surname_auth, email=args.email_auth, new_password=args.new_password_auth)
    elif type_operation == 'listusers':
        return Auth.list_users()
    # file management
    elif type_operation == 'listfiles':
        return DataInfo(args.username).list_files()
    elif type_operation == 'addfile':
        return DataInfo(args.username).add_file(args.filename, args.directory)
    elif type_operation == 'deletefile':
        return DataInfo(args.username).delete_file(args.filename, args.directory)
    elif type_operation == 'listcharacteristics':
        return DataInfo(args.username).list_characteristics(args.filename)
    elif type_operation == 'joinfiles':
        return DataInfo(args.username).join_db(args.filename, args.filename2, args.new_filename)
    else:
        # data treatment
        if type_operation == 'datatreatment':
            docker_interface = DataTreatment(args.username_sql, args.password_sql_user, args.host_sql,
                                             args.database_name,
                                             args.configuration_name, args.file_data_name)
        else: # training
            docker_interface = Training(args.path_preprocessed_database, args.path_configuration_model,
                                        args.target_label)
        DockerInterface().main(docker_info=docker_interface)


if __name__ == '__main__':
    parser = parse_args()
    args = parser.parse_args()
    require(parser, args)
    print(main(args))
