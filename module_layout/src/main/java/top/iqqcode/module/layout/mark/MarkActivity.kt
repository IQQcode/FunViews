package top.iqqcode.module.layout.mark

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import top.iqqcode.module.layout.R
import top.iqqcode.module.layout.databinding.ActivityMarkBinding
import top.iqqcode.module.layout.mark.screen.IScreenshotCallBack
import top.iqqcode.module.layout.mark.screen.ScreenshotBean
import top.iqqcode.module.layout.mark.screen.ScreenshotManager
import top.iqqcode.module.layout.mark.screen.WatermarkPostionCode
import top.iqqcode.module.layout.mark.utils.ImgUtlis

/**
 * 添加水印
 *
 * @constructor Create empty Mark activity
 */
class MarkActivity : AppCompatActivity(), View.OnClickListener {

    private val binding by lazy { ActivityMarkBinding.inflate(layoutInflater) }

    private val permissions: MutableList<String> = ArrayList()

    private var type = true
    private var position = 0

    init {
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initPermission()
        initData()
    }

    private fun initView() {
        binding.screenshotBtn01.setOnClickListener(this)
    }

    private fun initPermission() {
        PermissionX.init(this@MarkActivity)
            .permissions(permissions)
            .onExplainRequestReason(ExplainReasonCallback { explainScope, list ->
                val message = "PermissionX需要您同意以下权限才能正常使用"
                explainScope.showRequestReasonDialog(list, message, "Allow", "Deny")
            })
            .request(RequestCallback { allGranted, _, deniedList ->
                if (allGranted) {
                    Toast.makeText(this@MarkActivity, "所有申请的权限都已通过", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this@MarkActivity,
                        "您拒绝了如下权限：$deniedList",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun initData() {
        binding.screenshotRg01.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.screenshot_rb_text -> type = false
                R.id.screenshot_rb_bitmap -> type = true
                else -> {}
            }
        })
        binding.screenshotRg02.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.screenshot_rb_left_01 -> position =
                    WatermarkPostionCode.WATERMARK_POSITION_LEFT_TOP

                R.id.screenshot_rb_right_01 -> position =
                    WatermarkPostionCode.WATERMARK_POSITION_RIGHT_TOP

                R.id.screenshot_rb_right_02 -> position =
                    WatermarkPostionCode.WATERMARK_POSITION_RIGHT_BOTTOM

                R.id.screenshot_rb_center -> position =
                    WatermarkPostionCode.WATERMARK_POSITION_CENTER

                R.id.screenshot_rb_left_02 -> position =
                    WatermarkPostionCode.WATERMARK_POSITION_LEFT_BOTTOM

                else -> {}
            }
        })
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.screenshot_btn_01) {
            screenMarkTag()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun screenMarkTag() {
        ScreenshotManager.Builder(this@MarkActivity)
            .setWatermark(true)
            .setImgWatermark(type)
            //.setwatermarkBitmap(DrawableUtils.drawableToBitmap(getResources().getDrawable(R.drawable.icon)))
            .setText("这个是水印--------")
            .setTextColor(Color.BLUE)
            .setWatermarkPosition(position)
            .setTextSize(18)
            .addCallBack(object : IScreenshotCallBack {
                override fun onSuccess(bean: ScreenshotBean) {
                    ImgUtlis.saveImageToGallery(
                        bean.bitmap,
                        baseContext,
                        object : ImgUtlis.SaveImgCallBack {
                            override fun erro() {
                                Toast.makeText(
                                    this@MarkActivity,
                                    "图片保存失败",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun success(path: String) {
                                Toast.makeText(
                                    this@MarkActivity,
                                    "图片保存成功保存在$path",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }

                override fun onErro(bean: ScreenshotBean?) {
                    Toast.makeText(this@MarkActivity, "截图失败", Toast.LENGTH_SHORT).show()
                }
            }).build().create()
    }
}