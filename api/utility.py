import hashlib
import datetime
from random import randint


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

