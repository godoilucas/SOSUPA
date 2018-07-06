package com.example.carol.sosupa;

import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by carol on 06/05/2018.
 */

public class ExibeConteudo {
    public static void printTela(final TableRow txtItem, final TableLayout layout_conteudo){
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                layout_conteudo.addView(txtItem);
            }
        });
    }

    public static void atualizaTexto(final TextView textView, final String txt){
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                textView.setText(txt);
            }
        });
    }
}