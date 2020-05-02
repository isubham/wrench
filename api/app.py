import os
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
from flask import Flask, render_template, request, jsonify
from sqlalchemy.exc import IntegrityError
from utility import Utility
from cryptography.fernet import InvalidToken
from resources import Resources
import jwt

app = Flask(__name__)

app.config.from_object(os.environ['APP_SETTINGS'])
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)
mb = Marshmallow(app)

from models import User, user_schema, People, person_schema, Activity, activity_schema, License, license_schema


unprotected_paths = {'/auth/signup/', '/auth/signin/', '/person/'}
user_id = None
details = dict()


@app.before_request
def before_request_callback():
    details["method"] = request.method
    details["path"] = request.path

    if request.headers.__contains__('Content-Type'):
        if request.headers['Content-Type'] == 'application/json; charset=UTF-8':
            details["data"] = request.json

    if not unprotected_paths.__contains__(details["path"]):
        token = request.environ['HTTP_AUTHORIZATION']
        try:
            user_id = Utility.get_payload_from_jwt(token)["id"]
        except jwt.ExpiredSignatureError:
            return jsonify({"message": "token expired"})
        except jwt.InvalidTokenError:
            return jsonify({"message": "token invalid"})



@app.route('/auth/signup/', methods=['POST'])
def signup():
    signup_details = request.json
    user = User(signup_details["email"], Utility.get_hash(signup_details["password"]), os.environ["ASTRA_CODE"])
    try:
        db.session.add(user)
        db.session.commit()
        return jsonify({"token" : Utility.create_secret({"id" : user.id}).decode()})
    except IntegrityError as e:
        return jsonify(Resources.error_existing_email())


@app.route('/auth/signin/', methods=['POST'])
def sign_in():
    sign_in_details = request.json

    user_found = db.session.query(User).filter_by(email=sign_in_details["email"],
                                             password=Utility.get_hash(sign_in_details["password"])).first()
    if not user_found is None:
        user_found.post_signin()
        db.session.commit()
        return jsonify({"token" : Utility.create_secret({"id" : user_found.id}).decode()})

    else:
        return jsonify(Resources.error_email_password_incorrect())


@app.route('/auth/forgotpassword/', methods=['POST'])

@app.route('/auth/changepassword/', methods=['POST'])

@app.route('/auth/verifycode/', methods=['POST'])


@app.route('/person/', methods=['POST'])
def create_people():
    people = request.json
    user = User(people["email"], people["password"], os.environ["ASTRA_CODE"])
    try:
        db.session.add(user)
        db.session.flush()
    except IntegrityError as e:
        return jsonify(Resources.error_existing_email())

    people = People(user.id, people["first_name"], people["last_name"], Utility.get_date(people["dob"]),people["profile_pic"],
                    people["id_front"], people["id_back"], people["father_name"], people["username"], people["aadhar_id"])

    try:
        db.session.add(people)
        db.session.commit()
        return jsonify({"token" : Utility.create_secret({"id" : people.user_id}).decode()})
    except IntegrityError as e:
        db.session.rollback()
        return jsonify(Resources.error_existing_username_or_aadhar_id())


@app.route('/person/token', methods=['GET'])
def person_by_token(_id):
    person_found = db.session.query(People).filter_by(id=user_id).first()
    return person_schema.jsonify(person_found)



@app.route('/person/id/<_id>', methods=['GET'])
def get_person(_id):
    person_found = db.session.query(People).filter_by(user_id=_id).first()
    return person_schema.jsonify(person_found)


@app.route('/person/', methods=['GET'])
def person_search():
    name, father_name, dob = request.json["name"], request.json["father_name"], Utility.get_date(request.json["dob"])
    user_found = db.session.query(User)\
        .filter_by(first_name=name.split()[0], last_name=name.split()[1], father_name=father_name).first()

    if not user_found is None:
        person = user_found.People[0]
        if dob == Utility.get_date_from_datetime(str(person.dob)):
            return person_schema.jsonify(person)
        else:
            return jsonify(Resources.dob_incorrect())
    else:
        return jsonify(Resources.error_detail_not_found())


@app.route('/person/aadhar_id/<aadhar_id>', methods=['GET'])
def person_scan(aadhar_id):
    person_found = db.session.query(People).filter_by(aadhar_id=aadhar_id).first()
    return person_schema.jsonify(person_found)


@app.route('/activity/', methods=['POST'])
def create_activity():
    activity_json = request.json
    user_id, person_id, location, _type = activity_json["user_id"], activity_json['person_id'], \
                                          activity_json["location"], activity_json["type"]
    new_activity = Activity(user_id, person_id, location, _type)
    db.session.add(new_activity)
    db.session.commit()
    return activity_schema.jsonify(new_activity)


# will be exclusively generated by @pitavya
@app.route('/license/', methods=['POST'])
def create_license():
    license_json = request.json
    user_id, app_id, validity = license_json["user_id"], os.environ["ASTRA_CODE"], license_json["validity"]

    try:
        Utility.parseDateYMD(validity)
    except Exception as e:
        return jsonify(Resources.license_invalid_validity())

    license_exist = db.session.query(License).filter_by(user_id=user_id).first()

    if not license_exist is None:
        return jsonify(Resources.error_license_exist())

    try:
        new_license = License(user_id, app_id, validity)
        db.session.add(new_license)
        db.session.commit()
        return license_schema.jsonify(new_license)
    except IntegrityError as e:
        return jsonify({"message" : "user don't exist"})



@app.route('/license/valid/', methods=['POST'])
def validate_license():
    license_json = request.json
    user_id, app_code, license_text = license_json["user_id"], os.environ["ASTRA_CODE"], license_json["license_key"]

    result = db.session.query(License).filter_by(user_id=user_id).first()
    if not result is None:
        try:
            return result.license_valid(user_id, app_code, license_text)
        except InvalidToken as i:
            return jsonify({"message" : "invalid license"})
    else:
        return jsonify({"message" : "user not found"})


@app.route('/email/', methods=['POST'])
def send_simple_message():
    email_data = request.json
    _from, to, subject, text, domain = \
        email_data["from"], email_data["to"], email_data["subject"], email_data["text"], email_data["domain"]
    return Utility.send_email(domain, _from,  to, subject, text).json()



@app.route('/', methods=['GET'])
def index():
    return render_template("index.html")


if __name__ == '__main__':
    app.run(debug=True)

