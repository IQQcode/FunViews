package top.iqqcode.module.components.label;

/**
 * @Author: jiazihui
 * @Date: 2023-02-08 09:04
 * @Description:
 */

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerViews support the concept of ItemDecoration: special offsets and drawing around each element.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildPosition(view) != 0) {
            outRect.left = space;
        }
    }
}
