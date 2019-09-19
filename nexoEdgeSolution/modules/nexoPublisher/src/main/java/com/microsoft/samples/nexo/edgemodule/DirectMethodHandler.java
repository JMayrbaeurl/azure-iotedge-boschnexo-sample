package com.microsoft.samples.nexo.edgemodule;

/**
 * DirectMethodHandler
 */
public interface DirectMethodHandler {

    public static final String STD_OK_Response = "OK";
    public static final String STD_ERROR_Response = "Error";

    public String handleDirectMethodCall(Object methodData, Object context);
}