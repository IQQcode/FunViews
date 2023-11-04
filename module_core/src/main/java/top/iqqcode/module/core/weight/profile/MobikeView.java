package top.iqqcode.module.core.weight.profile;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: iqqcode
 * @Date: 2023-10-06 23:45
 * @Description: TODO
 */
public class MobikeView extends FrameLayout {

    private final Mobike mMobike;

    public MobikeView(@NonNull Context context) {
        this(context, null);
    }

    public MobikeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mMobike = new Mobike(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMobike.onSizeChanged(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mMobike.onLayout(changed);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        mMobike.onDraw(canvas);
    }

    public Mobike getmMobike() {
        return this.mMobike;
    }
}
