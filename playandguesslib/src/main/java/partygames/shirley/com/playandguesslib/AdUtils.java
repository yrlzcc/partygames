package partygames.shirley.com.playandguesslib;

//import net.youmi.android.AdManager;
//import net.youmi.android.banner.AdSize;
//import net.youmi.android.banner.AdView;
//import net.youmi.android.onlineconfig.OnlineConfigCallBack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;

import th.ds.wa.AdManager;
import th.ds.wa.normal.banner.BannerManager;
import th.ds.wa.normal.spot.SpotDialogListener;
import th.ds.wa.normal.spot.SpotManager;
import th.ds.wa.onlineconfig.OnlineConfigCallBack;

/**
 * Created by lichuang on 2016/5/18.
 */
public class AdUtils {
    private static boolean isTestOpen = false;  //自己测试时用的开关
    public static boolean isOpen = true;
    public static boolean isSplashOpen = true;
    public static boolean isBannerOpen = true;
    public static void setAD(Activity context,View view){
        // 实例化广告条
        View adView = BannerManager.getInstance(context).getBanner(context);

        // 获取要嵌入广告条的布局
        LinearLayout adLayout=(LinearLayout)view.findViewById(R.id.adLayout);

        // 将广告条加入到布局中
        adLayout.addView(adView);
    }

    public static void setAdInViewGroup(Activity context,LinearLayout adLayout){

        View adView = BannerManager.getInstance(context).getBanner(context);
        adLayout.addView(adView);
    }

    public static void getOnlineVar( Context context){
        // 方法二： 异步调用（可在任意线程中调用）
        SharedPreferences sharedPreferences = context.getSharedPreferences("onlinevar", Context.MODE_PRIVATE);
        final SharedPreferences.Editor  editor = sharedPreferences.edit();
        AdManager.getInstance(context).asyncGetOnlineConfig("openAd", new OnlineConfigCallBack() {
            @Override
            public void onGetOnlineConfigSuccessful(String key, String value) {
                // TODO Auto-generated method stub
                // 获取在线参数成功
                editor.putInt(key, Integer.parseInt(value));
                editor.commit();
                int v = Integer.parseInt(value);
                System.out.println("onlinevar:" + v);
                isOpen = (v == 1 ? true : false);
                isSplashOpen = isOpen;

            }

            @Override
            public void onGetOnlineConfigFailed(String key) {
                // TODO Auto-generated method stub
                System.out.println("get online var failed!");
            }
        });
    }

    public static boolean openAd(Context context){
        if(isOpen){
            SpotManager.getInstance(context).showSpotAds(context);
        }
        if(isTestOpen){
            SpotManager.getInstance(context).showSpotAds(context);
        }
        return isOpen;
    }

    public static void openTestAd(Context context){
        if(isTestOpen){
            SpotManager.getInstance(context).showSpotAds(context);
        }
    }

    public static boolean openAd(Context context,SpotDialogListener listener){
        if(isOpen){
            SpotManager.getInstance(context).showSpotAds(context,listener);
        }
        return isOpen;
    }

    public static boolean openSplashAd(Context context,Class cls){
        if(isSplashOpen){
            SpotManager.getInstance(context).showSplashSpotAds(context,cls);
        }
        return isSplashOpen;
    }

    public static void openBanner(Activity context,View view){
        if(isBannerOpen){
            setAD(context,view);
        }
    }
}
