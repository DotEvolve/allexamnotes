package com.itsanubhav.wordroid4.others;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {

    private List<Object> oldList;
    private List<Object> newList;

    public DiffUtilCallback(List<Object> oldList, List<Object> newList) {
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
}
