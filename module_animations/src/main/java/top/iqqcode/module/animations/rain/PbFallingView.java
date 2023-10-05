package top.iqqcode.module.animations.rain;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import top.iqqcode.lib.common.util.UtilHelper;
import top.iqqcode.module.animations.R;
import top.iqqcode.module.animations.rain.baserain.FallObject;

/**
 * PB商业广告彩蛋雨
 * Created by wangyan on 8/5/21
 * 重构红包雨控件，包含普通icon跟红包image，处理逻辑都统一放在这里
 *
 * v12.37 彩蛋雨组件封装  (剥离商业广告逻辑，可自定义动画参数)
 * 【备注】: 因涉及到商业广告和商业打点，PB彩蛋雨暂不做任何调整，仅重名重来区分组件
 */
public class PbFallingView extends FrameLayout {

    /**
     * 0 看内容触发
     */
    public static final int FIRST_CONTENT_TYPE = 0;

    /**
     * 1看评论触发
     */
    public static final int REPLAY_CONTENT_TYPE = 1;

    /**
     * 2发评论触发
     */
    public static final int SEND_REPLAY_TYPE = 2;

    /**
     * 默认宽度
     */
    private static final int DEFAULT_WIDTH = 600;

    /**
     * 默认高度
     */
    private static final int DEFAULT_HEIGHT = 1000;

    /**
     * 重绘间隔时间
     */
    private static final long INTERVAL_TIME = 5;

    /**
     * 可点击的图片imageView，即红包view
     */
    private ImageView clickImageView;

    /**
     * 可点击区域的layout，包含clickImageView跟expandLayout
     */
    private View clickLayout;

    /**
     * 可展开的layout，包含图片跟文字textview
     */
    private View expandLayout;

    /**
     * 可展开的右侧背景
     */
    private View expandImageView;

    /**
     * 可展开的右侧文字部分
     */
    private TextView expandTextView;

    /**
     * 不可点击图片的数据list
     */
    private final List<FallObject> fallObjects = new ArrayList<>();

    private final Context mContext;
    private int viewWidth;
    private int viewHeight;

    /**
     * 动画是否开启，所有icon都掉落完成
     */
    private boolean mIsFalling = false;

    /**
     * 不可点击图片是否还在下落
     */
    private boolean othersFalling = false;

    /**
     * 展开动画是否开始，包含开始执行和执行完成状态
     */
    private boolean expandAnimationStart = false;

    private Paint mPaint;

    /**
     * 红包icon的下落动画集合
     */
    private AnimatorSet animSet;

    /**
     * 左侧悬停的动画集合
     */
    private AnimatorSet expandAnimSet;

    /**
     * 动画消失，主要为了加延时
     */
    private ValueAnimator exitAnimator;

    /**
     * 存储任务队列，pm要求如果当前正在下落红包雨的话，要等这次动画完成后再触发下一次，这里先把触发任务存储起来
     */
    private final ConcurrentLinkedQueue<PbFallingModel> taskQueue = new ConcurrentLinkedQueue<>();

    /**
     * 动画开始和结束时的回调
     */
    private IViewAnimationListener animationListener;

    /**
     * 广告标签View(包含热区)
     */
    private View mAdLabelContainerView;
    /**
     * 广告标签View（内容区）
     */
    private View mAdLabelLayout;
    /**
     * 负反馈控件
     */
    private ImageView mFallingAdFeedbackImg;
    /**
     * 广告标内容控件
     */
    private TextView mFallingAdContent;
    /**
     * 负反馈点击回调
     */
    private FallingFeedbackListener mFallingFeedbackListener;

    /**
     * 更新view的runnable，回调到dispatchDraw
     */
    private final Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    /**
     * 处理红包图片的属性动画runnable
     */
    private final Runnable mFallingAdDownRunable = new Runnable() {

        @Override
        public void run() {
            performFallingAnimation(objType);
        }
    };

    /**
     * 处理点击事件的clickListener
     */
    private OnClickListener viewClickListener;

