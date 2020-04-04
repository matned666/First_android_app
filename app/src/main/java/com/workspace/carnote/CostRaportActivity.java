package com.workspace.carnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CostRaportActivity extends AppCompatActivity {

    private Button goToDetailedRaportFormButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_raport_layout);

        goToDetailedRaportFormButton = findViewById(R.id.goToDetailedRaportButton);
        goToDetailedRaportFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CostRaportActivity.this, DetailedCostRaport.class);
                startActivity(intent);
            }
        });

    }
}
