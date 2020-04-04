package com.workspace.carnote;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GpsActivity extends AppCompatActivity {

    private int bestTimeTo100;
    private int secTo100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inicjalizacja
        //odzyskać ostatnie zapisy
        if(savedInstanceState != null){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //podpięcie gps
    }

    @Override
    protected void onPause() {
        super.onPause();
        //odepnij gps
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //zapisać do bunle stan aktual;nej pozycji
        //ostatnie odczyty
        //rekord
        super.onSaveInstanceState(outState);
    }
}
