package com.haiforce.tophealthcareaio.aiaole.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.haiforce.tophealthcareaio.BaseActivity;
import com.haiforce.tophealthcareaio.R;

public class BindSuccessActivity extends BaseActivity implements
		OnClickListener {

	LinearLayout right_btn;
	LinearLayout backpage_button;

	public static final String UPDATE_EQ_DATA = "updateEqData";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_success);
		initView();
		updateHomePageData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		right_btn = (LinearLayout) findViewById(R.id.right_btn);
		backpage_button = (LinearLayout) findViewById(R.id.backpage_button);

		right_btn.setOnClickListener(this);
		backpage_button.setOnClickListener(this);

	}

	private void finishBefore() {
		String[] activtyArray = { EquipManageActivity.ACTIVITY_FLA,
				AddEquipActivity.ACTIVITY_FLA,
				BindBleActivity.ACTIVITY_FLA };
		finishActivity(activtyArray);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// 返回继续添加
		case R.id.right_btn: {
			finish();
		}
			break;
		// 跳转主页
		case R.id.backpage_button: {
			finishBefore();
			finish();
		}
			break;
		}
	}
}
