package com.cgaldo.brais.sistema.Controller.Storage;

import android.os.Environment;
import android.util.Log;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.ObjectsRequired;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;
import com.cgaldo.brais.sistema.Model.StaticData.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Problem implements ProblemInterface{

    // Singleton
    private static Problem problem;

    private Problem(){

    }

    public static Problem getInstance(){
        if (problem == null){
            problem = new Problem();
        }

        return problem;
    }

    @Override
    public boolean canReadAndWrite() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);

    }

    @Override
    public boolean existsProblems() {
        File problemsDirectory = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY);
        for(File file : problemsDirectory.listFiles()){
            if (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('/') + 1).endsWith(".json")){
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> obtainProblems() {
        File problemsDirectory = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY);
        List<String> problems = new ArrayList<>();
        for(File file : problemsDirectory.listFiles()){
            if (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('/')).endsWith(".json")){
                problems.add(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('/') + 1, file.getAbsolutePath().indexOf(".json")));
            }
        }
        return problems;
    }

    @Override
    public File createProblem(String nameProblem, String output, Map<String, List<String>> labels) {
        File problemFile = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY + '/' + nameProblem + ".json");

        if (problemFile.exists()){
            return null;
        }

        try {
            if (!problemFile.createNewFile()){
                Log.i("error file", "unable to create file");
                return null;
            }
        } catch (IOException e) {
            Log.i("errorFile", "unable to create file " + nameProblem);
            return null;
        }

        JSONObject jsonObject = new JSONObject();

        // We treat tags
        JSONObject jsonLabels = new JSONObject();

        try{
            for (String label: labels.keySet()){
                jsonLabels.put(label, new JSONArray(labels.get(label).toArray()));
            }
        } catch (Exception e){
            Log.i("errorJSON", "JSON error");
        }

        try {
            jsonObject.put("Output", output);
            jsonObject.put("Labels", jsonLabels);
        } catch (JSONException e) {
            Log.i("errorJson", "cannot create JSON");
            return null;
        }

        if (!writeFile(problemFile, jsonObject)){
            return null;
        }

        File modelDir = new File(Environment.getExternalStorageDirectory(), "Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.MODELS_DIRECTORY + '/' + nameProblem);
        if (!modelDir.mkdir()){
            return null;
        }

        return problemFile;
    }

    @Override
    public boolean testProlem(String nameProblem) {
        JSONObject jsonObject = firstTest(nameProblem);
        if (jsonObject == null){
            return false;
        }

        // We try to read it
        try {
            jsonObject.get("Labels");
            jsonObject.get("Output");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("error reading", "bad labels");
            return false;
        }

        // If file pass all tests, it is valid
        return true;
    }

    @Override
    public boolean removeProblem(String nameProblem) {
        File problemFile = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY + '/' + nameProblem + ".json");

        // First, we test that the file exists
        if (!problemFile.exists()){
            Log.i("errorfile", "file not exists");
            return true;
        }

        if (!problemFile.delete()){
            return false;
        }

        File modelDir = new File(Environment.getExternalStorageDirectory(), "Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.MODELS_DIRECTORY);
        if (!modelDir.exists()){
            Log.i("errorfile", "file not exists");
            return true;
        }

        if (!modelDir.delete()){
            return false;
        }

        return false;

    }

    @Override
    public boolean addScan(String nameProblem, String nameScan, Handler data, Map<String, String> labelsValue) {
        File problemFile = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY + '/' + nameProblem + ".json");

        JSONObject jsonObject = firstTest(nameProblem);
        if (jsonObject == null){
            return false;
        }

        JSONObject parametersScan = new JSONObject();

        // We try to read it
        try {
            JSONObject jsonScan = new JSONObject();
            List<JSONArray> jsonArrays = getArrays(data);
            JSONObject labels = getLabels(labelsValue);
            parametersScan = putParameters(parametersScan, jsonArrays, labels);
            jsonScan.put(nameScan, parametersScan);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("error reading", "bad labels");
            return false;
        }

        try {
            jsonObject.put(nameScan, parametersScan);
        } catch (JSONException e) {
            Log.i("errorWriting", "error introducing a new scan");
            return false;
        }

        return writeFile(problemFile, jsonObject);
    }

    @Override
    public boolean testScan(String nameProblem, String nameScan) {
        JSONObject jsonObject = firstTest(nameProblem);
        if (jsonObject == null){
            return false;
        }

        Map<String, List<String>> labels = takeLabels(jsonObject);
        JSONObject scan;
        // We try to read it
        try {
            // We test data
            scan = (JSONObject) jsonObject.get(nameScan);
            JSONArray absorbance = (JSONArray) scan.get("Absorbance");
            JSONArray intensity = (JSONArray) scan.get("Intensity");
            JSONArray reflectance = (JSONArray) scan.get("Reflectance");
            if (absorbance.length() == 0 || intensity.length() == 0 || reflectance.length() == 0){
                Log.i("errorData", "data not exists");
                return false;
            }
            scan = (JSONObject) scan.get("Labels");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("error reading", "bad labels");
            return false;
        }

        // We test if labels exists
        for (String label: labels.keySet()){
            try {
                scan.get(label);
            } catch (JSONException e) {
                Log.i("errorLabel", "label does not exists");
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean removeScan(String nameProblem, String nameScan) {
        File problemFile = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY + '/' + nameProblem + ".json");

        // First, we test that the file exists
        if (!problemFile.exists()){
            Log.i("errorfile", "file not exists");
            return false;
        }

        // Then, we read data
        StringBuilder bufferFile = new StringBuilder();
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new FileReader(problemFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                bufferFile.append(line);
            }
        }catch (Exception e){
            return false;
        }

        // We take it into a json object
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(bufferFile.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("errorfile", "invalidFile");
            return false;
        }

        JSONObject finalJson = new JSONObject();

        for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
            String key = it.next();
            if (!key.equals(nameScan)){
                try {
                    finalJson.put(key, jsonObject.get(key));
                } catch (JSONException e) {
                    Log.i("errorkey", "errorRemovingScan");
                    return false;
                }
            }
        }


        return writeFile(problemFile, finalJson);
    }

    @Override
    public boolean existsScan(String nameProblem, String nameScan) {
        JSONObject problem = firstTest(nameProblem);

        try {
            problem.get(nameScan);
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    @Override
    public List<String> obtainScans(String nameProblem) {
        JSONObject problem = firstTest(nameProblem);
        List<String> scans = new ArrayList<>();

        try {
            for (Iterator<String> it = problem.keys(); it.hasNext(); ) {
                String nameScan = it.next();
                if (!nameScan.equals("Labels") && !nameScan.equals("Output")) {
                    scans.add(nameScan);
                }
            }
        }catch (Exception e){
            return null;
        }

        return scans;
    }

    @Override
    public boolean putScan(String nameProblem, String nameScan, Handler handler) {
        JSONObject jsonObject = firstTest(nameProblem);
        JSONObject scan;

        try {
            scan = (JSONObject) jsonObject.get(nameScan);
        } catch (JSONException e) {
            return false;
        }

        List<Float> intensity = new ArrayList<>();
        List<Float> absorbance = new ArrayList<>();
        List<Float> reflectance = new ArrayList<>();
        List<Double> wavelength = new ArrayList<>();
        try {

            // We get data
            JSONArray intensityArray = (JSONArray) scan.get("Intensity");
            JSONArray absorbanceArray = (JSONArray) scan.get("Absorbance");
            JSONArray reflectanceArray = (JSONArray) scan.get("Reflectance");
            JSONArray wavelengthArray = (JSONArray) scan.get("Wavelength");

            for (int i = 0; i <wavelengthArray.length(); i++){
                intensity.add(Float.valueOf(String.valueOf(intensityArray.get(i))));
                absorbance.add(Float.valueOf(String.valueOf(absorbanceArray.get(i))));
                reflectance.add(Float.valueOf(String.valueOf(reflectanceArray.get(i))));
                wavelength.add(Double.valueOf(String.valueOf(wavelengthArray.get(i))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // We put data into object
        handler.handleSetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_INTENSITY, intensity);
        handler.handleSetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_ABSORBANCE, absorbance);
        handler.handleSetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_REFLECTANCE, reflectance);
        handler.handleSetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_WAVELENGTH, wavelength);
        handler.handleSetRequest(Requests.DEVICE_SCAN, ObjectsRequired.SCAN_SCAN_SIZE, wavelength.size());

        return true;
    }

    @Override
    public Map<String, List<String>> takeLabels(String nameProblem){
        Map<String, List<String>> labels;

        File problemFile = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY + '/' + nameProblem + ".json");

        // First, we test that the file exists
        if (!problemFile.exists()){
            Log.i("errorfile", "file not exists");
            return null;
        }

        // Then, we read data
        StringBuilder bufferFile = new StringBuilder();
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new FileReader(problemFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                bufferFile.append(line);
            }
        }catch (Exception e){
            return null;
        }

        // We take it into a json object
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(bufferFile.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("errorfile", "invalidFile");
            return null;
        }

        labels = takeLabels(jsonObject);

        return labels;
    }

    @Override
    public Map<String, String> takeLabelsFromProblems(String nameProblem, String nameScan) {
        JSONObject jsonObject = firstTest(nameProblem);
        Map<String, String> labels = new HashMap<>();

        JSONObject scan;

        try {
            scan = (JSONObject) jsonObject.get(nameScan);
        } catch (JSONException e) {
            return labels;
        }

        JSONObject labelsSaved;

        try {
            labels.put("Hour", scan.get("Hour").toString());
            labelsSaved = (JSONObject) scan.get("Labels");

            for (Iterator<String> it = labelsSaved.keys(); it.hasNext(); ) {
                String s = it.next();

                labels.put(s, labelsSaved.get(s).toString());

            }
        } catch (JSONException e){
            return labels;
        }


        return labels;
    }

    @Override
    public boolean editScan(String nameProblem, String nameScan, Handler data, Map<String, String> labelsValue){
        if (!removeScan(nameProblem, nameScan)){
            return false;
        }

        if (!addScan(nameProblem, nameScan, data, labelsValue)){
            return false;
        }
        
        return true;
    }

    private List<JSONArray> getArrays(Handler handler){
        List<Float> intensityList = (List<Float>) handler.handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_INTENSITY);
        JSONArray intensity = new JSONArray(intensityList);
        List<Float> absorbanceList = (List<Float>) handler.handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_ABSORBANCE);

        for (int i = 0; i<absorbanceList.size(); i++){
            if (Float.isNaN(absorbanceList.get(i)))
                absorbanceList.set(i, new Float(0));
        }

        JSONArray absorbance = new JSONArray(absorbanceList);
        List<Float> reflectanceList = (List<Float>) handler.handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_REFLECTANCE);
        JSONArray reflectance = new JSONArray(reflectanceList);
        List<Double> wavelengthList = (List<Double>) handler.handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_WAVELENGTH);
        JSONArray wavelength = new JSONArray(wavelengthList);
        List <JSONArray> arrays = new ArrayList<>();
        arrays.add(intensity);
        arrays.add(absorbance);
        arrays.add(reflectance);
        arrays.add(wavelength);
        return arrays;
    }

    private JSONObject getLabels(Map<String, String> valueLabels){
        JSONObject labels = new JSONObject();

        try {
            for (String key: valueLabels.keySet()) {
                labels.put(key, valueLabels.get(key));
            }
        }catch (Exception e){
            Log.e("errorLabel", "errorLabels");
        }

        return labels;
    }

    private Map<String, List<String>> takeLabels(JSONObject problem){
        Map<String, List<String>> labelMap = new HashMap<>();

        try {
            JSONObject jsonLabels = (JSONObject) problem.get("Labels");

            for (Iterator<String> it = jsonLabels.keys(); it.hasNext(); ) {
                String field = it.next();
                JSONArray values = (JSONArray) jsonLabels.get(field);
                ArrayList<String> valueLabels = new ArrayList<>();
                for (int i = 0; i < values.length(); i++){
                    valueLabels.add((String) values.get(i));
                }

                labelMap.put(field, valueLabels);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return labelMap;
    }

    private JSONObject putParameters(JSONObject parametersScan, List<JSONArray> jsonArrays, JSONObject labels){
        try {

            parametersScan.put("Intensity", jsonArrays.get(0));
            parametersScan.put("Absorbance", jsonArrays.get(1));
            parametersScan.put("Reflectance", jsonArrays.get(2));
            parametersScan.put("Wavelength", jsonArrays.get(3));
            parametersScan.put("Hour", Calendar.getInstance().getTime().toString());
            parametersScan.put("Labels", labels);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parametersScan;

    }

    private boolean writeFile(File problemFile, JSONObject jsonObject){
        OutputStreamWriter outputStreamWriter;

        try {
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(problemFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("error file","cannot create file");
            return false;
        }

        try {
            outputStreamWriter.write(jsonObject.toString());
        } catch (IOException e) {
            Log.i("error file", "cannot write into file");
            return false;
        }

        try {
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.i("errorFile", "error closing file");
            return false;
        }
        return true;
    }

    private JSONObject firstTest(String nameProblem){
        File problemFile = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.PROBLEMS_DIRECTORY + '/' + nameProblem + ".json");

        // First, we test that the file exists
        if (!problemFile.exists()){
            Log.i("errorfile", "file not exists");
            return null;
        }

        // Then, we read data
        StringBuilder bufferFile = new StringBuilder();
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new FileReader(problemFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                bufferFile.append(line);
            }
        }catch (Exception e){
            return null;
        }

        // We take it into a json object
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(bufferFile.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("errorfile", "invalidFile");
            return null;
        }
        return jsonObject;
    }
}
