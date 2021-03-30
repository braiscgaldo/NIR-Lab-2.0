from src.scripts.server.server import Server


def main():
    app = Flask(__name__)
    Server(app)


if __name__ == '__main__':
    main()
