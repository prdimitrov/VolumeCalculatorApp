package com.example.volumecalculatorapp.activity;

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

import com.example.volumecalculatorapp.MainActivity;
import com.example.volumecalculatorapp.R;
import com.example.volumecalculatorapp.enums.Unit;
import com.example.volumecalculatorapp.enums.VolumeUnit;
import com.example.volumecalculatorapp.listener.SimpleSpinnerListener;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

public class SphereActivity extends AppCompatActivity {

    EditText radius;
    TextView title, result;
    Spinner unitSpinner, volumeUnitSpinner;
    Button clearButton, backButton, copyButton;
    ClipboardManager clipboardManager;
    private String lastRadiusInput = ""; // The last entered radius
    private boolean isSpinnerInteraction = false; // To track if the spinner interacted with
    private int lastUnitSpinnerPosition = 0; // Store last spinner position for chosen units
    private int lastVolumeSpinnerPosition = 0; // Store last spinner position for volume units

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
            updateLive(radius, lastRadiusInput, isSpinnerInteraction);

            // Set initial values to avoid resetting spinners
            unitSpinner.setSelection(lastUnitSpinnerPosition, false); // Avoid triggering recalculation
            volumeUnitSpinner.setSelection(lastVolumeSpinnerPosition, false);

            // Track spinner selection and recalculate on change
            unitSpinner.setOnItemSelectedListener(new SimpleSpinnerListener(() -> {
                isSpinnerInteraction = true;
                if (unitSpinner.getSelectedItemPosition() != lastUnitSpinnerPosition) {
                    lastUnitSpinnerPosition = unitSpinner.getSelectedItemPosition();
                    calculateVolume();
                }
                isSpinnerInteraction = false;
            }));

            volumeUnitSpinner.setOnItemSelectedListener(new SimpleSpinnerListener(() -> {
                isSpinnerInteraction = true;
                if (volumeUnitSpinner.getSelectedItemPosition() != lastVolumeSpinnerPosition) {
                    lastVolumeSpinnerPosition = volumeUnitSpinner.getSelectedItemPosition();
                    calculateVolume();
                }
                isSpinnerInteraction = false;
            }));

            return insets;
        });
    }

    private void calculateVolume() {
        try {
            String radiusInput = radius.getText().toString();
            if (!radiusInput.isEmpty()) {
                BigDecimal volume = calculateSphereVolume(radiusInput);

                String selectedRadiusUnit = (String) unitSpinner.getSelectedItem();
                String selectedVolumeUnit = (String) volumeUnitSpinner.getSelectedItem();

                // Convert volume to m³ based on the selected radius unit
                if (selectedRadiusUnit.equals(Unit.MM.getAbbreviation())) {
                    volume = volume.divide(new BigDecimal("1000000000"), RoundingMode.HALF_UP); // mm³ to m³
                } else if (selectedRadiusUnit.equals(Unit.CM.getAbbreviation())) {
                    volume = volume.divide(new BigDecimal("1000000"), RoundingMode.HALF_UP); // cm³ to m³
                } else if (selectedRadiusUnit.equals(Unit.KM.getAbbreviation())) {
                    volume = volume.multiply(new BigDecimal("1000000000")); // km³ to m³
                }

                // Adjust the volume based on selected output unit
                if (selectedVolumeUnit.equals(VolumeUnit.CM3.getAbbreviation())) {
                    volume = volume.multiply(new BigDecimal("1000000")); // m³ to cm³
                    result.setText("V = " + volume.toPlainString() + " " + VolumeUnit.CM3.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.MM3.getAbbreviation())) {
                    volume = volume.multiply(new BigDecimal("1000000000")); // m³ to mm³
                    result.setText("V = " + volume.toPlainString() + " " + VolumeUnit.MM3.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.CUFT.getAbbreviation())) {
                    volume = volume.multiply(new BigDecimal("35.3147")); // m³ to cubic feet
                    result.setText("V = " + volume.toPlainString() + " " + VolumeUnit.CUFT.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.CUIN.getAbbreviation())) {
                    volume = volume.multiply(new BigDecimal("61023.7441")); // m³ to cubic inches
                    result.setText("V = " + volume.toPlainString() + " " + VolumeUnit.CUIN.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.CUYD.getAbbreviation())) {
                    volume = volume.multiply(new BigDecimal("1.30795")); // m³ to cubic yards
                    result.setText("V = " + volume.toPlainString() + " " + VolumeUnit.CUYD.getAbbreviation());
                } else if (selectedVolumeUnit.equals(VolumeUnit.L.getAbbreviation())) {
                    volume = volume.multiply(new BigDecimal("1000")); // m³ to liters
                    result.setText("V = " + volume.toPlainString() + " " + VolumeUnit.L.getAbbreviation());
                } else {
                    result.setText("V = " + volume.toPlainString() + " " + VolumeUnit.M3.getAbbreviation()); // m³
                }
            } else {
                result.setText(R.string.zero); // Reset if input is empty
            }
        } catch (NumberFormatException e) {
            enterValidNumberToast(this);
        }
    }

    private static BigDecimal calculateSphereVolume(String radiusInput) {
        BigDecimal r = new BigDecimal(radiusInput);

        BigDecimal pi = new BigDecimal(Math.PI, MathContext.DECIMAL128);

        BigDecimal fourThirds = new BigDecimal("4").divide(new BigDecimal("3"), MathContext.DECIMAL128);

        BigDecimal rCubed = r.multiply(r).multiply(r);

        // Formula: V = (4/3) * π * r³
        BigDecimal volume = fourThirds.multiply(pi).multiply(rCubed);

        return volume.setScale(20, RoundingMode.HALF_UP);
    }

    private void updateLive(TextView type, String inputType, boolean spinnerInteraction) {
        type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!spinnerInteraction) {
                    String newText = s.toString().trim();
                    // Calculate if the input value changes only
                    if (!newText.equals(inputType)) {
                        calculateVolume();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No-op
            }
        });
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
