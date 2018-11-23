package com.riyol.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.riyol.function.Optional;

public class MaterialAlertDialog extends BaseDialogFragment implements DialogInterface.OnClickListener {
    private static final String TAG = "MaterialAlertDialog";
    /**
     * The identifier for the positive button.
     */
    public static final int BUTTON_POSITIVE = -1;

    /**
     * The identifier for the negative button.
     */
    public static final int BUTTON_NEGATIVE = -2;

    /**
     * The identifier for the neutral button.
     */
    public static final int BUTTON_NEUTRAL = -3;

    private final static String ARG_TITLE = "title";
    private final static String ARG_MESSAGE = "message";
    private final static String ARG_POSITIVE_TEXT = "positive_text";
    private final static String ARG_NEGATIVE_TEXT = "negative_text";
    private final static String ARG_NEUTRAL_TEXT = "neutral_text";
    private final static String ARG_THEME_ID = "theme_id";
    private final static String ARG_CANCELABLE = "cancelable";

    private CharSequence title, message;
    private CharSequence positiveButtonText, negativeButtonText, neutralButtonText;
    private OnButtonClickListener positiveButtonClick, negativeButtonClick, neutralButtonClick;
    private OnDismissListener dismissListener;
    private boolean cancelable = true;

    private int themeId;


    private static MaterialAlertDialog create(Builder builder) {
        MaterialAlertDialog dialog = new MaterialAlertDialog();
        Bundle arg = new Bundle();
        arg.putCharSequence(ARG_TITLE, builder.title);
        arg.putCharSequence(ARG_MESSAGE, builder.message);
        arg.putCharSequence(ARG_POSITIVE_TEXT, builder.positiveButtonText);
        arg.putCharSequence(ARG_NEGATIVE_TEXT, builder.negativeButtonText);
        arg.putCharSequence(ARG_NEUTRAL_TEXT, builder.neutralButtonText);
        arg.putInt(ARG_THEME_ID, builder.themeId);
        arg.putBoolean(ARG_CANCELABLE, builder.cancelable);
        dialog.positiveButtonClick = builder.positiveButtonClick;
        dialog.negativeButtonClick = builder.negativeButtonClick;
        dialog.neutralButtonClick = builder.neutralButtonClick;
        dialog.dismissListener = builder.dismissListener;
        dialog.setArguments(arg);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = savedInstanceState;
        if (arg == null) {
            arg = getArguments();
        }
        themeId = arg.getInt(ARG_THEME_ID, 0);
        cancelable = arg.getBoolean(ARG_CANCELABLE, true);
        title = arg.getCharSequence(ARG_TITLE);
        message = arg.getCharSequence(ARG_MESSAGE);
        positiveButtonText = arg.getCharSequence(ARG_POSITIVE_TEXT);
        negativeButtonText = arg.getCharSequence(ARG_NEGATIVE_TEXT);
        neutralButtonText = arg.getCharSequence(ARG_NEUTRAL_TEXT);
        setCancelable(cancelable);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(ARG_TITLE, title);
        outState.putCharSequence(ARG_MESSAGE, message);
        outState.putCharSequence(ARG_POSITIVE_TEXT, positiveButtonText);
        outState.putCharSequence(ARG_NEGATIVE_TEXT, negativeButtonText);
        outState.putCharSequence(ARG_NEUTRAL_TEXT, neutralButtonText);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        if (themeId == 0) {
            builder = new AlertDialog.Builder(getContext());
        } else {
            builder = new AlertDialog.Builder(getContext(), themeId);
        }
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, this::onClick);
        }

        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, this::onClick);
        }

        if (!TextUtils.isEmpty(neutralButtonText)) {
            builder.setNeutralButton(neutralButtonText, this::onClick);
        }

        builder.setCancelable(cancelable);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                Optional.ofNullable(positiveButtonClick)
                        .ifPresent(l -> l.onDialogButtonClick(getTag(), BUTTON_POSITIVE));
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Log.e(TAG, "onButtonNegative");
                Optional.ofNullable(negativeButtonClick)
                        .ifPresent(l -> l.onDialogButtonClick(getTag(), BUTTON_NEGATIVE));
                break;

            case DialogInterface.BUTTON_NEUTRAL:
                Optional.ofNullable(neutralButtonClick)
                        .ifPresent(l -> l.onDialogButtonClick(getTag(), BUTTON_NEUTRAL));
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Optional.ofNullable(dismissListener).ifPresent(l->l.onDialogDismiss(getTag()));
        super.onDismiss(dialog);
    }

    //    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    public void onDestroyEvent() {
//        dismissAllowingStateLoss();
//    }


    public interface OnButtonClickListener {
        void onDialogButtonClick(String tag, int which);
    }

    public interface OnDismissListener {
        void onDialogDismiss(String tag);
    }

    public static Builder newBuilder(@NonNull Context context) {
        Builder builder = new Builder(context);
        return builder;
    }


    public static final class Builder {
        private Context context;
        private OnButtonClickListener neutralButtonClick;
        private OnButtonClickListener negativeButtonClick;
        private OnButtonClickListener positiveButtonClick;
        private OnDismissListener dismissListener;
        private CharSequence neutralButtonText;
        private CharSequence negativeButtonText;
        private CharSequence positiveButtonText;
        private CharSequence message;
        private CharSequence title;
        private boolean cancelable = true;
        @StyleRes
        private int themeId = 0;

        private Builder(Context context) {
            this.context = context;
        }

        public Builder setNeutralButton(CharSequence text, OnButtonClickListener listener) {
            neutralButtonText = text;
            neutralButtonClick = listener;
            return this;
        }

        public Builder setNeutralButton(int textId, OnButtonClickListener listener) {
            neutralButtonText = context.getText(textId);
            neutralButtonClick = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, OnButtonClickListener listener) {
            negativeButtonText = text;
            negativeButtonClick = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, OnButtonClickListener listener) {
            negativeButtonText = context.getText(textId);
            negativeButtonClick = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, OnButtonClickListener listener) {
            positiveButtonText = text;
            positiveButtonClick = listener;
            return this;
        }

        public Builder setPositiveButton(int textId, OnButtonClickListener listener) {
            positiveButtonText = context.getText(textId);
            positiveButtonClick = listener;
            return this;
        }

        public Builder setDismissListener(OnDismissListener listener) {
            this.dismissListener = listener;
            return this;
        }

        public Builder setMessage(CharSequence text) {
            message = text;
            return this;
        }

        public Builder setMessage(int textId) {
            message = context.getText(textId);
            return this;
        }

        public Builder setTitle(CharSequence text) {
            title = text;
            return this;
        }

        public Builder setTitle(int textId) {
            title = context.getText(textId);
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setThemeId(@StyleRes int themeId) {
            this.themeId = themeId;
            return this;
        }

        public MaterialAlertDialog build() {
            return MaterialAlertDialog.create(this);
        }
    }
}
