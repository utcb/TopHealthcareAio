package com.haiforce.tophealthcareaio.aiaole.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiforce.tophealthcareaio.BaseActivity;
import com.haiforce.tophealthcareaio.R;
import com.haiforce.tophealthcareaio.aiaole.Receiver.BleReceiver;
import com.haiforce.tophealthcareaio.aiaole.modle.DeviceModle;
import com.haiforce.tophealthcareaio.aiaole.modle.EquipmentModle;
import com.haiforce.tophealthcareaio.aiaole.util.CommonUtil;
import com.haiforce.tophealthcareaio.aiaole.util.NetRestClient;
import com.xtremeprog.sdk.ble.IBle;

import java.util.Iterator;

/**
 * 设备详情管理
 * 
 * @author sunshine
 * 
 */
public class EquipManageDetailActivity extends BaseActivity implements
		OnClickListener {

	public static final String ACTIVITY_FLA = "EquipManageDetailActivity";
	EquipmentModle modle;

	// 管理其他用户
	RelativeLayout manageOtherBtn;
	// 解除当前绑定
	LinearLayout delete_btn;

	// 设备信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipmanage_detail);
		setBackOnclickListener();

		modle = (EquipmentModle) getIntent().getSerializableExtra(
				EquipManageActivity.SEND_MODLE);

		initView();
		initListener();
		initData();
		setActivity_fla(ACTIVITY_FLA);
	}

	private void initData() {

		// 左边图标
		int iconType = modle.getIcon_type();
		ImageView leftIcon = (ImageView) findViewById(R.id.icon);
		switch (iconType) {
		case EquipmentModle.BLOOD_PRESSURE_BLUETOOTH:
			leftIcon.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.eq_boolpressure_blue));
			break;
		case EquipmentModle.BLOOD_PRESSURE:
			leftIcon.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.eq_boolpressure));
			break;
		case EquipmentModle.HEART_RATE:
			leftIcon.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.eq_hearrate_icon));
			break;
		}

		TextView equip_text = (TextView) findViewById(R.id.equip_text);
		TextView username_text = (TextView) findViewById(R.id.username_text);
		TextView username_role = (TextView) findViewById(R.id.username_role);
		TextView last_updatetime = (TextView) findViewById(R.id.last_updatetime);
		String isAdmin = modle.getIsAdmin();

		equip_text.setText(modle.getName());

		String deviceSerial = modle.getDeviceSerial();
		if (deviceSerial.length() > 6) {
			deviceSerial = deviceSerial.substring(deviceSerial.length() - 6,
					deviceSerial.length());
		}
		username_text.setText(deviceSerial);
		last_updatetime.setText(modle.getTime());
		if ("1".equals(isAdmin)) {
			username_role.setText(getResources().getString(
					R.string.actt_manager));
			manageOtherBtn.setVisibility(View.VISIBLE);
		} else {
			username_role.setText(getResources().getString(R.string.actt_user));
			manageOtherBtn.setVisibility(View.GONE);
		}

	}

	private void initListener() {
		right_btn.setOnClickListener(this);
		delete_btn.setOnClickListener(this);
		manageOtherBtn.setOnClickListener(this);
	}

	LinearLayout right_btn;

	private void initView() {
		// TODO Auto-generated method stub

		right_btn = (LinearLayout) findViewById(R.id.right_btn);
		delete_btn = (LinearLayout) findViewById(R.id.delete_btn);
		manageOtherBtn = (RelativeLayout) findViewById(R.id.manageOtherBtn);

	}

	private void disConnect() {
		Iterator<String> iter = BleReceiver.dMap.keySet().iterator();
		BleApplication app = (BleApplication) getApplication();
		IBle mBle = app.getIBle();
		if (null != mBle) {
			mBle.stopScan();
		}
		while (iter.hasNext()) {
			String key = iter.next();
			DeviceModle dModle = BleReceiver.dMap.get(key);
			byte[] messageByte = dModle.getMessageByte();
			if (null != messageByte) {
				String deviceSerial = CommonUtil.getDeviceSerial(messageByte);
				if (null != deviceSerial
						&& deviceSerial.equals(modle.getDeviceSerial())) {
					// List<BleGattCharacteristic> noticeGat = dModle
					// .getmNoticeCharacteristices();
					// for (BleGattCharacteristic mCharacteristic : noticeGat) {
					// mBle.requestStopNotification(key, mCharacteristic);
					// }
					// mBle.disconnect(key);
					dModle.setFla(DeviceModle.UNBind_STATE);
					dModle.setMessageByte(null);
					// BleReceiver.dMap.remove(key);
				}
			}
		}
	}

	// 解除绑定
	private void removeBind() {
		// 删除连接
		disConnect();
		/*
		String user_id = preferences.getString(NetRestClient.user_id, "");
		RequestParams params = new RequestParams();
		params.add(NetRestClient.homepage_device_id, modle.getDevice_id());
		params.add(NetRestClient.user_id, user_id);
		*/
		showLoadDiaLog(getResources().getString(R.string.equipmanage_unbindIng));
		// 禁止NetRestClient.post(NetRestClient.homepage_removeBind, params)
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 跳转添加设备界面
		case R.id.right_btn: {
			Intent addEquipIntent = new Intent(EquipManageDetailActivity.this,
					AddEquipActivity.class);
			startActivity(addEquipIntent);
		}
		// 解除绑定
		case R.id.delete_btn: {
			removeBind();
		}
			break;
		// 管理其他使用者
			/*
		case R.id.manageOtherBtn: {
			Intent manageUserIntent = new Intent(
					EquipManageDetailActivity.this,
					EquipManageUserActivity.class);
			manageUserIntent.putExtra(EquipManageActivity.SEND_MODLE, modle);
			startActivity(manageUserIntent);

		}

			break;
			*/
		}

	}
}
