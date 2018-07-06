package com.example.carol.sosupa;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author: Bruno Oenning, Lucas Godoi
 */

public class ExibeToast {
    public static void erroComunicacaoServidor(final Context context){
        final String msg = "Erro de comunicação com o servidor. Tente novamente mais tarde!";
        if (context != null && msg != null){
            new Handler(Looper.getMainLooper()).post(new Runnable(){
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static void erroParsingJson(final Context context){
        final String msg = "Falha no parsing do JSON";
        if (context != null && msg != null){
            new Handler(Looper.getMainLooper()).post(new Runnable(){
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public static void printTela(final TextView txtItem, final LinearLayout layout_conteudo){
            new Handler(Looper.getMainLooper()).post(new Runnable(){
                @Override
                public void run() {
                    layout_conteudo.addView(txtItem);
                }
            });
    }

    public static void exibeLoading(final ProgressBar pb){
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                pb.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void escondeLoading(final ProgressBar pb){
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                pb.setVisibility(View.GONE);
            }
        });
    }
}
