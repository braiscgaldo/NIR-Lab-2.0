from flask_login import UserMixin
from werkzeug.security import generate_password_hash, check_password_hash


class User(UserMixin):
    # id is username
    def __init__(self, username, name, surname, password, email, is_admin=False):
        self.id = username
        self.username = username
        self.name = name
        self.surname = surname
        self.email = email
        self.password = password
        self.is_admin = is_admin

    def set_password(self, password):
        self.password = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password, password)

    def __repr__(self):
        return '<User {}>'.format(self.email)


