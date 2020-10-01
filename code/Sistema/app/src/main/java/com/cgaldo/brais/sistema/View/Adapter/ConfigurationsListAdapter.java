package com.cgaldo.brais.sistema.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgaldo.brais.sistema.R;

import java.util.List;

public class ConfigurationsListAdapter extends BaseAdapter implements ListAdapterInterface {
    private Context context;
    private List<String> configurations;
    private LayoutInflater layoutInflater;

    // Singleton
    private static ConfigurationsListAdapter configurationsListAdapter;

    private ConfigurationsListAdapter(Context context, List<String> configurations){
        this.context = context;
        this.configurations = configurations;
    }

    public static ConfigurationsListAdapter getInstance(Context context, List<String> configurations){
        if (configurationsListAdapter == null){
            configurationsListAdapter = new ConfigurationsListAdapter(context, configurations);
        }

        return configurationsListAdapter;
    }

    public void updateList(List<String> configurations){
        this.configurations = configurations;
        this.notifyDataSetChanged();
    }

    public void add(String configuration){
        this.configurations.add(configuration);
        this.notifyDataSetChanged();
    }

    @Override
    public List<String> getProblemFiles() {
        return null;
    }

    public List<String> getConfigurations(){
        return configurations;
    }

    @Override
    public List<String> getScans() {
        return null;
    }


    @Override
    public int getCount() {
        return configurations.size();
    }

    @Override
    public Object getItem(int position) {
        return configurations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView txtProblem;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = layoutInflater.inflate(R.layout.list_problem, parent, false);

        // Locate the TextViews in listview_item.xml
        txtProblem = itemView.findViewById(R.id.list_row_problem);

        // Capture position and set to the TextViews
        txtProblem.setText(configurations.get(position));


        return itemView;

    }
}
