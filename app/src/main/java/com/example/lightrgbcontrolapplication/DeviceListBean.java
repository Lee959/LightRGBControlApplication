package com.example.lightrgbcontrolapplication;

import java.util.List;

public class DeviceListBean {
    private List<LightRGBDeviceBean> devices;

    public DeviceListBean(List<LightRGBDeviceBean> devices) {
        this.devices = devices;
    }

    public List<LightRGBDeviceBean> getDevices() {
        return devices;
    }

    public void setDevices(List<LightRGBDeviceBean> devices) {
        this.devices = devices;
    }
}