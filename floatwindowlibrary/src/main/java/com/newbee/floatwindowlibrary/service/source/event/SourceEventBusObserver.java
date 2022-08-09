package com.newbee.floatwindowlibrary.service.source.event;

public interface SourceEventBusObserver {

    public void eventListen(SourceEventType eventType, Object... objects);
}
