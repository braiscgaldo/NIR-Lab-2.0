import sys

sys.path.append('../../')
from src.scripts.database.ObtainedData import ObtainedData
from src.scripts.train.DevelopModel import DevelopModel
from src.scripts.predict.predict import Prediction

from flask_restful import reqparse
import threading
from flask import Flask, jsonify, request
import numpy as np

app = Flask(__name__)
app.config['FLASK_ENV'] = 'development'
app.config['HOST'] = '0.0.0.0'
app.config['BUNDLE_ERRORS'] = True


# Parse petition
def parse_request():
    return request.get_json(force=True)


@app.route('/training', methods=['POST'])
def train():
    args = parse_request()

    def train_back(**kwargs):
        od = ObtainedData(kwargs['path_preprocessed_database'], kwargs['path_configuration_model'], kwargs['target_label'])
        od.load_all_data()
        dm = DevelopModel(od)
        dm.run()

    thread = threading.Thread(target=train_back, kwargs=args)
    thread.start()

    return {"message": "Executing background task..."}, 202


@app.route('/predict', methods=['POST'])
def predict():
    args = parse_request()
    results = [np.argmax(sample) for sample in Prediction(data_file=args['data_file'], model_name=args['model_name']).predict()]

    return {"message": "Data predicted", "data": f'{results}'}, 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050)
