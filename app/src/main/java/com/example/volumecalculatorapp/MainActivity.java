package com.example.volumecalculatorapp;

import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.volumecalculatorapp.adapter.CustomAdapter;
import com.example.volumecalculatorapp.model.Shape;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 1 - AdapterView - the GridView
    GridView gridView;
    // 2 - Data Source - the ArrayList<Shape>
    ArrayList<Shape> shapesList;
    // 3 - Adapter - MyCustomAdapter
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            gridView = findViewById(R.id.gridView);
            shapesList = new ArrayList<>();

            Shape sphere = new Shape(R.drawable.sphere, "Sphere");
            Shape cylinder = new Shape(R.drawable.cylinder, "Cylinder");
            Shape cube = new Shape(R.drawable.cube, "Cube");
            Shape prism = new Shape(R.drawable.prism, "Prism");

            shapesList.add(sphere);
            shapesList.add(cylinder);
            shapesList.add(cube);
            shapesList.add(prism);

            customAdapter = new CustomAdapter(shapesList, getApplicationContext());

            gridView.setAdapter(customAdapter);
            //This setNumColumns can be done thru the .xml also!
            gridView.setNumColumns(2);
            return insets;
        });
    }
}