package partygames.shirley.com.baselib.utils;

import android.app.Activity;
import android.graphics.BitmapFactory;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import partygames.shirley.com.baselib.R;


/**
 * Created by Administrator on 2016/6/17.
 */
public class ShareUtils {
    private static ShareUtils instance = null;

    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    public static ShareUtils getInstance(){
        if(instance == null){
            instance = new ShareUtils();
        }
        return instance;
    }


    /**
     * 游戏下拉菜单中的分享
     *
     */
    public void showShare(Activity activity,String strText,UMShareListener listener) {
//        Constans.SHAREURL= MobclickAgent.getConfigParams(activity, "party_shareurl");
        Constans.SHAREURL = "https://www.pgyer.com/partygames";
        System.out.println("shareurl:" + Constans.SHAREURL);
        UMImage image = new UMImage(activity,
                BitmapFactory.decodeResource(activity.getResources(),R.drawable.icon));
        ShareContent content = new ShareContent();
        content.mTitle = "聚会神器";
        content.mText = strText;
        new ShareAction(activity).setDisplayList(displaylist)
                .withText(strText)
                .withTitle("聚会神器")
                .setShareContent(content)
                .withMedia(image)
                .withTargetUrl(Constans.SHAREURL)
                .setCallback(listener)
                .open();
    }

}
