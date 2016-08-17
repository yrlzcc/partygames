package partygames.shirley.com.undercoverlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 声音播放类
 * 
 * @author Wenson
 * @version 2013-07-06
 */
public class SoundPlayer{

	/* 背景音乐 */
	private static MediaPlayer music;
	/* 游戏音效 */
	private static SoundPool soundPool;
	/* 音乐开关 */
	private static boolean musicSt = true; 
	/* 音效开关 */
	private static boolean soundSt = true;
	private static Context context;
	private static Map<Integer,Integer> soundMap; 
	private static boolean inited = false;
	
	/**
	 * 初始化音效
	 * 
	 * @param pContext
	 */
	@SuppressLint("UseSparseArrays")
	public static void init(Context pContext) {
		context = pContext;
		if (!inited) {
			soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
			soundMap = new HashMap<Integer, Integer>();
			inited = true;
		}
	}
	
	/**
	 * 播放音效
	 * 
	 * @param resId
	 *            资源ID
	 */
	public static void playSound(int resId)
	{
		if(soundSt)
		{
			Integer soundId = soundMap.get(resId);
			if(soundId != null)
				soundPool.play(soundId, 1, 1, 1, 0, 1);
		}
	}
	
	public static void playSoundRepate(int resId) {
		if (soundSt) {
			Integer soundId = soundMap.get(resId);
			if (soundId != null)
				soundPool.play(soundId, 1, 1, 1, -1, 1);
		}
	}

	/**
	 * 加载音效
	 * 
	 * @param resId
	 */
	public static void pushSound(int resId)
	{
		if(!soundMap.containsKey(resId))
			soundMap.put(resId, soundPool.load(context, resId, 1));
	}
	
	/**
	 * 播放音乐
	 * 
	 * @param resId
	 * @param loop
	 */
	public static void playMusic(int resId, boolean loop) {
		if (musicSt) {
			music = MediaPlayer.create(context, resId);
			music.start();
			music.setLooping(loop);
		}
	}
	
	/**
	 * 暂停音乐
	 */
	public static void pauseMusic()
	{
		if( music!=null){
			if(music.isPlaying())
				music.pause();
		}
	}
	
	/**
	 * 获得音乐开关状态
	 * 
	 * @return
	 */
	public static boolean getMusicSt() {
		return musicSt;
	}
	
	/**
	 * 设置音乐开关状态
	 * 
	 * @param musicSt
	 */
	public static void setMusicSt(boolean musicSt) {
		SoundPlayer.musicSt = musicSt;
		if(musicSt)
			music.start();
		else
			music.stop();
	}
	
	/**
	 * 获取音效开关状态
	 * 
	 * @return
	 */
	public static boolean getSoundSt() {
		return soundSt;
	}
	
	/**
	 * 设置音效开关状态
	 * 
	 * @param soundSt
	 */
	public static void setSoundSt(boolean soundSt) {
		SoundPlayer.soundSt = soundSt;
		
	}
	
	public static void out() {
//		playSound(R.raw.whistle);
	}

	/**
	 * 人多数胜利
	 */
	public static void highSouce() {
//		playSound(R.raw.normalscore);
	}

	/**
	 * 人少数胜利
	 */
	public static void normalSouce() {
		playSound(R.raw.hiscore02);
	}


	public static void shake() {
//		playSound(R.raw.shake_sound);
	}
	public static void start() {
//		playSound(R.raw.uncover);
	}
	public static void click() {
//		playSound(R.raw.cancel);
	}
	public static void faile() {
		playSound(R.raw.failshout);
	}
}