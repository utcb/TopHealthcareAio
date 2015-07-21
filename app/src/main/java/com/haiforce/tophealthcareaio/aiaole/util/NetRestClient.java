package com.haiforce.tophealthcareaio.aiaole.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.haiforce.tophealthcareaio.aiaole.Receiver.ConnectionChangeReceiver;
import com.haiforce.tophealthcareaio.aiaole.modle.Gloal;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NetRestClient {
	public static final String DEBUG = Gloal.DEBUG + "NetRestClient";

	public static final String BASE_URL = "http://121.42.8.219:8088/aiaole/";

	/*
	private static AsyncHttpClient client = new AsyncHttpClient();

	static {
		client.setTimeout(1000 * 10);
	}

	public static final Integer SUCCESS_CODE = 200;

	private static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		if (ConnectionChangeReceiver.connectionFla) {
			client.get(getAbsoluteUrl(url), params, responseHandler);
		}

	}

	private static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if (ConnectionChangeReceiver.connectionFla) {
			Log.d(DEBUG, "进行提交操作");
			client.post(getAbsoluteUrl(url), params, responseHandler);
		}
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void loadBitMap(final Context context, String filePath,
			final ImageView v) throws FileNotFoundException {

		if (!ConnectionChangeReceiver.connectionFla) {
			return;
		}

		final String imageUrl = BASE_URL + filePath;

		File file = new File(context.getCacheDir(), ImageTools.Md5(imageUrl));

		if (file.exists()) {
			// 本地加载
			FileInputStream fis = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			v.setImageBitmap(ImageTools.toRoundBitmap(bitmap));
			return;
		}

		// 从网络加载
		client.get(imageUrl, new BinaryHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				ByteArrayInputStream is = new ByteArrayInputStream(arg2);
				Bitmap bm = BitmapFactory.decodeStream(is);
				v.setImageBitmap(ImageTools.toRoundBitmap(bm));
				// 保存在本地
				try {
					ImageTools.saveBitMap(context, imageUrl, bm);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

	public static void loadImgByUrl(final Context context,
			final String imageUrl, final ImageView v)
			throws FileNotFoundException {
		if (!ConnectionChangeReceiver.connectionFla) {
			return;
		}
		File file = new File(context.getCacheDir(), ImageTools.Md5(imageUrl));

		if (file.exists()) {
			// 本地加载
			FileInputStream fis = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			v.setImageBitmap(bitmap);
			return;
		}

		// 从网络加载
		client.get(imageUrl, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub

				ByteArrayInputStream is = new ByteArrayInputStream(arg2);
				Bitmap bm = BitmapFactory.decodeStream(is);
				v.setImageBitmap(ImageTools.toRoundBitmap(bm));
				// 保存在本地
				try {
					ImageTools.saveBitMap(context, imageUrl, bm);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}
*/
	// 登录请求

	public static String error_msg = "errorMsg";
	public static String user_id = "user_id";

	// 当前城市
	public static String userInfo_city = "city";

	public static String phone_rescue = "phone_rescue";
	public static String message_rescue = "message_rescue";

	public static String type = "type";

	public static String loginRequest = "base/login_user";
	public static String login_account = "account";
	public static String login_password = "password";

	public static String login_error_code = "11201";

	public static String register_nameUsered_code = "12121";

	// 注册-发送校验码
	public static String registerCode = "base/register_sendVliCode";
	public static String mobileOrEmail = "mobileOrEmail";
	// 注册账号
	public static String registerAccount = "base/register_accPass";
	public static String register_account = "account";
	public static String register_password = "password";
	public static String register_name = "name";

	// 找回密码-发送校验码
	public static String findBackCode = "base/forget_sendVliCode";
	// 找回密码-重置密码
	public static String forget_resetPass = "base/forget_resetPass";
	// 授权列表
	public static String grant_list = "friends/auth_getAllAuth";

	// 用户信息
	public static String userInfo_account = "account";
	public static String userInfo_sex = "sex";
	public static String userInfo_birthday = "birthday";
	public static String userInfo_headPic = "headpic";
	public static String userInfo_height = "height";
	public static String userInfo_name = "name";
	public static String userInfo_weight = "weight";

	public static String userInfo_maritalStatus = "maritalStatus";
	public static String userInfo_isHighOrSugar = "isHighOrSugar";
	// 上传头像
	public static String personset_uploadFile = "base/uploadFile";
	public static String personset_filedata = "filedata";
	public static String personset_filePath = "filePath";
	// 找回密码
	public static String findBack_newpass = "newpass";

	// 更新个人资料
	public static String personset_update = "base/register_suppInfo";
	public static String personset_headPic = "headPic";

	public static String personset_getUserById = "base/getUserInfoById";

	// 绑定设备
	public static String homepage_bindInfo = "homepage/getBindingInfo";
	public static String homepage_addGsmDevice = "homepage/device_addGsmDevice";

	public static String deviceSerial = "deviceSerial";
	public static String userNumber = "userNumber";
	public static String resule_success = "success";
	public static String resule_info = "info";

	// 获取设备和数据
	public static String homepage_getAllDeviceAndDataByDate = "homepage/getAllDeviceAndDataByDate";
	public static String homepage_startDate = "start";
	public static String homepage_endDate = "end";
	public static String homepage_deviceList = "deviceList";
	public static String homepage_deviceType = "deviceType";
	public static String homepage_device_id = "device_id";
	public static String homepage_deviceNetType = "deviceNetType";
	public static String homepage_userMeasureList = "userMeasureList";
	public static String homepage_measureList = "measureList";
	public static String homepage_sendTime = "sendTime";
	public static String homepage_measure_id = "measure_id";
	public static String homepage_result4 = "result4";
	public static String homepage_result1 = "result1";
	public static String homepage_result2 = "result2";
	public static String homepage_result3 = "result3";

	public static String homepage_viewType = "viewType";
	public static String homepage_measureType = "measureType";

	public static String homepage_getDeviceInfoByUserID = "homepage/getDeviceInfoByUserID";

	// 解除设备绑定
	public static String homepage_removeBind = "homepage/removeBind";
	// 获取设备的使用者
	public static String homepage_getIsNotAdminUserList = "homepage/getIsNotAdminUserList";

	// 手动添加记录
	public static String homepage_saveMeasure = "homepage/saveMeasure";
	// 根据根据类型获取手动记录
	public static String homepage_getUserMeasureByUserIdAndType = "homepage/getUserMeasureByUserIdAndType";
	// 删除手动数据
	public static String measure_delByID = "homepage/measure_delByID";
	// 获取历史记录
	public static String homepage_history_getData = "homepage/history_getData";

	public static String homepage_avgType = "avgType";
	public static String homepage_isGetAllData = "isGetAllData";

	// 知识分类
	public static String find_get_knowledgetype = "find/get_knowledgetype";
	// 知识列表
	public static String find_knowledge_getKnowledgeData = "find/knowledge_getKnowledgeData";

	public static String find_pagenumber = "pagenumber";
	public static String find_type = "type";
	public static String find_pagesize = "pagesize";
	public static String language = "language";
	// 知识详情
	public static String find_knowledge_getDetailKnowledgeData = "find/knowledge_getDetailKnowledgeData";
	public static String find_id = "find_id";
	// 收藏列表
	public static String find_get_userknowledgelist = "find/get_userknowledgelist";
	// 收藏发现
	public static String find_save_userknowledge = "find/save_userknowledge";
	// 删除收藏
	public static String find_del_userknowledge = "find/del_userknowledge";
	public static String find_del_id = "id";
	// 授权列表
	public static String friends_auth_getAllAuth = "friends/auth_getAllAuth";
	// 添加授权
	public static String friends_auth_addAuth = "friends/auth_addAuth";
	public static String authorize_mobileOrEmail = "authorize_mobileOrEmail";
	// 删除授权
	public static String friends_auth_deleteAuth = "friends/auth_deleteAuth";
	public static String authorize_user_id = "authorize_user_id";

	// 获取所以关注
	public static String friends_following_getAllFoll = "friends/following_getAllFoll";
	// 取消关注
	public static String friends_following_deleteFoll = "friends/following_deleteFoll";

	// 添加蓝牙设备
	public static String homepage_addBluetoothDevice = "homepage/addBluetoothDevice";
	public static String deviceProYear = "deviceProYear";
	public static String deviceProMonth = "deviceProMonth";
	public static String deviceType = "deviceType";

	// 提交蓝牙数据
	public static String homepage_saveMeasureB = "homepage/saveMeasureB";
	public static String measureTime = "measureTime";

	// 绑定蓝牙设备
	public static String homepage_bindingBluetoothDevice = "homepage/bindingBluetoothDevice";
	public static String android_mac = "android_mac";

	// 关于我们,服务器协议 参数 type 类型 7、中文版关于我们；8、中文版协议；9、英文版关于我们；10、英文版协议
	public static String base_getContentByType = "base/getContentByType";
	public static String contentType = "type";

	public static String locJsonStr = "locJsonStr";
	public static String locUnload = "locUnLoad";
	// 更新显示状态
	public static String measureViewFlagUpdate = "homepage/measureViewFlagUpdate";
	// 获取推送状态
	public static String get_userMSGList = "find/get_userMSGList";

}