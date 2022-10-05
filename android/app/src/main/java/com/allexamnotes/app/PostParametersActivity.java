package com.allexamnotes.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PostParametersActivity extends AppCompatActivity {


    private ImageView addCategory;
    private ImageView addTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Config.FORCE_RTL) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_parameters);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_chevron_left));
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));


        addCategory = findViewById(R.id.addCategory);
        addTags = findViewById(R.id.addTags);

        addCategory.setOnClickListener(view -> {
            BottomSheetFragment bsf = BottomSheetFragment.newInstance("category");
            bsf.show(getSupportFragmentManager(),"categories");
        });



    }
}
