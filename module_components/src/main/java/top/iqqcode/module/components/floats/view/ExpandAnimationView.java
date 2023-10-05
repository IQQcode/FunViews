package top.iqqcode.module.components.floats.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

import top.iqqcode.module.components.R;


/**
 * @Author: jiazihui
 * @Date: 2022-10-25 20:23
 * @Description: https://blog.csdn.net/CarsonWoo/article/details/108175992?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522166671507816782417096607%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=166671507816782417096607&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-2-108175992-null-null.142^v59^pc_rank_34_2,201^v3^control,213^v1^control&utm_term=%20%20%20%20%20%20%20%20final%20ObjectAnimator%20rotateAnimation%20%3D%20ObjectAnimator.ofFloat%28mImageView%2C%20rotation%2C%20-45F%2C%200F%29%3B&spm=1018.2226.3001.4187
 */
public class ExpandAnimationView extends LinearLayout {

    private ImageView mImageView;

    private final AnimatorSet mAnimatorSet = new AnimatorSet();

    private int mExpandWidth = 0;

    private final LinearLayout mExpandContainer = new LinearLayout(getContext());

    private final List<ExpandItem> mExpandItems = new ArrayList<>();

    private boolean mIsExpanding = false;

    private OnExpandViewClickListener mClickListener;

    public ExpandAnimationView(Context context) {
        this(context, null);
    }

    public ExpandAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mExpandItems.add(new ExpandItem(R.drawable.baseline_navigate_next_black_24dp, "收起"));
        mExpandItems.add(new ExpandItem(R.drawable.baseline_share_black_24dp, "分享"));
        initViews();
    }

    private void initViews() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.round_concern_bg, null));
        mImageView = new ImageView(getContext());
        addView(mImageView);
        mImageView.setLayoutParams(new LayoutParams(dip2px(25F), dip2px(25F)));
        LayoutParams p = (LayoutParams) mImageView.getLayoutParams();
        p.setMargins(dip2px(13F), dip2px(13F), dip2px(13F), dip2px(13F));
        mImageView.setLayoutParams(p);
        mImageView.setImageResource(R.drawable.baseline_add_black_24dp);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageView.performClick();
            }
        });

        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();
            }
        });

        initExpandView();
    }

    private void initExpandView() {
        if (mExpandItems == null || mExpandItems.isEmpty()) {
            return;
        }

        addView(mExpandContainer, 0);
        mExpandContainer.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
        ViewGroup.LayoutParams lp = mExpandContainer.getLayoutParams();
        mExpandContainer.setGravity(Gravity.CENTER_VERTICAL);

        for (int i = 0; i < mExpandItems.size(); i++) {
            final TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(13F);
            textView.setTextColor(Color.WHITE);
            textView.setText(mExpandItems.get(i).text);
            textView.setMaxLines(1);
            Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), mExpandItems.get(i).resourceId, null);
            if (drawable != null) {
                drawable.setBounds(0, 0, dip2px(15F), dip2px(15F));
            }
            textView.setCompoundDrawables(drawable, null, null, null);
            textView.setCompoundDrawablePadding(dip2px(3F));

            mExpandContainer.addView(textView);
            int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(mExpandItems.get(finalI).text);
                    }
                }
            });

            LayoutParams p = (LayoutParams) textView.getLayoutParams();
            p.setMargins(i == 0 ? dip2px(23F) : dip2px(13F), 0, dip2px(13F), 0);
            textView.setLayoutParams(p);
            final View divider = new View(getContext());
            divider.setBackgroundColor(Color.parseColor("#fafafa"));
            divider.setLayoutParams(new LayoutParams(dip2px(1F), dip2px(20F)));
            mExpandContainer.addView(divider);
            mExpandWidth += (textView.getTextSize() * mExpandItems.get(i).text.length()) + dip2px(15F + 20F + 1F + 3F);
        }

        mExpandWidth += dip2px(10F + 13F);

        mExpandContainer.setAlpha(0);
    }

    /**
     * 属性动画原理 与下面实现动画时 属性"expandWidth"相对应
     * @param width
     */
    public void setExpandWidth(float width) {
        LayoutParams p = (LayoutParams) mExpandContainer.getLayoutParams();
        p.width = (int) width;
        mExpandContainer.setLayoutParams(p);
    }

    // 开始动画
    public void startAnimation() {
        if (mIsExpanding) {
            collapse();
        } else {
            expand();
        }
    }

    private void collapse() {
        if (!mIsExpanding) {
            return;
        }
        mIsExpanding = false;
        final ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(mImageView, "rotation", -45F, 0F);
        final ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mImageView, "alpha", 0.5F, 1F);
        final ObjectAnimator collapseAnim = ObjectAnimator.ofFloat(this, "expandWidth", mExpandWidth, 0F);
        final ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(mExpandContainer, "alpha", 1F, 0F);
        mAnimatorSet.playTogether(rotateAnimation, alphaAnimation, collapseAnim, alphaAnim);
        mAnimatorSet.start();
        if (mClickListener != null) {
            mClickListener.onCollapse();
        }
    }

    private void expand() {
        if (mIsExpanding) {
            return;
        }
        mIsExpanding = true;
        final ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(mImageView, "rotation", 0F, -45F);
        final ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mImageView, "alpha", 1F, 0.5F);
        final ObjectAnimator expandAnim = ObjectAnimator.ofFloat(this, "expandWidth", 0F, mExpandWidth);
        final ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(mExpandContainer, "alpha", 0F, 1F);
        mAnimatorSet.playTogether(rotateAnimation, alphaAnimation, expandAnim, alphaAnim);
        mAnimatorSet.start();
        if (mClickListener != null) {
            mClickListener.onExpand();
        }
    }

    private int dip2px(float floatVal) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (floatVal * density + 0.5F);
    }

    public void setOnExpandViewClickListener(OnExpandViewClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), dip2px(25F + 26F));
    }

    public void destroy() {
        mAnimatorSet.removeAllListeners();
        mAnimatorSet.cancel();
    }

    class ExpandItem {
        public int resourceId;

        public String text;

        public ExpandItem() {
        }

        public ExpandItem(int resourceId, String text) {
            this.resourceId = resourceId;
            this.text = text;
        }
    }

    interface OnExpandViewClickListener {

        void onExpand();

        void onCollapse();

        void onItemClick(String tag);
    }
}
