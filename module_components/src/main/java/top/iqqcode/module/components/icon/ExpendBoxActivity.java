package top.iqqcode.module.components.icon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import top.iqqcode.module.animations.rain.FallingData;
import top.iqqcode.module.animations.rain.PbFallingView;
import top.iqqcode.module.components.R;

/**
 * 用户icon标识可拓展
 */
public class ExpendBoxActivity extends AppCompatActivity implements View.OnClickListener {

    private PbFallingView mPbFallingView;
    private Button mStartPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expend_box);

        initView();
    }

    private void initView() {
        mPbFallingView = findViewById(R.id.pb_falling_view);
        mStartPlayButton = findViewById(R.id.playButton);
        mStartPlayButton.setOnClickListener(this);
    }

    private void playAnimationBreath() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.playButton) {
            mPbFallingView.startFalling(FallingData.getFakeData(), getBaseContext(), false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPbFallingView.stopAllViews();
    }
}