package top.iqqcode.module.animations.rain;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

import top.iqqcode.lib.common.util.UtilHelper;
import top.iqqcode.module.animations.R;
import top.iqqcode.module.animations.rain.baserain.FallObject;
import top.iqqcode.module.animations.rain.baserain.FallingConfig;
import top.iqqcode.module.animations.rain.baserain.FallingView;

/**
 * @Author: jiazihui
 * @Date: 2023-02-20 11:18
 * @Description:
 */
public class RainFallingView extends FallingView {

    public RainFallingView(@NonNull Context context) {
        super(context);
    }

    public RainFallingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RainFallingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    public FallObject initFallObject(@NonNull Bitmap bitmap) {
        final Random random = new Random();
        // 初始化一个雪花样式的fallObject
        FallObject.Builder builder = new FallObject.Builder(bitmap);
        FallObject fallObject = builder
                .setWind(true, true)
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
                .build();
        return fallObject;
    }

    @NonNull
    @Override
    public FallingConfig initConfig() {
        return FallingConfig.from()
                .setBuoyDuration(FallingConfig.DEFAULT_BUOY_DURATION)
                .setRainEggsNum(FallingConfig.DEFAULT_RAIN_EGGS_NUM)
                .setIsDisplayBuoyExpend(false)
                .setBuoyHasBreath(false)
                .setHasPlayPriority(false);
    }

    @Override
    public void onBuoyClick(@NonNull View view, @NonNull FallingData fallingData) {

    }
}
