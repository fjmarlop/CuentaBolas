package com.example.cuentabolas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.cuentabolas.R;

import java.util.ArrayList;
import java.util.Random;

public class Graficos extends View {

    private static final String VELOCIDAD = "list_velocidad";
    private static final String NBOLAS = "list_nBolas";
    private static final String NCOLORES = "list_nColores";
    private static final String REBOTE = "switch_rebote";
    private static final int RADIO = 30;

    private static SharedPreferences preferencias;
    private int nColorPref = 4, nBolasPref = 4, velocidadPref = 150;

    public Paint p1, p2, p3, p4, p5;
    public int[][] arrayX;
    public int[][] arrayY;
    public int[][] incrementoX;
    public int[][] incrementoY;
    public ArrayList<Paint> pinceles;


    public Graficos(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // creamos los pinceles
        p1 = new Paint();
        p2 = new Paint();
        p3 = new Paint();
        p4 = new Paint();
        p5 = new Paint();

        // asignamos color
        p1.setColor(Color.GREEN);
        p2.setColor(Color.RED);
        p3.setColor(Color.YELLOW);
        p4.setColor(Color.BLUE);
        p5.setColor(Color.MAGENTA);

        // recogemos las preferencias
        PreferenceManager.setDefaultValues(getContext(), R.xml.preferencias_cuentabolas, false);
        preferencias = PreferenceManager.getDefaultSharedPreferences(getContext());
        // aisgnamos las preferencias a variables y asignamos un valor por defecto
        String velocidad = preferencias.getString(VELOCIDAD, "Medio");
        String bolas = preferencias.getString(NBOLAS, "4 bolas");
        String colores = preferencias.getString(NCOLORES, "4 colores");

        if (velocidad.equalsIgnoreCase("Rápido")) {
            velocidadPref = 100;
        }
        if (velocidad.equalsIgnoreCase("Medio")) {
            velocidadPref = 150;
        }
        if (velocidad.equalsIgnoreCase("Lento")) {
            velocidadPref = 200;
        }
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

        //preparamos arrays
        arrayX = new int[nColorPref][nBolasPref];
        arrayY = new int[nColorPref][nBolasPref];
        incrementoX = new int[nColorPref][nBolasPref];
        incrementoY = new int[nColorPref][nBolasPref];
        pinceles = new ArrayList<>();

        //cargamos los arrays con numeros aleatorios
        for (int i = 0; i < arrayX.length; i++) {
            for (int j = 0; j < arrayX[i].length; j++) {
                arrayX[i][j] = new Random().nextInt(901);
                arrayY[i][j] = new Random().nextInt(1401);
                incrementoX[i][j] = new Random().nextInt(100);
                incrementoY[i][j] = new Random().nextInt(100);
            }
        }
        //cargo el array de pinceles
        pinceles.add(p1);
        pinceles.add(p2);
        pinceles.add(p3);
        pinceles.add(p4);
        pinceles.add(p5);
    }


    protected void onDraw(Canvas c) {

        //recogemos el rebote de las preferencias
        boolean rebote = preferencias.getBoolean(REBOTE, true);

        // obtener los limites de la pantalla
        int ejeX = getWidth();
        int ejeY = getHeight();
        int limiteDerecha = ejeX - RADIO;
        int limiteIzquierda = RADIO;
        int limiteInferior = ejeY - RADIO;
        int limiteSuperior = RADIO;

        for (int color = 0; color < arrayX.length; color++) {
            for (int nbola = 0; nbola < arrayX[color].length; nbola++) {

                //Pintamos cada bola
                c.drawCircle(arrayX[color][nbola], arrayY[color][nbola], RADIO, pinceles.get(color));

                //Actualizamos las posicion de cada bola
                arrayX[color][nbola] += incrementoX[color][nbola];
                arrayY[color][nbola] += incrementoY[color][nbola];

                //Controlamos los límites de la pantalla y si existe o no rebote con las paredes

                if (!rebote) {
                    if (arrayX[color][nbola] <= limiteIzquierda) { //Control del lado izquierdo
                        arrayX[color][nbola] = ejeX;
                    }
                    if (arrayX[color][nbola] >= limiteDerecha) { //Control del lado derecho
                        arrayX[color][nbola] = RADIO;
                    }

                    if (arrayY[color][nbola] <= limiteSuperior) { //Control del lado superior
                        arrayY[color][nbola] = ejeY;
                    }
                    if (arrayY[color][nbola] >= limiteInferior) { //Control del lado inferior
                        arrayY[color][nbola] = RADIO;
                    }
                } else {
                    if (arrayX[color][nbola] <= limiteIzquierda) { //Control del lado izquierdo
                        arrayX[color][nbola] = RADIO;
                        incrementoX[color][nbola] *= -1;
                    }
                    if (arrayX[color][nbola] >= limiteDerecha) { //Control del lado derecho
                        arrayX[color][nbola] = limiteDerecha;
                        incrementoX[color][nbola] *= -1;
                    }

                    if (arrayY[color][nbola] <= limiteSuperior) { //Control del lado superior
                        arrayY[color][nbola] = RADIO;
                        incrementoY[color][nbola] *= -1;
                    }
                    if (arrayY[color][nbola] >= limiteInferior) { //Control del lado inferior
                        arrayY[color][nbola] = limiteInferior;
                        incrementoY[color][nbola] *= -1;
                    }
                }

            }
        }
        postInvalidateDelayed(velocidadPref);
    }


}
