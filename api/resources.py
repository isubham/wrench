
class Resources:

    @staticmethod
    def error_existing_email():
        return {"error": "Account with same email exists"}

    @staticmethod
    def error_email_password_incorrect():
        return {"message": "Incorrect email or password"}

    @staticmethod
    def error_existing_username_or_aadhar_id():
        return {"message": "Aadhar or Username exists"}

    @staticmethod
    def error_detail_not_found():
        return {"message": "No user with this detail"}


    @staticmethod
    def dob_incorrect():
        return {"message": "Incorrect Date of Birth"}


    @staticmethod
    def license_invalid_validity():
        return {"message": "invalid validity"}

    @staticmethod
    def error_license_exist():
        return {"message": "License exist"}

