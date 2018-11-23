package com.riyol.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.riyol.quicklib.R;


@BindingMethods(@BindingMethod(type = TitleValueWrapper.class,attribute = "wrap_value",method = "setValue"))
public class TitleValueWrapper extends ConstraintLayout {

    private TextView valueView;
    private TextView textView;
    private View divider;

    private String textFormat;

    public TitleValueWrapper(Context context) {
        this(context, null);
    }

    public TitleValueWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleValueWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleValueWrapper);

        int layoutRes = a.getResourceId(R.styleable.TitleValueWrapper_wrap_layout, R.layout.default_title_value_wrapper_layout);

        if (layoutRes == 0) {
            a.recycle();
            throw new IllegalArgumentException("ImageTextWrapper wrap_layout is null,please set it");
        }
        if (layoutRes != 0) {
            LayoutInflater.from(context).inflate(layoutRes, this);
            valueView = findViewById(R.id.wrap_value);
            textView = findViewById(R.id.wrap_text);
            divider = findViewById(R.id.wrap_divider);
        }


        if (textView == null) {
            throw new IllegalArgumentException("ImageTextWrapper wrap_layout is error,must contain a TextView and id " +
                    "is wrap_text");
        }
        String text = a.getString(R.styleable.TitleValueWrapper_wrap_text);
        textFormat = a.getString(R.styleable.TitleValueWrapper_wrap_title_format);
        if (!TextUtils.isEmpty(text)) {
            setText(text);
        }

        String value = a.getString(R.styleable.TitleValueWrapper_wrap_value);
        if (!TextUtils.isEmpty(value)) {
            setValue(value);
        }

        if (divider != null) {
            boolean show = a.getBoolean(R.styleable.ImageTextWrapper_wrap_divider_visible, true);
            showDivider(show);
        }
        a.recycle();
    }

    public void setText(CharSequence title) {
        setTitle(title);
    }

    public void setText(@StringRes int resId) {
        setTitle(getContext().getString(resId));
    }

    private void setTitle(CharSequence title) {
        if (textView == null) {
            return;
        }
        if (textFormat != null) {
            textView.setText(String.format(textFormat, title));
        } else {
            textView.setText(title);
        }
    }

    public void setValue(CharSequence title) {
        if (valueView != null) {
            valueView.setText(title);
        }
    }

    public void setValue(@StringRes int resId) {
        if (valueView != null) {
            valueView.setText(resId);
        }
    }

    public void showDivider(boolean show) {
        if (divider != null) {
            divider.setVisibility(show ? VISIBLE : INVISIBLE);
        }
    }

    @BindingAdapter("wrap_value")
    public void bindingValue(TextView view, String value) {
        view.setText(value);
    }
}
