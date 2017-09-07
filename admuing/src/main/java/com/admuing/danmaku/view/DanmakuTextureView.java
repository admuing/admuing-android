package com.admuing.danmaku.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.admuing.danmaku.bean.DanmakuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.admuing.danmaku.common.util.DrawHelper.clearCanvas;

/**
 * Created by duan .
 */
public class DanmakuTextureView extends SurfaceView implements SurfaceHolder.Callback {
    private final static String TAG = "DanmakuTextureView";
    private final static long speed = 5;
    private static long totalSpeed = 0;
    private int[] fontColors = new int[]{Color.RED, Color.WHITE, Color.BLUE, Color.YELLOW};
    private DanmakuCallback callback;
    //1 h 2 v
    private int type = 1;
    private final static float fontSize = 64f;
    //最多行数
    private final static int lineCount = 10;
    //每次绘制的偏移量
    private final static int size = 5;
    private final static int maxsize = 15;
    //每个弹幕之间的间距
    private final static int margin = 200;
    private int textHeigth;
    //弹幕内容
    private LinkedList<String> contents;

    private SurfaceHolder mSurfaceHolder;
    private boolean loop = true;
    private Paint paint;
    private boolean pause = false;

    //fps
    private LinkedList<Long> mDrawTimes = new LinkedList<>();
    private static final int MAX_RECORD_SIZE = 50;
    private static final int ONE_SECOND = 1000;

    public DanmakuTextureView(Context context) {
        super(context);
        init();
    }

    public DanmakuTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmakuTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setFontColors(int[] fontColors) {
        this.fontColors = fontColors;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCallback(DanmakuCallback callback) {
        this.callback = callback;
    }

    private void init() {
        setWillNotCacheDrawing(true);
        setDrawingCacheEnabled(false);
        setZOrderMediaOverlay(true);
        setZOrderOnTop(true);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(fontSize);
    }

    public void setData(LinkedList<String> contents) {
        if (contents == null || contents.size() == 0) {
            return;
        }
        this.contents = contents;

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        textHeigth = 10 + getFontHeight(fontSize);
        loop = true;
        new Thread(new drawRunable()).start();
    }

    private float fps() {
        long lastTime = SystemClock.uptimeMillis();
        mDrawTimes.addLast(lastTime);
        Long first = mDrawTimes.peekFirst();
        if (first == null) {
            return 0.0f;
        }
        float dtime = lastTime - first;
        int frames = mDrawTimes.size();
        if (frames > MAX_RECORD_SIZE) {
            mDrawTimes.removeFirst();
        }
        return dtime > 0 ? mDrawTimes.size() * ONE_SECOND / dtime : 0.0f;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            loop = false;
            Log.d(TAG, "surfaceDestroyed  ");
        }
    }

    private class drawRunable implements Runnable {
        //记录上一次绘制的信息 key= line,value = 本行最后一个弹幕的结束绘制时间
        private HashMap<Integer, Long> hashMap = new HashMap<>();
        //需要绘制的弹幕信息
        private List<DanmakuItem> list = new ArrayList<>();
        private final LinkedList<DanmakuItem> cacheList = new LinkedList<>();

        drawRunable() {
            //初始化首次要绘制的内容
            for (int a = 0; a < lineCount; a++) {
                if (a >= list.size()) {
                    setDanmakuItem(a);
                }
            }
        }