    /**
     * 触发方式：0 看内容触发(场景b) 1看评论触发(场景c) 2发评论/回复触发(场景a)
     */
    private int objType = -1;

    /**
     * objLocate，1: 漂浮彩蛋雨，2: 彩蛋雨左侧浮标
     */
    private int objLocate = -1;

    public PbFallingView(@NonNull Context context) {
        this(context, null, 0);
    }

    public PbFallingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PbFallingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        clickLayout = LayoutInflater.from(context).inflate(R.layout.falling_click_view, null, false);
        expandLayout = clickLayout.findViewById(R.id.expand_layout);
        expandImageView = clickLayout.findViewById(R.id.expand_background);
        expandTextView = clickLayout.findViewById(R.id.expand_text);
        clickImageView = clickLayout.findViewById(R.id.buoy_image);


        LayoutParams adViewLayout = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        clickImageView.setPadding(UtilHelper.INSTANCE.getDimens(context, R.dimen.tbds10),
                UtilHelper.INSTANCE.getDimens(context, R.dimen.tbds5),
                UtilHelper.INSTANCE.getDimens(context, R.dimen.tbds10),
                UtilHelper.INSTANCE.getDimens(context, R.dimen.tbds5));
        addView(clickLayout, adViewLayout);

        // 广告标
        mAdLabelContainerView = LayoutInflater.from(context).inflate(R.layout.falling_ad_label, null, false);
        mAdLabelLayout = mAdLabelContainerView.findViewById(R.id.ad_label_layout);
        mFallingAdFeedbackImg = mAdLabelContainerView.findViewById(R.id.falling_ad_feedback_img);
        mFallingAdContent = mAdLabelContainerView.findViewById(R.id.falling_ad_content);
        setAdLabelOnClickListener();
        LayoutParams adLabelLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        adLabelLayoutParams.gravity = Gravity.LEFT;
        mAdLabelContainerView.setY(
                UtilHelper.INSTANCE.getEquipmentHeight(mContext) - UtilHelper.INSTANCE.getDimens(mContext, R.dimen.tbds720));
        addView(mAdLabelContainerView, adLabelLayoutParams);

