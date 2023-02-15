package com.example.cuentabolas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;


public class Resultados extends AppCompatActivity {

    private static final String NBOLAS = "list_nBolas";
    private static final String NCOLORES = "list_nColores";
    private static SharedPreferences preferencias;

    int nColorPref, nBolasPref;

    TextView txtVerde, txtRojo, txtAmarillo, txtAzul, txtMagenta, txtMsg, txtRes, txtMsgDerrota;
    Button btcomprobar;
    ImageView img;
    TableLayout tabla;
    ConstraintLayout layout;
    MediaPlayer win, lose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        layout = (ConstraintLayout) findViewById(R.id.miLayout);
        tabla = (TableLayout) findViewById(R.id.tabla);

        img = (ImageView) findViewById(R.id.imagen);
        img.setVisibility(View.INVISIBLE);

        txtVerde = (TextView) findViewById(R.id.nVerde);
        txtRojo = (TextView) findViewById(R.id.nRojo);
        txtAmarillo = (TextView) findViewById(R.id.nAmarillo);
        txtAzul = (TextView) findViewById(R.id.nAzul);
        txtMagenta = (TextView) findViewById(R.id.nMagenta);
        txtMsg = (TextView) findViewById(R.id.txtmsg);
        txtRes = (TextView) findViewById(R.id.txtRes);

        btcomprobar = (Button) findViewById(R.id.btComprobar);
        btcomprobar.setOnClickListener(view -> comprobarResultado());

        txtMsgDerrota = (TextView) findViewById(R.id.msgDerrota);
        txtMsgDerrota.setVisibility(View.INVISIBLE);
    }

    public void comprobarResultado() {

        boolean victoria = false;

        // recoger preferencias
        PreferenceManager.setDefaultValues(this, R.xml.preferencias_cuentabolas, true);
        preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        //orden colores verde, rojo, amarillo, azul, magenta

        String bolas = preferencias.getString(NBOLAS, "4 bolas");
        String colores = preferencias.getString(NCOLORES, "4 colores");

        if (bolas.equalsIgnoreCase("3 bolas")) {
            nBolasPref = 3;
        }
        if (bolas.equalsIgnoreCase("4 bolas")) {
            nBolasPref = 4;
        }
        if (bolas.equalsIgnoreCase("5 bolas")) {
            nBolasPref = 5;
        }
        if (colores.equalsIgnoreCase("3 colores")) {
            nColorPref = 3;
        }
        if (colores.equalsIgnoreCase("4 colores")) {
            nColorPref = 4;
        }
        if (colores.equalsIgnoreCase("5 colores")) {
            nColorPref = 5;
        }
        //recoger valores introducidos por el usuario;

        int nVerde = txtVerde.getText().toString().equalsIgnoreCase("") ? 0 : Integer.parseInt(txtVerde.getText().toString());
        int nRojo = txtRojo.getText().toString().equalsIgnoreCase("") ? 0 : Integer.parseInt(txtRojo.getText().toString());
        int nAmarillo = txtAmarillo.getText().toString().equalsIgnoreCase("") ? 0 : Integer.parseInt(txtAmarillo.getText().toString());
        int nAzul = txtAzul.getText().toString().equalsIgnoreCase("") ? 0 : Integer.parseInt(txtAzul.getText().toString());
        int nMagenta = txtMagenta.getText().toString().equalsIgnoreCase("") ? 0 : Integer.parseInt(txtMagenta.getText().toString());

        int res = nVerde + nRojo + nAmarillo + nAzul + nMagenta;

        if (res == 0) {
            Toast.makeText(this, " Quiero saber si has estado atento. ¿Cuántas bolas había de cada color? ", Toast.LENGTH_LONG).show();
        } else {
            if (nVerde == nBolasPref && nRojo == nBolasPref && nAmarillo == nBolasPref && nAzul == nBolasPref && nMagenta == nBolasPref && nColorPref == 5) {
                victoria = true;
            }
            if (nVerde == nBolasPref && nRojo == nBolasPref && nAmarillo == nBolasPref && nAzul == nBolasPref && nMagenta == 0 && nColorPref == 4) {
                victoria = true;
            }
            if (nVerde == nBolasPref && nRojo == nBolasPref && nAmarillo == nBolasPref && nAzul == 0 && nMagenta == 0 && nColorPref == 3) {
                victoria = true;
            }

            if (victoria) {
                escondite();
                img.setVisibility(View.VISIBLE);
                Animation rot = AnimationUtils.loadAnimation(this, R.anim.animacion_rot);
                img.startAnimation(rot);
                win = MediaPlayer.create(this, R.raw.win);
                win.start();
                rot.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        volver();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            } else {
                escondite();
                lose = MediaPlayer.create(this, R.raw.lose);
                lose.start();
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.lose);
                Animation rot = AnimationUtils.loadAnimation(this, R.anim.animacion_rot);
                img.startAnimation(rot);
                txtMsgDerrota.setVisibility(View.VISIBLE);

                String mensaje = "Resultados";

                if (nVerde == nBolasPref) {
                    mensaje += "\nEn efecto, había " + nVerde + " de color verde.";
                }
                if (nRojo == nBolasPref) {
                    mensaje += "\nEn efecto, había " + nRojo + " de color rojo.";
                }
                if (nAmarillo == nBolasPref) {
                    mensaje += "\nEn efecto, había " + nAmarillo + " de color amarillo.";
                }
                if (nAzul == nBolasPref && (nColorPref == 4 || nColorPref == 5)) {
                    mensaje += "\nEn efecto, había " + nAzul + " de color azul.";
                }
                if (nMagenta == nBolasPref && nColorPref == 5) {
                    mensaje += "\nEn efecto, había " + nMagenta + " de color magenta.";
                }
                if (mensaje.equalsIgnoreCase("Resultados")) {
                    mensaje += "\nNo has acertado ninguno.";
                }
                mensaje += "\nMás suerte la próxima vez";
                txtMsgDerrota.setText(mensaje);

                rot.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        volver();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
    }

    public void escondite() {
        txtMsg.setVisibility(View.INVISIBLE);
        txtRes.setVisibility(View.INVISIBLE);
        btcomprobar.setVisibility(View.INVISIBLE);
        tabla.setVisibility(View.INVISIBLE);
        layout.setBackgroundColor(Color.BLACK);
    }

    public void volver() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}