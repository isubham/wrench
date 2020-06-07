package com.pitavya.astra.astra_common.tools;

public class Constants {

    // Gallery directory name to store the images or videos
    public static final String APP_DIRECTORY = "Astra";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final float BYTES_IN_MB = 1024.0f * 1024.0f;
    public static final float BYTES_PER_PX = 4.0f;
    public static final String FRONT_DOC = "FRONT";
    public static final String BACK_DOC = "BACK";
    public static final String PROFILE_PIC = "PROFILE_PIC";
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int HIGH_QUALITY = 100;
    public static final int MID_QUALITY = 80;

    // Parse Constants
    public static final String EMPTY_STRING = "";
    public static final String EMPTY_JSON = "{}";

    //AdminHomeScreen
    public static final String INVALID_USERNAME = "Invalid UserName";
    public static final String USER_NAME = "USER_NAME";
    public static final String NAME = "NAME";
    public static final String ID = "id";
    public static final String TOKEN = "token";


    public static final String ID_FRONT_URL = "ID_FRONT";
    public static final String ID_BACK_URL = "ID_BACK";
    public static final String PROFILE_PIC_URL = "PROFILE_PIC_URL";
    public static final String USER_TYPE = "USER_TYPE";
    public static final int USER_TYPE_ADMIN = 1;
    public static final int USER_TYPE_GENERAL = 2;
    public static final String ACTION_IN_OUT = "ACTION_IN_OUT";
    public static final int ACTION_IN = 1;
    public static final int ACTION_OUT = 2;


    public static final String GENERAL_USER_CLASS_JSON_RESPONSE = "GENERAL_USER_JSON_RESPONSE";


    public static final String WEAK_INTERNET = "com.android.volley.TimeoutError";
    public static final String NO_INTERNET = "com.android.volley.NoConnectionError";
    public static final String SERVER_ERROR = "com.android.volley.ServerError";

    //////////////////////////////////////////////////
    public static final String RESPONSE = "Response";

    public static final String TOKEN_INVALID = "token invalid";
    public static final String TOKEN_EXPIRED = "Token Expired";
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String UNPARSABLE_RESPONSE = "UnParsable Response Code";
    public static final long DELAYED_CLOSE_FOR_RESULT = 2000;
    public static final boolean TRUE = true;
    public static final boolean FALSE = false;
    public static final String VERIFICATION_SUCCESSFUL = "Verification Successful";
    public static final String TRY_AGAIN = "Verification Unsuccessful !! Try Again ..";
    public static final String NO_INTERNET_CONNECTION = "No Internet Connection";
    public static final String WEAK_INTERNET_CONNECTION = "Weak Internet Connection";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String GPS_IS_OFF = "Location is off";
    public static String password_confirm_password_dont_match = "Password and Confirm password do not match";


    //DIRECTORIES
    public static final String APP_LOGS = Constants.APP_DIRECTORY + " Logs";
    public static final String APP_IMAGES = Constants.APP_DIRECTORY + " Images";
    public static final String APP_LOG_FILE = "bug_report_log";

    //FILE EXTENSIONS
    public static final String IMAGE_EXTENSION = ".jpg";
    public static final String TXT_EXTENSION = ".txt";

    //CREATE ERROR LOGS
    public static final String ERROR_DESCRIPTION = "errorDescription";
    public static final String ERROR_TAG = "tag";
    public static final String ERROR_TIMESTAMP = "timestamp";





}
