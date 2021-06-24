package com.navercorp.pinpoint.bootstrap.context;

import com.navercorp.pinpoint.common.trace.LoggingInfo;

public interface SpanRecorder extends SpanCommonRecorder {

    boolean canSampled();

    boolean isRoot();

    void recordStartTime(long startTime);

    void recordTime(boolean autoTimeRecoding);

    void recordError();

    void recordRpcName(String rpc);

    void recordRemoteAddress(String remoteAddress);
    
    void recordParentApplication(String parentApplicationName, short parentApplicationType);

    void recordAcceptorHost(String host);
    
    void recordLogging(LoggingInfo loggingInfo);

    void recordStatusCode(int statusCode);
}