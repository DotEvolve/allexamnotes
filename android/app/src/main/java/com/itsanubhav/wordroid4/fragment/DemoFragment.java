package com.itsanubhav.wordroid4.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itsanubhav.wordroid4.R;

public class DemoFragment extends Fragment {

    private DemoViewModel mViewModel;

    public static DemoFragment newInstance(int color) {
        DemoFragment demoFragment = new DemoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("color",color);
        demoFragment.setArguments(bundle);
        return demoFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.demo_fragment, container, false);
        if (getArguments()!=null){
            view.setBackgroundColor(getResources().getColor(getArguments().getInt("color")));
        }else {
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DemoViewModel.class);
    }

}
