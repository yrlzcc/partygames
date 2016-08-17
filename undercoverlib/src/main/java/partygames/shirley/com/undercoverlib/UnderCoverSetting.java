package partygames.shirley.com.undercoverlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import partygames.shirley.com.baselib.SettingPreferences;

public class UnderCoverSetting extends UnderBaseActivity implements View.OnClickListener {
    private LinearLayout linearUndercover;
    private ToggleButton switch_blank;  //白板开关控件
    private TextView normal_num; //平民人数人控件
    private SeekBar seekPeople;
    private TextView labPeople;
    private TextView tv_undercoverCount;
    private TextView txtName;
    private boolean isBlankOpen = false; //白板是否打开
    private int blankPeopleCount = 0;   //白板人数
    private int peopleCount = 4;   //参与总人数
    private int maxUnderCount;  //最大卧底人数
    private int basePeople = 4; //最少人数
    private int undercovercount = 1; //卧底人数
    private int normalcount = 3; //平民人数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.undercover_setting);
        getSettings();
        context = this;
        linearUndercover = (LinearLayout) this.findViewById(R.id.tem2);
        seekPeople = (SeekBar) this.findViewById(R.id.seekPeople);
        labPeople = (TextView) this.findViewById(R.id.labPeople);
        tv_undercoverCount = (TextView) this.findViewById(R.id.undercover_num);
        normal_num = (TextView) this.findViewById(R.id.normal_num);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.people_minus).setOnClickListener(this);
        findViewById(R.id.people_add).setOnClickListener(this);
        switch_blank = (ToggleButton) findViewById(R.id.switch_blank);
        findViewById(R.id.undercover_num_minus).setOnClickListener(this);
        findViewById(R.id.undercover_num_add).setOnClickListener(this);
        seekPeople.setMax(12);
        seekPeople.setSecondaryProgress(12);
        basePeople = 4;
        seekPeople.setProgress(peopleCount - basePeople);
        labPeople.setText(String.valueOf(peopleCount));
        tv_undercoverCount.setText(String.format(context.getResources().getString(R.string.undercover_num), undercovercount));
        normal_num.setText(String.format(context.getResources().getString(R.string.normalnum), normalcount));

        /**
         * 参与人数修改
         */
        seekPeople.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                peopleCount = basePeople + progress;
                labPeople.setText(String.valueOf(peopleCount));
                undercovercount = (4 + progress) / 3;
                maxUnderCount = undercovercount;
                normalcount = peopleCount - undercovercount - blankPeopleCount;
                tv_undercoverCount.setText(String.format(context.getResources().getString(R.string.undercover_num), undercovercount));
                normal_num.setText(String.format(context.getResources().getString(R.string.normalnum), normalcount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }
        });

        /**
         * 白板开关
         */
        switch_blank.setChecked(isBlankOpen);
        initBlank();
        switch_blank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBlankOpen = isChecked;
                if (isBlankOpen) {
                    blankPeopleCount = 1;
                } else {
                    blankPeopleCount = 0;
                }
                normalcount = peopleCount - undercovercount - blankPeopleCount;
                normal_num.setText(String.format(context.getResources().getString(R.string.normalnum), normalcount));
            }
        });
    }

    private void initBlank() {
        if (isBlankOpen) {
            blankPeopleCount = 1;
        } else {
            blankPeopleCount = 0;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.undercover_num_minus) { /**卧底人数减少*/
            if (undercovercount == 1) {
                return;
            }
            undercovercount--;
            normalcount = peopleCount - undercovercount - blankPeopleCount;
            tv_undercoverCount.setText(String.format(context.getResources().getString(R.string.undercover_num), undercovercount));
            normal_num.setText(String.format(context.getResources().getString(R.string.normalnum), normalcount));
        } else if (id == R.id.undercover_num_add) { /**卧底人数增加*/
            if (undercovercount >= maxUnderCount) {
                return;
            }
            undercovercount++;
            normalcount = peopleCount - undercovercount - blankPeopleCount;
            tv_undercoverCount.setText(String.format(context.getResources().getString(R.string.undercover_num), undercovercount));
            normal_num.setText(String.format(context.getResources().getString(R.string.normalnum), normalcount));
        } else if (id == R.id.btnStart) { /**开始游戏*/
            saveSettings();
            Intent goMain = new Intent();
            goMain.setClass(UnderCoverSetting.this, UndercoverActivity.class);
            startActivity(goMain);
        } else if (id == R.id.people_minus) {     /** * 参与人数减少 */
            int now = seekPeople.getProgress();
            if (now > 0) {
                seekPeople.setProgress(now - 1);
            }
        } else if (id == R.id.people_add) {/** * 参与人数增加 */
            int now = seekPeople.getProgress();
            if (now < seekPeople.getMax()) {
                seekPeople.setProgress(now + 1);
            }
        }
    }

    /**
     * 保存设置数据
     */
    private void saveSettings() {
        SettingPreferences.setSettingValue(context, SettingPreferences.UNDERCOVER_SETTING_PEOPLECOUNT, peopleCount);
        SettingPreferences.setSettingValue(context, SettingPreferences.UNDERCOVER_SETTING_UNDERCOUNT, undercovercount);
        SettingPreferences.setSettingValue(context, SettingPreferences.UNDERCOVER_SETTING_NORMALCOUNT, normalcount);
        SettingPreferences.setSettingValue(context, SettingPreferences.UNDERCOVER_SETTING_ISBLANK, isBlankOpen);
    }

    /**
     * 恢复设置数据
     */
    private void getSettings() {
        peopleCount = getPeopleCount();
        undercovercount = getUnderCount();
        normalcount = getNormalCount();
        isBlankOpen = isBlankOpen();
    }
}
