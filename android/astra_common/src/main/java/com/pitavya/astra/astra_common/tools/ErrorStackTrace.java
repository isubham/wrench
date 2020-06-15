package com.pitavya.astra.astra_common.tools;

public class ErrorStackTrace {

    private String fileName;
    private String methodName;
    private String lineNumber;
    private String isNativeMethod;

    public ErrorStackTrace(String fileName, String methodName, String lineNumber, String isNativeMethod) {
        this.fileName = fileName;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
        this.isNativeMethod = isNativeMethod;
    }

    public static String toString(StackTraceElement ste) {
        return " fileName:'" + ste.getFileName() + '\'' +
                ", methodName:'" + ste.getMethodName() + '\'' +
                ", lineNumber:'" + ste.getLineNumber() + '\'' +
                ", isNativeMethod:'" + ste.isNativeMethod() + '\'';
    }
}
