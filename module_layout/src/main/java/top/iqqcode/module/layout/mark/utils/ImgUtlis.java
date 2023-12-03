package top.iqqcode.module.layout.mark.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Created by Distant Place
 * Data : 2018/11/19 17:29
 * E-Mail: 605322850@qq.com
 * Desc :
 */
public class ImgUtlis {

    /**
     * 图片保存
     *
     * @param bmp
     * @return
     */
    public static void saveImageToGallery(Bitmap bmp, Context mContext, SaveImgCallBack callBack) {
        if (bmp == null) {
            if (callBack != null) {
                callBack.erro();
            }
            return;
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(mContext, "SD卡不可用~", Toast.LENGTH_SHORT).show();
            return;
        }

        // 在Android 10及更高版本中使用公共媒体目录
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + "Camera");

        Uri imageUri = null;
        OutputStream fos = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (imageUri != null) {
                    fos = mContext.getContentResolver().openOutputStream(Objects.requireNonNull(imageUri));
                }
            } else {
                File galleryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + File.separator + "Camera");
                File file = new File(galleryPath, System.currentTimeMillis() + ".jpg");
                fos = new FileOutputStream(file);
                imageUri = Uri.fromFile(file);
            }

            if (fos != null) {
                // 通过IO流的方式来压缩保存图片
                boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();

                if (isSuccess) {
                    // 保存图片后通知媒体库更新
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        mContext.getContentResolver().update(imageUri, values, null, null);
                    } else {
                        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
                    }
                    if (callBack != null) {
                        callBack.success(imageUri.toString());
                    }
                } else {
                    if (callBack != null) {
                        callBack.erro();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.erro();
            }
        }
    }

    /**
     * Android 5.0以下某些手机需要手动创建文件夹
     *
     * @param filePath
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    public interface SaveImgCallBack {

        void erro();

        void success(String path);
    }

}
