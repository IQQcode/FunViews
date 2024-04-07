package top.iqqcode.module.components.flipper;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;

import top.iqqcode.lib.common.router.ComponentsRouter;
import top.iqqcode.module.components.R;
import top.iqqcode.module.components.util.MockDataUtil;


/**
 * @author jiazihui
 */
@Route(path = ComponentsRouter.CAROUSEL_DEMO_ACTIVITY)
public class CarouselDemoActivity extends AppCompatActivity {

    private AdapterViewFlipper mViewFlipper;
    private ViewFlipperDemoAdapter mViewFlipperAdapter;

    private Button mTestFlipperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel_demo);

        mViewFlipper = findViewById(R.id.view_flipper_adapter_demo);
        mTestFlipperButton = findViewById(R.id.test_view_flipper);
//        List<String> data = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            data.add("这是第" + i + "条消息");
//        }
        mViewFlipperAdapter = new ViewFlipperDemoAdapter(this);
        mViewFlipperAdapter.setData(MockDataUtil.getMockData());
        mViewFlipper.setAdapter(mViewFlipperAdapter);

        // 也可通过代码设置动画
//        @SuppressLint("Recycle") ObjectAnimator animatorEnter = ObjectAnimator.ofFloat(
//                mViewFlipper, View.TRANSLATION_Y, 200.0f, 0.0f);
//        animatorEnter.setDuration(500);
//        animatorEnter.setInterpolator(new AccelerateDecelerateInterpolator());
//        animatorEnter.start();
//        mViewFlipper.setInAnimation(animatorEnter);

//        @SuppressLint("Recycle") ObjectAnimator animatorOut = ObjectAnimator.ofFloat(
//                mViewFlipper, View.TRANSLATION_Y, 0.0f, -200.0f);
//        animatorOut.setDuration(500);
//        animatorOut.setInterpolator(new AccelerateDecelerateInterpolator());
//        animatorOut.start();
//        mViewFlipper.setOutAnimation(animatorOut);
        // avf_view.setInAnimation(ObjectAnimator.ofFloat(avf_view, View.TRANSLATION_Y,150.0f,0.0f));
        // avf_view.setOutAnimation(ObjectAnimator.ofFloat(avf_view, View.TRANSLATION_Y,0.0f,-150.0f));


        PropertyValuesHolder pvhInY = PropertyValuesHolder.ofFloat("y", 200f, 0f);
        PropertyValuesHolder pvhInAlpha = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        ObjectAnimator animatorEnter = ObjectAnimator.ofPropertyValuesHolder(mViewFlipper, pvhInY, pvhInAlpha).setDuration(500);
        mViewFlipper.setInAnimation(animatorEnter);

        PropertyValuesHolder pvhOutY = PropertyValuesHolder.ofFloat("y", 0f, -200f);
        PropertyValuesHolder pvhOutAlpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator animatorOut = ObjectAnimator.ofPropertyValuesHolder(mViewFlipper, pvhOutY, pvhOutAlpha).setDuration(500);
        mViewFlipper.setOutAnimation(animatorOut);

        mTestFlipperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewFlipper.showNext();
            }
        });
    }
}