package partygames.shirley.com.baselib;

import android.app.Application;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/5/5.
 */
public class BaseApplication extends Application {

        public static final String TAG = BaseApplication.class.getSimpleName();
        private static BaseApplication application;
        public static BaseApplication getInstance(){
            return application;
        }

        @Override
        public void onCreate() {
            // TODO Auto-generated method stub
            super.onCreate();
            application = this;
            MobclickAgent.setDebugMode(true);
//            AdManager.getInstance(this).init("97345f1b6d032a9a", "e9192ebe202a59b2", false, true);
//            AdUtils.getOnlineVar(this);
//            SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
//            SpotManager.getInstance(this).loadSpotAds();

//            PlatformConfig.setWeixin("wx937deb05a1d753fc", "71798ba222027d046b2ad8d0fdb3db8c");
//            //微信 appid appsecret
//            PlatformConfig.setSinaWeibo("wx937deb05a1d753fc", "71798ba222027d046b2ad8d0fdb3db8c");
//            //新浪微博 appkey appsecret
//            PlatformConfig.setQQZone("1105473130", "KxwTx0fR8SHCBdaD");
            // QQ和Qzone appid appkey
        }

}
