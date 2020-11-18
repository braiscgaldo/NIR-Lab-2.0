import sys
sys.path.append('../../')
from src.scripts.database.ObtainDataFromDB import ObtainDataFromDB
from src.scripts.treatment.TreatData import TreatData
import argparse


def parse_args():
    """
    Function used to parse arguments for developing the database
    """
    parser = argparse.ArgumentParser(description='This file works as an interface for the DataTreatment services. It '
                                                 'takes several arguments to do the treatment of the data required')
    parser.add_argument('-us', '--username_sql', required=True, type=str, help='Username for access to SQL database '
                                                                               'server')
    parser.add_argument('-ps', '--password_sql_user', required=True, type=str, help='Password for the user of the SQL '
                                                                                    'server')
    parser.add_argument('-hs', '--host_sql', required=True, type=str, help='Direction of the SQL database server')
    parser.add_argument('-dn', '--database_name', required=True, type=str, help='Name of the database to take data '
                                                                                'from')
    parser.add_argument('-cn', '--configuration_name', required=True, type=str, help='Name of the configuration to use')
    parser.add_argument('-fdn', '--file_data_name', required=True, type=str, help='Name of the file where is the '
                                                                                  'measures database')
    return parser.parse_args()


def main(args):
    """
    Main that represents the interface to the microservice
    """
    odb = ObtainDataFromDB(args.username_sql, args.password_sql_user,
                           args.host_sql, args.database_name, args.file_data_name, args.configuration_name)
    td = TreatData(odb, args.configuration_name[args.configuration_name.rindex('/')+1:])
    td.main()


if __name__ == '__main__':
    args = parse_args()
    main(args)
