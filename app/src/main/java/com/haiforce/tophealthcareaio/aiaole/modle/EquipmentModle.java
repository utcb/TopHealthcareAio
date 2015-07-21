package com.haiforce.tophealthcareaio.aiaole.modle;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

public class EquipmentModle implements Serializable {
	public static final String DEBUG = Gloal.DEBUG + "EquipmentModle";

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int getIcon_type() {
		return icon_type;
	}

	public void setIcon_type(int icon_type) {
		this.icon_type = icon_type;
	}

	public static final int MODE_RECORD_DATA = 0;
	public static final int MODE_EQUIPMENT = 1;
	public static final int MODE_EQUIPMENT_EDIT = 2;
	public static final int MODE_RECORD_BTN = 3;
	// 图标样式
	public static final int BLOOD_PRESSURE_BLUETOOTH = 0;
	public static final int BLOOD_PRESSURE = 1;
	public static final int HEART_RATE_BLUETOOTH = 2;
	public static final int HEART_RATE = 3;

	public static final int ERWENJI_BLUETOOTH = 5;

	private String state;
	private String name;
	private String time;
	private int showMode;
	private boolean showNumerical;
	private List<NumericalModle> numericalList;
	private String deviceSerial;
	private String deviceType;
	private String device_id;
	private String deviceNetType;
	private String android_mac;
	
	private String userNumber;

	private int viewCount;

	public String getAndroid_mac() {
		return android_mac;
	}

	public void setAndroid_mac(String android_mac) {
		this.android_mac = android_mac;
	}

	private String isAdmin;

	private NumericalModle userMeasure;

	public NumericalModle getUserMeasure() {
		return userMeasure;
	}

	public void setUserMeasure(NumericalModle userMeasure) {
		this.userMeasure = userMeasure;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getDeviceNetType() {
		return deviceNetType;
	}

	public void setDeviceNetType(String deviceNetType) {
		this.deviceNetType = deviceNetType;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceSerial() {
		return deviceSerial;
	}

	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}

	private int icon_type;

	public boolean isShowNumerical() {
		return showNumerical;
	}

	public void setShowNumerical(boolean showNumerical) {

		Log.d(DEBUG, "设置:" + showNumerical);
		this.showNumerical = showNumerical;
	}

	public List<NumericalModle> getNumericalList() {
		return numericalList;
	}

	public void setNumericalList(List<NumericalModle> numericalList) {
		this.numericalList = numericalList;
	}

	public int getShowMode() {
		return showMode;
	}

	public void setShowMode(int showMode) {
		this.showMode = showMode;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// 显示手动数值
	private String maxValue; // 最大值
	private String minValue; // 最小值
	private String updateTime; // 更新时间

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public boolean equals(Object o) {
		EquipmentModle m = (EquipmentModle) o;
		if (deviceSerial.equals(m.getDeviceSerial())) {
			return true;
		} else {
			return false;
		}

	}
}
