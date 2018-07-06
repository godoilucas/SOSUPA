package com.example.carol.sosupa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author: Bruno Oenning, Lucas Godoi
 */

public class Inicio extends AppCompatActivity {
    private static final String TAG = Inicio.class.getName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient okHttpClient;
    private Request request;
    private String url = "http://ec2-54-191-67-232.us-west-2.compute.amazonaws.com:3000/times";
    private Location location;
    private LocationManager locationManager;
    private String latLong = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //SOLICITAR PERMISSÃO
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }

            //JOGAR PARA O MAPA
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (location != null) {
            Log.i("LOG", "LAT: " + location.getLatitude());
            Log.i("LOG", "LONG: " + location.getLongitude());
            latLong=location.getLatitude()+","+location.getLongitude();
            request();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null){
                        Log.i("LOG", "LAT: "+location.getLatitude());
                        Log.i("LOG", "LONG: "+location.getLongitude());
                        latLong=location.getLatitude()+","+location.getLongitude();
                        request();
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    //JOGAR PARA O MAPA
                    Intent intent = new Intent(Inicio.this,
                            EntradaManual.class);
                    startActivity(intent);

                    finish();
                }
                return;
            }
        }
    }
    public void request(){
        okHttpClient = new OkHttpClient();

        //String json = "{\"origins\": \"-25.386177,-49.236164\"  }";
        String key="";
        try {
            key = Autenticacao.getMd5();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String json = "{\"origins\": \""+latLong+"\", \"key\": \"" + key + "\"}";

        RequestBody body = RequestBody.create(JSON, json);

        request = new Request.Builder().url(url).post(body).build();

        ProgressBar pb = findViewById(R.id.progress);
        ExibeToast.exibeLoading(pb);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ProgressBar pb = findViewById(R.id.progress);
                ExibeToast.escondeLoading(pb);
                Context contexto = getApplicationContext();
                ExibeToast.erroComunicacaoServidor(contexto);

                Log.i(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ProgressBar pb = findViewById(R.id.progress);
                ExibeToast.escondeLoading(pb);
                //Log.i(TAG,response.body().string());
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    JSONArray arrayDados = obj.getJSONArray("UPAS");

                    TableLayout layout_conteudo = findViewById(R.id.conteudo);

                    for(int i=0; i<arrayDados.length(); i++){
                        JSONObject upa = arrayDados.getJSONObject(i);
                        final TextView nomeUpa = new TextView(Inicio.this);
                        final TextView tempoEspera = new TextView(Inicio.this);

                        final TableRow row = new TableRow(Inicio.this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp);

                        TableRow.LayoutParams nomeParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                        nomeParams.span=8;
                        nomeUpa.setLayoutParams(nomeParams);

                        TableRow.LayoutParams tempoEsperaParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
                        tempoEspera.setLayoutParams(tempoEsperaParams);

                        nomeUpa.setId(i);
                        tempoEspera.setId(i);

                        nomeUpa.setTextSize(18);
                        tempoEspera.setTextSize(18);

                        nomeUpa.setTypeface(null, Typeface.BOLD_ITALIC);
                        tempoEspera.setTypeface(null,Typeface.BOLD);

                        nomeUpa.setText(i+1+"."+upa.getString("name"));
                        tempoEspera.setText("0h00");

                        if(upa.getString("tempo_atendimento")!=""){
                            tempoEspera.setText(upa.getString("tempo_atendimento"));
                        }

                        tempoEspera.setGravity(Gravity.CENTER);

                        final String nome = upa.getString("name");
                        final String distancia = upa.getString("distance");
                        nomeUpa.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Inicio.this, Informacoes.class);
                                intent.putExtra("nome", nome);
                                intent.putExtra("distancia", distancia);
                                startActivity(intent);
                            }
                        });

                        row.addView(nomeUpa);
                        row.addView(tempoEspera);

                        ExibeConteudo.printTela(row,layout_conteudo);


                        final TableRow row2 = new TableRow(Inicio.this);
                        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp2);

                        final ImageView bike = new ImageView(Inicio.this);
                        final ImageView carro = new ImageView(Inicio.this);
                        final ImageView onibus = new ImageView(Inicio.this);
                        final ImageView ape = new ImageView(Inicio.this);

                        final TextView tempoCarro = new TextView(Inicio.this);
                        final TextView tempoOnibus = new TextView(Inicio.this);
                        final TextView tempoApe = new TextView(Inicio.this);
                        final TextView tempoBike = new TextView(Inicio.this);

                        carro.setImageResource(R.drawable.bluecar);
                        carro.setMaxHeight(110);
                        carro.setMaxWidth(110);
                        carro.setPadding(0,20,5,45);
                        carro.setAdjustViewBounds(true);

                        onibus.setImageResource(R.drawable.bluebus);
                        onibus.setMaxHeight(110);
                        onibus.setMaxWidth(110);
                        onibus.setPadding(0,20,5,45);
                        onibus.setAdjustViewBounds(true);

                        ape.setImageResource(R.drawable.blueperson);
                        ape.setMaxHeight(110);
                        ape.setMaxWidth(110);
                        ape.setPadding(0,20,5,45);
                        ape.setAdjustViewBounds(true);

                        bike.setImageResource(R.drawable.bluebike);
                        bike.setMaxHeight(110);
                        bike.setMaxWidth(110);
                        bike.setPadding(0,20,5,45);
                        bike.setAdjustViewBounds(true);

                        tempoCarro.setText(upa.getString("driving"));
                        tempoCarro.setPadding(0,20,15,45);
                        tempoOnibus.setText(upa.getString("transit"));
                        tempoOnibus.setPadding(0,20,15,45);
                        tempoApe.setText(upa.getString("walking"));
                        tempoApe.setPadding(0,20,15,45);
                        tempoBike.setText(upa.getString("bicycling"));
                        tempoBike.setPadding(0,20,15,45);

                        row2.addView(carro);
                        row2.addView(tempoCarro);
                        row2.addView(onibus);
                        row2.addView(tempoOnibus);
                        row2.addView(ape);
                        row2.addView(tempoApe);
                        row2.addView(bike);
                        row2.addView(tempoBike);

                        ExibeConteudo.printTela(row2,layout_conteudo);



                        final View separador = new View(Inicio.this);

                        final TableRow row3 = new TableRow(Inicio.this);
                        TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                        row.setLayoutParams(lp3);

                        TableRow.LayoutParams separadorParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT);
                        separadorParams.span=9;
                        separador.setLayoutParams(separadorParams);

                        TypedArray array = Inicio.this.getTheme().obtainStyledAttributes(new int[] {android.R.attr.listDivider});
                        Drawable draw = array.getDrawable(0);
                        array.recycle();
                        separador.setBackgroundDrawable(draw);

                        row3.addView(separador);

                        row3.setPadding(0,0,0,40);

                        ExibeConteudo.printTela(row3,layout_conteudo);

                    }
                } catch (JSONException e) {
                    Context contexto = getApplicationContext();
                    ExibeToast.erroParsingJson(contexto);
                    e.printStackTrace();
                }
            }
        });
    }
}