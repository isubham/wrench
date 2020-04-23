import os
import json
from flask_sqlalchemy import SQLAlchemy
from flask import Flask, render_template, request, jsonify
import hashlib
from sqlalchemy.exc import IntegrityError

app = Flask(__name__)

app.config.from_object(os.environ['APP_SETTINGS'])
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

from models import User


@app.route('/auth/signup/', methods=['POST'])
def signup():
    signup_details = request.json
    user = User(signup_details["email"], get_hash(signup_details["password"]), os.environ["ASTRA_CODE"])
    try:
        db.session.add(user)
        db.session.commit()
        return jsonify("account created", {"id" : user.id}, True)
    except IntegrityError as e:
        return jsonify("account didnt created", {"details": {"id": ""}}, False)


@app.route('/auth/signin/', methods=['POST'])
def sign_in():
    sign_in_details = request.json
    all_users = User(db.session.query(User).filter_by(email=sign_in_details["email"],
                                                 password=get_hash(sign_in_details["password"])).first())


@app.route('/auth/forgotpassword/', methods=['POST'])

@app.route('/auth/changepassword/', methods=['POST'])

@app.route('/auth/verifycode/', methods=['POST'])




def get_hash(data):
    return hashlib.sha256(data.encode()).hexdigest()

if __name__ == '__main__':
    app.run(debug=True)

