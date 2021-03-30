from flask import Flask
from src.scripts.server.facade_interface.facade_interface import FacadeInterface


class Server:
    """
    Class representing the server object
    """
    def __init__(self, app):
        self.facade = FacadeInterface()
        self.app = app

def server_main()
app = Flask(__name__)
server = Server(app)


if __name__ == '__main__':
    app = Flask(__name__)
    s = Server(app)
