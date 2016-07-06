package partygames.shirley.com.playandguesslib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import partygames.shirley.com.baselib.BaseActivity;
import partygames.shirley.com.playandguesslib.model.WordState;

public class CompleteActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private int rightnum;
    private TextView complete_right_tips;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        context = this;
        findViewById(R.id.complete_left_iv_again).setOnClickListener(this);
        findViewById(R.id.complete_left_iv_home).setOnClickListener(this);
        listView = (ListView)findViewById(R.id.complete_lv_list);
        WordAdapter adapter = new WordAdapter(this,GBaseData.getInstance().getWordStateList());
        listView.setAdapter(adapter);
        rightnum = getRightnum();
        complete_right_tips = (TextView)findViewById(R.id.complete_right_tips);
        complete_right_tips.setText(context.getString(R.string.play_right_tips,rightnum));
    }

    /**
     * 获取正确数目
     * @return
     */
    private int getRightnum(){
        int num = 0;
        List<WordState> list = GBaseData.getInstance().getWordStateList();
        if (list == null|| list.size() == 0){
            return 0;
        }
        for(WordState word:list){
            if(word.isright()){
                num++;
            }
        }
        return num;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.complete_left_iv_home){
            Intent intent = new Intent(CompleteActivity.this,GMenuActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.complete_left_iv_again){
            Intent intent = new Intent(CompleteActivity.this,PlayActivity.class);
            startActivity(intent);
        }
    }

    public class WordAdapter extends BaseAdapter{

        private Context context;
        private List<WordState> wordStateList = null;

        public WordAdapter(Context context,List<WordState> list){
            this.context = context;
            wordStateList = new ArrayList<WordState>();
            if(list != null){
                wordStateList.addAll(list);
            }
        }

        @Override
        public int getCount() {
            if(wordStateList == null) {
                return 0;
            }
            return wordStateList.size();
        }

        @Override
        public Object getItem(int position) {
            if(wordStateList == null) {
                return null;
            }
            return wordStateList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = View.inflate(context,R.layout.adapter_word_item,null);
                viewHolder = new ViewHolder();
                viewHolder.tvContent = (TextView)convertView.findViewById(R.id.adapter_item_tv_word);
                viewHolder.ivImage = (ImageView)convertView.findViewById(R.id.adapter_item_iv_state);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.tvContent.setText(wordStateList.get(position).getWord());
            boolean isright = wordStateList.get(position).isright();
            if(isright){
                viewHolder.ivImage.setImageResource(R.mipmap.right);
            }
            else{
                viewHolder.ivImage.setImageResource(R.mipmap.wrong);
            }
            return convertView;
        }
    }

    public class ViewHolder{
        public TextView tvContent;
        public ImageView ivImage;
    }
}
