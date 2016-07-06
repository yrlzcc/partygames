package partygames.shirley.com.playandguesslib;

/**
 * Created by Administrator on 2016/6/29.
 */

import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 类名: FragmentSwitchAdapter 功能: viewpage的适配器(fragment) 日期: 2013-7-13 上午11:48:16
 *
 * @author Turbo
 */
public class FragmentSwitchAdapter extends PagerAdapter {
    /**
     * 作用: FRAGMENT数量
     */

    private LinearLayout ll_mask;

    private int NUMS = 2;
    public ForeFragment backFragment;
    public ForeFragment foreFragment;
    private boolean isInfiniteLoop;
    private String[]  data;

    public FragmentSwitchAdapter(String[] data) {
        this.data = data;
    }

//    @Override
//    public Fragment getItem(int position) {
//        System.out.println("jjjjjjjjjjjjjjjjjjjjj" + position);
//        position = position%2;
//        switch (position) {
//            case 0:
//                if(backFragment == null)
//                backFragment = ForeFragment.newInstance(String.valueOf(position), "aa");
//                return backFragment;
//            case 1:
//                foreFragment = ForeFragment.newInstance(String.valueOf(position), "bb");
//                return foreFragment;
//            default:
//                break;
//        }
//        return null;
//    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : NUMS;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public FragmentSwitchAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(fragments.get(position).getView()); // 移出viewpager两边之外的page布局
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        //对ViewPager页号求模取出View列表中要显示的项
//        position %= viewlist.size();
//        if (position<0){
//            position = viewlist.size()+position;
//        }
//        ImageView view = viewlist.get(position);
//        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
//        ViewParent vp =view.getParent();
//        if (vp!=null){
//            ViewGroup parent = (ViewGroup)vp;
//            parent.removeView(view);
//        }
//        container.addView(view);
//        //add listeners here if necessary
//        return view;
//    }


}
