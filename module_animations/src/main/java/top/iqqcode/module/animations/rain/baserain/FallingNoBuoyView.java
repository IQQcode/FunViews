package top.iqqcode.module.animations.rain.baserain;

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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.concurrent.ConcurrentLinkedQueue;

import top.iqqcode.lib.common.util.UtilHelper;
import top.iqqcode.module.animations.R;
import top.iqqcode.module.animations.rain.FallingData;

/**
 * 彩蛋雨基础组件(迁移自PB彩蛋雨)，包含
 * 1.自由下落彩蛋(不可点击图片)
 * 2.(可选呼吸态)下落并展开的浮标(可点击图片)
 */
public abstract class FallingNoBuoyView extends FrameLayout {

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
    private static final long INTERVAL_TIME = 10;

    private Binding binding;

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
    protected boolean mIsFalling = false;

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
     * 彩蛋icon的下落动画集合
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
     * 存储任务队列，如果当前正在下落彩蛋雨的话，要等这次动画完成后再触发下一次，这里先把触发任务存储起来
     */
    protected final ConcurrentLinkedQueue<FallingModel> taskQueue = new ConcurrentLinkedQueue<>();

    /**
     * 动画开始和结束时的回调
     */
    protected IViewAnimationListener animationListener;

    /**
     * 处理点击事件的clickListener
     */
    private OnClickListener viewClickListener;

    @NonNull
    private final FallingConfig config;

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
     * 处理浮标图片的属性动画runnable
     */
    private final Runnable mFallingAdDownRunnable = new Runnable() {

        @Override
        public void run() {
            // performFallingAnimation();
            exitAnimation();
        }
    };

    public FallingNoBuoyView(@NonNull Context context) {
        this(context, null);
    }

