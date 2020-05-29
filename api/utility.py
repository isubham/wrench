import hashlib
import datetime
from random import randint
import requests
import os
import jwt


class Utility:

    @staticmethod
    def get_hash(data):
        return hashlib.sha3_512(data.encode()).hexdigest()

    @staticmethod
    def get_date(date_string):
        return datetime.datetime.strptime(date_string, '%d-%m-%Y').date()

    @staticmethod
    def get_date_from_datetime(date_time):
        return datetime.datetime.strptime(date_time, '%Y-%m-%d %H:%M:%S').date()

    @staticmethod
    def parseDateYMD(date_string, formatter='%Y-%m-%d'):
        return datetime.datetime.strptime(date_string, formatter).date()

    @staticmethod
    def random_with_n_digits(n):
        range_start = 10 ** (n - 1)
        range_end = (10 ** n) - 1
        return randint(range_start, range_end)

    @staticmethod
    def send_email(domain, _from, to, subject, text):
        return requests.post(
            "https://api.mailgun.net/v3/{}/messages".format(domain),
            auth=("api", os.environ['MAILGUN_API_KEY']),
            data={"from": _from, "to": [to], "subject": subject, "text": text})


    @staticmethod
    def create_secret(data):
        return jwt.encode(data, os.environ['JWT_SECRET'], algorithm='HS256')


    @staticmethod
    def get_payload_from_jwt(encoded_data):
        # contains "Basic Key" so splitting
        cleaned_jwt = encoded_data.split()[1].encode()
        found_data = jwt.decode(cleaned_jwt, os.environ['JWT_SECRET'], algorithm=['HS256'])
        return found_data


    @staticmethod
    def get_datetime(date_string):
        _date, _month, _year = list(map(int, date_string.split("-")))
        date =  datetime(day=_date, month=_month, year=_year)
        return date


    @staticmethod
    def make_directory(path, directory_name):
        directory_path = os.path.join(path, directory_name)
        if not os.path.exists(directory_path):
            os.mkdir(directory_path)
        
