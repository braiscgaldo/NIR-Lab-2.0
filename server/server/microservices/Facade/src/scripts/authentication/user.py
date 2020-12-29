class User:
    """
    Class representing the user of the system
    """

    def __init__(self, username, name, surname, password, email):
        self.username, self.name, self.surname = username, name, surname
        self.password, self.email = password, email

