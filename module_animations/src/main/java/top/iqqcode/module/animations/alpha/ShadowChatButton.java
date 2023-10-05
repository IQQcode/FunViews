package top.iqqcode.module.animations.alpha;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import top.iqqcode.module.animations.R;


/**
 * @author jiazihui
 */
public class ShadowChatButton extends RelativeLayout {
    private float mWidth;
    private float mHeight;
    private final Paint mPaint = new Paint();
    private float cx;
    private float cy;
    private float radius;

    private final int color = R.color.CAM_X0207;
    private final int shadowColor = R.color.CAM_X0804;

    private final PorterDuffXfermode clearXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private final PorterDuffXfermode defaultXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);

    public ShadowChatButton(Context context) {
        this(context, null);
    }

    public ShadowChatButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowChatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        cx = mWidth / 2;
        cy = mHeight / 2;
        radius = mWidth / 2 - 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWidth <= 0 || mHeight <= 0) {
            return;
        }
        canvas.save();
        clear(canvas);
        canvas.rotate(45, mWidth / 2, mHeight / 2);

        // mPaint.setShadowLayer(16, 7, 7, shadowColor);
        canvas.drawCircle(cx, cy, radius, mPaint);
        canvas.restore();
    }

    private void clear(Canvas canvas) {
        mPaint.setXfermode(clearXfermode);
        canvas.drawPaint(mPaint);
        mPaint.setXfermode(defaultXfermode);
    }
}


