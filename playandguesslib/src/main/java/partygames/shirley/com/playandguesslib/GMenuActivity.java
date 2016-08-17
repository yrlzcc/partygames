package partygames.shirley.com.playandguesslib;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import partygames.shirley.com.baselib.BaseActivity;
import partygames.shirley.com.baselib.SettingPreferences;

public class GMenuActivity extends BaseActivity implements View.OnClickListener {
    private Dialog chooseTimeDialog = null;
    private Context context;
    private int type = 0;
    private TextView dialog_choose_time_60s;
    private TextView dialog_choose_time_90s;
    private TextView dialog_choose_time_180s;
    private ImageView dialog_choose_time_start;
    private ImageView dialog_choose_time_title;
    private int time; //时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmenu);
        context = this;
        findViewById(R.id.guess_menu_food).setOnClickListener(this);
        findViewById(R.id.guess_menu_chengyu).setOnClickListener(this);
        findViewById(R.id.guess_menu_tiyu).setOnClickListener(this);
        findViewById(R.id.guess_menu_song).setOnClickListener(this);
        findViewById(R.id.guess_menu_film).setOnClickListener(this);
        findViewById(R.id.guess_menu_popular).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.guess_menu_food) {
            showChooseTimeDialog(R.mipmap.title_food);
            type = 0;
        }
        else if(id == R.id.guess_menu_chengyu){
            showChooseTimeDialog(R.mipmap.title_chengyu);
            type = 1;
        }
        else if(id == R.id.guess_menu_tiyu){
            showChooseTimeDialog(R.mipmap.title_tiyu);
            type = 2;
        }
        else if(id == R.id.guess_menu_song){
            showChooseTimeDialog(R.mipmap.title_songs);
            type = 3;
        }
        else if(id == R.id.guess_menu_popular){
            showChooseTimeDialog(R.mipmap.title_popular);
            type = 4;
        }
        else if(id == R.id.guess_menu_film){
            showChooseTimeDialog(R.mipmap.title_film);
            type = 5;
        }
        else if(id == R.id.dialog_choose_time_start){
            if (chooseTimeDialog != null) {
                chooseTimeDialog.dismiss();
            }
            Intent intent = new Intent(GMenuActivity.this,PlayActivity.class);
            SettingPreferences.setSettingValue(context,SettingPreferences.GUESS_SETTING_TYPE,type);
            SettingPreferences.setSettingValue(context,SettingPreferences.GUESS_SETTING_TIME,time);
            startActivity(intent);
        }
        else if(id == R.id.dialog_choose_time_60s){
            time = 60000;
            dialog_choose_time_60s.setSelected(true);
            dialog_choose_time_90s.setSelected(false);
            dialog_choose_time_180s.setSelected(false);
        }
        else if(id == R.id.dialog_choose_time_90s){
            time = 90000;
            dialog_choose_time_60s.setSelected(false);
            dialog_choose_time_90s.setSelected(true);
            dialog_choose_time_180s.setSelected(false);
        }
        else if(id == R.id.dialog_choose_time_180s){
            time = 180000;
            dialog_choose_time_60s.setSelected(false);
            dialog_choose_time_90s.setSelected(false);
            dialog_choose_time_180s.setSelected(true);
        }
    }

    /**
     *选择时间对话框
     *
     */
    public void showChooseTimeDialog(int  resid) {
        if (chooseTimeDialog != null) {
            chooseTimeDialog.dismiss();
        }
        chooseTimeDialog = new Dialog(context, R.style.CustomDialog);
        View view = View.inflate(context, R.layout.choose_time_dialog, null);
        chooseTimeDialog.setContentView(view);
        DisplayMetrics outMetrics  = context.getResources().getDisplayMetrics();
        int width = outMetrics.widthPixels > outMetrics.heightPixels ? outMetrics.heightPixels : outMetrics.widthPixels;
//        chooseTimeDialog.getWindow().setLayout(width-100, 0);
        chooseTimeDialog.show();
        dialog_choose_time_title = (ImageView)view.findViewById(R.id.dialog_choose_time_title);
        dialog_choose_time_title.setImageResource(resid);
        dialog_choose_time_start =  (ImageView)view.findViewById(R.id.dialog_choose_time_start);
        dialog_choose_time_start.setOnClickListener(this);
        dialog_choose_time_60s = (TextView)view.findViewById(R.id.dialog_choose_time_60s);
        dialog_choose_time_60s.setOnClickListener(this);
        dialog_choose_time_90s = (TextView)view.findViewById(R.id.dialog_choose_time_90s);
        dialog_choose_time_90s.setOnClickListener(this);
        dialog_choose_time_180s = (TextView)view.findViewById(R.id.dialog_choose_time_180s);
        dialog_choose_time_180s.setOnClickListener(this);
        dialog_choose_time_60s.setSelected(true);
        dialog_choose_time_90s.setSelected(false);
        dialog_choose_time_180s.setSelected(false);
        time = 60000;
    }
}
