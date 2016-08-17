package partygames.shirley.com.partygames;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import partygames.shirley.com.baselib.BaseActivity;
import partygames.shirley.com.baselib.SettingPreferences;
import partygames.shirley.com.baselib.utils.DialogUtils;
import partygames.shirley.com.baselib.view.RoundSpinView;
import partygames.shirley.com.playandguesslib.GMenuActivity;
import partygames.shirley.com.undercoverlib.UnderCoverSetting;

public class Main2Activity extends BaseActivity implements RoundSpinView.onRoundSpinViewListener, View.OnClickListener {

    private RoundSpinView rsv_test;
    private Context context;
    private ImageView guess_menu_sound;
    private DialogUtils dialogUtils = null;
    private Dialog showHelpDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context = this;
        initView();
        isSoundOpen = SettingPreferences.getSettingValue(context,SettingPreferences.KEY_SETTING_SOUND,true);
        guess_menu_sound = (ImageView)findViewById(R.id.guess_menu_sound);
        guess_menu_sound.setOnClickListener(this);
        setSoundBack();
        findViewById(R.id.guess_menu_help).setOnClickListener(this);
        checkUpdate(context);
    }

    private void initView(){
        rsv_test = (RoundSpinView)this.findViewById(R.id.rsv_test);
        rsv_test.setOnRoundSpinViewListener(this);
    }

    /**
     * 设置声音按钮的背景
     */
    private void setSoundBack(){
        if(isSoundOpen){
            guess_menu_sound.setImageResource(partygames.shirley.com.playandguesslib.R.mipmap.sound_btn_on);
        }
        else{
            guess_menu_sound.setImageResource(partygames.shirley.com.playandguesslib.R.mipmap.sound_btn_off);
        }
    }

    @Override
    public void onSingleTapUp(int position) {
        // TODO Auto-generated method stub
        if(position == 0){
            Intent intent = new Intent(Main2Activity.this,GMenuActivity.class);
            startActivity(intent);
        }
        else if(position == 1){
            Intent intent = new Intent(Main2Activity.this,UnderCoverSetting.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.guess_menu_sound) {
            isSoundOpen = !isSoundOpen;
            SettingPreferences.setSettingValue(context, SettingPreferences.KEY_SETTING_SOUND, isSoundOpen);
            setSoundBack();
        }
        else if(id == R.id.guess_menu_help){
//            showHelpDialog();
//            AdUtils.openAd(context);
            Intent intent = new Intent(context,HelpActivity.class);
            startActivity(intent);
        }
    }


    /**
     * 帮助对话框
     *
     */
//    public void showHelpDialog() {
//        if (showHelpDialog != null) {
//            showHelpDialog.dismiss();
//        }
//        showHelpDialog = new Dialog(context, partygames.shirley.com.playandguesslib.R.style.CustomDialog);
//        View view = View.inflate(context, R.layout.help_dialog, null);
//        showHelpDialog.setContentView(view);
//        DisplayMetrics outMetrics  = context.getResources().getDisplayMetrics();
//        int width = outMetrics.widthPixels > outMetrics.heightPixels ? outMetrics.heightPixels : outMetrics.widthPixels;
////        chooseTimeDialog.getWindow().setLayout(width-100, 0);
//        showHelpDialog.show();
//    }

    private void checkUpdate(final Context context) {
        PgyUpdateManager.register(this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {
                        System.out.println("onUpdateAvailable:" + result);
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        dialogUtils = new DialogUtils(context, "更新", appBean.getReleaseNote(), new DialogUtils.OnDialogSelectId() {
                            @Override
                            public void onClick(int whichButton) {
                                switch (whichButton) {
                                    case 0:
                                        dialogUtils.dismiss();
                                        break;
                                    case 1:
                                        dialogUtils.dismiss();
                                        startDownloadTask(Main2Activity.this,
                                                appBean.getDownloadURL());
                                        break;
                                }
                            }
                        });
                        dialogUtils.show();
                        dialogUtils.setConfirmText(context.getResources().getString(partygames.shirley.com.playandguesslib.R.string.download_tips));
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        System.out.println("onNoUpdateAvailable-------------------");
                    }
                }

        );
    }
}