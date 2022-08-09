package com.newbee.floatwindowlibrary.service.source.event;

public interface SourceEventBusSubject {

    public void addObserver(SourceEventBusObserver observer);

    public void delectObjserver(SourceEventBusObserver observer);

    public void eventListen(SourceEventType eventType, Object... objects);

}
