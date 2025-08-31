package top.iqqcode.funviews.ui;

import static top.iqqcode.lib.common.util.JSONConfigRender.readJsonFromAssets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.bean.DefaultGroupedItem;

import java.util.List;

import top.iqqcode.funviews.R;
import top.iqqcode.funviews.databinding.FragmentRxmagicBinding;

public class FrameworksFragment extends Fragment {

    private FragmentRxmagicBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rxmagic, container, false);
        mBinding = FragmentRxmagicBinding.bind(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinkageRecyclerView linkageView = mBinding.linkage;
        initLinkageData(linkageView);
    }

    private void initLinkageData(LinkageRecyclerView linkageView) {
        Gson gson = new Gson();
        // 构造数据
        String jsonStr = readJsonFromAssets(requireContext(), "frameworks_data.json");
        List<DefaultGroupedItem> items = gson.fromJson(jsonStr,
                new TypeToken<List<DefaultGroupedItem>>() {
                }.getType());

        linkageView.init(items);
        linkageView.setDefaultOnItemBindListener(
                (primaryHolder, primaryClickView, title) -> {
                    Snackbar.make(primaryClickView, title, Snackbar.LENGTH_SHORT).show();
                    int position = primaryHolder.getAdapterPosition();
                },
                (primaryHolder, title) -> {

                },
                (secondaryHolder, item) -> {
                    secondaryHolder.getView(com.kunminx.linkage.R.id.level_2_item).setOnClickListener(v -> {
                        int position = secondaryHolder.getAdapterPosition();
                        String route = item.info.getRoute();
                        Snackbar.make(v, item.info.getTitle(), Snackbar.LENGTH_SHORT).show();
                        // 应用内简单的跳转(通过URL跳转在'进阶用法'中)
                        ARouter.getInstance().build(item.info.getRoute()).navigation();
                        // String jsonStr = readJsonFromAssets(requireContext(), "main_catalogue.json");
                        // 现在你可以使用jsonStr变量来访问JSON字符串
                        Log.d("JIAZIHUI", "initLinkageData: " + jsonStr);
                    });
                },
                (headerHolder, item) -> {

                },
                (footerHolder, item) -> {

                }
        );
    }
}
