package io.github.isubham.astra_client.tools;

public class Endpoints {

    public static final String LOCAL_URL = "";
    public static final String HEROKU_URL = "https://aastra-stag.herokuapp.com/";
    public static final String SERVER_URL = HEROKU_URL;

    public static final String SIGN_IN = SERVER_URL + "auth/signin/";
    public static final String CREATE_GENERAL_USER = SERVER_URL + "person/create/";
    public static final String CREATE_LOG = SERVER_URL + "activity/";
    public static final String SEARCH_BY_USER_NAME = SERVER_URL + "person/username/";
    public static final String SEARCH_EXISTING_USER = SERVER_URL + "person/fuzzy/";
    public static final String DOWNLOAD_REPORT = SERVER_URL + "activity/%1$s/%2$s/";


}
