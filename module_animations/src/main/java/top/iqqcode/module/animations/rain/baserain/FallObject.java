package top.iqqcode.module.animations.rain.baserain;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.util.Random;

/**
 * 红包雨对象
 * 【备注】: 随机数生成逻辑，图片变换逻辑迁移自PB彩蛋雨
 * @author jiazihui
 */
public class FallObject {

    /** π/2 */
    private static final float HALF_PI = (float) Math.PI / 2;

    /** 默认下降速度 */
    private static final int defaultSpeed = 10;

    /** 默认风力等级 */
    private static final int defaultWindLevel = 0;

    /** 默认单位风速 */
    private static final int defaultWindSpeed = 10;

    /** 随机数公式传入支持 */
    private final RandomFactorCallBack callback;

    /** 初始下降速度 */
    public int initSpeed;

    /** 初始风力等级 */
    public int initWindLevel = defaultWindLevel;

    /** 当前位置X坐标 */
    public float presentX;

    /** 当前位置Y坐标 */
    public float presentY;

    /** 当前下降速度 */
    public float presentSpeed;

    public int initX;
    public int initY;
    public Builder builder;

    /** 父容器宽度 */
    private int parentWidth;

    /** 父容器高度 */
    private int parentHeight;

    /** 下落物体高度 */
    private float objectHeight;

    /** 物体下落角度 */
    private float angle;

    /** 物体初始下降速度比例是否随机 */
    private final boolean isSpeedRandom;

    /** 物体初始大小比例是否随机 */
    private final boolean isSizeRandom;

    /** 物体宽度 */
    private final int width;

    /** 物体最小宽度 */
    private final int minWidth;

    /** 物体最小高度 */
    private final int minHeight;

    /** 物体初始风向和风力大小比例是否随机 */
    private final boolean isWindRandom;

    /** 物体下落过程中风向和风力是否产生随机变化 */
    private final boolean isWindChange;

    private Random random;
    private Bitmap bitmap;

    public FallObject(Builder builder, int parentWidth, int parentHeight) {
        this.builder = builder;
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        random = new Random();
        initX = random.nextInt(parentWidth * 8 / 9);
        initY = random.nextInt(parentHeight) - parentHeight;
        presentX = initX;
        presentY = initY;
        isSpeedRandom = builder.isSpeedRandom;
        isSizeRandom = builder.isSizeRandom;
        callback = builder.callback;
        minWidth = builder.minWidth;
        minHeight = builder.minHeight;
        width = builder.width;
        isWindRandom = builder.isWindRandom;
        isWindChange = builder.isWindChange;
        initSpeed = builder.initSpeed;
        randomSpeed();
        randomSize();
        randomWind();
    }

    private FallObject(Builder builder) {
        this.builder = builder;
        bitmap = builder.bitmap;
        width = builder.width;
        minWidth = builder.minWidth;
        callback = builder.callback;
        minHeight = builder.minHeight;
        initSpeed = builder.initSpeed;
        isSizeRandom = builder.isSizeRandom;
        isWindRandom = builder.isWindRandom;
        isWindChange = builder.isWindChange;
        isSpeedRandom = builder.isSpeedRandom;
    }