        private void setDanmakuItem(int line) {
            String subStrings = nextNews();
            if (TextUtils.isEmpty(subStrings)) {
                // 当没有更多弹幕的时候停止显示
                Log.d(TAG, "empty subStrings");
                if (callback != null) {
                    callback.noMore(getContext());
                }
                return;
            }
            //颜色
            int index = (int) (Math.random() * fontColors.length);
            int color = fontColors[index];
            DanmakuItem danmakuItem;
            synchronized (cacheList) {
                if (!cacheList.isEmpty()) {
                    danmakuItem = cacheList.pop();
                } else {
                    danmakuItem = new DanmakuItem();
                }
            }
            //记录绘制弹幕内容
            danmakuItem.setContent(subStrings);
            switch (type) {
                case 2: {
                    //偏移量
                    //记录本行弹幕绘制结束长度
                    float conlen = textHeigth;
                    hashMap.put(line, (long) (totalSpeed + conlen * (lineCount)));
                    danmakuItem.setX(0);
                    danmakuItem.setY(getHeight() + line * textHeigth);
                    danmakuItem.setSpeed((int) DanmakuTextureView.speed);
                    danmakuItem.setConlen(getHeight());
                    danmakuItem.setColor(Color.WHITE);
                }
                break;
                case 1:
                default: {
                    //偏移量
                    int speed = size + subStrings.length();
                    if (speed > maxsize) {
                        speed = maxsize;
                    }
                    //记录本行弹幕绘制结束长度
                    float conlen = paint.measureText(subStrings);
                    if (conlen < getWidth()) {
                        conlen = getWidth();
                    }
                    hashMap.put(line, totalSpeed + (long) (conlen + margin) / (long) speed * DanmakuTextureView.speed);
                    danmakuItem.setX(getWidth());
                    danmakuItem.setY(textHeigth + line * textHeigth);
                    danmakuItem.setSpeed(speed);
                    danmakuItem.setConlen(conlen);
                    danmakuItem.setColor(color);
                }
                break;
            }
            list.add(danmakuItem);
        }

        @Override
        public void run() {
            while (loop) {
                synchronized (this) {
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException ex) {
                        Log.e("TextSurfaceView", ex.getMessage() + "\n" + ex);
                    }
                    if (pause) {
                        continue;
                    }
                    draw();
                }
                totalSpeed = totalSpeed + speed;
            }
        }

        /**
         * 画图
         */
        private void draw() {
            if (mSurfaceHolder == null) {
                return;
            }
            //锁定画布
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas == null) {
                return;
            }
            //清屏
            clearCanvas(canvas);
            //fps
//            String fps = String.format(Locale.getDefault(),
//                    "fps %.2f", fps());
//            DrawHelper.drawFPS(canvas, fps);
            //画文字
            drawTxt(canvas);
            //判断是否需要添加新的弹幕
            checkDanmaku();
            //解锁显示
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }

        private void drawTxt(Canvas canvas) {
            if (list.size() == 0) {
                return;
            }
            Iterator<DanmakuItem> it = list.iterator();
            while (it.hasNext()) {
                DanmakuItem danmakuItem = it.next();
                switch (type) {
                    case 2:
                        if (danmakuItem.getY() < getHeight() / 2) {
                            cacheList.add(danmakuItem);
                            it.remove();
                        } else {
                            danmakuItem.setY(danmakuItem.getY() - danmakuItem.getSpeed());
                            paint.setColor(danmakuItem.getColor());
                            canvas.drawText(danmakuItem.getContent(), danmakuItem.getX(), danmakuItem.getY(), paint);
                        }
                        break;
                    case 1:
                    default:
                        if (danmakuItem.getX() < -danmakuItem.getConlen()) {
                            cacheList.add(danmakuItem);
                            it.remove();
                        } else {
                            danmakuItem.setX(danmakuItem.getX() - danmakuItem.getSpeed());
                            paint.setColor(danmakuItem.getColor());
                            canvas.drawText(danmakuItem.getContent(), danmakuItem.getX(), danmakuItem.getY(), paint);
                        }
                }

            }
        }

        private void checkDanmaku() {
            if (hashMap == null || hashMap.isEmpty()) {
                return;
            }
            for (int a = 0; a < lineCount; a++) {
                Long time = hashMap.get(a);
                if (time == null) {
                    return;
                }
                //最后一行弹幕是否已经走远了
                boolean canAdd = totalSpeed >= time;
                if (canAdd && contents != null && contents.size() > 0) {
                    setDanmakuItem(a);
                }
            }
        }

    }

    private String nextNews() {
        synchronized (this) {
            if (contents == null || contents.size() == 0) {
                return "";
            }
            return contents.pop();
        }
    }

    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

}
