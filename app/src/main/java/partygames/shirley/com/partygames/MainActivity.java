package partygames.shirley.com.partygames;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import partygames.shirley.com.baselib.BaseActivity;
import partygames.shirley.com.partygames.view.CircleMenuLayout;
import partygames.shirley.com.playandguesslib.GMenuActivity;

public class MainActivity extends BaseActivity {

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[] { "你比我猜", "谁是卧底", "真心话大冒险",
            "婚礼游戏", "杀人游戏", "摇骰子","游戏惩罚"};
    private int[] mItemImgs = new int[] { R.drawable.home_mbank_1_normal,
            R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
            R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal,
            R.drawable.home_mbank_6_normal,R.drawable.home_mbank_6_normal  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);



        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener()
        {

            @Override
            public void itemClick(View view, int pos)
            {
                Toast.makeText(MainActivity.this, mItemTexts[pos],
                        Toast.LENGTH_SHORT).show();
                if(pos == 0){
                    Intent intent = new Intent(MainActivity.this,GMenuActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void itemCenterClick(View view)
            {
                Toast.makeText(MainActivity.this,
                        "you can do something just like ccb  ",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}
