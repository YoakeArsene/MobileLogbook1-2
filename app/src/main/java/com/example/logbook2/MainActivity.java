package com.example.logbook2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.logbook2.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Handler handler = new Handler();
    ProgressDialog dialog;

    ImageView imageView;
    Button backward_button, forward_button;
    int i = 0;


    private final int[] CHANGE_IMAGE = {R.drawable.uno1, R.drawable.uno2, R.drawable.uno3, R.drawable.uno4, R.drawable.wildcard};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = (ImageView) findViewById(R.id.imageView);
        backward_button = (Button) findViewById(R.id.backward_button);
        forward_button = (Button) findViewById(R.id.forward_button);

        if (i == 0)
            backward_button.setVisibility(View.VISIBLE);

        if (i == 4)
            forward_button.setVisibility(View.VISIBLE);


        backward_button.setOnClickListener(view -> {
            if (i > 0)
                imageView.setImageResource(CHANGE_IMAGE[--i]);
        });

        forward_button.setOnClickListener(view -> {
            if (i < 4)
                imageView.setImageResource(CHANGE_IMAGE[++i]);

        });

        class AddImage extends Thread{

            final String url;
            Bitmap bitmap;

            AddImage(String URL){
                this.url = URL;
            }

            @Override
            public void run() {
                handler.post(() -> {
                    dialog = new ProgressDialog(MainActivity.this);
                    dialog.setCancelable(false);
                    dialog.setMessage("Loading the picture...");
                    dialog.show();
                });

                InputStream inputStream;
                try {
                    inputStream = new URL(url).openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                handler.post(() -> {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    binding.imageView.setImageBitmap(bitmap);
                });

            }
        }

        binding.addLinkButton.setOnClickListener(view -> {
            String url = binding.URLInput.getText().toString();
            new AddImage(url).start();
        });
    }
}