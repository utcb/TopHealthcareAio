package com.haiforce.tophealthcareaio.aiaole.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.haiforce.tophealthcareaio.R;
import com.haiforce.tophealthcareaio.aiaole.activity.BindSuccessActivity;
import com.haiforce.tophealthcareaio.aiaole.activity.BleApplication;
import com.haiforce.tophealthcareaio.aiaole.modle.Gloal;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	public static final String DEBUG = Gloal.DEBUG + "ConnectionChangeReceiver";

	private static ConnectionChangeReceiver instence;

	public static ConnectionChangeReceiver getInstence() {
		if (null == instence) {
			instence = new ConnectionChangeReceiver();
		}
		return instence;
	}

	public static boolean connectionFla = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

		if (activeNetInfo != null && activeNetInfo.isAvailable()) {
			Log.d(DEBUG, "连上了");
			connectionFla = true;
			// 上传未测试数据
			Intent uploadTestData = new Intent(BleApplication.UPLOAD_TESTDATA);
			context.sendBroadcast(uploadTestData);
			// 更新界面
			Intent bindSuccessBroad = new Intent(
					BindSuccessActivity.UPDATE_EQ_DATA);
			context.sendBroadcast(bindSuccessBroad);
		} else {
			Log.d(DEBUG, "网络断开了");
			connectionFla = false;
			Toast.makeText(context,
					context.getResources().getString(R.string.netwrok_tishi),
					Toast.LENGTH_LONG).show();
			// 更新界面
			Intent bindSuccessBroad = new Intent(
					BindSuccessActivity.UPDATE_EQ_DATA);
			context.sendBroadcast(bindSuccessBroad);
		}

	}
}