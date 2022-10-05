package com.itsanubhav.wordroid4;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsanubhav.libdroid.database.CategoryDatabase;
import com.itsanubhav.libdroid.model.category.Category;

import java.util.List;


public class BottomSheetFragment extends BottomSheetDialogFragment {

    private Listener mListener;
    private ItemAdapter adapter;

    public static BottomSheetFragment newInstance(String itemType) {
        BottomSheetFragment bsf = new BottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("itemType",itemType);
        bsf.setArguments(args);
        return bsf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list_dialog, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        new GetCategories().execute();
    }

    class GetCategories extends AsyncTask<Void,Void,List<Category>>{

        @Override
        protected List<Category> doInBackground(Void... voids) {
            CategoryDatabase catDb = CategoryDatabase.getAppDatabase(getContext());
            return catDb.categoryDao().getAllCategories();
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            super.onPostExecute(categories);
            adapter = new ItemAdapter(getContext());
            adapter.addItems(categories);
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onItemClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false));
            text = itemView.findViewById(R.id.text);
            text.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClicked(getAdapterPosition());
                    dismiss();
                }
            });
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<Category> mItemCount;
        private Context context;

        public ItemAdapter(Context context) {
            this.context = context;
        }

        public void addItems(List<Category> categories) {
            mItemCount = categories;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return mItemCount!=null ? mItemCount.size() : 0;
        }

    }

}
