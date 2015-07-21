package com.haiforce.tophealthcareaio.aiaole.Receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.haiforce.tophealthcareaio.aiaole.activity.BleApplication;
import com.xtremeprog.sdk.ble.IBle;

public class BluetoothChangeReceiver extends BroadcastReceiver {

	public static boolean bluetoothFla = true;
	IBle mBle;
	@Override
	public void onReceive(Context context, Intent intent) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Toast.makeText(this, "本机没有找到蓝牙硬件或驱动！",
			// Toast.LENGTH_SHORT).show();
			bluetoothFla = false;
		}
		// 如果本地蓝牙没有开启，则开启
		if (!mBluetoothAdapter.isEnabled()) {
			bluetoothFla = false;
			
			BleApplication app = (BleApplication) context
					.getApplicationContext().getApplicationContext();
			mBle = app.getIBle();
			if (mBle == null) {
				return;
			}
			mBle.stopScan();
			BleReceiver.dMap.clear();
			
		} else {
			bluetoothFla = true;
			BleApplication app = (BleApplication) context
					.getApplicationContext().getApplicationContext();
			mBle = app.getIBle();
			if (mBle == null) {
				return;
			}
			mBle.startScan();
		}
	}
}
