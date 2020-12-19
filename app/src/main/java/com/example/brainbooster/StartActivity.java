package com.example.brainbooster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    Button button;
    EditText noOfTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        button = findViewById(R.id.go);
        noOfTable = findViewById(R.id.noOfTable);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(noOfTable.getText()) || Integer.parseInt(noOfTable.getText().toString())<=0)
                {
                    noOfTable.setError("No. of table is required and it should be greater than 0");
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    String nTable = noOfTable.getText().toString();
                    intent.putExtra("nTable",nTable);
                    startActivity(intent);
                }
            }
        });
    }
}
