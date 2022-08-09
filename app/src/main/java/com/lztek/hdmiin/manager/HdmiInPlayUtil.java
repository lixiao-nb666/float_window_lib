package com.lztek.hdmiin.manager;

import com.lztek.hdmiin.IHdmiIn;

public class HdmiInPlayUtil {
    private IHdmiIn mHdmiIn;


    public void close() {
        this.mHdmiIn = null;
    }

    public void setIHdminIn(IHdmiIn mHdmiIn) {
        this.mHdmiIn = mHdmiIn;
    }


    public IHdmiIn getmHdmiIn() {
        return mHdmiIn;
    }

}
