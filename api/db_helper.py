from flask_sqlalchemy import SQLAlchemy


class db_helper:

    @staticmethod
    def insert(db:SQLAlchemy, obj):
        db.session.add(obj)
        db.session.commit()



