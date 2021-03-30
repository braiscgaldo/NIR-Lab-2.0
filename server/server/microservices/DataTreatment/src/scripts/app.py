import sys

sys.path.append('../../')
from src.scripts.database.ObtainDataFromDB import ObtainDataFromDB
from src.scripts.treatment.TreatData import TreatData

from flask_restful import reqparse
import threading
from flask import Flask, jsonify, request

app = Flask(__name__)
app.config['FLASK_ENV'] = 'development'
app.config['HOST'] = '0.0.0.0'
app.config['BUNDLE_ERRORS'] = True


# Parse petition
def parse_request():
    return request.get_json(force=True)


@app.route('/data_treatment', methods=['POST'])
def treat_data():
    args = parse_request()

    def treat_data_back(**kwargs):
        odb = ObtainDataFromDB(kwargs['username_sql'], kwargs['password_sql_user'],
                               kwargs['host_sql'], kwargs['database_name'], kwargs['file_data_name'], kwargs['configuration_name'])
        td = TreatData(odb, kwargs['configuration_name'][kwargs['configuration_name'].rindex('/') + 1:])
        td.main()

    thread = threading.Thread(target=treat_data_back, kwargs=args)
    thread.start()

    return {"message": "Executing background task..."}, 202


if __name__ == '__main__':
    app.run(host='0.0.0.0')