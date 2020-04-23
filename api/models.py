from app import db
from sqlalchemy.dialects.postgresql import JSON
from random import randint
from datetime import datetime
import os
import hashlib
# from email import Email

class User(db.Model):
    __tablename__ = 'User'

    id 		        = db.Column(db.Integer, primary_key=True)
    email 		    = db.Column(db.String(), unique=True)
    username 	    = db.Column(db.String(), unique=True)
    password 	    = db.Column(db.String())
    phone 		    = db.Column(db.String(), unique=True)
    appCode 	    = db.Column(db.String())
    verified 	    = db.Column(db.Integer)
    verify_code 	= db.Column(db.Integer)
    failed_attempts = db.Column(db.Integer)
    last_login	    = db.Column(db.DateTime)
    created		    = db.Column(db.DateTime)
    modified	    = db.Column(db.DateTime)

    def __init__(self, email, password, appCode):
        self.email 	        = email
        self.username 	    = None
        self.password 	    = password
        self.phone 	        = None
        self.appCode 	    = appCode
        self.verified 	    = 0
        self.verify_code     = self.random_with_n_digits(int(os.environ["VERIFY_CODE_LENGTH"]))
        self.failed_attempts = 0
        self.last_login	    = datetime.now()
        self.created	    = datetime.now()
        self.modified	    = datetime.now()
        # TODO send email


    def __repr__(self):
        return '<id {} email {} AppCode {} Verified {} FaildAttempts {} LastLogin {} Created {} Modified {}'\
            .format(self.id, self.email, self.appCode, self.verified, self.failedAttempts, self.lastLogin, self.created, self.modified)

    def random_with_n_digits(self, n):
        range_start = 10**(n-1)
        range_end = (10**n)-1
        return randint(range_start, range_end)

    def email_exists(self, email):
        pass

    def sign_in(self, email, password):
        pass

    def forgot_password(self, email):
        pass

    def change_password(self, id, password):
        pass

    def verify_code_valid(self, id, verify_code):
        pass