    /**
     * 改变bitmap的大小
     *
     * @param bitmap 目标bitmap
     * @param newW   目标宽度
     * @param newH   目标高度
     * @return
     */
    public static Bitmap changeBitmapSize(Bitmap bitmap, int newW, int newH) {
        int oldW = bitmap.getWidth();
        int oldH = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newW) / oldW;
        float scaleHeight = ((float) newH) / oldH;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldW, oldH, matrix, true);
        return bitmap;
    }

    /**
     * 绘制物体对象(彩蛋下落位移动画)
     *
     * @param canvas
     * @param paint
     */
    public boolean drawObject(Canvas canvas, Paint paint) {
        moveObject();
        if (presentY > parentHeight || presentY < 0) {
            return false;
        }

        canvas.drawBitmap(bitmap, presentX, presentY, paint);
        return true;
    }

    /**
     * 移动物体对象
     */
    private void moveObject() {
        moveX();
        moveY();
    }

    /**
     * X轴上的移动逻辑
     */
    private void moveX() {
        presentX += defaultWindSpeed * Math.sin(angle);
        if (isWindChange) {
            angle += (float) (random.nextBoolean() ? -1 : 1) * Math.random() * 0.0025;
        }
        // 边界处理
        if (presentX > parentWidth - width) {
            presentX = parentWidth - width;
        }
    }

    /**
     * Y轴上的移动逻辑
     */
    private void moveY() {
        presentY += presentSpeed;
    }

    /**
     * 重置object位置
     */
    private void reset() {
        presentY = -objectHeight;
        randomSpeed();//记得重置时速度也一起重置，这样效果会好很多
        randomWind();//记得重置一下初始角度，不然雪花会越下越少（因为角度累加会让雪花越下越偏）
    }

    /**
     * 随机物体初始下落速度
     */
    private void randomSpeed() {
        if (isSpeedRandom) {
            presentSpeed = (float) ((random.nextInt(3) + 1) * 0.1 + 1) * initSpeed;
        } else {
            presentSpeed = initSpeed;
        }
    }

    /**
     * 随机物体初始大小比例
     */
    private void randomSize() {
        if (isSizeRandom) {
            float r;
            if (callback != null) {
                r = callback.getRandomFactor();
            } else {
                r = (random.nextInt(10) + 1) * 0.1f;
            }

            float rW = 0;
            if (minWidth > 0) {
                rW = Math.max(minWidth, r * builder.bitmap.getWidth());
            }
            float rH = 0;
            if (minHeight > 0) {
                rH = Math.max(r * builder.bitmap.getHeight(), minHeight);
            }
            bitmap = changeBitmapSize(builder.bitmap, (int) rW, (int) rH);
        } else {
            bitmap = builder.bitmap;
        }
        objectHeight = bitmap.getHeight();
    }

    /**
     * 随机风的风向和风力大小比例，即随机物体初始下落角度
     */
    private void randomWind() {
        if (isWindRandom) {
            angle = (float) ((random.nextBoolean() ? -1 : 1) * Math.random() * initWindLevel / 50);
        } else {
            angle = (float) initWindLevel / 50;
        }

        // 限制angle的最大最小值
        if (angle > HALF_PI) {
            angle = HALF_PI;
        } else if (angle < -HALF_PI) {
            angle = -HALF_PI;
        }
    }

    public interface RandomFactorCallBack {
        float getRandomFactor();
    }

    public static final class Builder {
        private int initSpeed;
        private Bitmap bitmap;

        private boolean isSpeedRandom;
        private boolean isSizeRandom;
        private boolean isWindRandom;
        private boolean isWindChange;


        private int width;

        private int minWidth;
        private int minHeight;

        private RandomFactorCallBack callback;

        public Builder(Bitmap bitmap) {
            this.initSpeed = defaultSpeed;
            this.bitmap = bitmap;

            this.isSpeedRandom = false;
            this.isSizeRandom = false;
            this.isWindRandom = false;
            this.isWindChange = false;
        }

        /**
         * 设置物体的初始下落速度
         *
         * @param speed
         * @param isRandomSpeed 物体初始下降速度比例是否随机
         * @return
         */
        public Builder setSpeed(int speed, boolean isRandomSpeed) {
            this.initSpeed = speed;
            this.isSpeedRandom = isRandomSpeed;
            return this;
        }

        /**
         * 设置彩蛋(不可点击图片)大小
         *
         * @param width
         * @param height
         * @param isRandomSize 物体初始大小比例是否随机
         * @return
         */
        public Builder setSize(int width, int height, boolean isRandomSize) {
            this.width = width;
            this.bitmap = changeBitmapSize(this.bitmap, width, height);
            this.isSizeRandom = isRandomSize;
            return this;
        }

        /**
         * 设置彩蛋(不可点击图片) 最小尺寸
         * @param minWidth
         * @param minHeight
         * @return
         */
        public Builder setMinSize(int minWidth, int minHeight) {
            this.minWidth = minWidth;
            this.minHeight = minHeight;
            return this;
        }

        public Builder setRandomFactorCallBack(RandomFactorCallBack callback) {
            this.callback = callback;
            return this;
        }

        /**
         * 设置风力等级、方向以及随机因素
         *
         * @param isWindRandom 物体初始风向和风力大小比例是否随机
         * @param isWindChange 在物体下落过程中风的风向和风力是否会产生随机变化
         * @return
         */
        public Builder setWind(boolean isWindRandom, boolean isWindChange) {
            this.isWindRandom = isWindRandom;
            this.isWindChange = isWindChange;
            return this;
        }

        public FallObject build() {
            return new FallObject(this);
        }
    }
}
