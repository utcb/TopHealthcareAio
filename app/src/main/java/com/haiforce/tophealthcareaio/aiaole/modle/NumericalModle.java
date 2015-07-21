package com.haiforce.tophealthcareaio.aiaole.modle;

import java.io.Serializable;

/*
 * 血压、心率 实体
 */
public class NumericalModle implements Serializable {

	public String getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}

	public static final String ID = "_id";
	public static final String MEASURE_ID = "measure_id";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	public static final int BOOLPRESSRE_TYPE = 1;
	public static final int HEARTRATE_TYPE = 3;
	public static final int BLOODSUGAR_TYPE = 2;
	public static final int BLOODFAT_TYPE = 4;
	public static final int ERWEN_TYPE = 5;

	public static final int MAX_BLOODPRESSURE = 120;
	public static final int MIN_BLOODPRESSURE = 80;

	public String getDate() {
	
		
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public int getMinBloodPressure() {
		return minBloodPressure;
	}

	public void setMinBloodPressure(int minBloodPressure) {
		this.minBloodPressure = minBloodPressure;
	}

	public int getMaxBloodPressure() {
		return maxBloodPressure;
	}

	public void setMaxBloodPressure(int maxBloodPressure) {
		this.maxBloodPressure = maxBloodPressure;
	}

	private String date="";
	private int heartRate;
	private int minBloodPressure;
	private int maxBloodPressure;
	private boolean editMode;
	private boolean showFla = true;

	private String measure_id;

	private String result1="";
	private String result2="";
	private String result3="";
	private String result4="";

	private String viewType;
	private String measureType="";
	private String deviceId;

	private String deviceSerial;
	
	private String viewFlag;

	public String getDeviceSerial() {
		return deviceSerial;
	}

	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getMeasureType() {
		return measureType;
	}

	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	public String getResult1() {
		return result1;
	}

	public void setResult1(String result1) {
		this.result1 = result1;
	}

	public String getResult2() {
		return result2;
	}

	public void setResult2(String result2) {
		this.result2 = result2;
	}

	public String getResult3() {
		return result3;
	}

	public void setResult3(String result3) {
		this.result3 = result3;
	}

	public String getResult4() {
		return result4;
	}

	public void setResult4(String result4) {
		this.result4 = result4;
	}

	public String getMeasure_id() {
		return measure_id;
	}

	public void setMeasure_id(String measure_id) {
		this.measure_id = measure_id;
	}

	public boolean isShow() {
		return showFla;
	}

	public void setShowFla(boolean showFla) {
		this.showFla = showFla;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	@Override
	public boolean equals(Object o) {
		NumericalModle modle = (NumericalModle) o;
		boolean isEqual = true;

		if (!result1.equals(modle.getResult1())) {
			return false;
		}

		if (!result2.equals(modle.getResult2())) {
			return false;
		}

		if (!result3.equals(modle.getResult3())) {
			return false;
		}

		if (!result4.equals(modle.getResult4())) {
			return false;
		}

		if (!date.equals(modle.getDate())) {
			return false;
		}

		return isEqual;
	}

	@Override
	public String toString() {
		return super.toString() +
				"{deviceId: " + getDeviceId() +
				"serial: " + getDeviceSerial() +
				"time: " + getDate() +
				"bpHigh: " + getMaxBloodPressure() +
				"bpLow: " + getMinBloodPressure() +
				"hr: " + getHeartRate() +
				"result1: " + getResult1() +
				"result2: " + getResult2() +
				"result3: " + getResult3() +
				"result4: " + getResult4() +
				"}";
	}
}
