package top.iqqcode.module.animations.alpha;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import top.iqqcode.lib.common.util.UtilHelper;
import top.iqqcode.module.animations.R;


/**
 * 自然过渡
 * @author jiazihui
 */
public class NaturalAlphaActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewA;
    private TextView mTextViewB;
    private Button mAttributeButton;

    private RelativeLayout mShadowView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_natural_alpha);

        initView();
    }

    private void initView() {
        mTextViewA = findViewById(R.id.textA);
        mTextViewB = findViewById(R.id.textB);
        mAttributeButton = findViewById(R.id.attribute_button);
        mAttributeButton.setOnClickListener(this);

        mShadowView = findViewById(R.id.view_round);
        mShadowView.setElevation(UtilHelper.INSTANCE.dip2px(this, 7));
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.attribute_button) {
            if (mTextViewA.getVisibility() != View.VISIBLE) {
                mTextViewA.setVisibility(View.VISIBLE);
            } else {
                mTextViewA.setVisibility(View.GONE);
            }

            if (mTextViewB.getVisibility() != View.VISIBLE) {
                mTextViewB.setVisibility(View.VISIBLE);
            } else {
                mTextViewB.setVisibility(View.GONE);
            }
        }
    }
}