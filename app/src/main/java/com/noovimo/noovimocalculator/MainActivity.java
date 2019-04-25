package com.noovimo.noovimocalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static double NOTAIRE_PERCENTAGE = 7.5;

    private EditText etxtNetVendeur;
    private Button btnNetCalculate;
    private EditText etxtNotaireInclus;
    private TextView txtnotaire;
    private Button btnNotaireCalculate;
    private EditText etxtFAI;
    private TextView txtFa;
    private Button btnFAICalculate;
    private EditText etxtClient;
    private Button btnClientCalculate;

    private double net;
    private double fa;
    private double notaire;
    private double client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etxtNetVendeur = (EditText) findViewById(R.id.etxtNetVendeur);
        btnNetCalculate = (Button) findViewById(R.id.btnNetCalculate);
        etxtNotaireInclus = (EditText) findViewById(R.id.etxtNotaireInclus);
        txtnotaire = (TextView) findViewById(R.id.txtNotaire);
        btnNotaireCalculate = (Button) findViewById(R.id.btnNotaireCalculate);
        etxtFAI = (EditText) findViewById(R.id.etxtFAI);
        txtFa = (TextView) findViewById(R.id.txtFA);
        btnFAICalculate = (Button) findViewById(R.id.btnFAICalculate);
        etxtClient = (EditText) findViewById(R.id.etxtClient);
        btnClientCalculate = (Button) findViewById(R.id.btnClientCalculate);

        btnNetCalculate.setOnClickListener(btnNetCalculateListener);
        btnNotaireCalculate.setOnClickListener(btnNotaireCalculateListener);
        btnFAICalculate.setOnClickListener(btnFAICalculateListener);
        btnClientCalculate.setOnClickListener(btnClientCalculateListener);

        init();

    }

    private void init() {
        etxtNetVendeur.setText("150000");
        etxtNotaireInclus.setText("");
        txtnotaire.setText("");
        etxtFAI.setText("");
        txtFa.setText("");
        etxtClient.setText("");
        etxtNetVendeur.requestFocus();
    }

    private void reset() {
        net = 0;
        fa = 0;
        notaire = 0;
        client = 0;
    }

    private void calculateFAIFromNet(double net) {
        if (net <= 100000) {
            fa = net * 3.5 / 100;
        } else if (net > 100000 && net < 300000) {
            fa = net * 4.5 / 100;
        } else {
            fa = net * 5.5 / 100;
        }
        txtFa.setText(Double.toString(fa));
    }

    private void calculateNotaireNet(double net) {
        notaire = net * NOTAIRE_PERCENTAGE / 100;
        txtnotaire.setText(Double.toString(notaire));
    }

    private void calculateNetFromNotaireInclus(double notaireInclus) {
        net = notaireInclus / ( 1 + ( NOTAIRE_PERCENTAGE / 100 ) );
        notaire = net * NOTAIRE_PERCENTAGE / 100;
        txtnotaire.setText(Double.toString(notaire));
    }

    private void calculateNetFromFAI(double fai) {
        //net = notaireInclus / ( 1 + ( NOTAIRE_PERCENTAGE / 100 ) );
        //notaire = net * NOTAIRE_PERCENTAGE / 100;
        txtFa.setText(Double.toString(fa));
    }

    private View.OnClickListener btnNetCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (etxtNetVendeur.getText().toString() != "") {
                reset();
                net = Double.parseDouble(etxtNetVendeur.getText().toString());
                calculateFAIFromNet(net);
                double fai = net + fa;
                etxtFAI.setText(Double.toString(fai));
                calculateNotaireNet(net);
                double notaireInclus = net + notaire;
                etxtNotaireInclus.setText(Double.toString(notaireInclus));
                double client = net + notaire + fa;
                etxtClient.setText(Double.toString(client));
                Log.i("DEBUG", "Calcul depuis net vendeur : " + net + " FA = " + fa);
            }
        }
    };

    private View.OnClickListener btnNotaireCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (etxtNotaireInclus.getText().toString() != "") {
                reset();
                int notaireInclus = Integer.parseInt(etxtNotaireInclus.getText().toString());
                calculateNetFromNotaireInclus(notaireInclus);
                etxtNetVendeur.setText(Double.toString(net));
                calculateFAIFromNet(net);
                double fai = net + fa;
                etxtFAI.setText(Double.toString(fai));
                double client = net + notaire + fa;
                etxtClient.setText(Double.toString(client));
                Log.i("DEBUG", "Calcul depuis notaire inclus : " + notaireInclus);
            }
        }
    };

    private View.OnClickListener btnFAICalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (etxtFAI.getText().toString() != "") {
                reset();
                int fai = Integer.parseInt(etxtFAI.getText().toString());
                Log.i("DEBUG", "Calcul depuis FAI : " + fai);
            }
        }
    };

    private View.OnClickListener btnClientCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (etxtClient.getText().toString() != "") {
                reset();
                int client = Integer.parseInt(etxtClient.getText().toString());
                Log.i("DEBUG", "Calcul depuis client : " + client);
            }
        }
    };
}
