package partygames.shirley.com.partygames;

import android.os.Bundle;
import android.widget.TextView;

import partygames.shirley.com.baselib.BaseActivity;

public class GameDetailActivity extends BaseActivity {

    private TextView gamedetail_tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        gamedetail_tv_content = (TextView)findViewById(R.id.gamedetail_tv_content);
        String strcontent = getIntent().getStringExtra("content");
        gamedetail_tv_content.setText(strcontent);
    }
}
