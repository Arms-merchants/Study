package com.example.mdtest.bottomAppBar;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mdtest.R;
import com.example.mdtest.databinding.ActivityBottomAppBarBinding;
import com.google.android.material.tabs.TabLayout;


public class BottomAppBarActivity extends AppCompatActivity {

    private ActivityBottomAppBarBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomAppBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int white = ContextCompat.getColor(this, R.color.white);
        int blue = ContextCompat.getColor(this,R.color.Blue100);
        binding.tab.setTabTextColors(white,blue);
        binding.tab.addTab(binding.tab.newTab().setText("tab1"));
        binding.tab.addTab(binding.tab.newTab().setText("tab2"));
        binding.tab.addTab(binding.tab.newTab());
        binding.tab.addTab(binding.tab.newTab().setText("tab4"));
        binding.tab.addTab(binding.tab.newTab().setText("tab5"));
        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 2){
                    Toast.makeText(BottomAppBarActivity.this,"中间这个",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