        mPaint = new Paint();
        mPaint.setDither(true);
        clickImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(DEFAULT_HEIGHT, heightMeasureSpec);
        int width = measureSize(DEFAULT_WIDTH, widthMeasureSpec);
        setMeasuredDimension(width, height);
        viewWidth = width;
        viewHeight = height;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        long start = System.currentTimeMillis();
        super.dispatchDraw(canvas);
        if (fallObjects.size() > 0) {
            othersFalling = false;
            for (int i = 0; i < fallObjects.size(); i++) {
                // 然后进行绘制
                othersFalling = fallObjects.get(i).drawObject(canvas, mPaint) || othersFalling;
            }

            // 如果所有都不再绘制就不需要invaludate了，减少资源消耗
            if (othersFalling) {
                // 隔一段时间重绘一次
                postDelayed(drawRunnable, INTERVAL_TIME + start - System.currentTimeMillis());
            } else {
                // 如果延时3s消失的动画还在执行，那就先不停止，在3s动画后再stop
                if (exitAnimator != null && !exitAnimator.isStarted()
                        && expandAnimationStart) {
                    exitAnimator.start();
                }
            }
        }
    }

    public void startFalling(final FallingData segmentData, final Context pageContext, boolean force) {
        if (force) {
            stopAllViews();
            boolean res = start(segmentData, pageContext);
            if (res) {
                if (animationListener != null) {
                    animationListener.onAnimationStart();
                }
            }
        } else {
            if (mIsFalling) {
                PbFallingModel fallingModel = new PbFallingModel(segmentData, pageContext);
                taskQueue.add(fallingModel);
            } else {
                boolean res = start(segmentData, pageContext);
                if (res) {
                    if (animationListener != null) {
                        animationListener.onAnimationStart();
                    }
                }
            }
        }
    }

    /**
     * 开始红包雨
     *
     */
    private boolean start(final FallingData segmentData, final Context pageContext) {
        if (!TextUtils.isEmpty((segmentData.getContent()))) {
            String resultText = segmentData.getContent();
            if (resultText.length() > 10) {
                resultText = resultText.substring(0, 9) + "...";
            }
            expandTextView.setText(resultText);
        } else {
            expandTextView.setText(mContext.getString(R.string.check_immediately));
        }

        // 加载可点击图片
        Glide.with(getContext()).load(segmentData.getPicClick()).into(clickImageView);
        // 获取到下发的彩蛋图标之后，开始执行彩蛋动画
        final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(segmentData.getPic()))
                .setProgressiveRenderingEnabled(true).build();
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, mContext);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                startFallingRedpacketAnimation(pageContext, segmentData, bitmap, objType);
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
            }
        }, CallerThreadExecutor.getInstance());

        return true;
    }

    public void setAnimationListener(IViewAnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    /**
     * 向View添加下落物体对象
     *
     * @param fallObject 下落物体对象
     * @param num
     */
    private void addFallObject(final FallObject fallObject, final int num) {
        if (viewWidth == 0) {
            viewWidth = UtilHelper.INSTANCE.getEquipmentWidth(mContext);
        }

        if (viewHeight == 0) {
            viewHeight = UtilHelper.INSTANCE.getEquipmentHeight(mContext);
        }

        fallObjects.clear();
        for (int i = 0; i < num; i++) {
            FallObject newFallObject = new FallObject(fallObject.builder, viewWidth, viewHeight);
            newFallObject.presentY = -i * 180;
            fallObjects.add(newFallObject);
        }
    }

    /**
     * 外部调用的停止，会立即停止红包雨，内部的话全部动画走完才算停止
     */
    public void stopAllViews() {
        mIsFalling = false;
        expandAnimationStart = false;
        fallObjects.clear();
        removeCallbacks(drawRunnable);
        removeCallbacks(mFallingAdDownRunable);
        clickLayout.setVisibility(GONE);
        expandLayout.setVisibility(GONE);
        mAdLabelContainerView.setVisibility(GONE);
        if (animSet != null) {
            animSet.cancel();
        }

        if (expandAnimSet != null) {
            expandAnimSet.cancel();
        }

        if (exitAnimator != null) {
            exitAnimator.cancel();
        }
    }

    private void notifyStopAllViews() {
        stopAllViews();
        PbFallingModel model = taskQueue.poll();
        if (model != null) {
            boolean res = start(model.segmentData, model.pageContext);
            mAdLabelContainerView.setVisibility(res ? VISIBLE : GONE);
            if (res) {
            } else {
                mAdLabelContainerView.setVisibility(GONE);
            }
            if (!res && animationListener != null) {
                animationListener.onAnimationEnd();
            }
        } else {
            if (animationListener != null) {
                animationListener.onAnimationEnd();
            }
        }
    }

    /**
     * 是否在展示中
     * @return
     */
    public boolean isFalling() {
        return mIsFalling;
    }

    /**
     * 开始红包雨
     *
     * @param segmentData
     * @param bitmap
     * @param objType     0 看内容触发 1看评论触发 2发评论触发
     */
    private void startFallingRedpacketAnimation(Context pageContext, FallingData segmentData,
                                                Bitmap bitmap, int objType) {
        final Random random = new Random();
        // 初始化一个雪花样式的fallObject
        FallObject.Builder builder = new FallObject.Builder(bitmap);
        final FallObject fallObject = builder
                .setSpeed(UtilHelper.INSTANCE.getDimens(getContext(), R.dimen.tbds8), true)
                .setSize(UtilHelper.INSTANCE.getDimens(getContext(), R.dimen.tbds200),
                        UtilHelper.INSTANCE.getDimens(getContext(), R.dimen.tbds200), true)
                .setMinSize(UtilHelper.INSTANCE.getDimens(getContext(), R.dimen.tbds100),
                        UtilHelper.INSTANCE.getDimens(getContext(), R.dimen.tbds100))
                .setRandomFactorCallBack(new FallObject.RandomFactorCallBack() {
                    @Override
                    public float getRandomFactor() {
                        return random.nextInt(2) * 0.1f + 0.8f;
                    }
                })
                .setWind(true, true)
                .build();

        this.objType = objType;
        addFallObject(fallObject, 19);
        postDelayed(mFallingAdDownRunable, 1500);
        mIsFalling = true;
        invalidate();


        viewClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    /**
     * 重写事件分发逻辑，主要是为了解决动画期间点击困难的问题
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (clickLayout.getVisibility() != VISIBLE) {
            return super.dispatchTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN && containPoint(clickLayout, ev.getX(), ev.getY())) {
            if (viewClickListener != null) {
                viewClickListener.onClick(clickLayout);
            }

            // 下面的view都不再处理点击事件，直接截断
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断左边是否属于当前view
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean containPoint(View view, float x, float y) {
        float viewX = view.getX();
        float viewY = view.getY();
        return !(x < viewX) && !(x > (viewX + view.getWidth())) && !(y < viewY) && !(y > (viewY + view.getHeight()));
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }

        return result;
    }

    /**
     * 处理红包图片view的属性动画，向下并且带有呼吸效果
     * @param objType 0 看内容触发(场景b) 1看评论触发(场景c) 2发评论/回复触发(场景a)
     */
    private void performFallingAnimation(int objType) {
        if (clickLayout.getVisibility() != View.VISIBLE) {
            clickLayout.setVisibility(View.VISIBLE);
        }

        // 红包icon缩放效果
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1.0f, 1.2f);
        scaleAnimator.setDuration(600);
        scaleAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimator.setInterpolator(new AccelerateInterpolator());
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                clickImageView.setPivotX((float) (clickImageView.getWidth() / 2));
                clickImageView.setPivotY((float) (clickImageView.getHeight() / 2));
                clickImageView.setScaleX(value);
                clickImageView.setScaleY(value);
            }
        });

        int screenHeight = UtilHelper.INSTANCE.getEquipmentHeight(mContext);
        int screenWidth = UtilHelper.INSTANCE.getEquipmentWidth(mContext);

        // 红包iconX轴位移效果
        int halfWidth = clickImageView.getWidth() / 2;

        PointF point0 = new PointF((float) screenWidth * 3 / 4 - halfWidth, (float) -screenHeight / 4);
        PointF point1 = new PointF((float) UtilHelper.INSTANCE.getDimens(mContext, R.dimen.tbds50),
                (float) UtilHelper.INSTANCE.getDimens(mContext, R.dimen.tbds400));
        PointF point2 = new PointF((float) screenWidth / 2 - UtilHelper.INSTANCE.getDimens(mContext, R.dimen.tbds50),
                (float) screenHeight - UtilHelper.INSTANCE.getDimens(mContext, R.dimen.tbds655));

        PointF point3 = new PointF(UtilHelper.INSTANCE.getDimens(mContext, R.dimen.tbds0),
                (float) screenHeight - UtilHelper.INSTANCE.getDimens(mContext, R.dimen.tbds552));

        BezierEvaluator bezierEvaluator = new BezierEvaluator(point1, point2);
        ValueAnimator translationAnimator = ValueAnimator.ofObject(bezierEvaluator, point0, point3);
        translationAnimator.setDuration(5000);
        translationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                clickLayout.setX(pointF.x);
                clickLayout.setY(pointF.y);
            }
        });

        ValueAnimator expandAnimator = ValueAnimator.ofInt(0, getWidthView(expandLayout));
        expandAnimator.setInterpolator(new LinearInterpolator());
        expandAnimator.setDuration(500);
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (expandLayout.getVisibility() != VISIBLE) {
                    expandLayout.setVisibility(VISIBLE);
                }

                int width = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = expandLayout.getLayoutParams();
                layoutParams.width = width;
                expandLayout.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator scaleAnimatorLast = ValueAnimator.ofFloat(1.0f, 0.8f);
        scaleAnimatorLast.setDuration(600);
        scaleAnimatorLast.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorLast.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                clickImageView.setPivotX((float) (clickImageView.getWidth() / 2));
                clickImageView.setPivotY((float) (clickImageView.getHeight() / 2));
                clickImageView.setScaleX(value);
                clickImageView.setScaleY(value);
            }
        });
        scaleAnimatorLast.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                clickImageView.setScaleX(1.0f);
                clickImageView.setScaleY(1.0f);
            }
        });

        if (exitAnimator != null && exitAnimator.isStarted()) {
            exitAnimator.cancel();
        }

        exitAnimator = ValueAnimator.ofInt(0);
        exitAnimator.setDuration(3000);
        exitAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 如果不可点击图片不再下落了，那就stop掉当前的红包雨
                notifyStopAllViews();
            }
        });

        if (animSet != null && animSet.isStarted()) {
            animSet.cancel();
        }

        if (expandAnimSet != null && expandAnimSet.isStarted()) {
            expandAnimSet.cancel();
        }

        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                expandAnimationStart = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (exitAnimator.isStarted() || othersFalling) {
                    return;
                }

                exitAnimator.start();
            }
        });

        expandAnimSet = new AnimatorSet();
        expandAnimSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                objLocate = 2;

            }
        });
        expandAnimSet.playSequentially(scaleAnimatorLast, expandAnimator);

        // 等红包位移完成以后，要移除缩放效果，并开始展开动画
        translationAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scaleAnimator.cancel();
                expandAnimSet.start();
            }
        });

        animSet = new AnimatorSet();
        animSet.playTogether(translationAnimator, scaleAnimator);
        animSet.start();
    }

    /**
     * 无限制计算view的宽度
     * @param view 目标view
     * @return 返回view应该的长度
     */
    private int getWidthView(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredWidth();
    }

    public interface IViewAnimationListener {
        void onAnimationStart();

        void onAnimationEnd();
    }

    /**
     * 红包雨触发时候必须的参数，为了保存数据，一次触发完成以后再次触发下一次
     */
    static class PbFallingModel {
        final FallingData segmentData;

        final Context pageContext;

        public PbFallingModel(FallingData segmentData, Context pageContext) {
            this.segmentData = segmentData;
            this.pageContext = pageContext;
        }
    }

    /**
     * 贝塞尔曲线
     */
    static class BezierEvaluator implements TypeEvaluator<PointF> {
        /**
         * 这2个点是控制点
         */
        private final PointF point1;
        private final PointF point2;

        public BezierEvaluator(PointF point1, PointF point2) {
            this.point1 = point1;
            this.point2 = point2;
        }

        /**
         * @param t
         * @param startValue 初始点
         * @param endValue 终点
         * @return
         */
        @Override
        public PointF evaluate(float t, PointF startValue, PointF endValue) {
            // 利用三阶贝塞尔曲线公式算出中间点坐标
            int x = (int) (startValue.x * Math.pow((1 - t), 3) + 3 * point1.x * t * Math.pow((1 - t), 2) + 3 *
                    point2.x * Math.pow(t, 2) * (1 - t) + endValue.x * Math.pow(t, 3));
            int y = (int) (startValue.y * Math.pow((1 - t), 3) + 3 * point1.y * t * Math.pow((1 - t), 2) + 3 *
                    point2.y * Math.pow(t, 2) * (1 - t) + endValue.y * Math.pow(t, 3));
            return new PointF(x, y);
        }
    }

    /**
     * 设置负反馈控件的点击事件
     */
    private void setAdLabelOnClickListener() {
        if (mAdLabelContainerView == null) {
            return;
        }

        mAdLabelContainerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 停止红包雨
                PbFallingView.this.stopAllViews();

                if (mFallingFeedbackListener != null) {
                    mFallingFeedbackListener.onClick();
                }
            }
        });
    }

    /**
     * 设置负反馈点击时的回调
     * @param listener
     */
    public void setFallingFeedbackListener(FallingFeedbackListener listener) {
        this.mFallingFeedbackListener = listener;
    }

    /**
     * 负反馈点击事件listener接口
     */
    public interface FallingFeedbackListener {
        /**
         * 点击事件回调方法
         */
        void onClick();
    }
}
