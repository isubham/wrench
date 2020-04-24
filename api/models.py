from app import db
from app import mb
from sqlalchemy.dialects.postgresql import JSON
from random import randint
from datetime import datetime
import os
from enum import Enum
import hashlib


# from email import Email

class User(db.Model):
    __tablename__ = 'User'

    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(), unique=True)
    username = db.Column(db.String(), unique=True)
    password = db.Column(db.String())
    phone = db.Column(db.String(), unique=True)
    appCode = db.Column(db.String())
    verified = db.Column(db.Integer)
    verify_code = db.Column(db.Integer)
    failed_attempts = db.Column(db.Integer)
    last_login = db.Column(db.DateTime)
    created = db.Column(db.DateTime)
    modified = db.Column(db.DateTime)

    def __init__(self, email, password, appCode):
        self.email = email
        self.username = None
        self.password = password
        self.phone = None
        self.appCode = appCode
        self.verified = 0
        self.verify_code = self.random_with_n_digits(int(os.environ["VERIFY_CODE_LENGTH"]))
        self.failed_attempts = 0
        self.last_login = datetime.now()
        self.created = datetime.now()
        self.modified = datetime.now()
        # TODO send email

    def __repr__(self):
        return '<id {} email {} AppCode {} Verified {} Failed_attempts {} LastLogin {} Created {} Modified {}'.format(
            self.id, self.email, self.appCode, self.verified, self.failedAttempts, self.lastLogin, self.created,
            self.modified)

    def random_with_n_digits(self, n):
        range_start = 10 ** (n - 1)
        range_end = (10 ** n) - 1
        return randint(range_start, range_end)

    def update_modified_to_current_time(self):
        self.modified = datetime.now()

    def set_response(self, message, success):
        self.message = message
        self.success = success


class UserSchema(mb.Schema):
    class Meta:
        fields = ('id', 'password', 'email', 'username', 'appCode', 'verified', 'verify_code', 'failed_attempts',
                  'last_login', 'created', 'modified', 'message', 'success')


user_schema = UserSchema()
users_schema = UserSchema(many=True)


class Person(db.Model):
    __tablename__ = 'People'
    id = db.Column(db.Integer, primary_key=True)
    first_name = db.Column(db.String())
    last_name = db.Column(db.String())
    profile_pic = db.Column(db.String())
    id_front = db.Column(db.String())
    id_back = db.Column(db.String())
    father_name = db.Column(db.String())
    username = db.Column(db.String())
    created = db.Column(db.DateTime())
    modified = db.Column(db.DateTime())
    aadhar_id = db.Column(db.String())

    def __init__(self, id, first_name, last_name, profile_pic, id_front,
                 id_back, father_name, username, aadhar_id):
        self.id = id
        self.first_name = first_name
        self.last_name = last_name
        self.profile_pic = profile_pic
        self.id_front = id_front
        self.id_back = id_back
        self.father_name = father_name
        self.username = username
        self.aadhar_id = aadhar_id
        self.created = datetime.now()
        self.modified = datetime.now()

    def __repr__(self):
        pass


class PersonSchema(mb.Schema):
    class Meta:
        fields = (
            'id', 'first_name', 'last_name', 'profile_pic', 'id_front', 'id_back',
            'father_name', 'username', 'created', 'modified')


person_schema = PersonSchema()
people_schema = PersonSchema(many=True)
