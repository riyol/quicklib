package com.riyol.databinding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.riyol.databinding.recyclerview.SnappingLinearLayoutManager;
import com.riyol.databinding.recyclerview.adatper.ListBindingAdapter;
import com.riyol.function.Objects;

import java.util.List;

final public class BindingUtil {
    private BindingUtil() {
    }

    @BindingAdapter("data_list")
    public static void bindAdapterList(RecyclerView recyclerView, List<?> list) {
        ListBindingAdapter adapter = (ListBindingAdapter) recyclerView.getAdapter();
        if (adapter != null && list != null) {
            adapter.setItems(list);
        }
    }

    @BindingAdapter("image_res")
    public static void bindImageRes(ImageView view, int resId) {
        Objects.requireNonNull(view, "view is null");
        Glide.with(view.getContext())
                .load(resId)
                .into(view);
    }

    @BindingAdapter("image_url")
    public static void bindImageUrl(ImageView view, String url) {
        Objects.requireNonNull(view, "view is null");
        Glide.with(view.getContext())
                .load(url)
                .into(view);
    }


    public static GridLayoutManager setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        if (recyclerView == null) {
            return null;
        }
        GridLayoutManager glm = new GridLayoutManager(recyclerView.getContext(), spanCount);
        recyclerView.setLayoutManager(glm);
        return glm;
    }

    public static void setVerticalLayoutManager(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    public static void setSnappingLayoutManager(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(new SnappingLinearLayoutManager(recyclerView.getContext()));
    }

    public static void setHorizontalLayoutManager(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(),
                RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

}
