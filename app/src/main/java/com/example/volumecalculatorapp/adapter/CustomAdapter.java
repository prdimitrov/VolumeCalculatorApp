package com.example.volumecalculatorapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.volumecalculatorapp.R;
import com.example.volumecalculatorapp.model.Shape;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Shape> {
    private ArrayList<Shape> shapeList;
    private Context context;

    public CustomAdapter(ArrayList<Shape> shapeList, Context context) {
        super(context, R.layout.grid_item_layout, shapeList);
        this.shapeList = shapeList;
        this.context = context;
    }

    //View Holder: Used to cache references to the views within an item layout
    private static class MyViewHolder {
        TextView shapeName;
        ImageView shapeImage;
    }

    //GetView(): Used to create and return a view for a specific item in the Grid

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //1 - Get the shape object for the current position
        Shape shape = getItem(position);

        //2 - Inflating the Layout:
        MyViewHolder myViewHolder;

        if (convertView == null) {
            //No view went off-screen -> Create a new view
            myViewHolder = new MyViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(
                    R.layout.grid_item_layout,
                    parent,
                    false
            );

            //Finding the views
            myViewHolder.shapeName = (TextView) convertView.findViewById(R.id.textView);
            myViewHolder.shapeImage = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(myViewHolder);



        } else {
            //A view went off-screen -> Re-use it
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        //Get data from the model class (Shape)
        myViewHolder.shapeName.setText(shape.getShapeName());
        myViewHolder.shapeImage.setImageResource(shape.getShapeImage());

        return convertView;
        // return super.getView(position, convertView, parent);
    }
}
