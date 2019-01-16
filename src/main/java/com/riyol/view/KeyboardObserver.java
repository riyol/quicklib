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

            int childBottom = child.getBottom();
            int windowBottom = rect.bottom - rect.top;
//            Log.d("KeyboardObserver", "parent bottom:" + parent.getBottom()
//                    + " child bottom:" + childBottom
//                    + " visible rect:" + rect.toString());

            if (childBottom > windowBottom) {
                parent.scrollTo(0, childBottom - windowBottom);
            } else {
                parent.scrollTo(0, 0);
            }
        });
    }
}
