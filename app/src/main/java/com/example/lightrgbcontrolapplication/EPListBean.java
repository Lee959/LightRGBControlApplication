package com.example.lightrgbcontrolapplication;

import com.example.lightrgbcontrolapplication.owon.sdk.util.Constants;

public class EPListBean {
    private String name;
    private String id;
    private int deviceType;
    private boolean linkStatus;

    public EPListBean(String name, String id, int deviceType, boolean linkStatus) {
        this.name = name;
        this.id = id;
        this.deviceType = deviceType;
        this.linkStatus = linkStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(boolean linkStatus) {
        this.linkStatus = linkStatus;
    }

    public String getDeviceTypeName() {
        switch (deviceType) {
            case Constants.LIGHT_601:
                return "灯，只有开关";
            case Constants.LIGHT_EXTEND_LO_COLOR_TEMP_GOODVB:
                return "可调节亮度和色温灯";
            case DeviceTypeCode.TH_SENSOR:
                return "温湿度传感器";
            case DeviceTypeCode.LX_SENSOR:
                return "光照传感器";
            case DeviceTypeCode.SMOKE_SENSOR_ZONE:
                return "烟雾传感器";
            case DeviceTypeCode.MOTION_SENSOR_ZONE:
                return "人体传感器";
            case DeviceTypeCode.AC_SENSOR:
                return "红外转发器";
            case DeviceTypeCode.WARN_SENSOR:
                return "声光报警器";
            case DeviceTypeCode.WARN_MOTOR:
                return "窗帘电机";
            case DeviceTypeCode.DOOR_SENSOR:
                return "门磁传感器";
            default:
                return "未知设备";
        }
    }
}