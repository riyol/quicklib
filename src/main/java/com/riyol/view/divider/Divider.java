/*
 * Copyright 2017 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.riyol.view.divider;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;

import com.riyol.view.DimenUtil;

/**
 * <p>Divider of {@code RecyclerView}, you can get the width and height of the line.</p>
 * Created by YanZhenjie on 2017/8/16.
 */
public abstract class Divider extends RecyclerView.ItemDecoration {

    /**
     * Get the height of the divider.
     *
     * @return height of the divider.
     */
    public abstract int getHeight();

    /**
     * Get the width of the divider.
     *
     * @return width of the divider.
     */
    public abstract int getWidth();

    public static Divider getDivider(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new Api21ItemDivider(color);
        }
        return new Api20ItemDivider(color);
    }

    public static Divider getDivider(Context context, @ColorInt int color, int widthDp, int heightDp) {
        int width = (int) DimenUtil.dpToPx(context, widthDp);
        int height = (int) DimenUtil.dpToPx(context, heightDp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new Api21ItemDivider(color, width, height);
        }
        return new Api20ItemDivider(color, width, height);
    }

}