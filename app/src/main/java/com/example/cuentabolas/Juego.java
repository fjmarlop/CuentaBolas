package com.example.cuentabolas;

import static com.example.cuentabolas.R.drawable.ic_volume_up;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

public class Juego extends AppCompatActivity {

    private static final String TIEMPO = "list_tiempo";
    private static SharedPreferences preferencias;


    Toolbar toolbar;
    ImageButton botonPreferencias;
    TextView titulo, txtInicio;
    Graficos graficos;
    public static long tiempoPref = 20000;
    MediaPlayer sonido;
    ImageView mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        //recogemos el tiempo de la preferencias
        PreferenceManager.setDefaultValues(this, R.xml.preferencias_cuentabolas, false);
        preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        // asignamo valor por defecto al tiempo
        String tmp = preferencias.getString(TIEMPO, "20 segundos");

        if (tmp.equalsIgnoreCase("10 segundos")) {
            tiempoPref = 10000;
        }
        if (tmp.equalsIgnoreCase("20 segundos")) {
            tiempoPref = 20000;
        }
        if (tmp.equalsIgnoreCase("30 segundos")) {
            tiempoPref = 30000;
        }

        toolbar = findViewById(R.id.toolbar);

        titulo = findViewById(R.id.txtTitulo);
        Typeface fuente = Typeface.createFromAsset(getAssets(), "fonts/relish.otf");
        titulo.setTypeface(fuente);

        botonPreferencias = findViewById(R.id.imageButton);
        botonPreferencias.setOnClickListener(view -> verPreferencias());

        graficos = findViewById(R.id.graficos);
        graficos.setVisibility(View.INVISIBLE);

        txtInicio = findViewById(R.id.txtInicio);
        Typeface lcdFont = Typeface.createFromAsset(getAssets(), "fonts/LCD.otf");
        txtInicio.setTypeface(lcdFont);
        //cargamos el sonido del juego
        sonido = MediaPlayer.create(this,R.raw.tetris);
        mute = (ImageView) findViewById(R.id.mute);
        mute.setVisibility(View.INVISIBLE);
        mute.setOnClickListener(view -> mute());

        // declaramos las animaciones y la cargamos del xml
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.animacion);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.animacion);
        Animation anim3 = AnimationUtils.loadAnimation(this, R.anim.animacion_corta);

        // comienza la animacion
        txtInicio.startAnimation(anim1);
        //listener de la animacion, cuando termine cargara la siguiente y cambia el texto
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtInicio.setText("listos");
                txtInicio.startAnimation(anim2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        // mismo listener para la animacion 2 cuando termine cargara la siguiente y cambia el texto
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtInicio.setText(" ya ");
                txtInicio.startAnimation(anim3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        // mismo listener para la animacion 3 cuando termine hara invisible el texto y aparece el grafico
        anim3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //comienza el juego y se activa sonido
                sonido.start();
                mute.setVisibility(View.VISIBLE);
                txtInicio.setVisibility(View.INVISIBLE);
                graficos.setVisibility(View.VISIBLE);
                //empieza la cuenta atrás
                Tiempo t = new Tiempo(tiempoPref, 1000);
                t.start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void verPreferencias() {
        startActivity(new Intent(this, Preferencias.class));
        sonido.stop();
        finish();
    }

    private void mute() {
        if (sonido.isPlaying()){
            sonido.pause();
            mute.setImageResource(R.drawable.ic_volume_up);
        }else{
            sonido.start();
            mute.setImageResource(R.drawable.ic_volume_off);
        }
    }

    // clase Tiempo que será la encargada de la cuenta atrás
    public class Tiempo extends CountDownTimer {

        public Tiempo(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            TextView reloj = (TextView) findViewById(R.id.txtReloj);
            reloj.setText("" + l / 1000);
        }

        @Override
        public void onFinish() {
            Intent res = new Intent(getApplicationContext(),Resultados.class);
            startActivity(res);
            sonido.stop();
        }
    }

}