package com.noovimo.noovimocalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SettingsActivity extends AppCompatActivity {

    private EditText etxtNotaire;

    SharedPreferences preferences;

    float notaryPercentage;

    private NumberFormat formatter = new DecimalFormat("#0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences("PREFS", 0);
        notaryPercentage = preferences.getFloat("etxtNotaire", (float) 7.5);

        etxtNotaire = (EditText) findViewById(R.id.etxtSettingsNotaire);
        etxtNotaire.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    notaryPercentage = Float.parseFloat(etxtNotaire.getText().toString());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putFloat("etxtNotaire", notaryPercentage);
                    editor.apply();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        init();

    }

    private void init() {
        etxtNotaire.setText(simpleFormat(notaryPercentage));
        etxtNotaire.requestFocus();
    }

    private String simpleFormat(double value) {
        return formatter.format(value);
    }

}
