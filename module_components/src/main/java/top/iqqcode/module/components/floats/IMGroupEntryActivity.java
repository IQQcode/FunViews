package top.iqqcode.module.components.floats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import top.iqqcode.module.components.R;
import top.iqqcode.module.components.floats.demo.ExpandButtonLayout;


/**
 * @author jiazihui
 */
public class IMGroupEntryActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ViewFlipper viewFlipper;
    private List<String> titles;

    private Button mToLeftButton;
    private Button mToRightButton;
    private Button mResetButton;
    private TextView mBallText;

    private ExpandButtonLayout mExpandButtonLayout;
    private Button mTestButton01;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgroup_entry);

        initView();

        context = this;
        viewFlipper = findViewById(R.id.view_flipper);
        initData();
        setViews();
    }

    private void initView() {
        mBallText = findViewById(R.id.mTextBall);
        mResetButton = findViewById(R.id.button_to_reset);
        mToLeftButton = findViewById(R.id.button_to_left);
        mToRightButton = findViewById(R.id.button_to_right);
        mExpandButtonLayout = findViewById(R.id.expanded_button_layout);
        mTestButton01 = findViewById(R.id.button_to_test01);

        mToLeftButton.setOnClickListener(this);
        mToRightButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mBallText.setOnClickListener(this);
        mTestButton01.setOnClickListener(this);
    }

    private void setViews() {
        if (titles.size() > 0) {
            // 计算ViewFlipper视图的数目
            int viewNum = titles.size() / 2 + 1;
            for (int i = 0; i < viewNum; i++) {
                // 每一个视图的第一个新闻标题中集合中的下标值
                final int position = i * 2;
                View itemView = View.inflate(context, R.layout.carousel_item_view, null);
                TextView tvTitle1 = itemView.findViewById(R.id.tv_title1);
                TextView tvTitle2 = itemView.findViewById(R.id.tv_title2);
                LinearLayout ll = itemView.findViewById(R.id.ll_second);
                //得到textView实例之后，用集合中的数据进行填充，通过循环完成
                tvTitle1.setText(titles.get(position));

                //判断第二行是否有数据，这里考虑进奇数视图的情况
                if (position + 1 < titles.size()) {
                    tvTitle2.setText(titles.get(position + 1));
                } else {
                    //表示该视图的第二个标题没有数据，隐藏第二行布局
                    ll.setVisibility(View.GONE);
                }

                //标题1的点击事件
                tvTitle1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, titles.get(position), Toast.LENGTH_SHORT).show();
                    }
                });

                //标题2的点击事件
                tvTitle2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, titles.get(position + 1), Toast.LENGTH_SHORT).show();
                    }
                });

                viewFlipper.addView(itemView);
            }
        }
    }

    private void initData() {
        titles = new ArrayList<>();
        titles.add("你是最适合19.9元T恤的人！");
        titles.add("《高能少年团加长版》一期上线！");
        titles.add("古人是如何买年货的！");
        titles.add("为何让手机24小时不离身！");
        titles.add("我用了20年才看懂还珠格格");
        titles.add("你的英伦腔是装的还是真的？");
        titles.add("梁实秋：人没有不懒的");
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_to_left) {
            ViewPropertyAnimator animator = mBallText.animate();
            animator.setDuration(1000);
            // 点击一次会向右偏移，再点击没效果
            animator.translationX(-300f);
            animator.start();

        } else if (id == R.id.button_to_right) {
            ViewPropertyAnimator animator = mBallText.animate();
            animator.setDuration(1000);
            // 点击一次会向右偏移，再点击没效果
            animator.translationX(300f);
            animator.start();
        } else if (id == R.id.button_to_reset) {
            ViewPropertyAnimator animator = mBallText.animate();
            animator.setDuration(1000);
            // 点击一次会向右偏移，再点击没效果
            animator.translationX(0f);
            animator.start();
        } else if (id == R.id.mTextBall) {
            Toast.makeText(context, "文字点击啦", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.button_to_test01) {
            mExpandButtonLayout.toggle();
        }
    }

}