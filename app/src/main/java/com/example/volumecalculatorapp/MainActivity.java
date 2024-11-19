package com.example.volumecalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

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

            gridView.setOnItemClickListener((parent, view, position, id) -> {
                //We navigate with Intent
//                Intent intent = new Intent(this, Sphere.class);
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), SphereActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), CylinderActivity.class);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), CubeActivity.class);
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(), PrismActivity.class);
                        break;
                    default:
                        Toast.makeText(this, "Choose something", Toast.LENGTH_SHORT).show();
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            });
            return insets;
        });
    }
}