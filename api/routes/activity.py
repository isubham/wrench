from flask import request, jsonify
from app import db
from models import Activity
from resources import Resources
from . import routes
from utility import Utility

@routes.route('/activity/', methods=['POST'])
def create_activity():
    token = request.environ['HTTP_AUTHORIZATION']
    user_id = Utility.get_payload_from_jwt(token)["id"]
    activity_json = request.json
    person_id, location, _type = activity_json['person_id'], activity_json["location"], activity_json["type"]
    new_activity = Activity(user_id, person_id, location, _type)
    db.session.add(new_activity)
    db.session.commit()
    return jsonify(Resources.data["activity_logged"])
