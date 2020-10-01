package com.cgaldo.brais.sistema.View.Dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.Storage.Problem;
import com.cgaldo.brais.sistema.Controller.Storage.ProblemInterface;
import com.cgaldo.brais.sistema.Controller.Storage.ScanSelected;
import com.cgaldo.brais.sistema.Controller.Storage.TFLite.TFLiteController;
import com.cgaldo.brais.sistema.Controller.ViewsController.ProblemsController;
import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Broadcasts;
import com.cgaldo.brais.sistema.Model.StaticData.ObjectsRequired;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;
import com.cgaldo.brais.sistema.View.Adapter.ProblemsListAdapter;
import com.cgaldo.brais.sistema.View.Adapter.ScanListAdapter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Class for saving dialogs
 */

public class Dialogs implements DialogsInterface {
    private ProblemInterface problemInterface = Problem.getInstance();
    private ProblemsController problemsController = ProblemsController.getInstance();
    private ScanSelected scanSelected = ScanSelected.getInstance();
    private ConnectionsController connectionsController = BluetoothController.getInstance();

    private static Dialogs dialogs;
    private Dialogs(){

    }

    public static Dialogs getInstance(){
        if (dialogs == null){
            dialogs = new Dialogs();
        }
        return dialogs;
    }


    /*
     * Problems
     */
    // Add problem
    @Override
    public void createAddProblemDialog(final Context context){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);


        // Add a TextView here for the "Problem name" label
        final EditText nameBox = new EditText(context);
        nameBox.setHint("Problem Name");
        layout.addView(nameBox); // Notice this is an add method

        // Add a TextView here for the "Output" label
        final EditText outputBox = new EditText(context);
        outputBox.setHint("Output");
        layout.addView(outputBox); // Notice this is an add method

