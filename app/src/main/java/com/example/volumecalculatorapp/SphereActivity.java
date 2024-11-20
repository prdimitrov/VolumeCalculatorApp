package com.example.volumecalculatorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.volumecalculatorapp.enums.Unit;
import com.example.volumecalculatorapp.enums.VolumeUnit;

import java.util.Arrays;

public class SphereActivity extends AppCompatActivity {

    EditText radius;
    TextView title, result;
    Spinner radiusSpinner, volumeSpinner;
    Button clearButton, backButton;

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

            clearButton = findViewById(R.id.sphereClearButton);
            backButton = findViewById(R.id.sphereBackButton);

            radiusSpinner = findViewById(R.id.radiusUnitSpinner);
            volumeSpinner = findViewById(R.id.volumeUnitSpinner);

            radiusSpinner.setAdapter(spinnerRadiusValues(getApplicationContext()));
            volumeSpinner.setAdapter(spinnerVolumeValues(getApplicationContext()));

            title.setText(getString(R.string.sphere_volume_title));

            clearButton.setOnClickListener(v1 -> {
                result.setText("");
                radius.setText("");
            });

            backButton.setOnClickListener(v2 -> {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            });

            // Add listeners for live updates
            radius.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // No-op
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    calculateVolume();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // No-op
                }
            });

            radiusSpinner.setOnItemSelectedListener(new SimpleSpinnerListener(this::calculateVolume));
            volumeSpinner.setOnItemSelectedListener(new SimpleSpinnerListener(this::calculateVolume));

            return insets;
        });
    }

    private void calculateVolume() {
        try {
            String input = radius.getText().toString();
            if (!input.isEmpty()) {
                double r = Double.parseDouble(input);
                double volume = (4.0 / 3.0) * Math.PI * Math.pow(r, 3);

                String selectedRadiusUnit = (String) radiusSpinner.getSelectedItem();
                String selectedVolumeUnit = (String) volumeSpinner.getSelectedItem();

                if (selectedRadiusUnit.equals(Unit.MM.getAbbreviation())) {
                    volume /= 1_000_000_000; // mm³ to m³
                } else if (selectedRadiusUnit.equals(Unit.CM.getAbbreviation())) {
                    volume /= 1_000_000; // cm³ to m³
                } else if (selectedRadiusUnit.equals(Unit.KM.getAbbreviation())) {
                    volume *= 1_000_000_000; // km³ to m³
                }

                if (selectedVolumeUnit.equals(VolumeUnit.CM3.getAbbreviation())) {
                    volume *= 1_000_000; // m³ to cm³
                    result.setText("V = " + volume + " " + VolumeUnit.CM3.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.MM3.getAbbreviation())) {
                    volume *= 1_000_000_000; // m³ to mm³
                    result.setText("V = " + volume + " " + VolumeUnit.MM3.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.CUFT.getAbbreviation())) {
                    volume *= 35.3147; // m³ to cubic feet
                    result.setText("V = " + volume + " " + VolumeUnit.CUFT.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.CUYD.getAbbreviation())) {
                    volume *= 1.30795; // m³ to cubic yards
                    result.setText("V = " + volume + " " + VolumeUnit.CUYD.getAbbreviation());
                } else {
                    result.setText("V = " + volume + " " + VolumeUnit.M3.getAbbreviation());
                }
            } else {
                result.setText(R.string.result); // Reset if input is empty
            }
        } catch (NumberFormatException e) {
            enterValidNumberToast(this);
        }
    }

    private static void enterValidNumberToast(Context context) {
        Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show();
    }

    private static ArrayAdapter<String> spinnerRadiusValues(Context context) {
        String[] units = Arrays.stream(Unit.values()).map(Unit::getAbbreviation).toArray(String[]::new);
        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, units);
    }

    private static ArrayAdapter<String> spinnerVolumeValues(Context context) {
        String[] volumeUnits = Arrays.stream(VolumeUnit.values()).map(VolumeUnit::getAbbreviation).toArray(String[]::new);
        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, volumeUnits);
    }
}
