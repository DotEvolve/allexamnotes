package com.allexamnotes.app.others;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.allexamnotes.libdroid.model.category.Category;

import java.util.List;

public class CategoryDiffUtilCallback extends DiffUtil.Callback {

    private List<Object> oldList;
    private List<Category> newList;

    public CategoryDiffUtilCallback(List<Object> oldList, List<Category> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).equals(oldList.get(oldItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).equals(oldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
