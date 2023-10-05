package top.iqqcode.module.components.expend.entrance;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import top.iqqcode.lib.common.util.UtilHelper;
import top.iqqcode.module.components.R;
import top.iqqcode.module.components.util.MockDataUtil;

/**
 * @author jiazihui
 * Frs群聊入口
 */
public class FrsFloatEntranceActivity extends AppCompatActivity implements Animation.AnimationListener, View.OnClickListener {

    private CardViewLayout mChatGroupEntranceFloat;
    private AdapterViewFlipper mChatGroupFlipper;

    private RelativeLayout mCollapseFloat;

    private ViewFlipperAdapter mViewFlipperAdapter;
    private ImageView mImageViewLogo;

    private Button mChatGroupTestButton;
    private Button mExpendButton;
    private Button mCollapseButton;

    private final int duration = 1000;
    private boolean isExpand = true;
    private boolean isAnimation = false;

    private int savePaddingLeft = 0;
    private int savePaddingRight = 0;
    private int saveMarginLeft = 0;
    private int saveMarginRight = 0;
    private int mLinearLayoutWidth = 0;
    private int mLinearLayoutHeight = 0;
    private int allHeight = 0;

    private int collapseSize = 0;

    private static final int SHOW_NEXT_SPACE = 5500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frs_float_entrance);

        initView();

        initFlipper();
    }

    private void initView() {
        mChatGroupFlipper = findViewById(R.id.frs_chat_group_flipper);
        mChatGroupEntranceFloat = findViewById(R.id.chat_group_entrance_float);
        mImageViewLogo = findViewById(R.id.imageViewLogo);
        mCollapseFloat = findViewById(R.id.chat_entrance_float_collapse);
        mChatGroupTestButton = findViewById(R.id.chat_group_button);
        mCollapseButton = findViewById(R.id.collapse_button);
        mExpendButton = findViewById(R.id.expend_button);
        mChatGroupTestButton.setOnClickListener(this);
        mExpendButton.setOnClickListener(this);
        mCollapseButton.setOnClickListener(this);

        collapseSize = UtilHelper.INSTANCE.dip2px(this, 50.0f);
        initLayoutParams();
    }

    private void initLayoutParams() {
        // 在回调中来获取整个布局的高度,和需要缩放的宽度,还有缩放部分的内外间距
        ViewTreeObserver vto = mChatGroupEntranceFloat.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                allHeight = mChatGroupEntranceFloat.getHeight();
                mLinearLayoutWidth = mChatGroupEntranceFloat.getWidth();
                mLinearLayoutHeight = mChatGroupEntranceFloat.getHeight();
                Log.e("JIAZIHUI", "Width=" + mLinearLayoutWidth + ", allHeight=" + allHeight);
                savePaddingLeft = mChatGroupEntranceFloat.getPaddingLeft();
                savePaddingRight = mChatGroupEntranceFloat.getPaddingRight();
                ViewGroup.LayoutParams params = mChatGroupEntranceFloat.getLayoutParams();
                if (params instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginVglp = (ViewGroup.MarginLayoutParams) params;
                    saveMarginLeft = marginVglp.leftMargin;
                    saveMarginRight = marginVglp.rightMargin;
                    Log.e("JIAZIHUI", "vglp saveMarginLeft=" + saveMarginLeft + " saveMarginRight=" + saveMarginRight);
                }
                mChatGroupEntranceFloat.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // initBackGround();
            }
        });
    }

    // =================================== 轮播控件 =================================

    private void initFlipper() {
        // mHandler.postDelayed(mRunnable, 5000);

        mViewFlipperAdapter = new ViewFlipperAdapter(this);
        // 设置View之间切换的时间间隔
        mChatGroupFlipper.setFlipInterval(SHOW_NEXT_SPACE);
        // 切换会循环进行
        mChatGroupFlipper.startFlipping();
        mChatGroupFlipper.setAnimateFirstView(false);

        PropertyValuesHolder pvhInY = PropertyValuesHolder.ofFloat("y", 200f, 0f);
        PropertyValuesHolder pvhInAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        ObjectAnimator animatorEnter = ObjectAnimator.ofPropertyValuesHolder(mChatGroupFlipper, pvhInY, pvhInAlpha).setDuration(500);
        mChatGroupFlipper.setInAnimation(animatorEnter);

        PropertyValuesHolder pvhOutY = PropertyValuesHolder.ofFloat("y", 0f, -200f);
        PropertyValuesHolder pvhOutAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator animatorOut = ObjectAnimator.ofPropertyValuesHolder(mChatGroupFlipper, pvhOutY, pvhOutAlpha).setDuration(500);
        mChatGroupFlipper.setOutAnimation(animatorOut);
    }


    // =================================== 动画  ==============================

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
        mImageViewLogo.startAnimation(animationSet);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "width", mLinearLayoutWidth, collapseSize);
        ObjectAnimator animator2 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "height", mLinearLayoutHeight, collapseSize);
        ObjectAnimator animator3 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "paddingLeft", savePaddingLeft, 0);
        ObjectAnimator animator4 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "paddingRight", savePaddingRight, 0);
        ObjectAnimator animator5 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "marginLeft", saveMarginLeft, 0);
        // ObjectAnimator animator6 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "marginRight", saveMarginRight, 0);
        animatorSet.playTogether(animator1, animator2, animator3, animator4, animator5);
        animatorSet.setDuration(duration).start();
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                crossFadeView(mChatGroupEntranceFloat, mCollapseFloat);
            }
        });
    }


    public void open() {
        crossFadeView(mCollapseFloat, mChatGroupEntranceFloat);

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
        mImageViewLogo.startAnimation(animationSet);


        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "width", collapseSize, mLinearLayoutWidth);
        ObjectAnimator animator2 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "height", collapseSize, mLinearLayoutHeight);
        ObjectAnimator animator3 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "paddingLeft", 0, savePaddingLeft);
        ObjectAnimator animator4 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "paddingRight", 0, savePaddingRight);
        ObjectAnimator animator5 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "marginLeft", 0, saveMarginLeft);
        // ObjectAnimator animator6 = ObjectAnimator.ofInt(mChatGroupEntranceFloat, "marginRight", 0, saveMarginRight);
        animatorSet.playTogether(animator1, animator2, animator3, animator4, animator5);
        animatorSet.setDuration(duration).start();
        animatorSet.addListener(new AnimatorListenerAdapter() {

            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }
        });
    }

    private void crossFadeView(View hideView, View showView) {
        if (showView != null) {
            showView.setAlpha(0f);
            showView.setVisibility(View.VISIBLE);
            showView.animate()
                    .alpha(1f)
                    .setDuration(800)
                    .setListener(null);
        }

        if (hideView != null) {
            hideView.animate()
                    .alpha(0f)
                    .setDuration(800)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            hideView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            hideView.setVisibility(View.GONE);
                        }
                    });
        }
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.chat_group_button) {
            toggle();
        } else if (id == R.id.collapse_button) {
            if (!isAnimation) {
                if (isExpand) {
                    close();
                }
            }
        } else if (id == R.id.expend_button) {
            if (!isAnimation) {
                if (!isExpand) {
                    open();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewFlipperAdapter.setData(MockDataUtil.getMockData());
        mChatGroupFlipper.setAdapter(mViewFlipperAdapter);
    }
}