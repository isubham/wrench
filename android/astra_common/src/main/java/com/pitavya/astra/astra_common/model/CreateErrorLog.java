package com.pitavya.astra.astra_common.model;

import com.pitavya.astra.astra_common.tools.Constants;

public class CreateErrorLog {

    private String tag;
    private String errorDescription;
    private String timeStamp;
    private String errorStackTrace;

    public CreateErrorLog(String tag, String errorDescription, String timeStamp,String errorStackTrace) {
        this.tag = tag;
        this.errorDescription = errorDescription;
        this.timeStamp = timeStamp;
        this.errorStackTrace = errorStackTrace;
    }

    @Override
    public String toString() {

        return "{\n" + Constants.ERROR_TAG + ":'" + tag + '\'' +
                ",\n" + Constants.ERROR_DESCRIPTION + ":'" + errorDescription + '\'' +
                ",\n" + Constants.ERROR_TIMESTAMP + ":'" + timeStamp + '\'' +
                ",\n" +Constants.ERROR_STACKTRACE+ ":\"" + errorStackTrace + '\"' +
                "\n},";
    }
}
