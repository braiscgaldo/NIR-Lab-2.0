package com.cgaldo.brais.sistema.Controller.ViewsController;


import android.graphics.Color;
import android.util.Log;

import com.cgaldo.brais.sistema.Controller.Bluetooth.BluetoothController;
import com.cgaldo.brais.sistema.Controller.ConnectionsController;
import com.cgaldo.brais.sistema.Controller.JNICalls.JNICall;
import com.cgaldo.brais.sistema.Model.PostProcessedData.ScanInformation;
import com.cgaldo.brais.sistema.Model.StaticData.GraphViewConstants;
import com.cgaldo.brais.sistema.Model.StaticData.ObjectsRequired;
import com.cgaldo.brais.sistema.Model.StaticData.Requests;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ScanController implements ScanControllerInterface{

    private LineGraphSeries lineGraphSeriesReflectance;
    private LineGraphSeries lineGraphSeriesAbsorbance;
    private LineGraphSeries lineGraphSeriesIntensity;

    private static ConnectionsController connectionsController = BluetoothController.getInstance();

    // Singleton
    private static ScanController scanController;

    private ScanController(){

    }

    public static ScanController getInstance(){
        if (scanController == null){
            scanController = new ScanController();
        }

        return scanController;
    }

    @Override
    public void setData(GraphView graphView) {
        // We get data
        DataPoint[] reflectance = this.getDataPoints(ObjectsRequired.SCAN_REFLECTANCE);
        DataPoint[] absorbance = this.getDataPoints(ObjectsRequired.SCAN_ABSORBANCE);
        DataPoint[] intensity = this.getDataPoints(ObjectsRequired.SCAN_INTENSITY);

        // We set the values for the X axis
        List<Double> wavelength = (List<Double>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_WAVELENGTH);
        graphView.getViewport().setMinX(Math.round(wavelength.get(0)));
        graphView.getViewport().setMaxX(Math.round(wavelength.get(wavelength.size()-1)));

        lineGraphSeriesReflectance = new LineGraphSeries<>(reflectance);

        lineGraphSeriesReflectance.setColor(Color.BLUE);

        lineGraphSeriesAbsorbance = new LineGraphSeries<>(absorbance);

        lineGraphSeriesAbsorbance.setColor(Color.RED);

        lineGraphSeriesIntensity = new LineGraphSeries<>(intensity);

        lineGraphSeriesIntensity.setColor(Color.parseColor("#4A9A29"));

    }

    @Override
    public void setLinesVisibles(GraphView graphView, Integer parameter){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(3);
        nf.setMinimumIntegerDigits(1);
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));

        if (parameter == GraphViewConstants.INTENSITY){
            nf.setMinimumFractionDigits(0);
            nf.setMinimumIntegerDigits(1);
            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));

            graphView.removeSeries(this.lineGraphSeriesReflectance);
            graphView.removeSeries(this.lineGraphSeriesAbsorbance);
            graphView.addSeries(this.lineGraphSeriesIntensity);

            // We scale the graph
            graphView.getViewport().setMinY(getMin((List<Float>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_INTENSITY)) - 100);
            graphView.getViewport().setMaxY(getMax((List<Float>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_INTENSITY)) + 100);

        } else if (parameter == GraphViewConstants.ABSORBANCE){
            graphView.removeSeries(this.lineGraphSeriesReflectance);
            graphView.removeSeries(this.lineGraphSeriesIntensity);
            graphView.addSeries(this.lineGraphSeriesAbsorbance);

            // We scale the graph
            graphView.getViewport().setMinY(getMin((List<Float>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_ABSORBANCE)) - 0.4);
            graphView.getViewport().setMaxY(getMax((List<Float>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_ABSORBANCE)) + 0.4);

        } else if (parameter == GraphViewConstants.REFLECTANCE){
            graphView.removeSeries(this.lineGraphSeriesAbsorbance);
            graphView.removeSeries(this.lineGraphSeriesIntensity);
            graphView.addSeries(this.lineGraphSeriesReflectance);

            // We scale the graph
            graphView.getViewport().setMinY(getMin((List<Float>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_REFLECTANCE)) - 0.04);
            graphView.getViewport().setMaxY(getMax((List<Float>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_REFLECTANCE)) + 0.04);

        }

    }

    // Compute scan data
    @Override
    public void computeScan(){
        ByteArrayOutputStream dataArray = (ByteArrayOutputStream) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_PRE_SCAN, ObjectsRequired.DATA_SCAN);
        ByteArrayOutputStream coeffsArray = (ByteArrayOutputStream) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_CALIBRATION, ObjectsRequired.REFERENCE_CALIB_COEFF);
        ByteArrayOutputStream matrixArray = (ByteArrayOutputStream) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_CALIBRATION, ObjectsRequired.REFERENCE_CALIB_MATRIX);
        byte[] dataJNI = dataArray.toByteArray();
        byte[] coeffsJNI = coeffsArray.toByteArray();
        byte[] matrixJNI = matrixArray.toByteArray();
        new JNICall().dlpSpecScanInterpReference(dataJNI, coeffsJNI, matrixJNI);
        ScanInformation scanInformation = ScanInformation.getInstance();
        Integer scanSize = (Integer) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN, ObjectsRequired.SCAN_SCAN_SIZE);
        double[] wavePoints = (double[]) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN, ObjectsRequired.SCAN_WAVELENGTH_POINTS);
        int[] resultIntensity = (int[]) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN, ObjectsRequired.SCAN_RESULT_INTENSITY);
        int[] uncalibIntensity = (int[]) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN, ObjectsRequired.SCAN_UNCALIB_INTENSITY);
        scanInformation.convertScanData(scanSize, wavePoints, resultIntensity, uncalibIntensity);
        Log.i("scanData", "scanDataObtained");
    }

    private DataPoint[] getDataPoints(Integer objectRequired){
        Integer size = (Integer) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN, ObjectsRequired.SCAN_SCAN_SIZE);

        DataPoint[] dataPoints = new DataPoint[size];
        List<Float> dataList = (ArrayList<Float>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, objectRequired);
        List<Double> wavePoints = (ArrayList<Double>) connectionsController.getInformation().handleGetRequest(Requests.DEVICE_SCAN_INFORMATION, ObjectsRequired.SCAN_WAVELENGTH);

        for (int wp = 0; wp < size; wp++){
            dataPoints[wp] = new DataPoint(wavePoints.get(wp), (double)dataList.get(wp));

        }

        return dataPoints;
    }

    private float getMin(List<Float> data){
        float min = 1000000;

        for(float f: data){
            if (!Float.isNaN(f) && f != 0)
                min = Math.min(min, f);
        }

        return min;
    }

    private float getMax(List<Float> data){
        float max = -1000000;

        for(float f: data){
            if (!Float.isNaN(f) && f!=0)
                max = Math.max(max, f);
        }

        return max;
    }

}
