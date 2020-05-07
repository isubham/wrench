import os
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
from flask import Flask, render_template
import jwt

app = Flask(__name__)

app.config.from_object(os.environ['APP_SETTINGS'])
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)
mb = Marshmallow(app)

from routes import *
app.register_blueprint(routes)


unprotected_paths = {'/auth/signup/', '/auth/signin/', '/person/create/', '/person/fuzzy/', '/license/create/',
                     '/license/validate/'}
details = dict()


@app.before_request
def before_request_callback():
    details["method"] = request.method
    details["path"] = request.path

    if request.headers.__contains__('Content-Type'):
        if request.headers['Content-Type'] == 'application/json':
            details["data"] = request.json

    if not unprotected_paths.__contains__(details["path"]):
        token = request.environ['HTTP_AUTHORIZATION']
        try:
            Utility.get_payload_from_jwt(token)["id"]
        except jwt.ExpiredSignatureError:
            return jsonify({"message": "token expired"})
        except jwt.InvalidTokenError:
            return jsonify({"message": "token invalid"})



@app.route('/', methods=['GET'])
def index():
    return render_template("index.html")


if __name__ == '__main__':
    app.run(debug=True)

