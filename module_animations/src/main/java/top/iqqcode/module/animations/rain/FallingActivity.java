package top.iqqcode.module.animations.rain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import top.iqqcode.module.animations.R;
import top.iqqcode.module.animations.rain.baserain.FallingView;

/**
 * @author jiazihui
 */
public class FallingActivity extends AppCompatActivity implements View.OnClickListener {

    // 红包雨特效
    private RainFallingView mFallingView;

    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling);
        
        initView();
    }

    private void initView() {
        mStartButton = findViewById(R.id.startButton);
        mStartButton.setOnClickListener(this);

        mFallingView = findViewById(R.id.falling_view);
        mFallingView.setAnimationListener(new FallingView.IViewAnimationListener() {
            @Override
            public void onAnimationStart() {
                mFallingView.setTag(false);
            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }

    /**
     * 停止红包雨
     */
    public void stopFallingRedPacket() {
        if (mFallingView != null){
            mFallingView.stopAllViews();
        }
    }

    /**
     * 开始红包雨
     * @param segmentData
     */
    public void startFallingRedPacket(FallingData segmentData) {
        if (mFallingView != null) {
            mFallingView.startFalling(segmentData, getApplicationContext(), false);
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.startButton) {
            startFallingRedPacket(FallingData.getFakeData());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopFallingRedPacket();
    }
}