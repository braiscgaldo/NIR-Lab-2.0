import argparse
from src.scripts.dockers.data_treatment.data_treatment import DataTreatment
from src.scripts.dockers.training.training import Training
from src.scripts.dockers.docker_interface import DockerInterface


def parse_args():
    parser = argparse.ArgumentParser(description='This file works as an interface for the LoadBalancer service. '
                                                 'It takes several arguments to execute the required '
                                                 'microservice')
    parser.add_argument('-t', '--type', type=str, required=True, help='Type of the microservice to start. The possible'
                                                                      'values are:\n * DataTreatment\n * Training')
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
    return parser


def require(parser, args):
    """
    Function to require args if one is pressent
    :param parser: parser
    :param args: arguments passed to program
    :return: throws exception
    """
    if args.type.lower() != 'datatreatment' and args.type.lower() != 'training':
        parser.error('--args invalid value')
    if args.type.lower() == 'datatreatment' and (args.username_sql is None or args.password_sql_user is None or \
            args.host_sql is None or args.database_name is None or args.configuration_name is None or \
            args.file_data_name is None):
        parser.error('type DataTreatment invalid arguments: arguments lack')
    if args.type.lower() == 'training' and (args.path_preprocessed_database is None or \
            args.path_configuration_model is None or args.target_label is None):
        parser.error('type Training invalid arguments: argument lack')


def main(args):
    if args.type.lower() == 'datatreatment':
        docker_interface = DataTreatment(args.username_sql, args.password_sql_user, args.host_sql, args.database_name,
                                         args.configuration_name, args.file_data_name)
    else:
        docker_interface = Training(args.path_preprocessed_database, args.path_configuration_model,
                                    args.target_label)
    DockerInterface().main(docker_info=docker_interface)


if __name__ == '__main__':
    parser = parse_args()
    args = parser.parse_args()
    require(parser, args)
    main(args)
