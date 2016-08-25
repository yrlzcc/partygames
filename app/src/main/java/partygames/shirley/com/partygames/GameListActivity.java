package partygames.shirley.com.partygames;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import partygames.shirley.com.baselib.BaseActivity;

public class GameListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    ListView gamelist_list = null;
    private String[] games;
    private String[] titles;
    private String[] contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        gamelist_list= (ListView)findViewById(R.id.gamelist_list);
        games = getResources().getStringArray(R.array.games);
        splitContent();
        if(titles == null || titles.length <= 0){
            return;
        }
        gamelist_list.setAdapter(new ArrayAdapter<String>(this,R.layout.game_list_item,R.id.tv_game_list_item_title,titles));   //这里使用的android自带的android.R.layout.simple_list_item_1));
        gamelist_list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(contents == null || contents.length <= 0){
            return;
        }
        String strdetails = contents[position];
        Intent intent = new Intent(GameListActivity.this,GameDetailActivity.class);
        intent.putExtra("content",strdetails);
        startActivity(intent);
    }


    private void splitContent(){
        if(games == null){
            return;
        }
        int length = games.length;
        titles = new String[length];
        contents = new String[length];
        for(int i = 0;i < length;i++){
            String str = games[i];
            String[] arr = str.split("_");
            if(arr==null){
                continue;
            }
            titles[i] = arr[0];
            contents[i] = arr[1];
        }
    }
}
