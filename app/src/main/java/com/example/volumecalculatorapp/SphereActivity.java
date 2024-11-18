package com.example.volumecalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SphereActivity extends AppCompatActivity {

    EditText radius;
    TextView title, result;
    Button calculateBtn, backBtn, unitBtn, clearBtn;

    boolean inLiters = false;
    double tempVolume = 0.0;
    //TODO: TEST DIFFERENT CASES, THERE ARE BUGS TO BE FIXED!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sphere);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            radius = findViewById(R.id.enterRadiusEditText);
            title = findViewById(R.id.titleTextView);
            result = findViewById(R.id.resultTextView);
            calculateBtn = findViewById(R.id.calculateButton);
            backBtn = findViewById(R.id.backButton);
            unitBtn = findViewById(R.id.unitButton);
            clearBtn = findViewById(R.id.clearButton);

            calculateBtn.setOnClickListener(v1 -> {
                try {
                    double r = Double.parseDouble(radius.getText().toString());
                    double volume = ((double) 4 / 3) * Math.PI * r * r * r;
                    //TODO: TEST DIFFERENT CASES, THERE ARE BUGS TO BE FIXED!!!
                    tempVolume = volume;
                    result.setText("V=" + volume + "m³");
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
                //Formula: V = (4/3) * pi * r^3
            });

            backBtn.setOnClickListener(v2 -> {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            });

            unitBtn.setOnClickListener(v3 -> {

            });

            clearBtn.setOnClickListener(v4 -> {
                radius.setText("");
                result.setText(R.string.result);
            });

            unitBtn.setOnClickListener(v5 -> {
                //TODO: TEST DIFFERENT CASES, THERE ARE BUGS TO BE FIXED!!!
                    if (!inLiters) {
                        tempVolume = tempVolume * 1000.0;
                        result.setText("V=" + tempVolume + "L");
                        inLiters = true;
                    } else if (inLiters) {
                        tempVolume = tempVolume / 1000.0;  // 1000 L = 1 m³
                        result.setText("V=" + tempVolume + "m³");
                        inLiters = false;
                    }
            });
            return insets;
        });
    }
}