package com.example.actividad2_2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageView);

        findViewById(R.id.downloadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage("https://e00-elmundo.uecdn.es/assets/multimedia/imagenes/2024/07/10/17206126053713.jpg");
            }
        });
    }

    public void MainActiviy2Ver (View view){
        Intent intento = new Intent(this , MainActivity2.class);
        startActivity(intento);
    }

    // Método para descargar la imagen en segundo plano usando un Thread
    private void downloadImage(final String urlString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = loadImageFromNetwork("https://phantom-elmundo.unidadeditorial.es/72104c1f4f0fa073bb4769c48914d9d1/resize/920/f/webp/assets/multimedia/imagenes/2024/07/10/17206126053713.jpg");
                mImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
    }

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

}
