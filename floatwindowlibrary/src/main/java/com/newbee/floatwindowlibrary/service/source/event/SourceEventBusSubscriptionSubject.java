package com.newbee.floatwindowlibrary.service.source.event;

import java.util.ArrayList;
import java.util.List;

public class SourceEventBusSubscriptionSubject implements SourceEventBusSubject {

    private static SourceEventBusSubscriptionSubject subscriptionSubject;
    private List<SourceEventBusObserver> observerList = new ArrayList<>();

    private SourceEventBusSubscriptionSubject() {

    }

    public static SourceEventBusSubscriptionSubject getInstance() {
        if (null == subscriptionSubject) {
            synchronized (SourceEventBusSubscriptionSubject.class) {
                if (null == subscriptionSubject) {
                    subscriptionSubject = new SourceEventBusSubscriptionSubject();
                }
            }
        }
        return subscriptionSubject;
    }

    public void close() {
        observerList.clear();
    }


    @Override
    public void addObserver(SourceEventBusObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void delectObjserver(SourceEventBusObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void eventListen(SourceEventType eventType, Object... objects) {
        for (SourceEventBusObserver observer : observerList) {
            observer.eventListen(eventType, objects);
        }
    }


}
