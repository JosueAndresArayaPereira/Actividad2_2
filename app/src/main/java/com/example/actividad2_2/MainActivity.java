package com.example.actividad2_2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageView);

        // Botón para descargar la imagen
        findViewById(R.id.downloadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage("https://e00-elmundo.uecdn.es/assets/multimedia/imagenes/2024/07/10/17206126053713.jpg");
            }
        });

        // Inicializamos el sensor manager y el sensor de rotación
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Configuramos el listener para escuchar los cambios en el sensor de rotación
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                    float[] rotationMatrix = new float[9];
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                    float[] orientationAngles = new float[3];
                    SensorManager.getOrientation(rotationMatrix, orientationAngles);
                    float azimuth = orientationAngles[0]; // Rotación en Z (horizontal)
                    float pitch = orientationAngles[1];   // Rotación en X (frontal)
                    float roll = orientationAngles[2];    // Rotación en Y (lateral)

                    // Aquí rotamos la imagen en función de la rotación del teléfono
                    mImageView.setRotation((float) Math.toDegrees(-roll));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Manejo de cambios en la precisión del sensor
            }
        };

        // Registramos el listener para los eventos del sensor de rotación
        sensorManager.registerListener(sensorEventListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    // Método para descargar la imagen desde una URL
    private void downloadImage(final String urlString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = loadImageFromNetwork(urlString);
                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

    // Método auxiliar para cargar la imagen desde la red
    private Bitmap loadImageFromNetwork(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistramos el listener cuando la actividad está en pausa para ahorrar batería
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Volvemos a registrar el listener cuando la actividad se reanuda
        sensorManager.registerListener(sensorEventListener, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
