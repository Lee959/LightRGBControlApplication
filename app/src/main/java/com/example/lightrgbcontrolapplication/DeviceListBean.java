package com.example.lightrgbcontrolapplication;

import java.util.List;

public class DeviceListBean {
    private List<LightDeviceBean> devices;

    public DeviceListBean(List<LightDeviceBean> devices) {
        this.devices = devices;
    }

    public List<LightDeviceBean> getDevices() {
        return devices;
    }

    public void setDevices(List<LightDeviceBean> devices) {
        this.devices = devices;
    }
}