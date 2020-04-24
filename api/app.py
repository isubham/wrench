import os
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
from flask import Flask, render_template, request, jsonify
import hashlib
from sqlalchemy.exc import IntegrityError
import json

app = Flask(__name__)

app.config.from_object(os.environ['APP_SETTINGS'])
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)
mb = Marshmallow(app)

from models import User, user_schema

@app.route('/auth/signup/', methods=['POST'])
def signup():
    signup_details = request.json
    user = User(signup_details["email"], get_hash(signup_details["password"]), os.environ["ASTRA_CODE"])
    try:
        db.session.add(user)
        db.session.commit()
        return user_schema.jsonify(user)
    except IntegrityError as e:
        return user_schema.jsonify(user)


@app.route('/auth/signin/', methods=['POST'])
def sign_in():
    sign_in_details = request.json

    user_found = db.session.query(User).filter_by(email=sign_in_details["email"],
                                             password=get_hash(sign_in_details["password"])).first()
    if user_found is None:
        return jsonify("Email and password don't match", {}, False)
    else:
        user = user_schema.load(user_found)
        user.update_modified_to_current_time()
        db.session.commit()

        return user_schema.jsonify(user_found)



@app.route('/auth/forgotpassword/', methods=['POST'])

@app.route('/auth/changepassword/', methods=['POST'])

@app.route('/auth/verifycode/', methods=['POST'])




def get_hash(data):
    return hashlib.sha3_512(data.encode()).hexdigest()

if __name__ == '__main__':
    app.run(debug=True)

