package com.cgaldo.brais.sistema.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgaldo.brais.sistema.R;

import java.util.List;

public class LabelsListAdapter extends BaseAdapter implements ListAdapterInterface {
    private Context context;
    private static List<String> labels;
    private LayoutInflater layoutInflater;

    // Singleton
    private static LabelsListAdapter labelsListAdapter;

    private LabelsListAdapter(Context context, List<String> labelsList){
        this.context = context;
        labels = labelsList;
    }

    public static LabelsListAdapter getInstance(Context context, List<String> labelsList){
        if (labelsListAdapter == null){
            labelsListAdapter = new LabelsListAdapter(context, labelsList);
        }
        labels = labelsList;

        return labelsListAdapter;
    }

    public void updateList(List<String> labels){
        this.labels = labels;
        this.notifyDataSetChanged();
    }

    public void add(String label){
        this.labels.add(label);
        this.notifyDataSetChanged();
    }

    @Override
    public List<String> getProblemFiles() {
        return null;
    }

    public List<String> getScans(){
        return labels;
    }


    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView txtLabels;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = layoutInflater.inflate(R.layout.list_problem, parent, false);

        // Locate the TextViews in listview_item.xml
        txtLabels = itemView.findViewById(R.id.list_row_problem);

        // Capture position and set to the TextViews
        txtLabels.setText(labels.get(position));


        return itemView;

    }
}
