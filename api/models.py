from app import db
from app import mb
from datetime import datetime
import os, time
from sqlalchemy.orm import relationship
from cryptography.fernet import Fernet
from cryptography.fernet import InvalidToken

from utility import Utility

os.environ['TZ'] = 'Asia/Kolkata'
time.tzset()


class User(db.Model):
    __tablename__ = 'User'

    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(), db.ForeignKey('License.user_email'), unique=True)
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
    People = relationship("People", back_populates="User")
    Activity = relationship("Activity", back_populates="User")
    License = relationship("License", back_populates="User")

    def __init__(self, email, password, appCode):
        self.email = email
        self.username = None
        self.password = password
        self.phone = None
        self.appCode = appCode
        self.verified = 0
        self.verify_code = Utility.random_with_n_digits(int(os.environ["VERIFY_CODE_LENGTH"]))
        self.failed_attempts = 0
        self.last_login = datetime.now()
        self.created = datetime.now()
        self.modified = datetime.now()
        # TODO send email

    def __repr__(self):
        return '<id {} email {} AppCode {} Verified {} Failed_attempts {} LastLogin {} Created {} Modified {}'.format(
            self.id, self.email, self.appCode, self.verified, self.failedAttempts, self.lastLogin, self.created,
            self.modified)

    def post_signin(self):
        self.modified = datetime.now()

    def set_response(self, message, success):
        self.message = message
        self.success = success


class UserSchema(mb.Schema):
    class Meta:
        fields = ('id', 'password', 'email', 'username', 'appCode', 'verified', 'verify_code', 'failed_attempts',
                  'last_login', 'created', 'modified')


user_schema = UserSchema()
users_schema = UserSchema(many=True)


class People(db.Model):
    __tablename__ = 'People'
    id = db.Column(db.Integer, primary_key=True)
    first_name = db.Column(db.String())
    last_name = db.Column(db.String())
    dob = db.Column(db.Date())
    profile_pic = db.Column(db.String())
    id_front = db.Column(db.String())
    id_back = db.Column(db.String())
    father_name = db.Column(db.String())
    username = db.Column(db.String(), unique=True)
    created_by = db.Column(db.Integer)
    created = db.Column(db.DateTime())
    modified = db.Column(db.DateTime())
    aadhar_id = db.Column(db.String(), unique=True)
    contact = db.Column(db.String())
    pin_code = db.Column(db.String())
    address = db.Column(db.String())
    email = db.Column(db.String())
    user_id = db.Column(db.Integer, db.ForeignKey('User.id'), unique=True)
    User = relationship("User", back_populates="People")
    Activity = relationship("Activity", back_populates="People")

    def __init__(self, user_id, first_name, last_name, dob, profile_pic, id_front,
                 id_back, father_name, username, created_by, contact, pin_code, address, email, aadhar_id=None):
        self.user_id = user_id
        self.first_name = first_name
        self.last_name = last_name
        self.dob = dob
        self.profile_pic = profile_pic
        self.id_front = id_front
        self.id_back = id_back
        self.father_name = father_name
        self.username = username
        self.aadhar_id = aadhar_id
        self.created_by = created_by
        self.created = datetime.now()
        self.modified = datetime.now()
        self.contact = contact
        self.pin_code = pin_code
        self.address = address
        self.email = email

    def __repr__(self):
        pass


class PersonSchema(mb.Schema):
    class Meta:
        fields = (
            'id', 'user_id', 'first_name', 'last_name', 'dob', 'profile_pic', 'id_front', 'id_back',
            'father_name', 'username', 'created', 'created_by', 'modified', 'aadhar_id', 'contact', 'pincode', 'address', 'email')


person_schema = PersonSchema()
people_schema = PersonSchema(many=True)


class Activity(db.Model):
    __tablename__ = 'Activity'
    id = db.Column(db.Integer, primary_key=True)
    person_id = db.Column(db.Integer, db.ForeignKey("People.user_id"))
    when = db.Column(db.DateTime)
    type = db.Column(db.Integer)
    location = db.Column(db.String())
    purpose = db.Column(db.String())
    user_id = db.Column(db.Integer, db.ForeignKey("User.id"))
    User = relationship("User", back_populates="Activity")
    People = relationship("People", back_populates="Activity")

    def __init__(self, user_id, person_id, location, type, purpose):
        self.user_id = user_id
        self.person_id = person_id
        self.type = type
        self.location = location
        self.when = datetime.now()
        self.purpose = purpose


class ActivitySchema(mb.Schema):
    class Meta:
        fields = ('id', 'user_id', 'person_id', 'type', 'location', 'when', 'purpose')


activity_schema = ActivitySchema()
activities_schema = ActivitySchema(many=True)


class License(db.Model):
    __tablename__ = "License"
    app_code = db.Column(db.Integer)
    secret_key = db.Column(db.LargeBinary)
    license_key = db.Column(db.LargeBinary)
    user_email = db.Column(db.String(), primary_key=True)
    User = relationship("User", back_populates="License")


    def __init__(self, email, app_code, validity):
        self.user_email = email
        self.app_code = app_code
        self.secret_key = Fernet.generate_key()
        self.license_key = self.generate_license_key(email, app_code, validity)

    def generate_license_key(self, user_email, app_id, validity):
        f = Fernet(self.secret_key)
        license_salt = os.environ["LICENSE_SALT"]
        license_text = "user_email:{}|app_code:{}|license_valid:{}|license_salt:{}" \
            .format(user_email, app_id, validity, license_salt).encode()
        return f.encrypt(license_text)

    def license_valid(self, user_email, app_code, license_text):
        try:
            f = Fernet(self.secret_key)
            license_decryped = f.decrypt(license_text)

            license_components = license_decryped.decode().split('|')
            license_user_email, license_app_code, license_valid, license_salt = map(lambda s: s.split(":")[1],
                                                                                    license_components)

            user_email_matches = user_email == license_user_email
            app_id_matches = app_code == license_app_code
            license_validity_left = (Utility.parseDateYMD(license_valid) - \
                                     Utility.parseDateYMD(str(datetime.now()).split(" ")[0])).days

            if user_email_matches and app_id_matches and license_validity_left > 0:
                return {"valid": True, "day_left": license_validity_left}
            else:
                return {"invalid": False, "day_left": 0}

        except InvalidToken as i:
            raise InvalidToken

class LicenseSchema(mb.Schema):
    class Meta:
        fields = ('user_email', 'license_key')


license_schema = LicenseSchema()
