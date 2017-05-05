package com.example.clientdome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ClientMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new OtherFragment()).commit();
    }


}
