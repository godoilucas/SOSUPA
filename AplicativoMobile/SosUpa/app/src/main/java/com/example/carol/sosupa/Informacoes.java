package com.example.carol.sosupa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Informacoes extends AppCompatActivity {
    private static final String TAG = Inicio.class.getName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient;
    private Request request;
    private String url = "http://ec2-54-191-67-232.us-west-2.compute.amazonaws.com:3000/upa?nome=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Pega a intent de outra activity
        final Intent it = getIntent();

        //Recuperei a string da outra activity
        final String nome = it.getStringExtra("nome");
        final String strDistancia = it.getStringExtra("distancia");
        TextView nomeUpa = (TextView) findViewById(R.id.nomeUpa);
        TextView distancia = (TextView) findViewById(R.id.distancia);

        nomeUpa.setText(nome);
        distancia.setText(strDistancia.replace(".",","));
        url = url+"\""+nome+"\"";

        okHttpClient = new OkHttpClient();
        request = new Request.Builder().url(url).get().build();



        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Context contexto = getApplicationContext();
                ExibeToast.erroComunicacaoServidor(contexto);

                Log.i(TAG,e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject obj = new JSONObject(response.body().string());

                    TextView endereco = findViewById(R.id.endereco);
                    TextView telefone = findViewById(R.id.telefone);

                    ExibeConteudo.atualizaTexto(endereco,obj.getString("endereco"));
                    ExibeConteudo.atualizaTexto(telefone,obj.getString("telefone"));

                    TextView periodoDados = findViewById(R.id.periodoDados);
                    TextView porcentagemHomens = findViewById(R.id.porcentagemHomens);
                    TextView porcentagemMulheres = findViewById(R.id.porcentagemMulheres);
                    TextView totalAtendimentos = findViewById(R.id.totalAtendimentos);

                    ExibeConteudo.atualizaTexto(periodoDados,obj.getString("info"));
                    ExibeConteudo.atualizaTexto(porcentagemHomens,obj.getString("porcentagem_homens").replace(".",",")+"%");
                    ExibeConteudo.atualizaTexto(porcentagemMulheres,obj.getString("porcentagem_mulheres").replace(".",",")+"%");
                    ExibeConteudo.atualizaTexto(totalAtendimentos,obj.getString("total"));

                    TextView faixaEtaria1 = findViewById(R.id.faixaEtaria1);
                    TextView faixaEtaria2 = findViewById(R.id.faixaEtaria2);
                    TextView faixaEtaria3 = findViewById(R.id.faixaEtaria3);
                    TextView faixaEtaria4 = findViewById(R.id.faixaEtaria4);

                    ExibeConteudo.atualizaTexto(faixaEtaria1,obj.getString("porcentagem_12").replace(".",",")+"%");
                    ExibeConteudo.atualizaTexto(faixaEtaria2,obj.getString("porcentagem_12_25").replace(".",",")+"%");
                    ExibeConteudo.atualizaTexto(faixaEtaria3,obj.getString("porcentagem_25_60").replace(".",",")+"%");
                    ExibeConteudo.atualizaTexto(faixaEtaria4,obj.getString("porcentagem_60").replace(".",",")+"%");


                    JSONArray arrayDados = obj.getJSONArray("top10_diagnosticos");
                    TableLayout layoutTopAtendimentos = findViewById(R.id.layoutTopAtendimentos);

                    for(int i=0; i<arrayDados.length(); i++){
                        final TextView diagnostico = new TextView(Informacoes.this);
                        final TableRow row = new TableRow(Informacoes.this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);

                        int indice=i+1;

                        diagnostico.setId(i);
                        diagnostico.setTextSize(12);
                        diagnostico.setTypeface(null, Typeface.ITALIC);
                        diagnostico.setText(indice+". "+arrayDados.getString(i));
                        row.addView(diagnostico);
                        ExibeConteudo.printTela(row,layoutTopAtendimentos);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
