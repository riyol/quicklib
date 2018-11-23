package com.riyol.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.riyol.quicklib.R;

public class CenterTitleToolbarWrapper extends Toolbar {

    private TextView textView;

    public CenterTitleToolbarWrapper(Context context) {
        this(context, null);
    }

    public CenterTitleToolbarWrapper(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public CenterTitleToolbarWrapper(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenterTitleToolbarWrapper);

        int layoutRes = a.getResourceId(R.styleable.CenterTitleToolbarWrapper_wrap_text_layout, R.layout.default_toolbar_text_layout);

        int textColor = a.getColor(R.styleable.CenterTitleToolbarWrapper_android_textColor, 0);
        if (layoutRes != 0) {
            LayoutInflater.from(context).inflate(layoutRes, this);
            textView = findViewById(R.id.wrap_toolbar_title);
            if (textColor != 0) {
                textView.setTextColor(textColor);
            }
        }

        String text = a.getString(R.styleable.CenterTitleToolbarWrapper_android_text);
        if (!TextUtils.isEmpty(text)) {
            setTitle(text);
        }

    }

    @Override
    public void setTitle(int resId) {
        if (textView != null) {
            textView.setText(resId);
            return;
        }
        super.setTitle(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (textView != null) {
            textView.setText(title);
            return;
        }
        super.setTitle(title);
    }
}
