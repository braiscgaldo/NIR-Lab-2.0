class DataBaseConnection:
    def __init__(self, username, password, host, uri_database):
        self._username = username
        self._password = password
        self._host = host
        self._uri_database = uri_database

    def connect(self):
        pass

    def disconnect(self):
        pass
