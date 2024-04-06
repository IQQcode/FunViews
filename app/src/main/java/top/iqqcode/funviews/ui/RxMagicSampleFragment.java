package top.iqqcode.funviews.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.bean.DefaultGroupedItem;

import java.util.List;

import top.iqqcode.funviews.R;
import top.iqqcode.funviews.databinding.FragmentRxmagicBinding;

/**
 * @author iqqcode
 * @Description: todo
 */
public class RxMagicSampleFragment extends Fragment {

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
        initLinkageData(mBinding.linkage);
    }

    private void initLinkageData(LinkageRecyclerView linkageView) {
        Gson gson = new Gson();
        // 构造数据
        List<DefaultGroupedItem> items = gson.fromJson(getString(R.string.operators_json),
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
                    });
                },
                (headerHolder, item) -> {

                },
                (footerHolder, item) -> {

                }
        );
    }
}