    public FallingNoBuoyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FallingNoBuoyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
        config = initConfig();
    }

    private void initView(Context context) {
        // 左侧飘落浮标展开态
        View root = LayoutInflater.from(context).inflate(R.layout.falling_click_view, null, false);
        binding = Binding.findViews(root);

        LayoutParams adViewLayout = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        binding.buoyView.setPadding(UtilHelper.INSTANCE.getDimens(context, R.dimen.tbds10),
                UtilHelper.INSTANCE.getDimens(context, R.dimen.tbds5),
                UtilHelper.INSTANCE.getDimens(context, R.dimen.tbds10),
                UtilHelper.INSTANCE.getDimens(context, R.dimen.tbds5));
        binding.buoyView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        addView(binding.clickLayout, adViewLayout);

        mPaint = new Paint();
        mPaint.setDither(true);
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
                stopAllViews();
            }
        }
    }

    private void exitAnimation() {
        if (exitAnimator != null && exitAnimator.isStarted()) {
            exitAnimator.cancel();
        }

        exitAnimator = ValueAnimator.ofInt(0);
        exitAnimator.setDuration(10000);
        exitAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 如果不可点击图片不再下落了，那就stop掉当前的彩蛋雨
                notifyStopAllViews();
            }
        });
        exitAnimator.start();
    }

    public void startFalling(@NonNull final FallingData data, final Context pageContext,
                             boolean force) {
        if (force) {
            stopAllViews();
            boolean res = start(data, pageContext);
            if (res) {
                if (animationListener != null) {
                    animationListener.onAnimationStart();
                }
            }
        } else {
            if (mIsFalling) {
                if (config.hasPlayPriority()) {
                    FallingModel fallingModel = new FallingModel(data, pageContext);
                    taskQueue.add(fallingModel);
                }
            } else {
                boolean res = start(data, pageContext);
                if (res) {
                    if (animationListener != null) {
                        animationListener.onAnimationStart();
                    }
                }
            }
        }
    }

    /**
     * 开始彩蛋雨
     *
     * @param data
     */
    protected boolean start(@NonNull final FallingData data, final Context pageContext) {
        if (!TextUtils.isEmpty(data.getContent())) {
            String resultText = data.getContent();
            if (resultText.length() > 10) {
                resultText = resultText.substring(0, 9) + "...";
            }
            binding.expandTextView.setText(resultText);
        } else {
            binding.expandTextView.setText(mContext.getString(R.string.check_immediately));
        }
        // 加载浮标图片(可点击)
        Glide.with(pageContext).load(data.getPicClick()).into(binding.buoyView);

        // 获取到下发的彩蛋图标之后，开始执行彩蛋动画
        final ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(data.getPic()))
                .setProgressiveRenderingEnabled(true).build();
        DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline()
                .fetchDecodedImage(imageRequest, mContext);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                startFallingRainAnimation(pageContext, data, bitmap);
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
     * @param rainFallObject 下落物体对象
     * @param num
     */
    private void addFallObject(final FallObject rainFallObject, final int num) {
        if (viewWidth == 0) {
            viewWidth = UtilHelper.INSTANCE.getEquipmentWidth(mContext);
        }

        if (viewHeight == 0) {
            viewHeight = UtilHelper.INSTANCE.getEquipmentHeight(mContext);
        }

        fallObjects.clear();
        for (int i = 0; i < num; i++) {
            FallObject newFallObject = new FallObject(rainFallObject.builder, viewWidth, viewHeight);
            newFallObject.presentY = -i * 180;
            fallObjects.add(newFallObject);
        }
    }

    /**
     * 外部调用的停止，会立即停止彩蛋雨，内部的话全部动画走完才算停止
     */
    public void stopAllViews() {
        mIsFalling = false;
        expandAnimationStart = false;
        fallObjects.clear();
        removeCallbacks(drawRunnable);
        removeCallbacks(mFallingAdDownRunnable);
        binding.clickLayout.setVisibility(GONE);
        binding.expandLayout.setVisibility(GONE);
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
        FallingModel model = taskQueue.poll();
        if (model != null) {
            boolean res = start(model.fallingData, model.pageContext);
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
     *
     * @return
     */
    public boolean isFalling() {
        return mIsFalling;
    }

    /**
     * 开始彩蛋雨
     *
     * @param fallingData
     * @param bitmap
     */
    private void startFallingRainAnimation(final Context pageContext, final FallingData fallingData, Bitmap bitmap) {
        final FallObject rainFallObject = initFallObject(bitmap);
        addFallObject(rainFallObject, config.getRainEggsNum());
        postDelayed(mFallingAdDownRunnable, 1500);
        mIsFalling = true;
        invalidate();

        viewClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageContext == null || fallingData == null) {
                    return;
                }
                onBuoyClick(v, fallingData);
            }
        };
    }

    /**
     * 初始化彩蛋配置
     *
     * @param bitmap
     * @return
     */
    public abstract FallObject initFallObject(Bitmap bitmap);

    /**
     * 初始化动画配置
     *
     * @return
     */
    public abstract FallingConfig initConfig();

    /**
     * 浮标点击事件
     * @param view
     * @param fallingData
     */
    public abstract void onBuoyClick(@NonNull View view, @NonNull FallingData fallingData);

    /**
     * 重写事件分发逻辑，主要是为了解决动画期间点击困难的问题
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (binding.clickLayout.getVisibility() != VISIBLE) {
            return super.dispatchTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN && containPoint(binding.clickLayout, ev.getX(), ev.getY())) {
            if (viewClickListener != null) {
                viewClickListener.onClick(binding.clickLayout);
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
     * 无限制计算view的宽度
     *
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

        /**
         * 彩蛋雨动画开始
         */
        void onAnimationStart();

        /**
         * 彩蛋雨动画结束
         */
        void onAnimationEnd();
    }

    /**
     * 彩蛋雨触发时候必须的参数，为了保存数据，一次触发完成以后再次触发下一次
     */
    protected static class FallingModel {
        final FallingData fallingData;

        final Context pageContext;

        public FallingModel(FallingData data, Context pageContext) {
            this.fallingData = data;
            this.pageContext = pageContext;
        }
    }

    /**
     * 动画取消
     */
    private final Runnable delayTimeCancelAnim = new Runnable() {
        @Override
        public void run() {
            stopAllViews();
        }
    };

    /**
     * 贝塞尔曲线，计算可点击浮标动画路径
     */
    private static class BezierEvaluator implements TypeEvaluator<PointF> {
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
         * @param endValue   终点
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

    private static class Binding {

        View root;

        /**
         * 可点击区域的layout，包含buoyView跟expandLayout
         */
        private View clickLayout;

        /**
         * 悬停浮标View，可展开的layout，包含图片跟文字textview
         */
        View expandLayout;

        /**
         * 可展开的右侧背景
         */
        View expandBgView;

        /**
         * 可展开的右侧文字部分
         */
        private TextView expandTextView;

        /**
         * 可点击的图片imageView，即浮标view
         */
        private ImageView buoyView;

        static Binding findViews(@NonNull View root) {
            Binding binding = new Binding();
            binding.root = root;
            binding.clickLayout = root;
            binding.expandLayout = root.findViewById(R.id.expand_layout);
            binding.expandBgView = root.findViewById(R.id.expand_background);
            binding.expandTextView = root.findViewById(R.id.expand_text);
            binding.buoyView = root.findViewById(R.id.buoy_image);
            return binding;
        }
    }
}