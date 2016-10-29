package com.sx.brainsharp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.GridView;
import android.widget.Toast;

import com.chinaMobile.MobileAgent;
import com.sx.adapter.HomeEditappAdapter;
import com.sx.model.AppInfo;
import com.sx.sql.DatabaseHelper;
import com.sx.sql.DbMananger;
import com.sx.util.CommonUtil;
import com.sx.util.DateUtil;

public class HomeActivity extends BaseActivity {

	private GridView app_gridview;
	public static List<AppInfo> allApps = new ArrayList<AppInfo>();
	public static LinkedList<AppInfo> addAppInfos = new LinkedList<AppInfo>();
	protected DatabaseHelper sqlHelper;
	private boolean isShowDelete;
	private HomeEditappAdapter editappAdapter;
	private Vibrator mVibrator;
	private long firstime = 0;

	private String appId = "300008447500";
	private String channelId = "channe_mm";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		MobileAgent.init(this, appId, channelId);
		
		AdManager.getInstance(this).init("815f3f14c802f7f1",
				"7a2354124217599b", false);
		// ���ز岥��Դ
		SpotManager.getInstance(this).loadSpotAds();

		// ��ʼ���ӿڣ�Ӧ��������ʱ�����
		// ������appId, appSecret, ����ģʽ
//		AdManager.getInstance(this).init("cfdbdd2786ea88ea ","d8edde7d10dd0073", false);
		
		// ���ʹ�û��ֹ�棬����ص��û��ֹ��ĳ�ʼ���ӿ�:
		OffersManager.getInstance(this).onAppLaunch();
		
		// (��ѡ)�����û�����ͳ�Ʒ���,Ĭ�ϲ�����������falseֵҲ��������ֻ�д���true�Ż����
		AdManager.getInstance(this).setUserDataCollect(true);
		
		// (��ѡ)ע����ּ���-��ʱ��ػ�û��ֵı䶯���
//		PointsManager.getInstance(this).registerNotify(this);
		
		DbMananger db = new DbMananger(this);
		// boolean hasAwardPoint = db.queryLog(time);


		// ������ݿ�
		// sqlHelper = getHelper();
		if(DateUtil.isTime()){
			showad();
		}
		initView();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, HomeAddappActivity.class);
				startActivity(intent); // ����һ���µ�Activity
				finish();
			}
		};
		timer.schedule(task, 1000 * 5); // 10���
	}
	
	public void showad(){
		// չʾ�岥��棬���Բ�����loadSpot����ʹ��
		SpotManager.getInstance(HomeActivity.this).showSpotAds(
				HomeActivity.this, new SpotDialogListener() {
					@Override
					public void onShowSuccess() {
						Log.i("YoumiAdDemo", "չʾ�ɹ�");
					}

					@Override
					public void onShowFailed() {
						Log.i("YoumiAdDemo", "չʾʧ��");
					}

				}); // //
	}

	public void initView() {
		// mVibrator = (Vibrator) HomeActivity.this
		// .getSystemService(Context.VIBRATOR_SERVICE);
		// app_gridview=(GridView) findViewById(R.id.gridview);
		initAppInfos();
		editappAdapter = new HomeEditappAdapter(HomeActivity.this);
		editappAdapter.setData(addAppInfos);
		// app_gridview.setAdapter(editappAdapter);

	}

	// ��ʼ��Ӧ��ͼ��
	private void initAppInfos() {
		// Map<String,String> homeAppMap = getHomeAppMapFromSql();
		for (int i = 0; i < CommonUtil.MainAppApp.length; i++) {
			AppInfo appInfo = (CommonUtil.getApp(CommonUtil.MainAppApp[i]));
			appInfo.setId(i + 1);
			allApps.add(appInfo);
		}
		AppInfo _appInfo = (CommonUtil.getApp(CommonUtil.appId10));
		addAppInfos.add(_appInfo);
	}

	// protected DatabaseHelper getHelper() {
	// if(sqlHelper==null){
	// sqlHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
	// }
	// return sqlHelper;
	// }

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		editappAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobileAgent.onResume(this);
	}
	@Override
	protected void onPause() {

		// TODO �Զ����ɷ������
		super.onPause();
		MobileAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isShowDelete && keyCode == KeyEvent.KEYCODE_BACK) {
			isShowDelete = false;
			editappAdapter.setIsShowDelete(isShowDelete);
			editappAdapter.notifyDataSetChanged();
			return true;
		} else {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				long secondtime = System.currentTimeMillis();
				if (secondtime - firstime > 3000) {
					Toast.makeText(HomeActivity.this, "�ٰ�һ�η��ؼ��˳�",
							Toast.LENGTH_SHORT).show();
					firstime = System.currentTimeMillis();
					return true;
				} else {
					finish();
					System.exit(0);
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
