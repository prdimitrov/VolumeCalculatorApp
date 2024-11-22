package com.example.volumecalculatorapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.DecimalFormat;
import java.util.Arrays;

public class SphereActivity extends AppCompatActivity {

    EditText radius;
    TextView title, result;
    Spinner unitSpinner, volumeUnitSpinner;
    Button clearButton, backButton, copyButton;
    ClipboardManager clipboardManager;
    private String lastRadiusInput = ""; // the last entered radius

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
            copyButton = findViewById(R.id.sphereCopyButton);

            unitSpinner = findViewById(R.id.sphereRadiusUnitSpinner);
            volumeUnitSpinner = findViewById(R.id.sphereVolumeUnitSpinner);

            unitSpinner.setAdapter(spinnerRadiusValues(getApplicationContext()));
            volumeUnitSpinner.setAdapter(spinnerVolumeValues(getApplicationContext()));

            title.setText(getString(R.string.sphere_volume_title));

            clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            clearButton.setOnClickListener(v1 -> {
                result.setText(R.string.zero);
                radius.setText("");
                lastRadiusInput = "";
                Toast.makeText(getApplicationContext(), "Fields cleared!", Toast.LENGTH_SHORT).show();
            });

            copyButton.setOnClickListener(v3 -> {
                String textToCopy = result.getText().toString();  // Get the text from the result TextView
                if (!textToCopy.isEmpty()) {
                    ClipData clip = ClipData.newPlainText("Sphere Volume Result", textToCopy);
                    clipboardManager.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to clipboard!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Nothing to copy", Toast.LENGTH_SHORT).show();
                }
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
                    String newText = s.toString().trim();
                    // Calculate if the input value changes only
                    if (!newText.equals(lastRadiusInput)) {
                        lastRadiusInput = newText;
                        calculateSphereVolume();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // No-op
                }
            });

            unitSpinner.setOnItemSelectedListener(new SimpleSpinnerListener(this::calculateSphereVolume));
            volumeUnitSpinner.setOnItemSelectedListener(new SimpleSpinnerListener(this::calculateSphereVolume));

            return insets;
        });
    }

    private void calculateSphereVolume() {
        try {
            String radiusInput = radius.getText().toString();
            if (!radiusInput.isEmpty()) {
                double volume = calculateSphereVolume(radiusInput);

                String selectedRadiusUnit = (String) unitSpinner.getSelectedItem();
                String selectedVolumeUnit = (String) volumeUnitSpinner.getSelectedItem();

                if (selectedRadiusUnit.equals(Unit.MM.getAbbreviation())) {
                    volume /= 1_000_000_000; // mm³ to m³
                } else if (selectedRadiusUnit.equals(Unit.CM.getAbbreviation())) {
                    volume /= 1_000_000; // cm³ to m³
                } else if (selectedRadiusUnit.equals(Unit.KM.getAbbreviation())) {
                    volume *= 1_000_000_000; // km³ to m³
                }

                DecimalFormat df = new DecimalFormat("#,##0.00");

                if (selectedVolumeUnit.equals(VolumeUnit.CM3.getAbbreviation())) {
                    volume *= 1_000_000; // m³ to cm³
                    result.setText("V = " + df.format(volume) + " " + VolumeUnit.CM3.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.MM3.getAbbreviation())) {
                    volume *= 1_000_000_000; // m³ to mm³
                    result.setText("V = " + df.format(volume) + " " + VolumeUnit.MM3.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.CUFT.getAbbreviation())) {
                    volume *= 35.3147; // m³ to cubic feet
                    result.setText("V = " + df.format(volume) + " " + VolumeUnit.CUFT.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.CUIN.getAbbreviation())) {
                    volume *= 61023.7441; // m³ to cubic inches
                    result.setText("V = " + df.format(volume) + " " + VolumeUnit.CUIN.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.CUYD.getAbbreviation())) {
                    volume *= 1.30795; // m³ to cubic yards
                    result.setText("V = " + df.format(volume) + " " + VolumeUnit.CUYD.getAbbreviation());
                } else {
                    result.setText("V = " + df.format(volume) + " " + VolumeUnit.M3.getAbbreviation());
                }
            } else {
                result.setText(R.string.zero); // Reset if input is empty
            }
        } catch (NumberFormatException e) {
            enterValidNumberToast(this);
        }
    }

    private static double calculateSphereVolume(String radiusInput) {
        // Formula - V = (4/3) x π x r^3
        double r = Double.parseDouble(radiusInput);
        double volume = (4.0 / 3.0) * Math.PI * Math.pow(r, 3);
        return volume;
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
