package com.example.carol.sosupa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


/**
 * @author: Bruno Oenning, Lucas Godoi
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handle = new Handler();

        SharedPreferences settings = getSharedPreferences("Consentimento", 0);
        if(settings.getString("PrefUsuario", "").length() > 0){
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    inicio();
                }
            }, 3500);
        } else{
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    consentimento();
                }
            }, 3500);
        }
    }

    private void consentimento(){
        Intent intent = new Intent(MainActivity.this,
                TermoConsentimento.class);
        startActivity(intent);
        finish();
    }

    private void inicio(){
        Intent intent = new Intent(MainActivity.this,
                Inicio.class);
        startActivity(intent);
        finish();
    }

}
