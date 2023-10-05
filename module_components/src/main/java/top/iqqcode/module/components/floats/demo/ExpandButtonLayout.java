package top.iqqcode.module.components.floats.demo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import top.iqqcode.module.components.R;


/**
 * @Author: jiazihui
 * @Date: 2022-10-29 10:36
 * @Description: https://blog.csdn.net/viewstub_cn/article/details/77822822
 */
public class ExpandButtonLayout extends RelativeLayout implements Animation.AnimationListener {

    private RelativeLayout mRootView;
    private ImageView imageView;
    private MyLinearLayout mLinearLayout;

    public ExpandButtonLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_button_expand, this, true);
        mRootView = findViewById(R.id.mRootView);
        imageView = findViewById(R.id.imageView);
        mLinearLayout = mRootView.findViewById(R.id.mLinearLayout);

        // 在回调中来获取整个布局的高度,和需要缩放的宽度,还有缩放部分的内外间距
        ViewTreeObserver vto = mLinearLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                allHeight = getHeight();
                mLinearLayoutWidth = mLinearLayout.getWidth();
                Log.e("JIAZIHUI", "Width=" + mLinearLayoutWidth + ", allHeight=" + allHeight);
                savePaddingLeft = mLinearLayout.getPaddingLeft();
                savePaddingRight = mLinearLayout.getPaddingRight();
                ViewGroup.LayoutParams params = mLinearLayout.getLayoutParams();
                if (params instanceof MarginLayoutParams) {
                    MarginLayoutParams marginVglp = (MarginLayoutParams) params;
                    saveMarginLeft = marginVglp.leftMargin;
                    saveMarginRight = marginVglp.rightMargin;
                    Log.e("JIAZIHUI", "vglp saveMarginLeft=" + saveMarginLeft + " saveMarginRight=" + saveMarginRight);
                }
                mLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initBackGround();
            }
        });
    }

    private final int duration = 1000;
    private boolean isExpand = true;
    private boolean isAnimation = false;

    private int savePaddingLeft = 0;
    private int savePaddingRight = 0;
    private int saveMarginLeft = 0;
    private int saveMarginRight = 0;
    private int mLinearLayoutWidth = 0;
    private int allHeight = 0;

    /**
     * 设置圆角背景
     */
    private void initBackGround() {
        int[] colors = new int[]{
                Color.parseColor("#a5fecb"),
                Color.parseColor("#20bdff"),
                Color.parseColor("#5433ff")};
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        gd.setColors(colors);
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        // 圆角半径等于高度的一半,合成之后是一个完整的圆
        gd.setCornerRadius(allHeight / 2f);
        gd.getPadding(new Rect(0, 0, 0, 0));
        setBackground(gd);
    }

    private void initImageBackGround() {
//        Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.cool_background);
//        if (background != null) {
//            // 圆角半径等于高度的一半,合成之后是一个完整的圆
//            background.setCornerRadius(allHeight / 2f);
//            background.getPadding(new Rect(0, 0, 0, 0));
//            setBackground(background);
//        }
    }

    public void close() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(duration);
        animationSet.setAnimationListener(this);
        animationSet.setFillAfter(true);

        RotateAnimation rotateAnimation = new RotateAnimation(360, 270,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(rotateAnimation);
        Animation scaleAnimation = new ScaleAnimation(1f, 1.25f, 1f, 1.25f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(scaleAnimation);
        imageView.startAnimation(animationSet);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofInt(mLinearLayout, "width", mLinearLayoutWidth, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofInt(mLinearLayout, "paddingLeft", savePaddingLeft, 0);
        ObjectAnimator animator3 = ObjectAnimator.ofInt(mLinearLayout, "paddingRight", savePaddingRight, 0);
        ObjectAnimator animator4 = ObjectAnimator.ofInt(mLinearLayout, "marginLeft", saveMarginLeft, 0);
        ObjectAnimator animator5 = ObjectAnimator.ofInt(mLinearLayout, "marginRight", saveMarginRight, 0);
        animatorSet.playTogether(animator1, animator2, animator3, animator4, animator5);
        animatorSet.setDuration(duration).start();

    }


    public void open() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(duration);
        animationSet.setAnimationListener(this);
        animationSet.setFillAfter(true);

        RotateAnimation rotateAnimation = new RotateAnimation(270, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(rotateAnimation);
        Animation scaleAnimation = new ScaleAnimation(1.25f, 1f, 1.25f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animationSet.addAnimation(scaleAnimation);
        imageView.startAnimation(animationSet);


        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofInt(mLinearLayout, "width", 0, mLinearLayoutWidth);
        ObjectAnimator animator2 = ObjectAnimator.ofInt(mLinearLayout, "paddingLeft", 0, savePaddingLeft);
        ObjectAnimator animator3 = ObjectAnimator.ofInt(mLinearLayout, "paddingRight", 0, savePaddingRight);
        ObjectAnimator animator4 = ObjectAnimator.ofInt(mLinearLayout, "marginLeft", 0, saveMarginLeft);
        ObjectAnimator animator5 = ObjectAnimator.ofInt(mLinearLayout, "marginRight", 0, saveMarginRight);
        animatorSet.playTogether(animator1, animator2, animator3, animator4, animator5);
        animatorSet.setDuration(duration).start();
    }

    public void toggle() {
        if (!isAnimation) {
            if (isExpand) {
                close();
            } else {
                open();
            }
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        isAnimation = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        isAnimation = false;
        isExpand = !isExpand;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
