package com.riyol.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;

public class KeyboardLayout extends FrameLayout {
    private boolean mIsKeyboardActive;
    private int mKeyboardHeight;
    private KeyboardLayoutListener mListener;

    public interface KeyboardLayoutListener {
        void onKeyboardStateChanged(boolean z, int i);
    }

    private class KeyboardOnGlobalChangeListener implements OnGlobalLayoutListener {
        int mScreenHeight;

        private KeyboardOnGlobalChangeListener() {
            this.mScreenHeight = 0;
        }

        private int getScreenHeight() {
            if (this.mScreenHeight > 0) {
                return this.mScreenHeight;
            }

            Point point = new Point();
            ((WindowManager) KeyboardLayout.this.getContext().getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getSize(point);
            this.mScreenHeight = point.y;
            return this.mScreenHeight;
        }

        public void onGlobalLayout() {
            Rect rect = new Rect();
            ((Activity) KeyboardLayout.this.getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int screenHeight = getScreenHeight();
            int i = screenHeight - rect.bottom;
            boolean z = false;
            if (Math.abs(i) > screenHeight / 4) {
                z = true;
                KeyboardLayout.this.mKeyboardHeight = i;
            }
            KeyboardLayout.this.mIsKeyboardActive = z;
            if (KeyboardLayout.this.mListener != null) {
                KeyboardLayout.this.mListener.onKeyboardStateChanged(z, i);
            }
        }
    }

    public KeyboardLayout(Context context) {
        this(context, null, 0);
    }

    public KeyboardLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyboardLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsKeyboardActive = false;
        this.mKeyboardHeight = 0;
        getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardOnGlobalChangeListener());
    }

    public void setKeyboardListener(KeyboardLayoutListener keyboardLayoutListener) {
        this.mListener = keyboardLayoutListener;
    }

    public KeyboardLayoutListener getKeyboardListener() {
        return this.mListener;
    }

    public boolean isKeyboardActive() {
        return this.mIsKeyboardActive;
    }

    public int getKeyboardHeight() {
        return this.mKeyboardHeight;
    }


    private ScrollRunnable scrollRunnable = new ScrollRunnable();

    public void scrollToBottom(final ScrollView scrollView) {
        removeCallbacks(scrollRunnable);
        scrollRunnable.cancel();
        scrollRunnable.bindScrollView(scrollView);
        postDelayed(scrollRunnable, 100);
    }

    public void scrollStop() {
        removeCallbacks(scrollRunnable);
        scrollRunnable.cancel();
    }

    private class ScrollRunnable implements Runnable {
        private AnimatorSet animatorSet;

        @Override
        public void run() {
            if (animatorSet != null) {
                animatorSet.start();
            }
        }

        public void cancel() {
            if (animatorSet != null) {
                animatorSet.cancel();
                animatorSet = null;
            }
        }

        public void bindScrollView(ScrollView scrollView) {
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            ObjectAnimator ofInt = ObjectAnimator.ofInt(scrollView, "scrollX", new
                    int[]{0});
            ObjectAnimator ofInt2 = ObjectAnimator.ofInt(scrollView, "scrollY", new
                    int[]{scrollView.getBottom()});
            animatorSet = new AnimatorSet();
            animatorSet.setDuration(500);
            animatorSet.playTogether(new Animator[]{ofInt, ofInt2});
            animatorSet.addListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                }

                public void onAnimationCancel(Animator animator) {
                }
            });
        }
    }
}
