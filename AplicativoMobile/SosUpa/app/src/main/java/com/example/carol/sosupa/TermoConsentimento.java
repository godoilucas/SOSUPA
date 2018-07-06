package com.example.carol.sosupa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class TermoConsentimento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termo_consentimento);

        WebView webView = (WebView) findViewById(R.id.wv_content);
        webView.loadUrl("file:///android_asset/politica_privacidade.html");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("Consentimento", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("PrefUsuario", "nao_exibir_outra_vez");

                //Confirma a gravação dos dados
                editor.commit();

                Intent intent = new Intent(TermoConsentimento.this,
                        Inicio.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
