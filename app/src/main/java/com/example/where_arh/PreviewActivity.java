package com.example.where_arh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PreviewActivity extends AppCompatActivity {

    Button start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent start_intent = new Intent(v.getContext(), MainActivity.class){};
                startActivity(start_intent);
                finish();
            }
        });


    }



}