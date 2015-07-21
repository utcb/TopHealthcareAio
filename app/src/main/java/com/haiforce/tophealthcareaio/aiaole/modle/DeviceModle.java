package com.haiforce.tophealthcareaio.aiaole.modle;

import android.bluetooth.BluetoothDevice;

import com.xtremeprog.sdk.ble.BleGattCharacteristic;

import java.util.List;

public class DeviceModle {
	BluetoothDevice device;
	byte[] messageByte;
	byte[] resultByte;
	String uuid;
	List<BleGattCharacteristic> mWriteCharacteristices;
	List<BleGattCharacteristic> mNoticeCharacteristices;
	public List<BleGattCharacteristic> getmNoticeCharacteristices() {
		return mNoticeCharacteristices;
	}

	public void setmNoticeCharacteristices(
			List<BleGattCharacteristic> mNoticeCharacteristices) {
		this.mNoticeCharacteristices = mNoticeCharacteristices;
	}

	String sendTime;
	String dName;
	int fla = 0;

	public int getFla() {
		return fla;
	}

	public void setFla(int fla) {
		this.fla = fla;
	}

	public static final int UNLINK_STATE = 0;// 未连接状态
	public static final int LINK_STATE = 1;// 连接状态
	public static final int SYN_STATE = 2;// 数据上传状态
	public static final int UNBind_STATE = 3;// 解除绑定状态

	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}

	int gcCode;

	public int getGcCode() {
		return gcCode;
	}

	public void setGcCode(int gcCode) {
		this.gcCode = gcCode;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public List<BleGattCharacteristic> getmWriteCharacteristices() {
		return mWriteCharacteristices;
	}

	public void setmWriteCharacteristices(List<BleGattCharacteristic> mWriteCharacteristices) {
		this.mWriteCharacteristices = mWriteCharacteristices;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	boolean isGet = false;

	public boolean isGet() {
		return isGet;
	}

	public void setGet(boolean isGet) {
		this.isGet = isGet;
	}

	public BluetoothDevice getDevice() {
		return device;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}

	public byte[] getMessageByte() {
		return messageByte;
	}

	public void setMessageByte(byte[] messageByte) {
		this.messageByte = messageByte;
	}

	public byte[] getResultByte() {
		return resultByte;
	}

	public void setResultByte(byte[] resultByte) {
		this.resultByte = resultByte;
	}

}
