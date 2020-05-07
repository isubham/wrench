from flask import request, jsonify
from app import db
from models import Activity
from resources import Resources
from . import routes


@routes.route('/activity/', methods=['POST'])
def create_activity():
    activity_json = request.json
    user_id, person_id, location, _type = activity_json["user_id"], activity_json['person_id'], \
                                          activity_json["location"], activity_json["type"]
    new_activity = Activity(user_id, person_id, location, _type)
    db.session.add(new_activity)
    db.session.commit()
    return jsonify(Resources.activity_logged())
