package com.noovimo.noovimocalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

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

    private Button btnClear;
    private Button btnSettings;

    private double net;
    private double fa;
    private double notaire;
    private double client;

    private NumberFormat formatter = new DecimalFormat("#0.0");

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

        btnClear = (Button) findViewById(R.id.btnClear);
        btnSettings = (Button) findViewById(R.id.btnSettings);

        btnNetCalculate.setOnClickListener(btnNetCalculateListener);
        btnNotaireCalculate.setOnClickListener(btnNotaireCalculateListener);
        btnFAICalculate.setOnClickListener(btnFAICalculateListener);
        btnClientCalculate.setOnClickListener(btnClientCalculateListener);

        btnClear.setOnClickListener(btnClearListener);
        btnSettings.setOnClickListener(btnSettingsListener);

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

    private void clearAll() {
        txtFa.setText("");
        txtnotaire.setText("");

        etxtNetVendeur.setText("");
        etxtNotaireInclus.setText("");
        etxtFAI.setText("");
        etxtClient.setText("");
    }

    private void calculateFAIFromNet(double net) {
        if (net <= 100000) {
            fa = net * 3.5 / 100;
        } else if (net > 100000 && net < 300000) {
            fa = net * 4.5 / 100;
        } else {
            fa = net * 5.5 / 100;
        }
        txtFa.setText(format(fa));
    }

    private void calculateNotaireNet(double net) {
        notaire = net * getNotaryPercentage() / 100;
        txtnotaire.setText(format(notaire));
    }

    private float getNotaryPercentage() {
        preferences = getSharedPreferences("PREFS", 0);
        return preferences.getFloat("etxtNotaire", (float) 7.5);
    }

    private void calculateNetFromNotaireInclus(double notaireInclus) {
        float percentage = getNotaryPercentage();
        net = notaireInclus / ( 1 + ( percentage / 100 ) );
        notaire = net * percentage / 100;
        txtnotaire.setText(format(notaire));
    }

    private void calculateNetFromFAI(double fai) {
        double nettmp = fai / ( 1 + ( 3.5 / 100 ) );
        if (nettmp <= 100000) {
            net = nettmp;
        } else {
            nettmp = fai / ( 1 + ( 4.5 / 100 ) );
            if (nettmp > 100000 && nettmp < 300000) {
                net = nettmp;
            } else {
                net = fai / ( 1 + ( 5.5 / 100 ) );
            }
        }
        fa = fai - net;
        txtFa.setText(format(fa));
    }

    private void calculateNetFromClient(double client) {
        float percentage = getNotaryPercentage();
        double fap = 3.5;
        double nettmp = client / ( 1 + ( fap / 100 ) + (percentage / 100) );
        if (nettmp <= 100000) {
            net = nettmp;
        } else {
            fap = 4.5;
            nettmp = client / ( 1 + ( fap / 100 ) + (percentage / 100) );
            if (nettmp > 100000 && nettmp < 300000) {
                net = nettmp;
            } else {
                fap = 5.5;
                net = client / ( 1 + ( fap / 100 ) + (percentage / 100) );
            }
        }
        fa = net * fap / 100;
        txtFa.setText(format(fa));
        notaire = net * percentage / 100;
        txtnotaire.setText(format(notaire));
    }

    private String format(double value) {
        return formatter.format(value) + " €";
    }

    private String simpleFormat(double value) {
        return formatter.format(value);
    }

    private View.OnClickListener btnNetCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(etxtNetVendeur.getText())) {
                reset();
                net = Double.parseDouble(etxtNetVendeur.getText().toString());
                calculateFAIFromNet(net);
                double fai = net + fa;
                etxtFAI.setText(simpleFormat(fai));
                calculateNotaireNet(net);
                double notaireInclus = net + notaire;
                etxtNotaireInclus.setText(simpleFormat(notaireInclus));
                double client = net + notaire + fa;
                etxtClient.setText(simpleFormat(client));
            }
        }
    };

    private View.OnClickListener btnNotaireCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(etxtNotaireInclus.getText())) {
                Log.i("DEBUG", "je passe !!!" + etxtNotaireInclus.getText().toString());
                reset();
                double notaireInclus = Double.parseDouble(etxtNotaireInclus.getText().toString());
                calculateNetFromNotaireInclus(notaireInclus);
                etxtNetVendeur.setText(simpleFormat(net));
                calculateFAIFromNet(net);
                double fai = net + fa;
                etxtFAI.setText(simpleFormat(fai));
                double client = net + notaire + fa;
                etxtClient.setText(simpleFormat(client));
            }
        }
    };

    private View.OnClickListener btnFAICalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(etxtFAI.getText())) {
                reset();
                double fai = Double.parseDouble(etxtFAI.getText().toString());
                calculateNetFromFAI(fai);
                etxtNetVendeur.setText(simpleFormat(net));
                calculateFAIFromNet(net);
                calculateNotaireNet(net);
                double notaireInclus = net + notaire;
                etxtNotaireInclus.setText(simpleFormat(notaireInclus));
                double client = net + notaire + fa;
                etxtClient.setText(simpleFormat(client));
            }
        }
    };

    private View.OnClickListener btnClientCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(etxtClient.getText())) {
                reset();
                double client = Double.parseDouble(etxtClient.getText().toString());
                calculateNetFromClient(client);
                etxtNetVendeur.setText(simpleFormat(net));
                calculateFAIFromNet(net);
                double fai = net + fa;
                etxtFAI.setText(simpleFormat(fai));
                calculateNotaireNet(net);
                double notaireInclus = net + notaire;
                etxtNotaireInclus.setText(simpleFormat(notaireInclus));
            }
        }
    };

    private View.OnClickListener btnClearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            reset();
            clearAll();
        }
    };

    private View.OnClickListener btnSettingsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
    };

}
