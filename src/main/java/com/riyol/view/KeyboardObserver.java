package com.riyol.view;

import android.graphics.Rect;
import android.view.View;

import com.riyol.function.Objects;

public class KeyboardObserver {

    public void observe(final View parent, final View child) {
        Objects.requireNonNull(parent, "Parent layout is null");
        Objects.requireNonNull(child, "Child view is null");

        parent.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            parent.getWindowVisibleDisplayFrame(rect);

            int[] location = new int[2];
            child.getLocationInWindow(location);
            int childBottom = location[1] + child.getHeight();
            int windowBottom = rect.bottom;

            if (childBottom > windowBottom) {
                parent.scrollTo(0, childBottom - windowBottom+4);
            } else {
                parent.scrollTo(0, 0);
            }
        });
    }
}
