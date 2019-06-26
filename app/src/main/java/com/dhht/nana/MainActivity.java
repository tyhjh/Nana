package com.dhht.nana;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.dhht.nana.service.AutoService;
import com.dhht.nana.util.AccessbilityUtil;

public class MainActivity extends AppCompatActivity {


    Button btStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btStart = findViewById(R.id.btStart);
        btStart.setOnClickListener(v -> {
            if (!AccessbilityUtil.isAccessibilitySettingsOn(this, AutoService.class)) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
    }
}
