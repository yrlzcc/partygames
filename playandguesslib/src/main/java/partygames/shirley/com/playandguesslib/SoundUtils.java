package partygames.shirley.com.playandguesslib;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/7.
 */
public class SoundUtils {
    public static final int CARD_OPEN = 1;
    public static final int CORRECT = 2;
    public static final int PASS = 3;
    public static final int TIMEUP = 4;
    private SoundPool sp = null;
    HashMap<Integer,Integer> spMap = null;
    private Context context;

    public SoundUtils(Context context){
        this.context = context;
        initSoundPool();
    }

    public void playSound(int sound, int number) { //播放声音,参数sound是播放音效的id，参数number是播放音效的次数
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);//实例化AudioManager对象
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC); //返回当前AudioManager对象的最大音量值
        float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);//返回当前AudioManager对象的音量值
        float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
        sp.play(
                spMap.get(sound), //播放的音乐id
                volumnRatio, //左声道音量
                volumnRatio, //右声道音量
                1, //优先级，0为最低
                number, //循环次数，0不循环，-1永远循环
                1 //回放速度 ，该值在0.5-2.0之间，1为正常速度
        );
    }

    public void initSoundPool() { //初始化声音池
        sp = new SoundPool(
                5, //maxStreams参数，该参数为设置同时能够播放多少音效
                AudioManager.STREAM_MUSIC, //streamType参数，该参数设置音频类型，在游戏中通常设置为：STREAM_MUSIC
                0 //srcQuality参数，该参数设置音频文件的质量，目前还没有效果，设置为0为默认值。
                );
        spMap = new HashMap();
        spMap.put(CARD_OPEN, sp.load(context, R.raw.cardsopen, 1));
        spMap.put(CORRECT, sp.load(context, R.raw.correct, 1));
        spMap.put(PASS, sp.load(context, R.raw.pass, 1));
        spMap.put(TIMEUP, sp.load(context, R.raw.timeover, 1));
        }
}
