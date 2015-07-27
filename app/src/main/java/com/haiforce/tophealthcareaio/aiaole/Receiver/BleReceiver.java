package com.haiforce.tophealthcareaio.aiaole.Receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.haiforce.tophealthcareaio.BaseActivity;
import com.haiforce.tophealthcareaio.MainActivity;
import com.haiforce.tophealthcareaio.R;
import com.haiforce.tophealthcareaio.aiaole.activity.BindSuccessActivity;
import com.haiforce.tophealthcareaio.aiaole.activity.BleApplication;
import com.haiforce.tophealthcareaio.aiaole.modle.DeviceModle;
import com.haiforce.tophealthcareaio.aiaole.modle.Gloal;
import com.haiforce.tophealthcareaio.aiaole.modle.NumericalModle;
import com.haiforce.tophealthcareaio.aiaole.util.CommonUtil;
import com.haiforce.tophealthcareaio.aiaole.util.NetRestClient;
import com.haiforce.tophealthcareaio.aiaole.util.Utils;
import com.xtremeprog.sdk.ble.BleGattCharacteristic;
import com.xtremeprog.sdk.ble.BleGattService;
import com.xtremeprog.sdk.ble.BleService;
import com.xtremeprog.sdk.ble.IBle;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BleReceiver extends BroadcastReceiver {
	public static final String DEBUG = Gloal.DEBUG + "BleReceiver";

	public final static String POSTION_STR = "postionStr";
	public final static String SHOW_DEVICESERIAL = "showDeviceSerial";
	public static final String CHANGE_DATA = "changeData";

	private static BleReceiver bleReceiver;

	public static BleReceiver getInstence() {
		if (null == bleReceiver) {
			bleReceiver = new BleReceiver();
		}
		return bleReceiver;
	}

    ByteArrayOutputStream bufferSB = new ByteArrayOutputStream();

	Context context;

	private IBle mBle;

	public Handler mHandler = new Handler();

	public static final String SCAN_ACTION = "BleApplication.scanAction";
	public static final String SCAN_FLA = "scanFla";

	public static final int SCAN_OPEN = 101;
	public static final int SCAN_COLSE = 102;

	public static final String SCAN_STATE = "BleApplication.scanState";
	public static final String UPDATE_SCANVIEW = "updateScanView";
	public static final String ENTER_MEASURE = "enter_measure";

	public SharedPreferences preferences;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";

	public final static String BP_SERVICE = "D44BC439-ABFD-45A2-B575-925416129601";
	public final static String ERWEN_SERVICE = "BEF8D6C9-9C21-4C9E-B632-BD58C1009F9F";
	public final static String WRITE_CHARACTERISTIC = "D44BC439-ABFD-45A2-B575-925416129600";

	public final static String bp = "00001002-0000-1000-8000-00805F9B34FB";
	public static String BP_CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

	public final static String unBp = "00002a";

	// 耳温计标示
	public final static String ERWEN_BIAOSHI = "424634303330";

	int guocheng_count = 0;
	long lastGuoCheng;

	@Override
	public void onReceive(final Context context, Intent intent) {

		this.context = context;
		if (null == preferences)
			preferences = context.getSharedPreferences(BaseActivity.SHARE_NAME, 0);
		String action = intent.getAction();

		// 读取
		Bundle extras = intent.getExtras();
		final String address = extras.getString(BleService.EXTRA_ADDR);
		String uuid = extras.getString(BleService.EXTRA_UUID);

		if (BleService.BLE_NOT_SUPPORTED.equals(action)) {
			Log.d(DEBUG, "不支持");

		} else if (BleService.BLE_DEVICE_FOUND.equals(action)) {
			Log.d(DEBUG, "发现设备");
			BluetoothDevice device = extras.getParcelable(BleService.EXTRA_DEVICE);
            MainActivity.setBleStatusText("发现蓝牙设备：" + device.getName() + ", 开始读取设备...");
			readDevices(device);
		} else if (BleService.BLE_NO_BT_ADAPTER.equals(action)) {
			Log.d(DEBUG, "No bluetooth adapter");

		} else if (BleService.BLE_GATT_CONNECTED.equals(action)) {
			Log.d(DEBUG, "Gatt 连上了");
            bufferSB.reset(); // 准备buffer
            MainActivity.setBleStatusText("蓝牙设备连接成功");
        } else if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {
			Log.d(DEBUG, "Gatt 断开了, begin...");
            bufferSB.reset(); // 清空buffer
            MainActivity.setBleStatusText("蓝牙设备断开");
            dMap.remove(address);
			updateHomePage();
			// 更新扫描界面
			updateScanView();
			Log.d(DEBUG, "Gatt 断开了, end..." + address);
			// scanning();
		} else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
			Log.d(DEBUG, "Service Dicovered读取了");
            bufferSB.reset(); // 清空buffer
			displayGattServices(address);
		} else if (BleService.BLE_CHARACTERISTIC_READ.equals(action)
				|| BleService.BLE_CHARACTERISTIC_CHANGED.equals(action)) {

			byte[] val = extras.getByteArray(BleService.EXTRA_VALUE);

            Log.d(DEBUG, "intent " + intent + ", extras " + extras + ", val " + new String(Hex.encodeHex(val)));
			// System.out.println("数值:" + valStr);
			// if ("00".equals(valStr)) {
			// // 血压计结束
			// System.out.println("血压计结束" + valStr);
			// mBle.disconnect(address);
			// dMap.remove(address);
			// return;
			// }

			DeviceModle modle = dMap.get(address);
			if (null == modle) {
                Log.d(DEBUG, "model is not found for address: " + address);
				return;
			}
            if (val == null || val.length == 0) { // no data, continue
                Log.d(DEBUG, "No data read/changed, waiting for next action");
                return;
            }
            if (bufferSB.size() > 0) { // 先判断前包是否合法，不合法先清空，避免后续包被污染
                byte[] prevPkg = bufferSB.toByteArray();
                if (prevPkg[0] != 85) { // not start with 0x55
                    bufferSB.reset();
                }
            }
            bufferSB.write(val, 0, val.length); // 加入buffer，和前包合并
            val = bufferSB.toByteArray();
            int pkgindex = 0; // val中通讯包的起始index
            while (pkgindex < val.length) { // 可能包含多于一个通讯包的内容，循环处理
                byte pkgstart = val[pkgindex + 0]; // 起始碼
                if (pkgstart != 85) { // 起始碼：固定为一常量：55H
                    Log.d(DEBUG, "蓝牙通讯包首字节不是起始码0x55，废弃已有缓存");
                    bufferSB.reset();
                    return;
                }
                if (val.length - pkgindex <= 3) { // 起始码(0x55), 包长度，包类别，至少3字节长度
                    Log.d(DEBUG, "蓝牙通讯包长度小于3字节，继续读取");
                    return;
                }
                byte pkglen = val[pkgindex + 1]; // 包长度
                byte pkgtype = val[pkgindex + 2]; // 包类别
                if (val.length - pkgindex < pkglen) { // 长度不够，继续
                    Log.d(DEBUG, "蓝牙通讯包长度不够:[ " + val.length + " - " + pkgindex + " < " + pkglen + "] ");
                    return;
                }
                byte[] pkgval = Arrays.copyOfRange(val, pkgindex, pkgindex + pkglen);
                Log.d(DEBUG, "组装好的数据:" + new String(Hex.encodeHex(pkgval)));
                pkgindex += pkglen; // 指向下一个包头
                bufferSB.reset();
                bufferSB.write(val, pkgindex, val.length - pkgindex); // bufferSB存放剩余的内容
                // 处理完整包
                if (pkgtype == 0) { // 信息包
                    Log.d(DEBUG, "信息包");
                    // showToast("信息包");
                /*
                if (modle.getMessageByte() != null && modle.getMessageByte().length > 0 && modle.getMessageByte()[5] == 1) {
                    Log.d(DEBUG, "已经接收过信息包，此次不处理");
                    return;
                }
                */
                    modle.setMessageByte(pkgval); // 保存信息包
                    try {
                        // displayGattServices(address);
                        write(getWriteCurrentTime(), address);
                    } catch (DecoderException e) {
                        Log.d(DEBUG, "写入异常", e);
                    }
                    String deviceSerial = CommonUtil.getDeviceSerial(pkgval); // 设备序列号
                    Log.d(DEBUG, "deviceSerial is " + deviceSerial);
                /*
                // 提交服务器
                // postBle(val[9] + "", val[8] + "", val[5] + "", deviceSerial, address);

                // 更新扫描界面
                updateScanView();
                */
                    // 更新测量界面
                    Intent updateMeasure = new Intent(Gloal.update_Serial);
                    updateMeasure.putExtra("deviceSerial", deviceSerial);
                    context.sendBroadcast(updateMeasure);
                } else if (pkgtype == 1) { // 开始包
                    Log.d(DEBUG, "开始包");
                    try {
                        guocheng_count = 0;
                        lastGuoCheng = 0;
                        write(getWriteCurrentTime(), address);
                    } catch (DecoderException e) {
                        Log.d(DEBUG, "写入异常", e);
                    }
                } else if (pkgtype == 2) { // 过程包
                    Log.d(DEBUG, "过程包");
                    guocheng_count++;
                    Log.d(DEBUG, "gc_" + guocheng_count + "");
                    if (lastGuoCheng == 0) {
                        lastGuoCheng = System.currentTimeMillis();

                    } else {
                        Long nowTime = System.currentTimeMillis();
                        lastGuoCheng = nowTime;
                    }
                    // Log.d(DEBUG, "间隔秒数:" + guocheng_count + "");
                    Log.d(DEBUG, "guocheng_count:" + guocheng_count);
                } else if (pkgtype == 3) { // 结果包
                    Log.d(DEBUG, "结果包");
                    modle.setResultByte(pkgval); // 设置结果包
                    byte[] messageByte = modle.getMessageByte(); // 信息包
                    byte[] resuleByte = modle.getResultByte(); // 结果包

                    if (messageByte == null || messageByte.length <= 6) {
                        continue;
                    }
                    byte mtype = messageByte[5]; // 5.機種碼
                    String result1, result2, result3, result4;
                    result1 = result2 = result3 = result4 = "";
                    if (mtype == 1) { // 0x01: 血压计
                        result2 = CommonUtil.getShort(resuleByte, 8) + ""; // [8,9]: 收缩压，低位在前
                        result3 = resuleByte[10] + ""; // [10]: 舒张压
                        result4 = resuleByte[11] + ""; // [11]: 心率

                        if (Integer.parseInt(result2) > 500
                                || Integer.parseInt(result2) <= 0) { // 收缩压大于500，或小于0，认为出错
                            continue;
                        }
                    } else { // 血糖仪
                        result4 = CommonUtil.getShort(resuleByte, 9) + ""; // 血糖
                    }

                    // 测量时间
                    String time;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    time = sdf.format(new Date());
                    String lastTime = modle.getSendTime();
                    if (null == lastTime || !time.equals(lastTime)) {
                        Log.d(DEBUG, "lastTime2:" + time);
                        modle.setSendTime(time);
                        String deviceSerial = CommonUtil.getDeviceSerial(messageByte);
                        saveBleMeasure(address, result1, result2, result3, result4, mtype + "", deviceSerial, time);
                    }

                    try {
                        write(getWriteCurrentTime(), address);
                        updateMeatureView(messageByte, resuleByte);
                        Log.d(DEBUG, "结束_" + guocheng_count + "");
                    } catch (DecoderException e) {
                        Log.d(DEBUG, "写入异常", e);
                    }
                } else if (pkgtype == 4) { // 结束包
                    Log.d(DEBUG, "结束包，本次测量结束");
                } else if (pkgtype == 5) { // APP返回，确认包。注意：这是APP发送给蓝牙设备的确认包
                    Log.d(DEBUG, "APP返回确认包");
                } else if (pkgtype == 6) { // APP终止连接包。注意：这是APP发送给拉洋设备的终止包
                    Log.d(DEBUG, "APP终止连接包");
                } else { // UNKNOWN
                    Log.d(DEBUG, "未知包类型：" + pkgtype);
                }
            }
		} else if (BleService.BLE_CHARACTERISTIC_NOTIFICATION.equals(action)) {
			boolean mNotifyStarted = extras.getBoolean(BleService.EXTRA_VALUE);
			if (mNotifyStarted) {
				Log.d(DEBUG, "Start Notify");
			} else {
				Log.d(DEBUG, "Stop Notify");
			}
		} else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
			Log.d(DEBUG, "BLE_REQUEST_FAILED异常:" + address + ", type=" + extras.get(BleService.EXTRA_REQUEST) + ", reason is " + extras.get(BleService.EXTRA_REASON));
			// closeDev(address);
			// displayGattServices(address);
			// CommonUtil.scan(context, true);
		} else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
			Log.d(DEBUG, "发现服务");
		} else if (BleService.BLE_CHARACTERISTIC_WRITE.equals(action)) {
			Log.d(DEBUG, "写入东西。status: " + extras.get(BleService.EXTRA_STATUS));
		} else if (BleService.BLE_STATUS_ABNORMAL.equals(action)) {
			Log.d(DEBUG, "未知:" + BleService.BLE_STATUS_ABNORMAL + ", reason is " + extras.get(BleService.EXTRA_VALUE));
		}
	}

	boolean isSend = false;

	/**
	 * 更新测量视图
	 */
	private void updateMeatureView(byte[] messageByte, byte[] resuleByte) {
		if (isSend) {
			return;
		}
		Intent measureIntent = new Intent(Gloal.update_measure_result);
		measureIntent.putExtra(Gloal.send_messageByte, messageByte);
		measureIntent.putExtra(Gloal.send_resuleByte, resuleByte);
		context.sendBroadcast(measureIntent);

		isSend = true;
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				isSend = false;
			}
		}, 4 * 1000);
	}

	/**
	 * 关闭设备连接
	 * 
	 * @param mac_address
	 */
	private void closeDev(String mac_address) {

		// 连接设备
		BleApplication app = (BleApplication) context.getApplicationContext();
		mBle = app.getIBle();
		if (mac_address == null || mBle == null) {
			return;
		}
		dMap.remove(mac_address);
		// mBle.disconnect(mac_address);
		updateHomePage();
	}

	/**
	 * 发送信息包数据
	 */
	private void sendMessageData() {

	}

	public static Map<String, DeviceModle> dMap = new HashMap<String, DeviceModle>();
	public static Map<String, DeviceModle> linkMap = new HashMap<String, DeviceModle>();

	/*
	 * 操作蓝牙设备
	 */
	private void readDevices(BluetoothDevice device) {

		// 连接设备
		BleApplication app = (BleApplication) context.getApplicationContext();
		mBle = app.getIBle();
		if (device == null || mBle == null) {
			return;
		}
		String mDeviceAddress = device.getAddress();
		if (null != mDeviceAddress) {
			// 连上设备的情况
			if (!dMap.containsKey(mDeviceAddress)
					&& CommonUtil.isBsp(device.getName())) {
				DeviceModle modle = new DeviceModle();
				modle.setFla(DeviceModle.LINK_STATE);
				modle.setdName(device.getName());
				modle.setDevice(device);
				dMap.put(mDeviceAddress, modle);
				updateHomePage();
				Log.d(DEBUG, "进行连接");
                MainActivity.setBleStatusText("尝试连接蓝牙设备：" + device.getName());
				mBle.requestConnect(mDeviceAddress);

				// 更新扫描界面
				updateScanView();

			} else if (dMap.containsKey(mDeviceAddress)) {
				DeviceModle modle = dMap.get(mDeviceAddress);
				if (modle.getFla() == DeviceModle.UNLINK_STATE) {
					modle.setFla(DeviceModle.LINK_STATE);
					modle.setdName(device.getName());
					mBle.requestConnect(mDeviceAddress);
					// 更新扫描界面
					updateScanView();
				}
			}
		}

	}

	private void updateScanView() {
		Intent intent = new Intent(UPDATE_SCANVIEW);
		context.sendBroadcast(intent);
	}

	// 更新主页数据
	protected void updateHomePageData(NumericalModle numModle) {
		Intent bindSuccessBroad = new Intent(BindSuccessActivity.UPDATE_EQ_DATA);
		if (null != numModle.getDeviceSerial() && !"".equals(numModle.getDeviceSerial())) {
			Editor edit = preferences.edit();
			edit.putString(SHOW_DEVICESERIAL, numModle.getDeviceSerial());
			edit.commit();
			context.sendBroadcast(bindSuccessBroad);
		}

	}

	private void updateHomePage() {
		// 发送广播更新数据
		Intent updateHomePage = new Intent(CHANGE_DATA);
		context.sendBroadcast(updateHomePage);
	}

	private void enterMeasure(String mDeviceAddress) {
		// 发送广播更新数据
		Intent enterMeasure = new Intent(ENTER_MEASURE);
		enterMeasure.putExtra(Gloal.send_address, mDeviceAddress);
		context.sendBroadcast(enterMeasure);

	}

	protected void displayGattServices(String address) {
		DeviceModle modle = dMap.get(address);
		if (null == modle || modle.isGet()) {
			return;
		}
		List<BleGattCharacteristic> mWriteCharacteristices = modle.getmWriteCharacteristices();
		if (null == mWriteCharacteristices) {
			mWriteCharacteristices = new ArrayList<BleGattCharacteristic>();
		}
		mWriteCharacteristices.clear();

		List<BleGattCharacteristic> mNoticeCharacteristices = modle
				.getmNoticeCharacteristices();
		if (null == mNoticeCharacteristices) {
			mNoticeCharacteristices = new ArrayList<BleGattCharacteristic>();
		}
		mNoticeCharacteristices.clear();

		if (null == mBle) {
			BleApplication app = (BleApplication) context.getApplicationContext();
			mBle = app.getIBle();

		}

		List<BleGattService> gattServices = mBle.getServices(address);

		if (gattServices == null)
			return;
		modle.setGet(true);
		String unknownServiceString = context.getResources().getString(
				R.string.unknown_service);
		String unknownCharaString = context.getResources().getString(
				R.string.unknown_characteristic);
		for (BleGattService gattService : gattServices) {
			HashMap<String, String> currentServiceData = new HashMap<String, String>();

			List<BleGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

			for (BleGattCharacteristic gattCharacteristic : gattCharacteristics) {
				String uuid = gattCharacteristic.getUuid().toString().toUpperCase();
				BleGattService mService = mBle.getService(address, UUID.fromString(gattService.getUuid().toString()));
				if (null != mService) {
					BleGattCharacteristic mCharacteristic = mService.getCharacteristic(UUID.fromString(gattCharacteristic.getUuid().toString()));
					final int charaProp = mCharacteristic.getProperties();

					if ((charaProp & BleGattCharacteristic.PROPERTY_NOTIFY) > 0) {
						if (uuid.toLowerCase().equals(bp.toLowerCase())) {
							mBle.requestCharacteristicNotification(address, mCharacteristic);
							mNoticeCharacteristices.add(mCharacteristic);
							break;
						}
						if (uuid.equals(BP_SERVICE)) {
							mBle.requestCharacteristicNotification(address, mCharacteristic);
							mNoticeCharacteristices.add(mCharacteristic);
							break;
						}

					}
					if (!Utils.BLE_SERVICES.containsKey(gattService.getUuid().toString())) {
						mWriteCharacteristices.add(mCharacteristic);
					}

                }
			}
			modle.setmWriteCharacteristices(mWriteCharacteristices);
			modle.setmNoticeCharacteristices(mNoticeCharacteristices);
		}
		try {
            Thread.sleep(100);
			write(getWriteCurrentTime(), address); // 配对成功，发送05应答包，开始通讯
            Thread.sleep(100);
            Log.d(DEBUG, "回应设备:" + address);
		} catch (DecoderException e) {
			Log.d(DEBUG, "displayGattServices异常", e);
		} catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 停掉
		// mHandler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub // 连上设备停止扫描
		CommonUtil.scan(context, false);
        /*
		// 进入测试页面
		enterMeasure(address);
		// }
		// }, 500);
        */
	}

	private byte[] getWriteCurrentTime() {
		byte[] data = new byte[11];

		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR) - 2000;
		int dom = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int count = 106 + year + month + day + hour + min;

		data[0] = 90;
		data[1] = 11;
		data[2] = 5;
		data[3] = (byte) year;
		data[4] = (byte) month;
		data[5] = (byte) day;
		data[6] = (byte) hour;
		data[7] = (byte) min;
		data[8] = (byte) count;
		data[9] = 0;
		data[10] = 0;

		return data;

	}

    static int writeSeq = 0;
	// 给蓝牙设备发送指令
	private void write(byte[] data, String adress) throws DecoderException {
		DeviceModle modle = dMap.get(adress);

		if (null != modle) {
			List<BleGattCharacteristic> mWriteCharacteristices = modle.getmWriteCharacteristices();
			if (null != mWriteCharacteristices) {
				for (BleGattCharacteristic item : mWriteCharacteristices) {
					String uuid = item.getUuid().toString();
					if (!uuid.startsWith(unBp)) {
						item.setValue(data);
						BleApplication app = (BleApplication) context.getApplicationContext();
						if (null == mBle) {
							mBle = app.getIBle();
						}
						Log.d(DEBUG, "写入蓝牙设备[" + uuid + "]:" + new String(Hex.encodeHex(data)));
						mBle.requestWriteCharacteristic(adress, item, "0x5A0B05 pkg-" + writeSeq++);
					}
					if (bp.equals(uuid)) {
                        Log.d(DEBUG, "Found bp device [" + bp + "], stop 写入蓝牙设备");
						break;
					}
				}
			}
		}
	}

	/*
	 * 提交服务器绑定蓝牙设备
	 */

	private void postBle(String month, String year, String type,
			String deviceSerial, String android_mac) {

        /*
		RequestParams param = new RequestParams();

		param.add(NetRestClient.deviceProMonth, month);
		param.add(NetRestClient.deviceProYear, year);
		param.add(NetRestClient.deviceType, type);
		param.add(NetRestClient.deviceSerial, deviceSerial);
		param.add(NetRestClient.android_mac, android_mac);
		*/
		// 回传给主页的数据
        // 禁止NetRestClient.post(NetRestClient.homepage_addBluetoothDevice, param)回传主页
	}

	/*
	 * 保存蓝牙设备数值
	 */
	private void saveBleMeasure(final String address, String result1,
			String result2, String result3, String result4, String meaSureType,
			String deviceSerial, String dateStr) {

		// if (dMap.containsKey(address)) {
		// DeviceModle dModle = dMap.get(address);
		// dModle.setFla(DeviceModle.SYN_STATE);
		// dMap.put(address, dModle);
		// updateHomePage();
		// }

		Log.d(DEBUG, "dateStr:" + dateStr);
		Log.d(DEBUG, "result1:" + result1);
		Log.d(DEBUG, "result2:" + result2);
		Log.d(DEBUG, "result3:" + result3);
		Log.d(DEBUG, "result4:" + result4);

		/*
		RequestParams param = new RequestParams();

		String user_id = preferences.getString(NetRestClient.user_id, "");

		param.add(NetRestClient.user_id, user_id);
		param.add(NetRestClient.deviceSerial, deviceSerial);

		// 返回主页的数据
		final NumericalModle backModle = new NumericalModle();

		param.add(NetRestClient.homepage_measureType, meaSureType);
		backModle.setDeviceSerial(deviceSerial);

		backModle.setMeasureType(meaSureType);
		int type = Integer.parseInt(meaSureType);
		if (type == 1) {
			// 血压
			param.add(NetRestClient.homepage_result2, result2);
			param.add(NetRestClient.homepage_result3, result3);
			param.add(NetRestClient.homepage_result4, result4);
		} else {
			param.add(NetRestClient.homepage_result4, result4);
			backModle.setResult4(result4);
		}

		saveUnUpload(result1, result2, result3, result4, meaSureType,
				deviceSerial, dateStr);

		param.add(NetRestClient.measureTime, dateStr);
		backModle.setDate(dateStr);
		*/

		// 禁止NetRestClient.post(NetRestClient.homepage_saveMeasureB, param)同步
	}

	public static List<NumericalModle> unUpLoadList = new ArrayList<NumericalModle>();

	private void saveUnUpload(String result1, String result2, String result3,
			String result4, String meaSureType, String deviceSerial,
			String dateStr) {
		// 保存未上传数值
		NumericalModle unUploadModle = new NumericalModle();
		unUploadModle.setResult1(result1);
		unUploadModle.setResult2(result2);
		unUploadModle.setResult3(result3);
		unUploadModle.setResult4(result4);
		unUploadModle.setMeasureType(meaSureType);
		unUploadModle.setDate(dateStr);
		unUploadModle.setDeviceSerial(deviceSerial);
		unUploadModle.setMeasure_id("unknow");
		if (!ConnectionChangeReceiver.connectionFla) {
			unUpLoadList.add(0, unUploadModle);
			writeToJson();
		}
		unUploadModle.setShowFla(true);
		updateHomePage(unUploadModle);
	}

	private void writeToJson() {

		SharedPreferences preferences = context.getSharedPreferences(
				BaseActivity.SHARE_NAME, 0);
		JSONArray list = new JSONArray();
		for (NumericalModle unUploadModle : unUpLoadList) {
			JSONObject json = new JSONObject();
			try {
				json.put("result1", unUploadModle.getResult1());
				json.put("result2", unUploadModle.getResult2());
				json.put("result3", unUploadModle.getResult3());
				json.put("result4", unUploadModle.getResult4());
				json.put("date", unUploadModle.getDate());
				json.put("deviceSerial", unUploadModle.getDeviceSerial());
				json.put("meaSureType", unUploadModle.getMeasureType());
				list.put(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String jsonStr = list.toString();
		Editor editor = preferences.edit();
		editor.putString(NetRestClient.locUnload, jsonStr);
		editor.commit();
	}

	private void updateHomePage(NumericalModle modle) {
		// 发送广播更新数据

		if (null != modle.getDeviceSerial() && !"".equals(modle.getDeviceSerial())) {
			Editor edit = preferences.edit();
			edit.putString(SHOW_DEVICESERIAL, modle.getDeviceSerial());
			edit.commit();
		}

		Intent updateHomePage = new Intent(CHANGE_DATA);
		updateHomePage.putExtra(UNLOAD, modle);
		context.sendBroadcast(updateHomePage);
	}

	public final static String UNLOAD = "unload";

}
