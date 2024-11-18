package com.example.volumecalculatorapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SphereActivity extends AppCompatActivity {

    EditText sphereRadius;
    TextView title, result;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sphere);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            sphereRadius = findViewById(R.id.editTextSphere);
            title = findViewById(R.id.sphereTitle);
            result = findViewById(R.id.sphereResult);
            button = findViewById(R.id.sphereButton);

            button.setOnClickListener(v1 -> {
                String radius = sphereRadius.getText().toString();

                int r = Integer.parseInt(radius);
                //Formula: V = (4/3) * pi * r^3

                double volume = (4/3) * Math.PI * r*r*r;

                result.setText("V = "+ volume + " m^3");
            });
            return insets;
        });
    }
}