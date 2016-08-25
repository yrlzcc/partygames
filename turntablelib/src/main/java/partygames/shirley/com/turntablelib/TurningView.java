package partygames.shirley.com.turntablelib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2016/8/17.
 */
public class TurningView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    // surface
    private SurfaceHolder mHolder;

    // 与surface绑定在一起的Canvas
    private Canvas mCanvas;

    // 用于绘制的线程
    private Thread mThread;

    // 线程的控制开关
    private boolean isRunning;

    // 描述抽奖的文字
    private String[] mName = new String[] { "单反相机", "IPAD", "手气不好", "IPHONE",
            "张杰一枚", "手气不好" };

    // 每块的颜色
    private int deepColor = 0xFFFFC300;
    private int lightColor = 0xFFF17E01;
    private int[] mColors = new int[] { deepColor, lightColor, deepColor,
            lightColor, deepColor, lightColor, };

    // 与文字对应的图片
    private int[] mImgs = new int[] { R.drawable.shape_btn_back_press, R.drawable.shape_btn_back_press,
            R.drawable.shape_btn_back_press, R.drawable.shape_btn_back_press,R.drawable.shape_btn_back_press,
            R.drawable.shape_btn_back_press};

    // 与文字对应的图片的数组
    private Bitmap[] mImgsBitmap;

    // 盘块的个数
    private final int mItemCount = 6;

    // 绘制盘块的范围
    private RectF mRange = new RectF();

    // 圆的直径
    private int mRadius;

    // 绘制盘块的画笔
    private Paint mArcPaint;

    // 绘制文字的画笔
    private Paint mTextPaint;

    // 滚动的速度
    private double mSpeed;
    private volatile float mStartAngle = 0;

    // 递减的加速度
    private int aSpeed = 1;

    // 是否点击了停止
    private boolean isShouldEnd;

    // 控件的中心位置
    private int mCenter;

    // 控件的padding
    private int mPadding;

    // 背景图片
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),
            R.color.white);

    // 文字的大小
    private float mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    public TurningView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 获得holder,和与之关联的Canvans
        mHolder = getHolder();
        mHolder.addCallback(this);

        // 设置可获得焦点，以及常亮
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
    }

    //去调用三个参数的构造函数
    public TurningView(Context context, AttributeSet attrs) {
        this(context,null,0);

    }

    // 一个参数的构造函数去调用两个构造参数的构造函数
    public TurningView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获得宽高当中最小的
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int min = width < height ? width : height;

        // 获得圆的直径
        mRadius = min - getPaddingLeft() - getPaddingRight();

        // 获得padding值，一paddingleft为基准
        mPadding = getPaddingLeft();

        // 设置中心点
        mCenter = min / 2;

        setMeasuredDimension(min, min);
    }

    // 做一些初始化的工作
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {

        // 初始化绘制圆弧的画笔,并设置锯齿之类的
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        // 初始化绘制文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFffffff);
        mTextPaint.setTextSize(mTextSize);

        // 圆弧的绘制范围,绘制的范围刚好是一个正方形,这个我得做一个插图（1），不然理解不了
        mRange = new RectF(mPadding, mPadding, mRadius + mPadding, mRadius
                + mPadding);

        // 初始化图片
        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),
                    mImgs[i]);
        }

        // 开启线程
        isRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    // 主要是用来关闭线程的
    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // 通知线程关闭
        isRunning = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void run() {

        // 不断地进行绘图，这样就给你一个错觉，转盘在不停的转
        while (isRunning) {

            // 这一次开始绘图的时间
            long start = System.currentTimeMillis();

            // 真正的绘图操作
            draw();

            // 这一次绘图的结束时间
            long end = System.currentTimeMillis();

            // 如果你的手机太快，绘图分分钟的事情，那也得让他把那个50等完
            try {

                if (end - start < 50) {
                    Thread.sleep(50 - (end - start));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // 绘图
    private void draw() {

        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {

                // 首先绘制背景图
                drawBg();

                // 绘制每个弧形，以及每个弧形上的文字，以及每个弧形上的图片
                float tmpAngle = mStartAngle;
                float sweepAngle = (float) (360 / mItemCount);

                for (int i = 0; i < mItemCount; i++) {

                    // 这个就是传说中的，背景颜色
                    mArcPaint.setColor(mColors[i]);

                    // 这里真的画了一个扇形出来，干脆我在这里也弄一个插图算了（4）详细可以参见
                    // http://blog.sina.com.cn/s/blog_783ede0301012im3.html
                    // oval :指定圆弧的外轮廓矩形区域。
                    // startAngle: 圆弧起始角度，单位为度。
                    // sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。
                    // useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
                    // paint: 绘制圆弧的画板属性，如颜色，是否填充等。
                    mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true,
                            mArcPaint);

                    // 绘制文本
                    drawText(tmpAngle, sweepAngle, mName[i]);

                    // 绘制Icon
                    drawIcon(tmpAngle, i);

                    // 转换角度，不能再一个地方一直绘制，
                    tmpAngle += sweepAngle;

                }

                // 当mspeed不等于0时，相当于滚动
                mStartAngle += mSpeed;

                // 当点击停止时，设置mspeed慢慢递减，而不是一下就停了下来
                if (isShouldEnd) {
                    mSpeed -= aSpeed;
                }

                // mspeed小于0的时候就该停止了
                if (mSpeed < 0) {
                    mSpeed = 0;
                    isShouldEnd = false;
                }

                // 根据当前旋转的mStartAngle计算当前滚动的区域
//                callInExactArea(mStartAngle);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }

    }

    // 绘制背景图
    private void drawBg() {

        // 根据当前旋转的mStartAngle计算当前滚动到的区域 绘制背景，不重要，完全为了美观
        mCanvas.drawColor(0xFFFFFFFF);

        // 这里这个绘图一般又看不懂了，得有个插图才行，这个貌似比圆弧的绘制范围大了那么一圈
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2,
                mPadding / 2, getMeasuredWidth() - mPadding / 2,
                getMeasuredWidth() - mPadding / 2), null);
    }

    /**
     * 绘制文本
     *
     * @param startAngle
     * @param sweepAngle
     * @param mName2
     */
    private void drawText(float startAngle, float sweepAngle, String mName2) {

        // path
        Path path = new Path();

        // 将写字区域加上去
        path.addArc(mRange, startAngle, sweepAngle);

        // 文字的宽度
        float textWidth = mTextPaint.measureText(mName2);

        // 利用水平偏移和垂直偏移让文字居中,是不是理解不了 ，我也是，画个插图，（3）

        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);

        float vOffset = (float) (mRadius / 2 / 6);

        // 得把文字画上去了
        mCanvas.drawTextOnPath(mName2, path, hOffset, vOffset, mTextPaint);

    }

    /**
     * 绘制Icon
     *
     * @param startAngle
     * @param i
     */
    private void drawIcon(float startAngle, int i) {

        // 设置图片的宽度，为直径的1/8，当然可以随便改
        int imgWidth = mRadius / 8;

        // 换算成弧度
        float angle = (float) ((30 + startAngle) * (Math.PI / 180));

        // x,y ... 这个或许要一张图篇才能理解，（5）
        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        // 确定绘制图片的位置
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
                / 2, y + imgWidth / 2);

        // 绘制
        mCanvas.drawBitmap(mImgsBitmap[i], null, rect, null);
    }


    /**
     * 现在总算看穿了，一切电商的阴谋，都是骗人的，电商可以显示的设置你转盘的结果
     *
     * @param luckyIndex
     */
    public void luckyStart(int luckyIndex) {

        // 每一项的角度大小
        float angle = (float) (360 / mItemCount);

        // 中奖角度范围，因为指针是朝上的所以范围是在210-270，这里要一个插图才能明白啊（6）

        float from = 270 - (luckyIndex + 1) * angle;

        float to = from + angle;

        // 停下来是旋转的距离
        float targetFrom = 4 * 360 + from;

    /*
     *
     * 这里有点绕，等细细评味
     */

        float v1 = (float) (Math.sqrt(1 * 1 + 8 * targetFrom) - 1) / 2;

        float targetTo = 4 * 360 + to;

        float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;

        mSpeed = (float) (v1 + Math.random() * (v2 - v1));

        isShouldEnd = false;
    }

    public void luckyEnd() {
        mStartAngle = 0;
        isShouldEnd = true;
    }

    public boolean isStart() {
        return mSpeed != 0;
    }

    public boolean isShouldEnd() {
        return isShouldEnd;
    }
}
