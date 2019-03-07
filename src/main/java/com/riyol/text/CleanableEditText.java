package com.riyol.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.riyol.quicklib.R;


/**
 *
 */
public class CleanableEditText extends AppCompatEditText implements TextWatcher {

    @DrawableRes
    private static final int DEFAULT_CLEAR_ICON_RES_ID = R.drawable.abc_ic_clear_material;

    private Drawable mClearIconDrawable;

    private boolean mIsClearIconShown = false;

    private boolean mClearIconDrawWhenFocused = true;

    public CleanableEditText(Context context) {
        this(context, null);
    }

    public CleanableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CleanableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a =
                getContext().obtainStyledAttributes(attrs, R.styleable.CleanableEditText, defStyle, 0);

        if (a.hasValue(R.styleable.CleanableEditText_clearIconDrawable)) {
            mClearIconDrawable = a.getDrawable(R.styleable.CleanableEditText_clearIconDrawable);
            mClearIconDrawable.setCallback(this);
        } else {

            final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_clear_black_18dp);
            final Drawable wrappedDrawable = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted
            // pre Lollipop
            DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
            mClearIconDrawable = wrappedDrawable;
        }
        mClearIconDrawable.setBounds(0, 0, mClearIconDrawable.getIntrinsicHeight(), mClearIconDrawable
                .getIntrinsicHeight());
        mClearIconDrawable.setCallback(this);

        if (ViewCompat.getMinimumHeight(this) <= 0) {
            // We should make sure that the EditText has the same min-height as the password
            // toggle view. This ensure focus works properly, and there is no visual jump
            // if the password toggle is enabled/disabled.

            setMinimumHeight(mClearIconDrawable.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom());
        }

        mClearIconDrawWhenFocused = a
                .getBoolean(R.styleable.CleanableEditText_clearIconDrawWhenFocused, true);

        a.recycle();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // no operation
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return mIsClearIconShown ? new ClearIconSavedState(superState, true)
                : superState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof ClearIconSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        ClearIconSavedState savedState = (ClearIconSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mIsClearIconShown = savedState.isClearIconShown();
        showClearIcon(mIsClearIconShown);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus()) {
            showClearIcon(!TextUtils.isEmpty(s));
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        showClearIcon(
                (!mClearIconDrawWhenFocused || focused) && !TextUtils.isEmpty(getText().toString()));
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClearIconTouched(event)) {
            setText(null);
            event.setAction(MotionEvent.ACTION_CANCEL);
            showClearIcon(false);
            return false;
        }
        return super.onTouchEvent(event);
    }

    private boolean isClearIconTouched(MotionEvent event) {
        if (!mIsClearIconShown) {
            return false;
        }

        final int touchPointX = (int) event.getX();

        final int widthOfView = getWidth();
        final int compoundPaddingRight = getCompoundPaddingRight();

        return touchPointX >= widthOfView - compoundPaddingRight;
    }

    private void showClearIcon(boolean show) {
        if (show) {
            // show icon on the right
            if (mClearIconDrawable != null) {
                setCompoundDrawables(null, null, mClearIconDrawable, null);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(0, 0, DEFAULT_CLEAR_ICON_RES_ID, 0);
            }
        } else {
            // remove icon
            setCompoundDrawables(null, null, null, null);
        }
        mIsClearIconShown = show;
    }

    protected static class ClearIconSavedState extends BaseSavedState {

        public static final Creator<ClearIconSavedState> CREATOR =
                new Creator<ClearIconSavedState>() {
                    @Override
                    public ClearIconSavedState createFromParcel(Parcel source) {
                        return new ClearIconSavedState(source);
                    }

                    @Override
                    public ClearIconSavedState[] newArray(int size) {
                        return new ClearIconSavedState[size];
                    }
                };
        private final boolean mIsClearIconShown;

        private ClearIconSavedState(Parcel source) {
            super(source);
            mIsClearIconShown = source.readByte() != 0;
        }

        ClearIconSavedState(Parcelable superState, boolean isClearIconShown) {
            super(superState);
            mIsClearIconShown = isClearIconShown;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeByte((byte) (mIsClearIconShown ? 1 : 0));
        }

        boolean isClearIconShown() {
            return mIsClearIconShown;
        }
    }
}
