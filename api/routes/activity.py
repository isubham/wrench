from flask import request, jsonify, send_file
from app import db
from models import Activity, activity_schema, activities_schema
from models import User
from resources import Resources
from . import routes
from utility import Utility
from datetime import datetime
from ctypes import cast
from sqlalchemy.types import Date
from _datetime import timedelta
from openpyxl import Workbook
import os


@routes.route('/activity/', methods=['POST'])
def create_activity():
    token = request.environ['HTTP_AUTHORIZATION']
    user_id = Utility.get_payload_from_jwt(token)["id"]
    activity_json = request.json
    person_id, location, _type, purpose = activity_json['person_id'], activity_json["location"], activity_json["type"], activity_json["purpose"]
    new_activity = Activity(user_id, person_id, location, _type, purpose)
    db.session.add(new_activity)
    db.session.commit()
    return jsonify(Resources.data["activity_logged"])


@routes.route('/activity/<start_date>/<end_date>/', methods=['GET'])
def view_activity(start_date, end_date):
    token = request.environ['HTTP_AUTHORIZATION']
    _user_id = Utility.get_payload_from_jwt(token)["id"]
     
    base_report_path = './data/report'
    user_folder = str(_user_id)
    report_file_namea = '{}_{}.xlsx'.format(start_date, end_date)
    report_path = os.path.join(base_report_path, user_folder, report_file_namea)
    report_absolute_location = os.path.abspath(report_path)
    Utility.make_directory(base_report_path, user_folder)

    if os.path.exists(report_absolute_location):
        os.remove(report_absolute_location)
    else:
        parsed_start_date = get_date(start_date)
        parsed_end_date   = get_date(end_date) + timedelta(days=1)
        result = db.session.query(Activity).filter(Activity.when >= parsed_start_date, Activity.when < parsed_end_date , Activity.user_id==_user_id).all()

        upsert_activities(result, start_date, end_date, report_absolute_location)

    return send_file(report_path, attachment_filename=report_file_namea)


def get_date(date_time_string):
    _day, _month, _year = list(map(int, date_time_string.split("-")))
    return datetime(day=_day, month=_month, year=_year)


  


def upsert_activities(activities, start_date, end_date, report_location):
    
        workbook = Workbook()
        sheet = workbook.active
        sheet.title = "{}_{}".format(start_date, end_date)

        sheet.append(["DATE", "IN/OUT", "FULL NAME", "FATHER NAME",  "MOBILE", "PURPOSE"])

        row_readable = {1 : "in", 2 : "out"}
        for row in activities:
            sheet.append([row.when.strftime("%d-%B-%Y %H:%M"), row_readable[row.type], row.People.first_name, row.People.father_name, row.People.contact, row.purpose])

        workbook.save(report_location)
