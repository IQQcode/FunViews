package top.iqqcode.module.components.expend.hotspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;
import top.iqqcode.lib.common.util.UtilHelper;
import top.iqqcode.module.components.R;

/**
 * 测试Drawable图片缩放效果
 *
 * @author jiazihui
 * https://blog.csdn.net/qq_35928566/article/details/102744519
 */
public class ExpendBaseDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mShowAnniButton;
    private Button mHideAnniButton;
    private CardView mCardViewBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expend_base_demo);
        initView();
    }

    private void initView() {
        mCardViewBubble = findViewById(R.id.bubble_container_card);
        mShowAnniButton = findViewById(R.id.show_anni_button);
        mHideAnniButton = findViewById(R.id.hide_anni_button);
        mShowAnniButton.setOnClickListener(this);
        mHideAnniButton.setOnClickListener(this);
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(arg0 -> {
            int value = (int) arg0.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.width = value;
            v.setLayoutParams(layoutParams);

        });
        return animator;
    }

    private void show(View view, int tvWidth, long delay) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = createDropAnimator(view, 0, tvWidth);
        valueAnimator.setDuration(500);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> disMiss(view, tvWidth));
                    }
                };
                timer.schedule(timerTask, delay);
            }
        });
        valueAnimator.start();
    }

    private void disMiss(View view, int tvWidth) {
        ValueAnimator valueAnimator = createDropAnimator(view, tvWidth, 0);
        valueAnimator.setDuration(500);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        valueAnimator.start();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.show_anni_button) {
            int width = UtilHelper.INSTANCE.dip2px(this, 345);
            show(mCardViewBubble, width, 2000);
        }
    }
}