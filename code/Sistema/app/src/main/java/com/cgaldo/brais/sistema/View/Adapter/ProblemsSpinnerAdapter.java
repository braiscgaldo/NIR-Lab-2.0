package com.cgaldo.brais.sistema.View.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cgaldo.brais.sistema.R;

public class ProblemsSpinnerAdapter extends ArrayAdapter<String> {

    private Activity context;
    String[] data = null;

    public ProblemsSpinnerAdapter(Activity context, int resource, String[] data2)
    {
        super(context, resource, data2);
        this.context = context;
        this.data = data2;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if(row == null)
        {
            //inflate your customlayout for the textview
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.fragment_scan, parent, false);
        }
        //put the data in it
        String item = data[position];
        if(item != null)
        {
            TextView text1 = row.findViewById(R.id.list_row_problem);
            text1.setTextColor(Color.BLACK);
            text1.setText(item);
        }

        return row;
    }
}
