package partygames.shirley.com.undercoverlib;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class UnderPunishActivity extends UnderBaseActivity {
	TextView txtPunish;
	TextView txtLast;
	Button btnNext;
	ImageView imgBg;
	ImageView imgType;
	Timer timer;
	int remainSec=0;
	int onesSec=300;
	
	int thisid=0;
	ProgressBar proBar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.under_punish);
		showShack = true;
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		txtPunish=(TextView)this.findViewById(R.id.txtPunish);
		txtLast=(TextView)this.findViewById(R.id.txtLast);
		btnNext=(Button)this.findViewById(R.id.btnNext);

		proBar=(ProgressBar)this.findViewById(R.id.proBar);
		
		imgBg=(ImageView)this.findViewById(R.id.imgBg);
		imgType=(ImageView)this.findViewById(R.id.imgType);
		imgType.setVisibility(View.INVISIBLE);
		btnNext.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				SoundPlayer.click();
				nextPunish();
			}
		});		

//		btnLocal.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intentGo = new Intent();
//				intentGo.setClass(UnderPunishActivity.this, local_punish_list.class);
//				startActivity(intentGo);
//			}
//		});

		
//		buttonLike.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				addCollect(thisid,1);
//			}
//		});
//		buttonDislike.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				addCollect(thisid,2);
//			}
//		});
		
		imgShake();
		timer = new Timer();
		timer.schedule(timetask, 0, 10);
		proBar.setMax(onesSec);
		
		txtLast.setText(getLastString());
		runAniSetting();
	}
	
//	protected void addCollect(int id,int type) {
//		PublishHandler publishHandler = new PublishHandler(this);
//		publishHandler.addCollect(id,type);
//	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mincost();
			super.handleMessage(msg);
		}
	};
	
	// 传递时间
	private TimerTask timetask = new TimerTask() {
		@Override
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	
	
	/**
	 * 这个方法为了3秒取一次惩罚
	 */
	private void mincost(){
		if(remainSec>0){
			remainSec--;
			proBar.setProgress(remainSec);
		}
		if(remainSec<=0){
			btnNext.setEnabled(true);
		}
	}
	
	
	

	/**
	 * 背景图摇晃
	 */
	public void imgShake(){
		RotateAnimation rt = new RotateAnimation(-10,10,
				dip2px(this,151),dip2px(this,120));
		rt.setDuration(1000);
		rt.setFillAfter(true);
		imgBg.startAnimation(rt);
		rt.setRepeatCount(-1);	
        rt.setRepeatMode(Animation.REVERSE);
	}
	
	@Override
	public void shackAction() {
		if(nextPunish()){
			SoundPlayer.shake();
		}
	}
	private boolean nextPunish(){
//		if (remainSec <= 0) {
//			btnNext.setEnabled(false);
//			remainSec = onesSec;
//		} else {
//			return false;
//		}
		String punish = getRandomMaoxianFromLocate(false);
		txtLast.setText(getLastString());
		txtPunish.setText(punish);
		setGameIsNew(ConstantControl.GAME_PUNISH,false);
		return true;
	}
	public void setLastString(String str){
		setToObject("PUNISH_LAST",str);
	}
	public String getLastString(){
		return getFromObject("PUNISH_LAST");
	}

	@Override
	public void CallBackPublicCommand(JSONObject jsonobj, String cmd) {
		super.CallBackPublicCommand(jsonobj, cmd);
		if (cmd.equals(ConstantControl.PUNISH_RANDOMONE)) {
			try {
				JSONObject obj = new JSONObject(jsonobj.getString("data"));
				JSONArray objarr=obj.getJSONArray("content");
				JSONObject random=objarr.getJSONObject(0);
				String punish=random.getString("content");
				int temid=random.getInt("_id");
				thisid=temid;

				int contenttype=random.getInt("contenttype");
				txtPunish.setText(punish);
				if (contenttype == 1) {
//					imgType.setBackgroundResource(R.drawable.turns_1);
					imgType.setVisibility(View.VISIBLE);
				} else if (contenttype == 2) {
//					imgType.setBackgroundResource(R.drawable.turns_2);
					imgType.setVisibility(View.VISIBLE);
				} else if (contenttype == 3) {
//					imgType.setBackgroundResource(R.drawable.turns_3);
					imgType.setVisibility(View.VISIBLE);
				}else{
					imgType.setVisibility(View.INVISIBLE);
				}
				setLastString(punish);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				txtPunish.setText("可以免除惩罚");
			}
		}
		else if (cmd.equals(ConstantControl.SEND_PUBLISH_COLLECT)) {
//			ToastMessage("操作成功");
		}
	}
	
	@Override
	public void MessageCallBackWrong(String cmd) {
		super.MessageCallBackWrong(cmd);
		if(cmd.equals(ConstantControl.PUNISH_RANDOMONE))
		{
			try{
				String punish=getRandomMaoxianFromLocate(false);
				setLastString(punish);
				txtPunish.setText(punish);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	protected void runAniSetting(){
        //创建一个AnimationSet对象，参数为Boolean型，
        //true表示使用Animation的interpolator，false则是使用自己的
        AnimationSet animationSet = new AnimationSet(true);
        //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.5f);
        //设置动画执行的时间
        alphaAnimation.setDuration(1500);
        alphaAnimation.setRepeatCount(-1);	
        alphaAnimation.setRepeatMode(Animation.REVERSE);
//        alphaAnimation.reset();
        //将alphaAnimation对象添加到AnimationSet当中
        animationSet.addAnimation(alphaAnimation);
	}
}
