package partygames.shirley.com.playandguesslib;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/6/30.
 */
public class MyPagerAdapter extends PagerAdapter {

    public List<View> listViews = null;
    public Context mContext;
    public int mIndex;//滑动索引
    public View view;
    private String strTime;
    private List<String> words = null;

    public MyPagerAdapter(Context mContext,List<String> data) {
//        this.listViews = listViews;
        this.mContext = mContext;
        words = data;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       Log.v("TipsPagerAdapter", "destroyItem is called   " + position);
        ((ViewPager) container).removeViewAt(0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
       Log.v("TipsPagerAdapter", "instantiateItem is called   " + position);
        view = LayoutInflater.from(mContext).inflate(R.layout.fragment_fore, null);
//        AdUtils.openBanner((Activity)mContext,view);
        TextView tv = (TextView)view.findViewById(R.id.play_fragment_tv_word);
        position = position%words.size();
        tv.setText(words.get(position));
        try {
            ((ViewPager) container).addView(view);
        } catch (Exception e) {

        }
        return view;
    }

    /**
     * 进行View预加载处理
     */
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        Log.v("TipsPagerAdapter", "setPrimaryItem is called   " + position);
        mCurrentView = (View)object;
        super.setPrimaryItem(container, position, object);
        if(mCurrentView == null){
            return;
        }
        TextView tvTime = (TextView)mCurrentView.findViewById(R.id.play_fragment_tv_time);
        tvTime.setText(strTime);
    }

    private View mCurrentView;

    public View getPrimaryItem() {
        return mCurrentView;
    }

    public String getCurrentContent(){
        Log.v("TipsPagerAdapter", "getCurrentContent is called   " + mCurrentView);
        if(mCurrentView != null){
            TextView tv = (TextView)mCurrentView.findViewById(R.id.play_fragment_tv_word);
            return tv.getText().toString();
        }
        if(words != null && words.size() > 0){
            return words.get(0);
        }
        return "";
    }


    public void updateContent(String text){
        if(mCurrentView == null){
            return;
        }
        TextView tv = (TextView)mCurrentView.findViewById(R.id.play_fragment_tv_word);
        tv.setText(text);
        if(text.equals("跳过")) {
            mCurrentView.setBackgroundResource(R.color.color_play_back_skip);
        }
        else if(text.equals("正确")){
            mCurrentView.setBackgroundResource(R.color.color_play_back_right);
        }
        else if(text.equals("时间到")){
            mCurrentView.setBackgroundResource(R.color.color_play_back_timeup);
        }
        else{
            mCurrentView.setBackgroundResource(R.color.color_play_back_normal);
        }
    }

    public void updateTime(String text){
        if(mCurrentView == null){
            return;
        }
        strTime = text;
        TextView tv = (TextView)mCurrentView.findViewById(R.id.play_fragment_tv_time);
        tv.setText(text);
    }
}
