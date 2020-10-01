package com.cgaldo.brais.sistema.Controller.Storage.TFLite;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.ObjectsRequired;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;
import com.cgaldo.brais.sistema.Model.StaticData.Storage;

import org.tensorflow.lite.Tensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TFLiteController extends TFLiteControllerAbstract {
    private static TFLiteController tfLiteController;
    private TFLiteController(){
        this.labels = new ArrayList<>();
    }

    public static TFLiteController getInstance(){
        if (tfLiteController == null){
            tfLiteController = new TFLiteController();
        }

        return tfLiteController;
    }

    private boolean loadLabels(String nameProblem){
        // Load labels from labels.txt file
        File labelsFile = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.MODELS_DIRECTORY + '/' + nameProblem + "/" + "labels.txt");

        String label;
        FileReader f;
        try {
            f = new FileReader(labelsFile);

            BufferedReader b = new BufferedReader(f);
            while((label = b.readLine())!=null) {
                this.labels.add(label);
            }
            b.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<String> getModels(String nameProblem){
        List<String> models = new ArrayList<>();
        File modelsFile = new File(Environment.getExternalStorageDirectory(),"Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.MODELS_DIRECTORY + '/' + nameProblem);
        if (!modelsFile.isDirectory()){
            return null;
        }

        for(String model : modelsFile.list()){
            if(model.endsWith(".tflite")){
                models.add(model);
            }
        }

        return models;
    }

    @Override
    public boolean loadModel(AppCompatActivity activity, String nameProblem, String modelName) {
        if (!super.loadModel(activity, nameProblem, modelName)){
            // Error loading model
            return false;
        }

        if(!loadLabels(nameProblem)){
            // Error loading labels
            return false;
        }

        return true;
    }

    @Override
    public String predict(Handler handler) {
        List<Integer> intensityList = (List<Integer>) handler.handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_INTENSITY);
        List<Float> absorbanceList = (List<Float>) handler.handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_ABSORBANCE);
        List<Float> reflectanceList = (List<Float>) handler.handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_REFLECTANCE);

        Float[] intensity = intensityList.toArray(new Float[228]);
        Float[] absorbance = absorbanceList.toArray(new Float[228]);
        Float[] reflectance = reflectanceList.toArray(new Float[228]);

        float[][] input = getInput(intensity, absorbance, reflectance);
        List<Float> numberLabels = new ArrayList<>();
        for(int i = 0; i < labels.size(); i++){
            numberLabels.add((float) i);
        }

        float[][] output = {new float[labels.size()]};
        for(int i = 0; i < labels.size(); i++){
            output[0][i] = i;
        }

        float[][] outputBinary = {new float[1]};
        outputBinary[0][0] = 0;


        if (!testSize(tflite.getInputTensor(0), input)){
            Log.i("ErrorInput", "Error in input");
            restoreParameters();
            return "Error in input dimensions";
        }

        if (!testSize(tflite.getOutputTensor(0), output)){
            Log.i("ErrorOutput", "Error in output");
            restoreParameters();
            return "Error in output dimensions";
        }

        if (tflite.getOutputTensor(0).shape()[tflite.getOutputTensor(0).shape().length - 1] == 1) {
            input = normalize(input);
            tflite.run(input, outputBinary);
        }else{
            tflite.run(input, output);
        }

        List<Float> predictedValues = new ArrayList<>();
        for (Float f : output[0]){
            predictedValues.add(f);
        }

        Integer predictedValue;
        if (tflite.getOutputTensor(0).shape()[tflite.getOutputTensor(0).shape().length - 1] == 1) {
            predictedValue = getMax(outputBinary);
        }else{
            predictedValue = getMax(output);
        }
        if (labels.size() > predictedValue) {
            prediction = labels.get(predictedValue);
        }else{
            prediction = String.valueOf(predictedValue);
        }

        restoreParameters();
        return prediction;
    }

    private Integer getMax(float[][] output){
        Float value = output[0][0];
        Integer position = 0;

        if (output[0].length > 1) {
            for (int i = 1; i < output[0].length; i++) {
                if (output[0][i] > value) {
                    value = output[0][i];
                    position = i;
                }
            }
        }else{
            position = Math.round(value);
        }

        return position;
    }

    private float[][] getInput(Float[] intensityF, Float[] absorbanceF, Float[] reflectanceF){
        float[] intensity = new float[228];
        float[] absorbance = new float[228];
        float[] reflectance = new float[228];

        for(int i = 0; i < 228; i++){
            intensity[i] = intensityF[i];
            absorbance[i] = absorbanceF[i];
            reflectance[i] = reflectanceF[i];
        }

        return new float[][]{intensity, absorbance, reflectance};

    }

    private void restoreParameters(){
        tflite = null;
        labels = new ArrayList<>();
    }

    private boolean testSize(Tensor tensor, float[][] points){
        int[] shape = tensor.shape();

        if (shape[shape.length-2] == 1 && points[0].length == 2){
            return true;
        }

        if (shape[shape.length-2] != points.length || shape[shape.length-1] != points[0].length){
            return false;
        }

        return true;
    }

    private float[][] normalize(float[][] input){
        for (int i = 0; i < input[0].length; i++){
            input[0][i] /= 1000;
        }

        return input;
    }


}
