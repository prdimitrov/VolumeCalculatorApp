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

public class CylinderActivity extends AppCompatActivity {
    TextView title, result;
    EditText radius, height;
    Button calculateBtn, backBtn, clearBtn, unitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cylinder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            title = findViewById(R.id.cylinderTitleTextView);
            result = findViewById(R.id.cylinderResultTextView);
            radius = findViewById(R.id.cylinderEnterRadiusEditText);
            height = findViewById(R.id.cylinderEnterHeightEditText);
            calculateBtn = findViewById(R.id.cylinderCalculateButton);
            backBtn = findViewById(R.id.cylinderBackButton);
            clearBtn = findViewById(R.id.cylinderClearButton);
            unitBtn = findViewById(R.id.cylinderUnitButton);
            return insets;
            //Formula - V=π×r^2×h
        });
    }
}