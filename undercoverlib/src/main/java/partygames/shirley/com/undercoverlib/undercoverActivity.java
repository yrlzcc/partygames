package partygames.shirley.com.undercoverlib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Random;

import partygames.shirley.com.baselib.SettingPreferences;

public class UndercoverActivity extends UnderBaseActivity {
    private String underWord;
    private String[] content;  //词组
    private String[] libary;
    private TextView txtShenfen;
    private TextView txtName;
    private Button imagePan;
    /**
     * 按钮--记住了，传给下一位
     */
    private ImageView imagebg;
    private Random random;
    private int nowIndex = 1;
    private boolean isShow;
    private boolean isBlankOpen;
    private boolean isShowWords = false;
    //
    private int peopleCount;   //参与人数
    private int undercovercount; //卧底人数
    private int normalcount; //平民人数

    private SharedPreferences gameInfo;
    private String blank;
    private String word;
    private Animation animation;
    private LinearLayout linChangeword;
    protected String faguan;
    protected String police;
    protected String killer;
    protected String nomalpeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.under_getword);
        context = this;
        getSettings();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        txtShenfen = (TextView) findViewById(R.id.txtShenfen);
        txtName = (TextView) findViewById(R.id.txtName);
        imagePan = (Button) findViewById(R.id.imagePan);
        imagebg = (ImageView) findViewById(R.id.imagebg);
        blank = "";
        random = new Random();
        content = new String[0];
        gameInfo = getSharedPreferences("gameinfo", 0);
        // 游戏一轮结束后，快速开始用。
        word = "全部";
        initFanpai();
        animation = AnimationUtils.loadAnimation(this,
                R.anim.reflash);


        imagePan.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapPai(v);
            }
        });

        imagebg.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapPai(v);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initFanpai();
    }


    protected void tapPai(View v) {
        setContentVis(!isShowWords);
        // 在这里更新nowIndex，不至于呀恢复时错开一个
        if (isShowWords) {
            nowIndex++;
            if (nowIndex <= content.length) {
                initPan(nowIndex);
            } else {
                Intent goMain = new Intent();
                goMain.setClass(UndercoverActivity.this, UnderCoverGuess.class);
                startActivity(goMain);
                finish();
                //这一块重新初始化好了
            }
        } else {
			SoundPlayer.click();
        }
        isShowWords = !isShowWords;
    }

    // 重新翻牌
    protected void initFanpai() {
        // 如果是杀人游戏，界面显示去掉一些东西
            libary = getUnderWords(word);
            int selectindex = Math.abs(random.nextInt()) % libary.length;
            content = getRandomStringUnderCover(libary[selectindex]);
            gamewillstart();
        // 设置content

    }

    protected void gamewillstart() {
        setContent(content);
        nowIndex = 1;
        setContentVis(false);
        initPan(nowIndex);
		SoundPlayer.start();
    }

    protected void initPan(int index) {
        String strTips = "";
        if(index == 1){
            strTips = String.format(context.getString(R.string.first_to_see),index);
        }
        else{
            strTips = String.format(context.getString(R.string.other_to_see),index);
        }
        txtName.setText(strTips);
        setContentVis(false);
        txtShenfen.setText(content[index - 1]);
    }

    protected void Log(String string) {
        Log.v("tag", string);
    }

    protected void setContentVis(boolean show) {
        if (show) {
            txtShenfen.setVisibility(View.VISIBLE);
            imagePan.setVisibility(View.VISIBLE);
            imagebg.setBackgroundResource(R.mipmap.card_info);
            imagebg.setClickable(false);
            imagebg.setEnabled(false);
        } else {
            txtShenfen.setVisibility(View.INVISIBLE);
            imagePan.setVisibility(View.INVISIBLE);
            imagebg.setBackgroundResource(R.mipmap.card_back);
            imagebg.setClickable(true);
            imagebg.setEnabled(true);
        }
    }

    /**
     * 获取随机的一堆词条
     * @return
     */
    private String[] getRandomStringUnderCover(String word) {
        String[] children=word.split("_");
        setHasGuessed(word);
        int sonindex = Math.abs(random.nextInt()) % 2;
        underWord = children[sonindex];
        String father = children[Math.abs(sonindex - 1)];
        String[] ret = new String[peopleCount];
        for (int n = 0; n < ret.length; n++) {
            ret[n] = father;
        }
        for (int i = 0; i < undercovercount; i++) {
            int tem;
            do {
                tem = Math.abs(random.nextInt()) % peopleCount;
            } while (ret[tem].equals(underWord));
            ret[tem] = underWord;
        }

        if (isBlankOpen) {
            for (int i = 0; i < 1; i++) {  //白板就1个
                int tem;
                do {
                    tem = Math.abs(random.nextInt()) % peopleCount;
                } while (ret[tem].equals(underWord));
                ret[tem] = blank;
            }
        }
        setUnderWord(underWord);
        setNormalWord(father);
        return ret;
    }


    /**
     * 获取随机的一堆词条
     * @return
     */
    private String[] getRandomString() {
        peopleCount = gameInfo.getInt("peopleCount", 4);
        int policeCount = Math.max((int) Math.floor(peopleCount / 4), 1);
        int killerCount = Math.max((int) Math.floor(peopleCount / 4), 1);
        String[] ret = new String[peopleCount];
        for (int n = 0; n < ret.length; n++) {
            ret[n] = nomalpeople;
        }
        ret[Math.abs(random.nextInt()) % peopleCount] = faguan;
        for (int i = 0; i < policeCount; i++) {
            int tem;
            do {
                tem = Math.abs(random.nextInt()) % peopleCount;
            } while (!ret[tem].equals(nomalpeople));
            ret[tem] = police;
        }
        for (int i = 0; i < killerCount; i++) {
            int tem;
            do {
                tem = Math.abs(random.nextInt()) % peopleCount;
            } while (!ret[tem].equals(nomalpeople));
            ret[tem] = killer;
        }
        // 设置content
        setContent(ret);
        setUnderWord(underWord);
        return ret;
    }
    @Override
    public void CallBackPublicCommand(JSONObject jsonobj, String cmd) {
        super.CallBackPublicCommand(jsonobj, cmd);
        if (cmd.equals(ConstantControl.WORD_UNDERCOVER)) {
            try {
                JSONObject obj = new JSONObject(jsonobj.getString("data"));
                content = getRandomStringUnderCover(obj.getString("word"));
                //obj={"uid":"A0000043A574DC","username":"","gameuid":310,"time":1414514074,"newgameimage":"http:\/\/192.168.1.120\/CenturyServer\/www\/image\/recom_1.png","newgamename":"我爱我OR不要脸","_id":310,"newgame":1,"pushcount":"0","photo":"","channel":"ANDROID"}
            } catch (Exception e) {
                libary = getUnderWords(word);
                int selectindex = Math.abs(random.nextInt()) % libary.length;
                content = getRandomStringUnderCover(libary[selectindex]);
                // TODO: handle exception
                e.printStackTrace();
            }
            gamewillstart();
        }
    }

    @Override
    public void CallBackPublicCommandWrong( String cmd) {
        super.CallBackPublicCommandWrong( cmd);
        if (cmd.equals(ConstantControl.WORD_UNDERCOVER)) {
            libary = getUnderWords(word);
            int selectindex = Math.abs(random.nextInt()) % libary.length;
            content = getRandomStringUnderCover(libary[selectindex]);
            // TODO: handle exception
            gamewillstart();
        }
    }
    /**
     * 恢复设置数据
     */
    private void getSettings(){
        peopleCount = SettingPreferences.getValue(context,SettingPreferences.UNDERCOVER_SETTING_PEOPLECOUNT,4);
        undercovercount = SettingPreferences.getValue(context,SettingPreferences.UNDERCOVER_SETTING_UNDERCOUNT,1);
        normalcount = SettingPreferences.getValue(context,SettingPreferences.UNDERCOVER_SETTING_NORMALCOUNT,3);
        isBlankOpen = SettingPreferences.getSettingValue(context,SettingPreferences.UNDERCOVER_SETTING_ISBLANK,false);
    }
}
