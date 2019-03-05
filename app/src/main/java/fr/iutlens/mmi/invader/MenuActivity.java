package fr.iutlens.mmi.invader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }
    public void onClick(View v) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}
