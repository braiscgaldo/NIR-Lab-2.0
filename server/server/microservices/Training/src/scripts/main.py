import argparse
import sys
sys.path.append('../../')
from src.scripts.database.ObtainedData import ObtainedData
from src.scripts.train.DevelopModel import DevelopModel


def parse_args():
    configparser = argparse.ArgumentParser(description='This file works as an interface for the Training service. '
                                                       'It takes several arguments to do the training of the model with'
                                                       ' the data required')
    configparser.add_argument('-pp', '--path_preprocessed_database', type=str, required=True, 
                              help='Path to preprocessed database')
    configparser.add_argument('-pm', '--path_configuration_model', type=str, required=True,
                              help='Path to configuration model file')
    configparser.add_argument('-tl', '--target_label', type=str, required=True, help='Target label to infer')

    return configparser.parse_args()


def main(args):
    od = ObtainedData(args.path_preprocessed_database, args.path_configuration_model, args.target_label)
    od.load_all_data()
    dm = DevelopModel(od)
    dm.run()


if __name__ == '__main__':
    args = parse_args()
    main(args)
