package partygames.shirley.com.partygames;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;

import com.google.gson.Gson;

import partygames.shirley.com.baselib.BaseActivity;
import partygames.shirley.com.baselib.SettingPreferences;
import partygames.shirley.com.baselib.utils.ReadAssetUtil;
import partygames.shirley.com.baselib.utils.Utils;
import partygames.shirley.com.playandguesslib.AdUtils;
import partygames.shirley.com.playandguesslib.GBaseData;
import partygames.shirley.com.playandguesslib.GMenuActivity;
import partygames.shirley.com.playandguesslib.model.GuessResult;

public class SplashActivity extends BaseActivity {
	private static final int READ_COMPLETE = 1;
    private boolean gotoGuide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
		AdUtils.openSplashAd(this,null);


	new Thread(new Runnable() {
		@Override
		public void run() {
			ReadAssetUtil utils = new ReadAssetUtil(SplashActivity.this);
			String strResult = utils.read("playandguess.json");
			Gson gson = new Gson();
			GBaseData.getInstance().guessResult = gson.fromJson(strResult, GuessResult.class);
			mainThreadHandler.sendEmptyMessage(READ_COMPLETE);

		}
	}).start();
}


	public MainHandler mainThreadHandler = new MainHandler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
				case READ_COMPLETE:
					handler.postDelayed(runnable, 1500);
					break;
			}
		}
	};

	private Handler handler = new Handler(Looper.getMainLooper());

	/**
	 * 跳转主界面的runnable 方便需要移除的时候移除
	 */
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			goHome(false);
		}
	};

	private void goHome(boolean isWeb) {
		Intent intent = null;
		if (SettingPreferences.isFristLunch(this)) {
			if(gotoGuide){
				intent = getNextIntent(isWeb);
			}else{
				intent = getNextIntent(isWeb);
			}
			SettingPreferences.setSettingValue(this, SettingPreferences.KEY_FIRST_LUNCH, Utils.getVersion(this));
		} else {
			intent = getNextIntent(isWeb);
		}
		if (intent != null) {
			isDestory = true;
			startActivity(intent);
			finish();
		}
	}

	private Intent getNextIntent(boolean isWeb) {
		Intent intent;
		intent  = new Intent(this,GMenuActivity.class);
		return intent;
	}
}