        // We add a button for adding labels
        Button addLabel = new Button(context);
        addLabel.setText("Add label");
        layout.addView(addLabel);

        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddLabelDialog(context, dialog);
            }
        });

        dialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (!nameBox.getText().toString().equals("") && !outputBox.getText().toString().equals("") && (nameBox.getText().toString().indexOf('.') == -1)) {
                        problemInterface.createProblem(nameBox.getText().toString(), outputBox.getText().toString(), problemsController.getTags());
                        problemsController.reset();
                    } else {
                        Toast toast = Toast.makeText(context.getApplicationContext(), "Error creating problem.", Toast.LENGTH_SHORT);

                        toast.show();
                        createAddProblemDialog(context);
                    }
                } catch (Exception e) {
                    // We return to put another dialog
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Error creating problem.", Toast.LENGTH_SHORT);

                    toast.show();
                    createAddProblemDialog(context);
                    Log.i("errorNumber", "numberTag is not a number");
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Only close
            }
        });


        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show(); // Again this is a set method
        alertDialog.setCanceledOnTouchOutside(false);
    }

    // Add a label to the problem
    private void createAddLabelDialog(final Context context, final AlertDialog.Builder dialog){
        final AlertDialog.Builder labelDialog = new AlertDialog.Builder(context);
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        dialog.setTitle("Add Label");

        // Name
        final EditText labelName = new EditText(context);
        labelName.setHint("Label Name");
        layout.addView(labelName);

        // Label type
        String[] typeLabel = {"Numerical", "Categorical"};

        labelDialog.setItems(typeLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogs, int which) {
                if (!labelName.getText().toString().equals("")){
                    List<String> type = new ArrayList<>();
                    if (which == 0){
                        type.add("Numerical");
                        problemsController.getTags().put(labelName.getText().toString(), type);
                        TextView labelView = new TextView(context);
                        labelView.setText(labelName.getText().toString());
                        layout.addView(labelView);
                    }else{
                        createCategoricalDialog(context, labelName.getText().toString());
                    }
                }else {
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Not created.", Toast.LENGTH_SHORT);

                    toast.show();
                    createAddLabelDialog(context, dialog);
                }
            }
        });

        labelDialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogs, int which) {
                if (labelName.getText().toString().equals("")){
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Not created.", Toast.LENGTH_SHORT);

                    toast.show();
                    createAddLabelDialog(context, dialog);
                }
            }
        });

        labelDialog.setView(layout);

        AlertDialog alertDialog = labelDialog.show(); // Again this is a set method
        alertDialog.setCanceledOnTouchOutside(false);
    }

    // Add category to the label
    private void createCategoricalDialog(final Context context, final String labelName){
        final List<String> categoricalList = new ArrayList<>();
        final AlertDialog.Builder categoricalDialog = new AlertDialog.Builder(context);
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final List<EditText> categoricalTypedList = new ArrayList<>();


        EditText category = new EditText(context);
        category.setHint("Category");
        categoricalTypedList.add(category);
        layout.addView(category);

        Button addCategory = new Button(context);
        addCategory.setText("Add Category");
        layout.addView(addCategory);

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addCategory = new EditText(context);
                addCategory.setHint("Category");
                categoricalTypedList.add(addCategory);
                layout.addView(addCategory, layout.getChildCount()-1);
                categoricalDialog.setView(layout);
            }
        });

        categoricalDialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean allTyped = true;
                for(EditText cat : categoricalTypedList){
                    if (cat.getText().toString().equals("")){
                        allTyped = false;
                        break;
                    }

                    categoricalList.add(cat.getText().toString());
                }

                if (allTyped){
                    problemsController.getTags().put(labelName, categoricalList);
                } else {
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Error creating category.", Toast.LENGTH_SHORT);

                    toast.show();
                    createCategoricalDialog(context, labelName);
                }

            }
        });

        categoricalDialog.setView(layout);

        AlertDialog alertDialog = categoricalDialog.show(); // Again this is a set method
        alertDialog.setCanceledOnTouchOutside(false);

    }

    // Dialog delete problem
    @Override
    public void createDialogDeleteProblem(final Context context, final String nameProblem, final TextView noProblemsTv, final ProblemsListAdapter problemsListAdapter){
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        deleteDialog.setTitle("Are you sure to delete " + nameProblem + " scan?");

        deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                problemInterface.removeProblem(nameProblem);
                prepareList(noProblemsTv, problemsListAdapter);
            }
        });

        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Only close
            }
        });

        AlertDialog alertDialog = deleteDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    // Prepare list
    private void prepareList(TextView noProblemsTv, ProblemsListAdapter problemsListAdapter){
        noProblemsTv.setVisibility(View.GONE);
        problemsListAdapter.updateList(problemInterface.obtainProblems());
        problemsListAdapter.notifyDataSetChanged();
    }

    // Scans saved
    // Prepare list
    private void prepareList(TextView noScansTv, ScanListAdapter scansListAdapter){
        noScansTv.setVisibility(View.GONE);
        scansListAdapter.updateList(problemInterface.obtainScans(ScanSelected.getInstance().getProblemSelectedString()));
        scansListAdapter.notifyDataSetChanged();
    }

    // Dialog delete
    @Override
    public void createDialogDeleteScan(final Context context, final String nameProblem, final String nameScan, final TextView noScansTv, final ScanListAdapter scansListAdapter){
        final AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);

        deleteDialog.setTitle("Are you sure to delete " + nameScan + " scan from " + nameProblem + "?");
        deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                problemInterface.removeScan(nameProblem, nameScan);
                prepareList(noScansTv, scansListAdapter);
            }
        });

        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Only close
            }
        });

        AlertDialog alertDialog = deleteDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    //Scans

    // See problems saved into the phone
    @Override
    public void createDialogProblems(final Context context){

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        final List<String> problems = problemInterface.obtainProblems();
        dialog.setItems(problems.toArray(new CharSequence[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanSelected.setProblemSelectedString(problems.get(which));
            }
        });

        dialog.create();

        AlertDialog alertDialog = dialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }


    // See labels of the problem saving the name of the scan
    @Override
    public void createDialogLabels(final Context context, final String nameProblem, final String nameScan){

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a TextView here for the "Problem name" label
        final EditText nameBox = new EditText(context);
        nameBox.setHint("Scan Name");
        nameBox.setText(nameScan);
        layout.addView(nameBox); // Notice this is an add method

        final String[] labelsText;

        // Add another TextView here for the "Number of tags" label
        final Map<String, List<String>> labels = problemInterface.takeLabels(nameProblem);
        labelsText = new String[labels.keySet().size()];
        int i = 0;
        for (String label : labels.keySet()){
            labelsText[i] = label;
            i++;
        }

        dialog.setItems(labelsText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogs, int which) {
                if (labels.get(labelsText[which]).get(0).equals("Numerical")){
                    createNumericalDialog(context, nameProblem, nameBox.getText().toString(), labelsText[which], true);
                }else{
                    createCategoricalDialog(context, nameProblem, nameBox.getText().toString(), labelsText[which], labels, true);
                }
            }
        });

        dialog.setPositiveButton("Add Scan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogs, int which) {
                try {
                    if (!nameBox.getText().toString().equals("") && (nameBox.getText().toString().indexOf('.') == -1) && (!problemInterface.existsScan(nameProblem, nameBox.getText().toString()))){
                        scanSelected.setScanSelectedString(nameBox.getText().toString());

                        problemInterface.addScan(scanSelected.getProblemSelectedString(), scanSelected.getScanSelectedString(), connectionsController.getInformation(), scanSelected.getLabelsMap());
                        String problem = scanSelected.getProblemSelectedString();
                        scanSelected.reset();
                        scanSelected.setProblemSelectedString(problem);
                    }
                }catch (Exception e){
                    // We return to put another dialog
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Error creating labels.", Toast.LENGTH_SHORT);

                    toast.show();
                    createDialogLabels(context, nameProblem, nameScan);
                    Log.i("errorNumber", "numberTag is not a number");
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Only close
            }
        });


        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show();

        alertDialog.setCanceledOnTouchOutside(false); // Again this is a set method
    }

    // Numerical dialog
    private void createNumericalDialog(final Context context, final String nameProblem, final String nameScan, final String label, final boolean addScan){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // EditText for numbers
        final EditText numericView = new EditText(context);
        numericView.setHint("Value");
        numericView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(numericView);

        dialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addScan) {
                    createDialogLabels(context, nameProblem, nameScan);
                }else{
                    createEditLabelsDialog(context, nameProblem, nameScan);
                }
                scanSelected.getLabelsMap().put(label, numericView.getText().toString());
                Toast toast = Toast.makeText(context.getApplicationContext(), "Data added.", Toast.LENGTH_SHORT);

                toast.show();
            }
        });


        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }

    //See all categories of the label
    private void createCategoricalDialog(final Context context, final String nameProblem, final String nameScan, final String label, Map<String, List<String>> possibleValues, final boolean addScan){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // List for characteristics
        List<String> valuesList = possibleValues.get(label);

        final String[] values = valuesList.toArray(new String[0]);

        dialog.setItems(values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Code
                Toast toast = Toast.makeText(context.getApplicationContext(), "Category selected.", Toast.LENGTH_SHORT);

                toast.show();
                if (addScan) {
                    createDialogLabels(context, nameProblem, nameScan);
                }else{
                    createEditLabelsDialog(context, nameProblem, nameScan);
                }
                scanSelected.getLabelsMap().put(label, values[which]);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createDialogLabels(context, nameProblem, nameScan);
            }
        });


        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

    }

    // Configurations
    @Override
    public void createConfigurationDialog(final Context context, Handler handler){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a TextView here for the "Configuration Name" field
        final TextView configurationName = new TextView(context);
        configurationName.setText("Configuration Name: " + new String((byte[])handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_CONF_NAME), StandardCharsets.UTF_8));
        configurationName.setTextSize(20);
        layout.addView(configurationName); // Notice this is an add method

        // Add a TextView here for the "Scan Type" field
        final TextView scanType = new TextView(context);
        scanType.setText("Scan Type: " + String.valueOf(handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SCAN_TYPE)));
        scanType.setTextSize(20);
        layout.addView(scanType); // Notice this is an add method

        // Add a TextView here for the "Scan Configuration Index" field
        final TextView scanConfigurationIndex = new TextView(context);
        scanConfigurationIndex.setText("Scan Configuration Index: " + String.valueOf(handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SCAN_CONF_INDEX)));
        scanConfigurationIndex.setTextSize(20);
        layout.addView(scanConfigurationIndex); // Notice this is an add method

        // Add a TextView here for the "Serial Number" field
        final TextView serialNumber = new TextView(context);
        serialNumber.setText("Serial Number: " + new String((byte[])handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SERIAL_NUMBER), StandardCharsets.UTF_8));
        serialNumber.setTextSize(20);
        layout.addView(serialNumber); // Notice this is an add method

        // Check configuration type:
        Boolean slewType = (Boolean) handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SLEW_TYPE);

        final TextView slewTypeBox = new TextView(context);
        slewTypeBox.setText("Slew Type: " + String.valueOf(slewType));
        slewTypeBox.setTextSize(20);
        layout.addView(slewTypeBox); // Notice this is an add method

        if (slewType){
            layout = createConfigurationSlewTypeDialog(context, handler, layout);
        }else{
            layout = createConfigurationNotSlewTypeDialog(context, handler, layout);
        }

        dialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Only close
            }
        });


        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show(); // Again this is a set method
        alertDialog.setCanceledOnTouchOutside(false);
    }

    // Information
    @Override
    public void createInformationDialog(Context context, String title, String text) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        dialog.setTitle(title);

        // Add a TextView here for the "Information" field
        final TextView information = new TextView(context);
        information.setText(text);
        information.setTextSize(20);
        layout.addView(information); // Notice this is an add method
        layout.setPadding(20, 20, 20, 20);

        dialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show(); // Again this is a set method
        alertDialog.setCanceledOnTouchOutside(false);
    }

    // Edit labels
    @Override
    public void createEditLabelsDialog(final Context context, final String nameProblem, final String nameScan) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        // Add a TextView here for the "Problem name" label
        final TextView nameBox = new TextView(context);
        nameBox.setHint("Scan Name");
        nameBox.setText(nameScan);
        nameBox.setTextSize(30);
        nameBox.setTextColor(Color.BLACK);
        nameBox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(nameBox); // Notice this is an add method

        final String[] labelsText;

        // Add another TextView here for the "Number of tags" label
        final Map<String, List<String>> labels = problemInterface.takeLabels(nameProblem);
        labelsText = new String[labels.keySet().size()];
        int i = 0;
        for (String label : labels.keySet()){
            labelsText[i] = label;
            i++;
        }

        dialog.setItems(labelsText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogs, int which) {
                if (labels.get(labelsText[which]).get(0).equals("Numerical")){
                    createNumericalDialog(context, nameProblem, nameBox.getText().toString(), labelsText[which], false);
                }else{
                    createCategoricalDialog(context, nameProblem, nameBox.getText().toString(), labelsText[which], labels, false);
                }
            }
        });

        dialog.setPositiveButton("Edit Scan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogs, int which) {
                try {

                    problemInterface.editScan(scanSelected.getProblemSelectedString(), scanSelected.getScanSelectedString(), connectionsController.getInformation(), scanSelected.getLabelsMap());
                    Intent intent = new Intent();
                    intent.setAction(Broadcasts.FINISH_EDIT_SCAN);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }catch (Exception e){
                    // We return to put another dialog
                    Toast toast = Toast.makeText(context.getApplicationContext(), "Error creating labels.", Toast.LENGTH_SHORT);

                    toast.show();
                    createEditLabelsDialog(context, nameProblem, nameScan);
                    Log.i("errorNumber", "numberTag is not a number");
                }
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Only close
            }
        });


        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show();

        alertDialog.setCanceledOnTouchOutside(false); // Again this is a set method

    }

    @Override
    public void createSelectModelDialog(final Context context, final String nameProblem, final Handler handler) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        List<String> models = TFLiteController.getInstance().getModels(nameProblem);
        final String[] values = models.toArray(new String[models.size()]);
        dialog.setItems(values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TFLiteController.getInstance().loadModel((AppCompatActivity) context, nameProblem, values[which]);
                String prediction = TFLiteController.getInstance().predict(handler);

                predictionsDialog(context, prediction);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do not do anything
            }
        });

        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void predictionsDialog(final Context context, final String prediction){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        final TextView predictionView = new TextView(context);
        predictionView.setTextSize(40);
        predictionView.setText(prediction);
        layout.addView(predictionView);

        dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        dialog.setView(layout);

        AlertDialog alertDialog = dialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public LinearLayout createConfigurationSlewTypeDialog(final Context context, Handler handler, LinearLayout layout){
        // Add a TextView here for the "Number Sections Name" field
        final TextView numberSections = new TextView(context);
        String number = "";
        number += String.valueOf(handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_NUM_SECTIONS)) + " ";
        numberSections.setText("Number Sections: " + number);
        numberSections.setTextSize(20);
        layout.addView(numberSections); // Notice this is an add method

        // Add a TextView here for the "Section Scan Types" field
        final TextView sectionScanTypes = new TextView(context);
        number = "";
        for(byte b:(byte[]) handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SECTION_SCAN_TYPES)){
            number += b + ", ";
        }
        sectionScanTypes.setText("Section Scan Types: " + number);
        sectionScanTypes.setTextSize(20);
        layout.addView(sectionScanTypes); // Notice this is an add method

        // Add a TextView here for the "Section Widths" field
        final TextView sectionWidths = new TextView(context);
        number = "";
        for(byte b:(byte[]) handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SECTION_WIDTHS)){
            number += b + ", ";
        }
        sectionWidths.setText("Section Widths: " + number);
        sectionWidths.setTextSize(20);
        layout.addView(sectionWidths); // Notice this is an add method

        // Add a TextView here for the "Section Wavelength Start" field
        final TextView sectionWavelengthStart = new TextView(context);
        number = "";
        for(int i:(int []) handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SECTION_WAVELENGTH_START_NM)){
            number += i + ", ";
        }
        sectionWavelengthStart.setText("Section Wavelength Start: " + number);
        sectionWavelengthStart.setTextSize(20);
        layout.addView(sectionWavelengthStart); // Notice this is an add method

        // Add a TextView here for the "Section Wavelength End" field
        final TextView sectionWavelengthEnd = new TextView(context);
        number = "";
        for(int i:(int []) handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SECTION_WAVELENGTH_END_NM)){
            number += i + ", ";
        }
        sectionWavelengthEnd.setText("Section Wavelength End: " +  number);
        sectionWavelengthEnd.setTextSize(20);
        layout.addView(sectionWavelengthEnd); // Notice this is an add method

        // Add a TextView here for the "Section Number Patterns" field
        final TextView sectionNumberPatterns = new TextView(context);
        number = "";
        for(int i:(int []) handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SECTION_NUM_PATTERNS)){
            number += i + ", ";
        }
        sectionNumberPatterns.setText("Section Number Patterns: " + number);
        sectionNumberPatterns.setTextSize(20);
        layout.addView(sectionNumberPatterns); // Notice this is an add method

        // Add a TextView here for the "Section Number Repeats" field
        final TextView sectionNumberRepeats = new TextView(context);
        number = "";
        for(int i:(int []) handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SECTION_NUM_REPEATS)){
            number += i + ", ";
        }
        sectionNumberRepeats.setText("Section Number Repeats: " + number);
        sectionNumberRepeats.setTextSize(20);
        layout.addView(sectionNumberRepeats); // Notice this is an add method

        // Add a TextView here for the "Section Exposure Time" field
        final TextView sectionExposureTime = new TextView(context);
        number = "";
        for(int i:(int []) handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_SECTION_EXPOSURE_TIME)){
            number += i + ", ";
        }
        sectionExposureTime.setText("Section Exposure Time: " + number);
        sectionExposureTime.setTextSize(20);
        layout.addView(sectionExposureTime); // Notice this is an add method

        return layout;
    }

    public LinearLayout createConfigurationNotSlewTypeDialog(final Context context, Handler handler, LinearLayout layout){
        // Add a TextView here for the "Widths" field
        final TextView widths = new TextView(context);
        widths.setText("Widths: " + String.valueOf(handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_WIDTH_PX)));
        widths.setTextSize(20);
        layout.addView(widths); // Notice this is an add method

        // Add a TextView here for the "Wavelength Start" field
        final TextView wavelengthStart = new TextView(context);
        wavelengthStart.setText("Wavelength Start: " + String.valueOf(handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_WAVELENGTH_START_NM)));
        wavelengthStart.setTextSize(20);
        layout.addView(wavelengthStart); // Notice this is an add method

        // Add a TextView here for the "Wavelength End" field
        final TextView wavelengthEnd = new TextView(context);
        wavelengthEnd.setText("Wavelength End: " + String.valueOf(handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_WAVELENGTH_END_NM)));
        wavelengthEnd.setTextSize(20);
        layout.addView(wavelengthEnd); // Notice this is an add method

        // Add a TextView here for the "Number Patterns" field
        final TextView numberPatterns = new TextView(context);
        numberPatterns.setText("Number Patterns: " + String.valueOf(handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_NUM_PATTERNS)));
        numberPatterns.setTextSize(20);
        layout.addView(numberPatterns); // Notice this is an add method

        // Add a TextView here for the "Number Repeats" field
        final TextView numberRepeats = new TextView(context);
        numberRepeats.setText("Section Number Repeats: " + String.valueOf(handler.handleGetRequest(Requests.DEVICE_SCAN_CONFIGURATION, ObjectsRequired.CONF_NUM_REPEATS)));
        numberRepeats.setTextSize(20);
        layout.addView(numberRepeats); // Notice this is an add method

        return layout;
    }


}
