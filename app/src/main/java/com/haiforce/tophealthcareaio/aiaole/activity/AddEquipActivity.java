package com.haiforce.tophealthcareaio.aiaole.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.haiforce.tophealthcareaio.BaseActivity;
import com.haiforce.tophealthcareaio.R;
import com.xtremeprog.sdk.ble.BleService;
import com.xtremeprog.sdk.ble.BleService.BLESDK;

/**
 * 添加设备界面
 * 
 * @author sunshine
 * 
 */
public class AddEquipActivity extends BaseActivity implements OnClickListener {

	public static final String ACTIVITY_FLA = "AddEquipActivity";

	LinearLayout right_btn;
	LinearLayout br_gsm_btn;
	LinearLayout bp_gsm_btn;

	LinearLayout bp_bule_btn;
	LinearLayout heartrate_bule_btn;

	LinearLayout erwen_bule_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_equip);
		setBackOnclickListener();

		initView();
		initListener();
		setActivity_fla(ACTIVITY_FLA);
	}

	private void initView() {
		right_btn = (LinearLayout) findViewById(R.id.right_btn);
		br_gsm_btn = (LinearLayout) findViewById(R.id.br_gsm_btn);
		bp_gsm_btn = (LinearLayout) findViewById(R.id.bp_gsm_btn);

		bp_bule_btn = (LinearLayout) findViewById(R.id.bp_bule_btn);

		erwen_bule_btn = (LinearLayout) findViewById(R.id.erwen_bule_btn);
		heartrate_bule_btn = (LinearLayout) findViewById(R.id.heartrate_bule_btn);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		br_gsm_btn.setOnClickListener(this);
		bp_gsm_btn.setOnClickListener(this);

		bp_bule_btn.setOnClickListener(this);
		heartrate_bule_btn.setOnClickListener(this);

		erwen_bule_btn.setOnClickListener(this);
		// super.setBackOnclickListener();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.bp_bule_btn: {
			if (BleService.goble_BleSDK != BLESDK.ANDROID) {
				Toast.makeText(
						this,
						this.getResources().getString(
								R.string.ble_tishi),
						Toast.LENGTH_LONG).show();
				return;
			}

			Intent bpIntent = new Intent(AddEquipActivity.this,
					BindBleActivity.class);
			bpIntent.putExtra(BindBleActivity.bindType, BindBleActivity.BP_TYPE);
			startActivity(bpIntent);
		}
			break;
		case R.id.heartrate_bule_btn: {
			if (BleService.goble_BleSDK != BLESDK.ANDROID) {
				Toast.makeText(
						this,
						this.getResources().getString(
								R.string.ble_tishi),
						Toast.LENGTH_LONG).show();
				return;
			}

			Intent bpIntent = new Intent(AddEquipActivity.this,
					BindBleActivity.class);
			bpIntent.putExtra(BindBleActivity.bindType, BindBleActivity.BS_TYPE);
			startActivity(bpIntent);

		}
			break;
		case R.id.erwen_bule_btn: {
			if (BleService.goble_BleSDK != BLESDK.ANDROID) {
				Toast.makeText(
						this,
						this.getResources().getString(
								R.string.ble_tishi),
						Toast.LENGTH_LONG).show();
				return;
			}

			Intent bpIntent = new Intent(AddEquipActivity.this,
					BindBleActivity.class);
			bpIntent.putExtra(BindBleActivity.bindType, BindBleActivity.EW_TYPE);
			startActivity(bpIntent);
		}
			break;
		}

	}
}
