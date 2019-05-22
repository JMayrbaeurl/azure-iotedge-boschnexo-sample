package com.microsoft.samples.nexo.edgemodule;

/**
 * DirectMethodHandler
 */
public interface DirectMethodHandler {

    public boolean handleDirectMethodCall(Object methodData, Object context);
}