package com.example.mdtest.materialtext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.mdtest.databinding.ActivityTextInputBinding;
import com.google.android.material.textfield.TextInputLayout;


public class TextInputActivity extends AppCompatActivity {

    private ActivityTextInputBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.materialbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showError(binding.textInput,"错误展示！！！");
            }
        });
    }

    private void showError(TextInputLayout textInputLayout, String error){
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }
}