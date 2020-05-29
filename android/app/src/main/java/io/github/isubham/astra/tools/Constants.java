package io.github.isubham.astra.tools;

public class Constants {

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "Astra";
    public static final String IMAGE_EXTENSION = "jpg";
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
    // Base App URL
    public static final String LOCAL_URL = "";
    public static final String HEROKU_URL = "https://aastra-stag.herokuapp.com/";
    public static final String SERVER_URL = HEROKU_URL;
    public static final String EMPTY_STRING = "";
    public static final String EMPTY_JSON = "{}";
    public static final String DOWNLOAD_REPORT = SERVER_URL + "activity/%1$s/%2$s/";

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
    public static String password_confirm_password_dont_match = "Password and Confirm password do not match";

}
