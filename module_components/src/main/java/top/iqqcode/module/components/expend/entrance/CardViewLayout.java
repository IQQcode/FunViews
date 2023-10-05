package top.iqqcode.module.components.expend.entrance;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

/**
 * @Author: jiazihui
 * @Date: 2022-10-29 10:37
 * @Description:
 */
public class CardViewLayout extends CardView {

    private MarginLayoutParams marginParams;

    public CardViewLayout(Context context) {
        super(context);
    }

    public CardViewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardViewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp != null) {
            lp.width = width;
            setLayoutParams(lp);
        }
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp != null) {
            lp.height = height;
            requestLayout();
        }
    }

    public void setPaddingLeft(int left) {
        setPadding(left, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    public void setPaddingRight(int right) {
        setPadding(getPaddingLeft(), getPaddingTop(), right, getPaddingBottom());
    }

    public void setMarginLeft(int left) {
        checkMarginLayoutParams();
        if (marginParams != null) {
            marginParams.leftMargin = left;
        }
    }

    public void setMarginRight(int right) {
        checkMarginLayoutParams();
        if (marginParams != null) {
            marginParams.rightMargin = right;
        }
    }

    private void checkMarginLayoutParams() {
        if (marginParams == null) {
            ViewGroup.LayoutParams vglp = getLayoutParams();
            if (vglp instanceof MarginLayoutParams) {
                marginParams = (MarginLayoutParams) vglp;
            }
        }
    }
}