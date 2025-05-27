package com.example.appjogohulk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView hulk01, hulk02, hulk03;
    ProgressBar progressBar;
    TextView pts, ptsM;
    Button iniciar;
    Random r;
    int pontos = 0, fps = 1000, ptsMax = 0, tempoStatus = 0;
    int imgAtv = 0, imgAtvsalva = 0;
    AnimationDrawable an;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciar = findViewById(R.id.iniciar);
        pts = findViewById(R.id.pts);
        ptsM = findViewById(R.id.recorde);
        progressBar = findViewById(R.id.progressBar);

        hulk01 = findViewById(R.id.hulk_01);
        hulk01.setVisibility(View.INVISIBLE);
        hulk02 = findViewById(R.id.hulk_02);
        hulk02.setVisibility(View.INVISIBLE);
        hulk03 = findViewById(R.id.hulk_03);
        hulk03.setVisibility(View.INVISIBLE);

        SharedPreferences prefs = getSharedPreferences("recorde", MODE_PRIVATE);
        ptsMax = prefs.getInt("recorde", 0);
        ptsM.setText("Recorde: " + ptsMax);

        r = new Random();
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pontos = 0;
                tempoStatus = 100;
                pts.setText("Pontos: " + pontos);
                progressBar.setProgress(tempoStatus);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        minhaEngine();
                    }
                }, 1000);

                iniciar.setEnabled(false);

                final Handler handler2 = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (tempoStatus > 0) {
                            tempoStatus--;
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler2.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(tempoStatus);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        hulk01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hulk01.setImageResource(R.drawable.hulk_03);
                pontos++;
                pts.setText("Pontos: " + pontos);
                hulk01.setEnabled(false);
            }
        });

        hulk02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hulk02.setImageResource(R.drawable.hulk_03);
                pontos++;
                pts.setText("Pontos: " + pontos);
                hulk02.setEnabled(false);
            }
        });

        hulk03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hulk03.setImageResource(R.drawable.hulk_03);
                pontos++;
                pts.setText("Pontos: " + pontos);
                hulk03.setEnabled(false);
            }
        });
    }

    private void minhaEngine() {
        if(pontos < 10) fps = 1000;
        else if (pontos < 15) fps = 950;
        else if (pontos < 20) fps = 900;
        else if (pontos < 25) fps = 850;
        else if (pontos < 30) fps = 800;
        else if (pontos < 35) fps = 750;
        else if (pontos < 40) fps = 700;
        else if (pontos < 45) fps = 650;
        else if (pontos < 50) fps = 600;
        else if (pontos < 55) fps = 550;
        else if (pontos < 60) fps = 500;
        else if (pontos < 65) fps = 450;
        else fps = 400;

        an = (AnimationDrawable) ContextCompat.getDrawable(this, R.drawable.animacao);
        do {
            imgAtv = r.nextInt(3) + 1;
        } while(imgAtvsalva == imgAtv);
        imgAtvsalva = imgAtv;

        if (imgAtv == 1) {
            hulk01.setImageDrawable(an);
            hulk01.setVisibility(View.VISIBLE);
            hulk01.setEnabled(true);
        } else if (imgAtv == 2) {
            hulk02.setImageDrawable(an);
            hulk02.setVisibility(View.VISIBLE);
            hulk02.setEnabled(true);
        } else if (imgAtv == 3) {
            hulk03.setImageDrawable(an);
            hulk03.setVisibility(View.VISIBLE);
            hulk03.setEnabled(true);
        }

        an.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hulk01.setVisibility(View.INVISIBLE);
                hulk02.setVisibility(View.INVISIBLE);
                hulk03.setVisibility(View.INVISIBLE);
                hulk01.setEnabled(false);
                hulk02.setEnabled(false);
                hulk03.setEnabled(false);

                if (tempoStatus < 1) {
                    Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                    iniciar.setEnabled(true);
                    if(ptsMax < pontos) ptsMax = pontos;
                    ptsM.setText("Recorde: " + ptsMax);

                    SharedPreferences prefs = getSharedPreferences("recorde", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("recorde", ptsMax);
                    editor.apply();
                } else {
                    minhaEngine();
                }
            }
        }, fps);
    }
}