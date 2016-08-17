package partygames.shirley.com.undercoverlib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class GridAdapter extends BaseAdapter {

    private List<GridItem> gridItems;
    private Context context;
    public GridAdapter(Context context,List<GridItem> gridItems){
        this.context = context;
        this.gridItems = gridItems;
    }

    @Override
    public int getCount() {
        if(gridItems != null){
           return gridItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(gridItems != null){
            return gridItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
       return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(context,R.layout.grid_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.grid_item_image);
            viewHolder.top_right_imageView = (ImageView)convertView.findViewById(R.id.grid_item_topright_info);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder)convertView.getTag();
        GridItem item = gridItems.get(position);
        viewHolder.imageView.setBackgroundResource(R.drawable.ic_default_head);
        int resId = context.getResources().getIdentifier("spy_superscript_" + (position + 1), "drawable", context.getPackageName());
        if(item.isDead){
            if(item.role == UnderCoverGuess.ROLE_NORMAL){
                viewHolder.top_right_imageView.setImageResource(R.drawable.civilian_label);
            }
            else if(item.role == UnderCoverGuess.ROLE_BLANK){
                viewHolder.top_right_imageView.setImageResource(R.drawable.blank_label);
            }
            else if(item.role == UnderCoverGuess.ROLE_UNDER) {
                viewHolder.top_right_imageView.setImageResource(R.drawable.spy_label);
            }
        }
        else{
            viewHolder.top_right_imageView.setImageResource(resId);
        }
        return convertView;
    }

    public class ViewHolder{
        public ImageView imageView;
        public ImageView top_right_imageView;
    }
}
