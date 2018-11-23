package com.riyol.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riyol.quicklib.R;


public class ImageTextWrapper extends ConstraintLayout {

    private ImageView imageView;
    private TextView textView;
    private View divider;

    public ImageTextWrapper(Context context) {
        this(context, null);
    }

    public ImageTextWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextWrapper);

        int layoutRes = a.getResourceId(R.styleable.ImageTextWrapper_wrap_layout, R.layout.default_image_text_wrapper_layout);

        if (layoutRes == 0) {
            a.recycle();
            throw new IllegalArgumentException("ImageTextWrapper wrap_layout is null,please set it");
        }
        if (layoutRes != 0) {
            LayoutInflater.from(context).inflate(layoutRes, this);
            imageView = findViewById(R.id.wrap_image);
            textView = findViewById(R.id.wrap_text);
            divider = findViewById(R.id.wrap_divider);
        }


        if (imageView != null) {
            int imageRes = a.getResourceId(R.styleable.ImageTextWrapper_wrap_image, 0);
            if (imageRes != 0) {
                setImageResource(imageRes);
            } else {
                hideImageView();
            }
        }

        if (textView == null) {
            throw new IllegalArgumentException("ImageTextWrapper wrap_layout is error,must contain a TextView and id " +
                    "is wrap_text");
        }
        String text = a.getString(R.styleable.ImageTextWrapper_wrap_text);
        if (!TextUtils.isEmpty(text)) {
            setText(text);
        }

        if (divider != null) {
            int dividerColor = a.getColor(R.styleable.ImageTextWrapper_wrap_divider_color, 0);
            if (dividerColor != 0) {
                divider.setBackgroundColor(dividerColor);
            }
            boolean show = a.getBoolean(R.styleable.ImageTextWrapper_wrap_divider_visible, true);
            showDivider(show);
        }
        a.recycle();
    }

    public void hideImageView() {
        if (imageView != null) {
            imageView.setVisibility(GONE);
        }
        textView.setPadding(0, textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
    }

    public void setImageResource(@DrawableRes int resId) {
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
    }

    public void setImageDrawable(Drawable drawable) {
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    public void setText(CharSequence title) {
        if (textView != null) {
            textView.setText(title);
        }
    }

    public void setText(@StringRes int resId) {
        if (textView != null) {
            textView.setText(resId);
        }
    }

    public void showDivider(boolean show) {
        if (divider != null) {
            divider.setVisibility(show ? VISIBLE : INVISIBLE);
        }
    }
}
