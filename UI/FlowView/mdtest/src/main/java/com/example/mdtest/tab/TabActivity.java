package com.example.mdtest.tab;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mdtest.databinding.ActivityTabBinding;


public class TabActivity extends AppCompatActivity {

    private ActivityTabBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}