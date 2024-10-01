package com.example.actividad2_2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor rotationSensor;
    private TextView accelerometerData;
    private TextView rotationData;

    // Declaramos el sensorEventListener como variable de clase (global)
    private SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        accelerometerData = findViewById(R.id.accelerometerData);
        rotationData = findViewById(R.id.rotationData);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Ahora inicializamos el listener dentro de onCreate()
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    accelerometerData.setText("Acelerómetro\nX: " + x + "\nY: " + y + "\nZ: " + z);
                }

                if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                    float[] rotationMatrix = new float[9];
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                    float[] orientationAngles = new float[3];
                    SensorManager.getOrientation(rotationMatrix, orientationAngles);
                    float azimuth = orientationAngles[0]; // Rotación en Z (horizontal)
                    float pitch = orientationAngles[1];   // Rotación en X (frontal)
                    float roll = orientationAngles[2];    // Rotación en Y (lateral)

                    rotationData.setText("Rotación\nAzimuth (Z): " + azimuth + "\nPitch (X): " + pitch + "\nRoll (Y): " + roll);

                    // Aplica la rotación al TextView en función de los valores de rotación.
                    // Aquí, rotamos el texto según el "roll" (o el eje Y)
                    rotationData.setRotation((float) Math.toDegrees(-roll));
                }
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Manejo de cambios en la precisión del sensor
            }
        };

        // Registramos los listeners para los sensores
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistramos los listeners de los sensores
        sensorManager.unregisterListener(sensorEventListener);
    }
}
