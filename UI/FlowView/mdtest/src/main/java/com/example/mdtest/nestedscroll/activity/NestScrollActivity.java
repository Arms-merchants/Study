package com.example.mdtest.nestedscroll.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mdtest.databinding.ActivityNestScrollBinding;


public class NestScrollActivity extends AppCompatActivity {

    private ActivityNestScrollBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNestScrollBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}