package com.example.volumecalculatorapp;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sphere);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            radius = findViewById(R.id.sphereEnterRadiusEditText);
            title = findViewById(R.id.sphereTitleTextView);
            result = findViewById(R.id.sphereResultTextView);
            calculateBtn = findViewById(R.id.sphereCalculateButton);
            backBtn = findViewById(R.id.sphereBackButton);
            unitBtn = findViewById(R.id.sphereUnitButton);
            clearBtn = findViewById(R.id.sphereClearButton);

            title.setText(getString(R.string.sphere_volume_title));

            calculateBtn.setOnClickListener(v1 -> {
                try {
                    double r = Double.parseDouble(radius.getText().toString());
                    double volume = (4.0 / 3.0) * Math.PI * Math.pow(r, 3);
                    result.setText("V=" + volume + "m³");
                    inLiters = false;
                } catch (NumberFormatException e) {
                    enterValidNumberToast(SphereActivity.this);
                }
            });

            backBtn.setOnClickListener(v2 -> {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            });

            clearBtn.setOnClickListener(v4 -> {
                radius.setText("");
                result.setText(R.string.result);
                inLiters = false;
            });

            unitBtn.setOnClickListener(v5 -> {
                try {
                    if (!radius.getText().toString().isEmpty()) {
                        double r = Double.parseDouble(radius.getText().toString());
                        double volume = (4.0 / 3.0) * Math.PI * Math.pow(r, 3);

                        if (!inLiters) {
                            double volumeInLiters = volume * 1000.0;
                            result.setText("V=" + volumeInLiters + "L");
                            inLiters = true;
                        } else {
                            result.setText("V=" + volume + "m³");
                            inLiters = false;
                        }
                    } else {
                        enterValidNumberToast(SphereActivity.this);
                    }
                } catch (NumberFormatException e) {
                    enterValidNumberToast(SphereActivity.this);
                }
            });
            return insets;
        });
    }
    private static void enterValidNumberToast(Context context) {
        Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show();
    }
}
