package com.cgaldo.brais.sistema.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgaldo.brais.sistema.R;

import java.util.List;

public class ScanListAdapter extends BaseAdapter implements ListAdapterInterface {

    private Context context;
    private List<String> scans;
    private LayoutInflater layoutInflater;

    // Singleton
    private static ScanListAdapter scansListAdapter;

    private ScanListAdapter(Context context, List<String> scans){
        this.context = context;
        this.scans = scans;
    }

    public static ScanListAdapter getInstance(Context context, List<String> scans){
        if (scansListAdapter == null){
            scansListAdapter = new ScanListAdapter(context, scans);
        }

        return scansListAdapter;
    }

    public void updateList(List<String> scans){
        this.scans = scans;
        this.notifyDataSetChanged();
    }

    public void add(String scan){
        this.scans.add(scan);
        this.notifyDataSetChanged();
    }

    @Override
    public List<String> getProblemFiles() {
        return null;
    }

    public List<String> getScans(){
        return scans;
    }


    @Override
    public int getCount() {
        return scans.size();
    }

    @Override
    public Object getItem(int position) {
        return scans.get(position);
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
        txtProblem.setText(scans.get(position));


        return itemView;

    }
}
