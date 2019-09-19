package com.microsoft.samples.nexo.edgemodule;

/**
 * PerformanceStatisticsMgrHandler
 */
public class ResetStatsHandler implements DirectMethodHandler {

    private final PerformanceStatisticsMgr performanceStatisticsMgr;

    public ResetStatsHandler(PerformanceStatisticsMgr mgr) {
        this.performanceStatisticsMgr = mgr;
    }

    @Override
    public String handleDirectMethodCall(Object methodData, Object context) {

        this.performanceStatisticsMgr.getStats().setNumberOfRequest(0);
        this.performanceStatisticsMgr.getStats().setNumberOfDeliveries(0);

        return DirectMethodHandler.STD_OK_Response;
    }
    
}