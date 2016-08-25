package partygames.shirley.com.turntablelib;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

import partygames.shirley.com.baselib.BaseActivity;

public class TurnActivity extends BaseActivity {

    private TurningView id_luckypadview;
    private ImageView id_imageview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);
        id_luckypadview=(TurningView)findViewById(R.id.id_luckypadview);
        id_imageview=(ImageView)findViewById(R.id.id_imageview);
        setListener();
    }


    private void setListener() {
        id_imageview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!id_luckypadview.isStart()){
                    id_imageview.setImageResource(R.color.btn_dialog_back_end_color);
                    Random random=new Random();

                    id_luckypadview.luckyStart(random.nextInt()%6);
                }else {
                    if (!id_luckypadview.isShouldEnd()){
                        id_imageview.setImageResource(R.color.btn_number_normal);
                        id_luckypadview.luckyEnd();
                    }
                }

            }
        });
    }
}
