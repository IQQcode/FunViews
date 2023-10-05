package top.iqqcode.module.components.label;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.iqqcode.module.components.R;


public class EmotionLabelActivity extends AppCompatActivity {

    private RecyclerView mHorizontalTagRecyclerView;
    private HorizontalTagAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_label);
    }

    /**
     * 初始化水平滑动标签列表
     * add By Author
     */
    private void initHorizontalTagRecycler() {
        // 创建一个线性布局管理器
        FrameLayout tagFrameLayout = findViewById(R.id.tag_recyclerView_container);
        mHorizontalTagRecyclerView = new RecyclerView(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        mHorizontalTagRecyclerView.setLayoutManager(layoutManager);
        // 设置间距
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.tbds5);
        mHorizontalTagRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //去掉RecyclerView滑到边界产生的蓝色阴影
        mHorizontalTagRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        tagFrameLayout.addView(mHorizontalTagRecyclerView);
        mAdapter = new HorizontalTagAdapter(this);
        mHorizontalTagRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new HorizontalTagAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                List<TalentLableItem> showItems = new ArrayList<TalentLableItem>();
                showItems = mAdapter.getItems();
                int count = 0;
                for (TalentLableItem item : showItems) {
                    if (item.isSelected()) {
                        count++;
                    }
                }
                if (count < 3) {
                    mAdapter.setSeclection(position);
                    mAdapter.notifyDataSetChanged();
                } else {
//                    if (items.get(position).isSelected()) {
//                        mAdapter.setSeclection(position);
//                        mAdapter.notifyDataSetChanged();
//                    } else {
//                        ToastUtils.toast(CreateLiveRoomActivity.this, "最多只能选择三个标签");
//                    }

                }
            }
        });
    }
}