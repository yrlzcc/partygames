package partygames.shirley.com.baselib;



import android.content.Context;
import android.content.SharedPreferences;

import partygames.shirley.com.baselib.utils.Utils;

/**
 * Created by Administrator on 2016/5/9.
 */
public class SettingPreferences {
  private static final String SETTING_PRE_NAME = "setting_pre_config";

    public static final String KEY_SETTING_SOUND = "key_setting_sound";
    /** 是否是首次运行 */
    public static final String KEY_FIRST_LUNCH = "first_lunch";

    public static final String GUESS_SETTING_TYPE = "type";  //猜词类型
    public static final String GUESS_SETTING_TIME = "time";  //猜词时间

    public static final String UNDERCOVER_SETTING_PEOPLECOUNT = "peopleCount";  //参与人数
    public static final String UNDERCOVER_SETTING_UNDERCOUNT = "underCount";  //卧底人数
    public static final String UNDERCOVER_SETTING_NORMALCOUNT = "normalCount"; //平民人数
    public static final String UNDERCOVER_SETTING_ISBLANK = "isBlank"; //是否白板
    public static final String UNDERCOVER_SETTING_CONTENT = "content"; // 每个人选的词组
    public static final String UNDERCOVER_SETTING_UNDERCOVER_WORD = "undercoverword"; // 卧底词组
    public static final String UNDERCOVER_SETTING_NORMAL_WORD = "normalword"; // 平民词组
    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param value
     *            Boolean类型的值
     */
    public static void setSettingValue(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param value
     *            String类型的值
     */
    public static void setSettingValue(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param value
     *            Int类型的值
     */
    public static void setSettingValue(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param value
     *            Long类型的值
     */
    public static void setSettingValue(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    /**
     * @param context
     *            上下文对象
     * @param key
     *            sharedPreferences 存储的key
     * @param defaultValue
     *            默认的值
     * @return
     */
    public static boolean getSettingValue(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defaultValue);
        return value;
    }

    public static String getSetStringValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        String value = sp.getString(key, "");
        return value;
    }

    public static int getValue(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defaultValue);
        return value;
    }

    public static long getValue(Context context, String key, long defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING_PRE_NAME, Context.MODE_PRIVATE);
        long value = sp.getLong(key, defaultValue);
        return value;
    }

    public static boolean isFristLunch(Context context) {
        String version = getSetStringValue(context, KEY_FIRST_LUNCH);
        if (version.equals(Utils.getVersion(context))) {
            return false;
        }
        return true;

    }
}

