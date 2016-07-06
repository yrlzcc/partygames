package partygames.shirley.com.playandguesslib;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import partygames.shirley.com.baselib.BaseActivity;
import partygames.shirley.com.baselib.view.AutoScrollViewPager;
import partygames.shirley.com.playandguesslib.model.WordState;

public class PlayActivity extends BaseActivity {
    private static final int STATE_NONE = 0; //初始状态
    private static final int STATE_READY = 1; //准备状态
    private static final int STATE_PLAY = 2; //准备状态
    private static final int MODE_NONE = 3;  //竖立，准备，出词
    private static final int MODE_VERTICAL = 4;  //竖立，准备，出词
    private static final int MODE_UP = 5;   //上翻，跳过
    private static final int MODE_DOWN = 6; //下翻，正确
    private static final int MESSAGE_TIME_UP = 10;
    private Handler handler = new Handler(Looper.getMainLooper());
    private AutoScrollViewPager viewPager;
    private SensorManager sensorManager;
    private MySensorEventListener sensorEventListener;
    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    private static final String TAG = "sensor";
    //需要两个Sensor
    private Sensor aSensor;
    private Sensor mSensor;
    private int mode = MODE_NONE;
    private int currentMode = MODE_NONE;
    private StateChangeListener listener;
    private MyPagerAdapter  adapter;
    private String[] data = {"aa","bb","cc","dd","ff","gg"};
    private PlayCount mc;
    private ReadyCount rc;
    private String[] words = null;
    private int state = STATE_NONE;
    private TextView gplay_tv_tips;
    private TextView gplay_tv_time;
    private WordState wordState;
    private int time;  //时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        int type = getIntent().getIntExtra("type",0);
        time = getIntent().getIntExtra("time",6000);
        words = GBaseData.getInstance().guessResult.getGrouplist().get(type).getWords();
        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        gplay_tv_tips = (TextView)findViewById(R.id.gplay_tv_tips);
        gplay_tv_time = (TextView)findViewById(R.id.gplay_tv_time);
        adapter = new MyPagerAdapter(this,words);
        viewPager.setAdapter(adapter);
        viewPager.setInterval(2000);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setTouchEnable(false);
        //获取感应器管理器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorEventListener = new MySensorEventListener();
        aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(sensorEventListener, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
        setPagerVisible();
        GBaseData.getInstance().clear();
        setOnStateChangeLister(new StateChangeListener() {
            @Override
            public void onStateChangeListener() {
                String str = "";
                if(state == STATE_READY){
                    str = "准备";
                    gplay_tv_tips.setText(str);
                    setPagerVisible();
                    rc = new ReadyCount(4000,1000);
                    rc.start();
                }
                else if(state == STATE_PLAY) {
                    if (currentMode == MODE_VERTICAL) {
                        viewPager.autoScrollToNext();
                    } else if (currentMode == MODE_DOWN) {
                        wordState = new WordState();
                        wordState.setWord(adapter.getCurrentContent());
                        wordState.setIsright(true);

                        GBaseData.getInstance().addWord(wordState);
                        str = "正确";
                        adapter.updateContent(str);
                    } else if (currentMode == MODE_UP) {
                        wordState = new WordState();
                        wordState.setWord(adapter.getCurrentContent());
                        str = "跳过";
                        wordState.setIsright(false);
                        GBaseData.getInstance().addWord(wordState);
                        adapter.updateContent(str);
                    }
                }
            }
        });
    }

    /**
     * 设置viewpager是否显示
     */
    private void setPagerVisible(){
        if(state == STATE_NONE){
            viewPager.setVisibility(View.GONE);
            gplay_tv_time.setVisibility(View.GONE);
            gplay_tv_tips.setVisibility(View.VISIBLE);
        }
        else if(state == STATE_READY){
            viewPager.setVisibility(View.GONE);
            gplay_tv_time.setVisibility(View.VISIBLE);
            gplay_tv_tips.setVisibility(View.VISIBLE);
        }
        else if(state == STATE_PLAY){
            viewPager.setVisibility(View.VISIBLE);
            gplay_tv_time.setVisibility(View.GONE);
            gplay_tv_tips.setVisibility(View.GONE);
        }
    }
    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            System.out.println("onPageSelected---------------------" + arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            System.out.println("onPageScrolled---------------------" + arg0);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            System.out.println("onPageScrollStateChanged---------------------" + arg0);
        }
    }

    private final class MySensorEventListener implements SensorEventListener {
        @Override
        //可以得到传感器实时测量出来的变化值
        public void onSensorChanged(SensorEvent event) {
            //重力传感器
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticFieldValues = event.values;
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = event.values;
            calculateOrientation();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);
        // 要经过一次数据格式的转换，转换为度
        values[2] = (float) Math.toDegrees(values[2]);
        float v = Math.abs(values[2]);
        Log.i(TAG, String.valueOf(v));
        if(v >= 80 && v <= 100){
            mode = MODE_VERTICAL;
            Log.i(TAG, "竖起");
        }
        else if(v >= 150 && v <= 180){
            mode = MODE_DOWN;
            Log.i(TAG, "下翻");
        }
        else if(v >= 0 && v <= 30){
            mode = MODE_UP;
            Log.i(TAG, "上翻");
        }
        if(currentMode != mode){
            if(state == STATE_NONE && mode == MODE_VERTICAL){//初始状态只能响应竖立状态
                state = STATE_READY;
            }
            else if(state == STATE_READY){
                return;
            }
            else if(state == STATE_PLAY){
                if((mode == MODE_DOWN && currentMode == MODE_UP) || (mode == MODE_UP && currentMode == MODE_DOWN)){  //上翻直接到下翻，或者下翻直接到上翻无效
                    return;
                }
            }
            currentMode = mode;
            listener.onStateChangeListener();
        }

    }

    //我们在onResume方法中创建重力传感器，并向系统注册监听器
    protected void onResume() {
        Sensor sensor_accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(sensorEventListener, sensor_accelerometer, SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    //最后我们在onPause()中注销所有传感器的监听，释放重力感应器资源!
    protected void onPause() {
    // 注销所有传感器的监听
        sensorManager.unregisterListener(sensorEventListener);
        super.onPause();
    }

    private void setOnStateChangeLister(StateChangeListener listener){
        this.listener = listener;
    }

    public interface StateChangeListener{
        void onStateChangeListener();
    }

    /*定义一个倒计时的内部类*/
    public class ReadyCount extends CountDownTimer{
        public ReadyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
//            tv.setText("finish");
            state = STATE_PLAY;
            if(adapter != null){
                mc = new PlayCount(time, 1000);
                mc.start();
                setPagerVisible();
//                wordState = new WordState();
//                wordState.setWord(adapter.getCurrentContent());
            }
        }
        @Override
        public void onTick(long millisUntilFinished) {
            gplay_tv_time.setText("" + millisUntilFinished / 1000);
        }
    }


    /*定义一个倒计时的内部类*/
    public class PlayCount extends CountDownTimer{
        public PlayCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            if(adapter != null){
                adapter.updateContent("时间到");
                adapter.updateTime("");
                handler.postDelayed(runnable, 3000);
            }
        }
        @Override
        public void onTick(long millisUntilFinished) {
//            tv.setText("请等待30秒(" + millisUntilFinished / 1000 + ")...");
//            Toast.makeText(NewActivity.this, millisUntilFinished / 1000 + "", Toast.LENGTH_LONG).show();//toast有显示时间延迟
            if(adapter != null){
                adapter.updateTime("" + millisUntilFinished / 1000);
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(PlayActivity.this,CompleteActivity.class);
            startActivity(intent);
        }
    };
}
