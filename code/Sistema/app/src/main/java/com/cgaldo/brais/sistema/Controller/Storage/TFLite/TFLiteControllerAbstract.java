package com.cgaldo.brais.sistema.Controller.Storage.TFLite;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cgaldo.brais.sistema.Model.Handler;
import com.cgaldo.brais.sistema.Model.StaticData.Storage;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public abstract class TFLiteControllerAbstract {

    Interpreter tflite;
    List<String> labels;
    String prediction;

    public Interpreter getTflite() {
        return tflite;
    }

    public void setTflite(Interpreter tflite) {
        this.tflite = tflite;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    private MappedByteBuffer loadModelFile(AppCompatActivity activity, String nameProblem, String modelName){
        FileInputStream inputStream;
        File modelFile;
        try {
            String filename = Environment.getExternalStorageDirectory() + "/Documents/" + Storage.PARENT_DIRECTORY + '/' + Storage.MODELS_DIRECTORY + '/' + nameProblem + '/' + modelName;
            modelFile = new File(filename);
            if(!modelFile.exists()){
                Log.i("errorFile", "file not exists");
            }
            inputStream = new FileInputStream(modelFile);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Error loading model", "Error model file");
            return null;
        }
        FileChannel fileChannel = inputStream.getChannel();
        try {
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, modelFile.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean loadModel(AppCompatActivity activity, String nameProblem, String nameModel){ 
        tflite = new Interpreter(loadModelFile(activity, nameProblem, nameModel));

        return true;
    }

    public String predict(Handler handler){
        return "";
    }

}
