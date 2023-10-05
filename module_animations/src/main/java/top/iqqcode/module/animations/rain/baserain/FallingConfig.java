package top.iqqcode.module.animations.rain.baserain;

/**
 * Date: 2023-02-12
 * Description:彩蛋雨动画参数配置
 */
public class FallingConfig {

    /**
     * 默认彩蛋雨数目
     */
    public static final int DEFAULT_RAIN_EGGS_NUM = 19;

    /**
     * 默认浮标下落动画时长
     */
    public static final int DEFAULT_BUOY_DURATION = 7000;

    /**
     * 浮标渐隐动画时长
     */
    public static final int DEFAULT_BUOY_FADE_OUT = 600;

    /**
     * 浮标下落动画时长
     */
    private int buoyDuration = DEFAULT_BUOY_DURATION;

    /**
     * 彩蛋雨数目
     */
    private int rainEggsNum = DEFAULT_RAIN_EGGS_NUM;

    /**
     * 浮标是否添加呼吸态动画
     */
    private boolean isBuoyHasBreath = true;

    /**
     * 是否添加内部优先级队列
     */
    private boolean hasPlayPriority = true;

    /**
     * 是否展示浮标悬停View
     */
    private boolean isDisplayBuoyExpend = true;

    public static FallingConfig from() {
        return new FallingConfig();
    }

    public FallingConfig setBuoyDuration(int duration) {
        this.buoyDuration = duration;
        return this;
    }

    public FallingConfig setRainEggsNum(int num) {
        this.rainEggsNum = num;
        return this;
    }

    public FallingConfig setBuoyHasBreath(boolean hasBreath) {
        this.isBuoyHasBreath = hasBreath;
        return this;
    }

    public FallingConfig setHasPlayPriority(boolean hasPriority) {
        this.hasPlayPriority = hasPriority;
        return this;
    }

    public FallingConfig setIsDisplayBuoyExpend(boolean isDisplay) {
        this.isDisplayBuoyExpend = isDisplay;
        return this;
    }

    public int getBuoyDuration() {
        return buoyDuration;
    }

    public boolean isBuoyHasBreath() {
        return isBuoyHasBreath;
    }

    public boolean hasPlayPriority() {
        return hasPlayPriority;
    }

    public int getRainEggsNum() {
        return rainEggsNum;
    }

    public boolean isDisplayBuoyExpend() {
        return isDisplayBuoyExpend;
    }
}
