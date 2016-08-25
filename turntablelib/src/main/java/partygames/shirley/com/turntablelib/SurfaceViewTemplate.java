package partygames.shirley.com.turntablelib;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2016/8/17.
 */
public class SurfaceViewTemplate extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    //定义一个SurfaceHolder 用于接受获取的SurfaceHolder
    private SurfaceHolder mHolder;

    //用于获取绑定mHOlder的Canvas
    private Canvas mCanvas;

    //用于不断绘图的线程
    private Thread mThread;

    //用于控制线程的开关
    private boolean isRunning;


    //这个时代三个参数的构造函数，一般自会在有使用自定义属性的时候才会调用这个构造函数

    /**
     * @param context  上下文
     * @param attrs    xml文件定义的属性
     * @param defStyle 自定义属性
     */
    public SurfaceViewTemplate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    //当时用xml文件定义这个自定义View的时候，就会调用这个带两个参数的构造函数
    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取SurfaceHolder
        mHolder = getHolder();

        //添加callback
        mHolder.addCallback(this);

        //设置一些属性，焦点，屏幕常亮
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    //当是在代码里面显示的定义的时候就会调用这个带一个参数的构造函数
    public SurfaceViewTemplate(Context context) {
        //在这里我们让他去调用带两个参数的构造函数，以便就算是在代码里面定义的也能完成一些初始化操作
        this(context, null);
    }

    //主要，也是最核心的工作都是在run方法里面执行的，如draw（）
    @Override
    public void run() {
        try {
            //这里通过死循环,不断的进行绘图，给你一种盘在不断旋转的错觉
            while (isRunning) {
//                draw();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    //在这里做一些初始化的工作，开启线程。。。
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {

        //实例化线程，并设置isRunning
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    //当SurfaceView执行destroy的时候关闭线程
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

        //关闭线程只需设置isRunning
        isRunning = false;
    }

    //这里制定以下控件的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}


