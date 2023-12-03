package top.iqqcode.module.layout.mark.screen;

import android.graphics.Bitmap;

/**
 * Desc :
 */
public class ScreenshotBean {
    int code;
    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    boolean isSuccess;

    @Override
    public String toString() {
        return "ScreenshotBean{" +
                "code=" + code +
                ", bitmap=" + bitmap +
                ", isSuccess=" + isSuccess +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ScreenshotBean(int code, Bitmap bitmap, boolean isSuccess) {
        this.code = code;
        this.bitmap = bitmap;
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;

    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
