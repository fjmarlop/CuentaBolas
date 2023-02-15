package com.example.cuentabolas;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;

import com.example.cuentabolas.R;

public class Preferencias extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FragmentoPreferencias())
                .commit();
    }

    public static class FragmentoPreferencias extends PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferencias_cuentabolas);
        }
    }

    public void volver(View v) {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

}