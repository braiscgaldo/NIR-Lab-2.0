from os import listdir, remove
from shutil import copyfile
import json


class DataInfo:
    """
    Class for reading data from the directory of the user
    """
    def __init__(self, username):
        self.username = username

    @staticmethod
    def __list_files_in_dir(directory):
        """
        Method for list files in a directory
        """
        files_in_dir = listdir(directory)
        return len(files_in_dir), files_in_dir

    def list_files(self):
        """
        Method for list all files stored by the user
        :return: dictionary with related info
        """
        return {'databases_config': DataInfo.__list_files_in_dir(
            '/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/databases_config/'),
            'databases': DataInfo.__list_files_in_dir(
            '/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/databases/'),
            'models_config': DataInfo.__list_files_in_dir(
            '/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/models_config/'),
            'models': DataInfo.__list_files_in_dir(
            '/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/models/')}

    def add_file(self, file, directory):
        """
        Method for add a file to the correspondent place
        :param: file: info contained into file
        :param: directory: directory where put the file
        :return: None
        """
        copyfile(file, '/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/' +
                 directory + '/' + file[file.rindex('/'):])

    def delete_file(self, file, directory):
        """
        Method for delete a file
        :param: file: file to delete from server
        :param: directory: directory where the file is
        :return: None
        """
        remove('/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/' + directory + '/' +
               file)

    def list_characteristics(self, file):
        """
        Method for listing the characteristics of a database. The first and second parameters are treated data
        :param: file: file to list the variables
        :return: characteristics
        """
        with open('/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/databases/' + file +
                  '.json') as f:
            db = json.load(f)
            return {'chars': list(db[list(db.keys())[3]].keys())[:-1],
                    'labels': db[list(db.keys())[3]]['Labels']}

    def join_db(self, file_db1, file_db2, new_db):
        """
        Method for join 2 databases
        :params: file_db1: first database
        :params: file_db2: second database
        :params: new_db: file with the 2 databases
        :return: None
        """
        if self.list_characteristics(file_db1) == self.list_characteristics(file_db2):
            with open('/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/databases/' +
                      new_db + '.json', 'w') as db:
                with open('/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/databases/' +
                          file_db1 + '.json') as db1:
                    with open(
                            '/home/brais/NIR-Lab/server/server/microservices/scripts/' + self.username + '/databases/' +
                            file_db2 + '.json') as db2:
                        db_lines1, db_lines2 = db1.readlines(), db2.readlines()
                        db_lines1[-1] = db_lines1[-1][:db_lines1[-1].rindex('}')] # delete the last }
                        db_lines2[0]


if __name__ == '__main__':
    di = DataInfo('MongoDB')
    files = di.list_files()
    print(files)
    di.add_file('/home/brais/NIR-Lab/server/server/microservices/scripts/MongoDB/add_user.ns', 'models')
    files = di.list_files()
    print(files)
    di.delete_file('add_user.ns', 'models')
    files = di.list_files()
    print(files)
    chars = di.list_characteristics('HadaBeer_config_hadamard')
    print(chars)
