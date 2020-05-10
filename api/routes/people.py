import os
from flask import request, jsonify
from app import db
from utility import Utility
from models import User, People, person_schema
from sqlalchemy.exc import IntegrityError
from . import routes
from resources import Resources

@routes.route('/person/create/', methods=['POST'])
def create_people_by_admin():
    people = request.json
    # user = User(people["email"], people["password"], os.environ["ASTRA_CODE"])
    user = User(None, None, os.environ["ASTRA_CODE"])

    person_found_by_aadhar = db.session.query(People).filter_by(aadhar_id=people["aadhar_id"]).first()
    if person_found_by_aadhar is not None:
        return jsonify(Resources.data["error_existing_aadhar_id"])

    person_found_by_username = db.session.query(People).filter_by(username=people["username"]).first()
    if person_found_by_username is not None:
        return jsonify(Resources.data["error_existing_username"])

    try:
        db.session.add(user)
        db.session.flush()
    except IntegrityError as e:
        return jsonify(Resources.data["error_existing_email"])

    people = People(user.id, people["name"], None, Utility.get_date(people["dob"]), people["profile_pic"],
                    people["id_front"], people["id_back"], people["father_name"], people["username"], people["created_by"],
                    people['contact'], people['address'], people['pincode'], people['email'], people["aadhar_id"])
    try:
        db.session.add(people)
        db.session.commit()
        return jsonify({"token": Utility.create_secret({"id": user.id}).decode()})
    except IntegrityError as e:
        db.session.rollback()
        return jsonify(Resources.data["error_existing_username_or_aadhar_id"])




@routes.route('/person/token/', methods=['GET'])
def person_by_token():
    token = request.environ['HTTP_AUTHORIZATION']
    user_id = Utility.get_payload_from_jwt(token)["id"]
    person_found = db.session.query(People).filter_by(user_id=user_id).first()
    return person_schema.jsonify(person_found)


@routes.route('/person/id/<_id>', methods=['GET'])
def get_person(_id):
    person_found = db.session.query(People).filter_by(user_id=_id).first()
    return person_schema.jsonify(person_found)


@routes.route('/person/username/<_username>', methods=['GET'])
def get_person_by_username(_username):
    person_found = db.session.query(People).filter_by(username=_username).first()
    return person_schema.jsonify(person_found)


@routes.route('/person/fuzzy/', methods=['POST'])
def person_search():
    first_name, father_name, dob = request.json["name"], request.json["father_name"], \
                                   Utility.get_date(request.json["dob"])
    user_found = db.session.query(People) \
        .filter_by(first_name=first_name, father_name=father_name, dob = dob).first()

    if user_found is not None:
        return person_schema.jsonify(user_found)
    else:
        return jsonify(Resources.data["error_detail_not_found"])


@routes.route('/person/aadhar_id/<aadhar_id>', methods=['GET'])
def person_scan(aadhar_id):
    person_found = db.session.query(People).filter_by(aadhar_id=aadhar_id).first()
    return person_schema.jsonify(person_found)
