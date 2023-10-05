package top.iqqcode.module.components.label;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.iqqcode.module.components.R;


/**
 * @Author: jiazihui
 * @Date: 2023-02-08 09:05
 * @Description:
 */
public class HorizontalTagAdapter extends RecyclerView.Adapter<HorizontalTagAdapter.ViewHolder> {

    private static final String TAG = "HorizontalTagAdapter";

    private final Context context;
    private List<TalentLableItem> items;
    private final LayoutInflater mInflater;
    private int clickTemp = -1;

    public HorizontalTagAdapter(Context context) {
        super();
        this.context = context;
        items = new ArrayList<TalentLableItem>();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        View view = View.inflate(viewGroup.getContext(), R.layout.talent_label_item, null);
        // 创建一个ViewHolder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        // 绑定数据到ViewHolder上
        viewHolder.mTextView.setText("#" + items.get(i).getLabel() + "#");
        TalentLableItem TLItem = items.get(i);
        if (TLItem.isSelected()) {
            viewHolder.mTextView.setBackgroundResource(R.drawable.shape_roundrect_label_selected);
            viewHolder.mTextView.setTextColor(context.getResources().getColor(R.color.colorGreen));
        } else {
            viewHolder.mTextView.setBackgroundResource(R.drawable.shape_roundrect_label_normal);
            viewHolder.mTextView.setTextColor(context.getResources().getColor(R.color.white));
        }

        if (clickTemp == i) {
            if (TLItem.isSelected()) {
                viewHolder.mTextView.setBackgroundResource(R.drawable.shape_roundrect_label_normal);
                viewHolder.mTextView.setTextColor(context.getResources().getColor(R.color.white));
                TLItem.setSelected(false);
            } else {
                viewHolder.mTextView.setBackgroundResource(R.drawable.shape_roundrect_label_selected);
                viewHolder.mTextView.setTextColor(context.getResources().getColor(R.color.colorGreen));
                TLItem.setSelected(true);
            }

        }
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textLabel);
        }
    }

    public void setShowItems(ArrayList<TalentLableItem> items) {
        this.items = items;
        notifyDataSetChanged();

    }

    public List<TalentLableItem> getItems() {
        return items;

    }

    public void setSeclection(int position) {
        clickTemp = position;

    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
