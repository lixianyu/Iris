package com.baidu.locSDK.test;

//import com.baidu.locTest.Location;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.*;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class mainActivity extends Activity {

	private TextView mTv = null;
	private EditText mSpanEdit;
	private EditText mAddrEdit;
	private EditText mCoorEdit;
	private CheckBox mGpsCheck;
	private Button   mStartBtn;
	private Button	 mSetBtn;
	private Button 	 mLocBtn;
	private Button 	 mPoiBtn;
	private Button 	 mOffLineBtn;
	private boolean  mIsStart;
	private static int count = 1;
	private Vibrator mVibrator01 =null;
	private LocationClient mLocClient;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		mTv = (TextView)findViewById(R.id.textview);
		mSpanEdit = (EditText)findViewById(R.id.edit);
		mCoorEdit = (EditText)findViewById(R.id.coorEdit);
		mAddrEdit = (EditText)findViewById(R.id.addrEdit);
		mGpsCheck = (CheckBox)findViewById(R.id.gpsCheck);
		mStartBtn = (Button)findViewById(R.id.StartBtn);
		mLocBtn = (Button)findViewById(R.id.locBtn);
		mSetBtn = (Button)findViewById(R.id.setBtn);       
		mPoiBtn = (Button)findViewById(R.id.PoiReq);
		mOffLineBtn = (Button)findViewById(R.id.offLineLocation);
		mIsStart = false;
	
		mLocClient = ((Location)getApplication()).mLocationClient;
		((Location)getApplication()).mTv = mTv;
		mVibrator01 =(Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		((Location)getApplication()).mVibrator01 = mVibrator01;
		//开始/停止按钮
		mStartBtn.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mIsStart) {
					setLocationOption();
					mLocClient.start();
					mStartBtn.setText("停止");
					mIsStart = true;
					
				} else {
					mLocClient.stop();
					mIsStart = false;
					mStartBtn.setText("开始");
				} 
				Log.d("locSDK_Demo1", "... mStartBtn onClick... pid="+Process.myPid()+" count="+count++);
			}
		});

		//定位按钮
		mLocBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLocClient != null && mLocClient.isStarted()){
					setLocationOption();
					mLocClient.requestLocation();	
				}				
				else 
					Log.d("boot", "locClient is null or not started");
				Log.d("locSDK_Demo1", "... mlocBtn onClick... pid="+Process.myPid()+" count="+count++);
			}
		});

		//设置按钮
		mSetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setLocationOption();
			}
		});

		mPoiBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLocClient.requestPoi();
			}
		});    

		mOffLineBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLocClient.requestOfflineLocation();
			}
		});    
	}   

	@Override
	public void onDestroy() {
		mLocClient.stop();
		((Location)getApplication()).mTv = null;
		super.onDestroy();
	}

	//设置相关参数
	private void setLocationOption(){
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(mGpsCheck.isChecked());				//打开gps
		option.setCoorType(mCoorEdit.getText().toString());		//设置坐标类型
		option.setAddrType(mAddrEdit.getText().toString());		//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
		option.setScanSpan(Integer.parseInt(mSpanEdit.getText().toString()));	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
		mLocClient.setLocOption(option);
	}

}