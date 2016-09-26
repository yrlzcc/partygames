package partygames.shirley.com.undercoverlib;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import partygames.shirley.com.baselib.utils.SoundPlayer;

public class UnderCoverGuess extends UnderBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final int NORMAL_SUCCESS = 1;
    private static final int UNDER_SUCCESS = 2;
    private static final int BLANK_SUCCESS = 3;
    public static final int ROLE_BLANK = 1;
    public static final int ROLE_NORMAL = 2;
    public static final int ROLE_UNDER = 3;
    private static final int WORD_TIPS = 1;
    private GridView gridView;
    TextView txtTitle;
    // 卧底人数
    private int underCount;
    private int blankCount;
    private boolean isBlankOpen = false;
    // 平民人数
    private int normalcount;
    private int policeCount;
    private int killerCount;
    private int otherCount;
    // 卧底的词语
    private String underCoverWord;
    private String normalWord;
    // 0-n人数的词语数组
    private String[] content;
    private Button punishBtn;
    private Button btn_restart;

    private int temindex;
    private Random random = new Random();
    private List<GridItem> gridItems;
    GridAdapter gridAdapter = null;
//	private TextView txtLong;
    // 还有人员分配
    /**
     * 判断游戏是否结束
     */
    private boolean gamefinish = false;
    private boolean[] hasClicked;
    private Dialog showCompleteDialog = null;
    private Button btn_forget_word;
    private TextView tv_forget_word_tips;
    private boolean isWordTipsState = false;
    private TextView tv_forget_word_show;

    /**
     * 用来注册所有的按键，用户来停用或可用
     */
    private List<Button> regButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.under_guess);
        context = this;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        regButton = new ArrayList<Button>();
        gridView = (GridView) findViewById(R.id.tableContent);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        content = getGuessContent();
        btn_forget_word = (Button) findViewById(R.id.btn_forget_word);
        btn_forget_word.setOnClickListener(this);
        tv_forget_word_tips = (TextView) findViewById(R.id.tv_forget_word_tips);
        tv_forget_word_show = (TextView) findViewById(R.id.tv_forget_word_show);
        initUnderCover();
        gridAdapter = new GridAdapter(this, gridItems);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        txtTitle.setText(saySeqString());
    }

    /**
     * 初始化
     */
    private void initUnderCover() {
        underCoverWord = getUnderWord();
        normalWord = getNormalWord();
        underCount = getUnderCount();
        normalcount = getNormalCount();
        isBlankOpen = isBlankOpen();
        if (isBlankOpen) {
            blankCount = 1;
        } else {
            blankCount = 0;
        }
        gridItems = new ArrayList<GridItem>();
        gridItems.clear();
        for (int i = 0; i < content.length; i++) {
            GridItem item = new GridItem();
            item.isDead = false;
            item.strContent = content[i];
            if (content[i].equals(underCoverWord)) {
                item.role = ROLE_UNDER;
            } else if (content[i].equals(getResources().getString(R.string.blank))) {
                item.role = ROLE_BLANK;
            } else {
                item.role = ROLE_NORMAL;
            }
            gridItems.add(item);
        }
    }


    protected void setAllButton(boolean useable) {
        for (int i = 0; i < regButton.size(); i++) {
            Button tem = regButton.get(i);
            tem.setOnLongClickListener(new Button.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }

    /**
     * 下一轮描述顺序
     *
     * @return
     */
    protected String saySeqString() {
        List<Integer> hasNotClick = new ArrayList<Integer>();
        if(gridItems == null || gridItems.size() == 0){
            return "";
        }
        int size = gridItems.size();
        for (int i = 0; i < size; i++) {
            GridItem item = gridItems.get(i);
            if (!item.isDead) {
                hasNotClick.add(i);
            }
        }
        int seq = Math.abs(random.nextInt()) % hasNotClick.size();
        return (hasNotClick.get(seq) + 1) + "号玩家开始描述,描述完成后，线下投票，长按头像验证结果";
    }

    /**
     * 返回剩余人员配置
     *
     * @return
     */
    protected String getUnderCoverRemain() {
        return strFromId("aliveNUM") + underCount + strFromId("citizenNUM") + normalcount + strFromId("unitGE");
    }

    protected String getKillerCoverRemain() {
        return strFromId("aliveNUM") + underCount + strFromId("citizenNUM") + normalcount + strFromId("unitGE");
    }

    protected void checkGameOver() {
        int whoSuccess = NORMAL_SUCCESS;
        int count = underCount + normalcount + blankCount;
        if(underCount <= 0 && blankCount <= 0){
            whoSuccess = NORMAL_SUCCESS;
            SoundPlayer.highSouce();
            onGameOver(whoSuccess);
        }
        else if(count < 3 ){
            if(blankCount > 0) {
                whoSuccess = BLANK_SUCCESS;
            }
            else{
                whoSuccess = UNDER_SUCCESS;
            }
            SoundPlayer.highSouce();
            onGameOver(whoSuccess);
        }
        else if(normalcount <= 0 && blankCount <= 0){
            whoSuccess = UNDER_SUCCESS;
            SoundPlayer.highSouce();
            onGameOver(whoSuccess);
        }
        else{
            SoundPlayer.out();
            txtTitle.setText(saySeqString());
        }
    }

    private void onGameOver(int success) {
        if (!gamefinish) {
            gamefinish = true;
        }
//        showAllWord();
//        initControlBtn();
//        setAllButton(false);
//        punishBtn.setText(getUnderStr());
        cleanStatus();
        setGameIsNew(ConstantControl.GAME_UNDERCOVER, false);
        showSuccessDialog(success);
    }

    protected String getUnderStr() {
        String str = strFromId("shibaizhe");
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals(underCoverWord)) {
                int temhao = i + 1;
                String tem = String.format(strFromId("hao"), temhao);
                str += tem;
            }
        }
        return str;
    }

    protected String getNormalStr() {
        String str = strFromId("shibaizhe");
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals(underCoverWord)) {
                continue;
            }
            int temhao = i + 1;
            String tem = String.format(strFromId("hao"), temhao);
            str += tem;
        }
        return str;
    }

    protected void Log(String string) {
        Log.v("tag", string);
    }

    /**
     * 显示身份按钮
     */
    private void showAllWord() {
        // 所有身份亮明
        for (int i = 0; i < regButton.size(); i++) {
            Button tembtn = regButton.get(i);
            tembtn.setText(content[i]);
        }
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        // game_guess_finish
        super.finish();
        if(showCompleteDialog != null) {
            showCompleteDialog.dismiss();
        }
    }

    /**
     * 帮助对话框
     */
    public void showSuccessDialog(int success) {
        if (showCompleteDialog != null) {
            showCompleteDialog.dismiss();
        }
        showCompleteDialog = new Dialog(context, R.style.CustomDialog);
        showCompleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
                showCompleteDialog = null;
            }
        });
        View view = View.inflate(context, R.layout.complete_dialog, null);
        ImageView ivtitle = (ImageView) view.findViewById(R.id.dialog_complete_title);
        if (success == NORMAL_SUCCESS) {  //平民胜利
            ivtitle.setImageResource(R.mipmap.success_normal);
        } else if (success == UNDER_SUCCESS) { //卧底胜利
            ivtitle.setImageResource(R.mipmap.success_undercover);
        } else {      //白板胜利
            ivtitle.setImageResource(R.mipmap.success_blank);
        }
        TextView tvContent = (TextView) view.findViewById(R.id.dialog_complete_content);
        String strText = "平民词：" + normalWord + "\n";
        strText += "卧底词：" + underCoverWord;
        tvContent.setText(strText);
        showCompleteDialog.setContentView(view);
        DisplayMetrics outMetrics = context.getResources().getDisplayMetrics();
        int width = outMetrics.widthPixels > outMetrics.heightPixels ? outMetrics.heightPixels : outMetrics.widthPixels;
        btn_restart = (Button) view.findViewById(R.id.btn_restart);
        btn_restart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showCompleteDialog != null) {
                    showCompleteDialog.dismiss();
                }
                finish();
            }
        });

        punishBtn = (Button) view.findViewById(R.id.btn_punish);
        punishBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showCompleteDialog != null) {
                    showCompleteDialog.dismiss();
                }
                Intent goMain = new Intent();
                goMain.setClass(UnderCoverGuess.this, UnderPunishActivity.class);
                startActivity(goMain);
            }
        });
        showCompleteDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_forget_word) {
            tv_forget_word_tips.setVisibility(View.VISIBLE);
            isWordTipsState = true;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WORD_TIPS:
                    isWordTipsState = false;
                    tv_forget_word_show.setVisibility(View.GONE);
                    tv_forget_word_tips.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isWordTipsState) {
            return;
        }
        String word = content[position];
        tv_forget_word_show.setText(word);
        tv_forget_word_show.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(WORD_TIPS, 3000);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.print("onitemlongclick");
        if(gridItems == null){
            return false;
        }
        if(gridItems.get(position).isDead == true)
            return false;
        gridItems.get(position).isDead = true;
        gridAdapter.notifyDataSetChanged();
        updateCount(position);
        checkGameOver();
        return true;
    }

    private void updateCount(int position){
        if(gridItems == null || gridItems.size() == 0){
            return;
        }
        GridItem gridItem = gridItems.get(position);
        if(gridItem.role == ROLE_NORMAL){
            normalcount -- ;
        }
        else if(gridItem.role == ROLE_UNDER){
            underCount --;
        }
        else if(gridItem.role == ROLE_BLANK){
            blankCount--;
        }
    }

    @Override
    protected void onDestroy() {
        if(showCompleteDialog != null){
            showCompleteDialog.dismiss();
        }
        super.onDestroy();
    }
}
