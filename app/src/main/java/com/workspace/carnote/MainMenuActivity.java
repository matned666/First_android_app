package com.workspace.carnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.workspace.carnote.model.AutoData;

public class MainMenuActivity extends AppCompatActivity {

    public static final String SPECIAL_DATA = "SPECIAL_DATA";
    private Button goToTankFormButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu_layout);
        goToTankFormButton = findViewById(R.id.go_to_tank_btn);

        goToTankFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GasTankUpActivity.class);
                intent.putExtra(SPECIAL_DATA, new AutoData("Ford", "Turneo Courier", "Blue"));
                startActivity(intent);
            }
        });
    }
}
