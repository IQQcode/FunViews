package top.iqqcode.lib.common.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * @author jiazihui
 */
public class AnimationHelper {

    /**
     * View渐现动画效果
     */
    public static void startShowAlphaAnim(final View view, int duration, final Animation.AnimationListener listener) {
        if (null == view || duration < 0) {
            return;
        }
        view.clearAnimation();
        Animation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mShowAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation anim) {
                if (listener != null) {
                    listener.onAnimationStart(anim);
                }
            }

            @Override
            public void onAnimationRepeat(Animation anim) {
                if (listener != null) {
                    listener.onAnimationRepeat(anim);
                }
            }

            @Override
            public void onAnimationEnd(Animation anim) {
                if (listener != null) {
                    listener.onAnimationEnd(anim);
                }
                if (view != null) {
                    view.clearAnimation();
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        view.setVisibility(View.VISIBLE);
        view.startAnimation(mShowAnimation);
    }


    /**
     * View渐现动画效果
     *
     * @param view
     * @param duration
     * @param listener
     */
    public static void startHideAlphaAnim(final View view, int duration, final Animation.AnimationListener listener) {
        if (null == view || duration < 0) {
            return;
        }

        view.clearAnimation();
        // 监听动画结束的操作
        Animation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation anim) {
                if (listener != null) {
                    listener.onAnimationStart(anim);
                }
            }

            @Override
            public void onAnimationRepeat(Animation anim) {
                if (listener != null) {
                    listener.onAnimationRepeat(anim);
                }
            }

            @Override
            public void onAnimationEnd(Animation anim) {
                if (listener != null) {
                    listener.onAnimationEnd(anim);
                }
                if (view != null) {
                    view.clearAnimation();
                    view.setVisibility(View.GONE);
                }
            }
        });
        view.setVisibility(View.GONE);
        view.startAnimation(mHideAnimation);
    }


    /**
     * View渐隐动画效果
     */
    public static void startHideAlphaAnim(final View view, int duration) {
        startHideAlphaAnim(view, duration, null);
    }


    /**
     * View渐现动画效果
     */
    public static void startShowAlphaAnim(final View view, int duration) {
        startShowAlphaAnim(view, duration, null);
    }

    /**
     * View淡入淡出切换效果
     *
     * @param hideView
     * @param hideDuration
     * @param showView
     * @param showDuration
     */
    public static void crossFadeView(View hideView, long hideDuration, View showView, long showDuration) {
        if (null == hideView || showView == null || hideDuration < 0 || showDuration < 0) {
            return;
        }
        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);
        showView.animate()
                .alpha(1f)
                .setDuration(showDuration)
                .setListener(null);

        hideView.animate()
                .alpha(0f)
                .setDuration(hideDuration)
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
