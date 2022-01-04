package com.yc.basis.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.basis.R;
import com.yc.basis.utils.DataUtils;

public class TitleView extends FrameLayout {
    public View titleLayout;
    public ImageView ivRight, backIcon;
    public TextView title, tvRight;
    private String titleTxt, rightText;
    private int rightId, rightTextBg, rightTextColor;
    private boolean isWhite;//是否是白色的状态

    public TitleView(@NonNull Context context) {
        this(context, null);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleView);
        titleTxt = a.getString(R.styleable.TitleView_title);
        rightText = a.getString(R.styleable.TitleView_rightText);
        isWhite = a.getBoolean(R.styleable.TitleView_isWhite, false);
        rightId = a.getResourceId(R.styleable.TitleView_rightIcon, -1);
        rightTextBg = a.getResourceId(R.styleable.TitleView_rightTextBg, -1);
        rightTextColor = a.getColor(R.styleable.TitleView_rightTextColor, -1);

        init();
    }

    private void init() {
        titleLayout = LayoutInflater.from(getContext()).inflate(R.layout.g_title2, this, false);
        backIcon = titleLayout.findViewById(R.id.iv_title_back);
        ivRight = titleLayout.findViewById(R.id.iv_title_right);
        title = titleLayout.findViewById(R.id.tv_title_title);
        tvRight = titleLayout.findViewById(R.id.tv_title_right);

        if (!DataUtils.isEmpty(rightText)) {
            tvRight.setText(rightText);
            tvRight.setVisibility(View.VISIBLE);
            if (rightTextBg != -1) {
                tvRight.setBackgroundResource(rightTextBg);
            }
            if (rightTextColor != -1) {
                tvRight.setTextColor(rightTextColor);
            }
        }

        if (rightId != -1) {
            ivRight.setImageResource(rightId);
            ivRight.setVisibility(View.VISIBLE);
        }

        title.setText(titleTxt);

        if (isWhite) {
            backIcon.setImageResource(R.drawable.back_white);
            title.setTextColor(getResources().getColor(R.color.color_FFFFFF));
        }
        addView(titleLayout);
    }

    public void setRightTvClick(OnClickListener click) {
        tvRight.setOnClickListener(click);
    }

    public void setRightIvClick(OnClickListener click) {
        ivRight.setOnClickListener(click);
    }

    public void setTitleText(String str) {
        title.setText(str);
    }

    public void setTvRight(String rightText, int rightTextColor, int rightTextBg) {
        if (!DataUtils.isEmpty(rightText)) {
            tvRight.setText(rightText);
            tvRight.setVisibility(View.VISIBLE);
            if (rightTextBg != -1) {
                tvRight.setBackgroundResource(rightTextBg);
            } else {
                tvRight.setBackground(null);
            }
            if (rightTextColor != -1) {
                tvRight.setTextColor(rightTextColor);
            }
        }
    }

    public void setIvRight(int rightId) {
        if (rightId != -1) {
            ivRight.setImageResource(rightId);
            ivRight.setVisibility(View.VISIBLE);
        }
    }

    public void setWhite(boolean isWhite) {
        if (isWhite) {
            backIcon.setImageResource(R.drawable.back_white);
            title.setTextColor(getResources().getColor(R.color.color_FFFFFF));
        } else {
            backIcon.setImageResource(R.drawable.back_black);
            title.setTextColor(getResources().getColor(R.color.color_000000));
        }
    }

}
