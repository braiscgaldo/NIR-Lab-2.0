package com.cgaldo.brais.sistema.View.Dialogs;

import android.content.Context;
import android.widget.TextView;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.InformationFragment.ConfigurationViewInformation;
import com.cgaldo.brais.sistema.View.Adapter.ProblemsListAdapter;
import com.cgaldo.brais.sistema.View.Adapter.ScanListAdapter;

public interface DialogsInterface {

    // Problems
    void createAddProblemDialog(final Context context);
    void createDialogDeleteProblem(final Context context, final String nameProblem, final TextView noProblemsTv, final ProblemsListAdapter problemsListAdapter);

    //Scans Saved
    void createDialogDeleteScan(final Context context, final String nameProblem, final String nameScan, final TextView noScansTv, final ScanListAdapter scansListAdapter);

    // Scans
    void createDialogProblems(final Context context);
    void createDialogLabels(final Context context, final String nameProblem, final String nameScan);

    // Configurations
    void createConfigurationDialog(final Context context, Handler handler);

    // Information
    void createInformationDialog(final Context context, String title, String text);

    // Edit
    void createEditLabelsDialog(final Context context, final String nameProblem, final String nameScan);

    // Models
    void createSelectModelDialog(final Context context, final String nameProblem, final Handler handler);
}
