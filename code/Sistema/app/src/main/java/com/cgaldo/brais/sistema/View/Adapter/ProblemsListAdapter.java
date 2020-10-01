package com.cgaldo.brais.sistema.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgaldo.brais.sistema.R;

import java.util.List;

public class ProblemsListAdapter extends BaseAdapter implements ListAdapterInterface {

    private Context context;
    private List<String> problemFiles;
    private LayoutInflater layoutInflater;

    // Singleton
    private static ProblemsListAdapter problemsListAdapter;

    private ProblemsListAdapter(Context context, List<String> problemFiles){
        this.context = context;
        this.problemFiles = problemFiles;
    }

    public static ProblemsListAdapter getInstance(Context context, List<String> problemFiles){
        if (problemsListAdapter == null){
            problemsListAdapter = new ProblemsListAdapter(context, problemFiles);
        }

        return problemsListAdapter;
    }

    public void updateList(List<String> problemFiles){
        this.problemFiles = problemFiles;
        this.notifyDataSetChanged();
    }

    public void add(String problem){
        this.problemFiles.add(problem);
        this.notifyDataSetChanged();
    }

    public List<String> getProblemFiles(){
        return problemFiles;
    }

    @Override
    public List<String> getScans() {
        return null;
    }


    @Override
    public int getCount() {
        return problemFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return problemFiles.get(position);
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
        txtProblem.setText(problemFiles.get(position));


        return itemView;

    }
}
