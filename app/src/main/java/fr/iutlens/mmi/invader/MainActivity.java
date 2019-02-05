package fr.iutlens.mmi.invader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fr.iutlens.mmi.invader.utils.Pad;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.gameView);
        Pad pad = findViewById(R.id.pad);

        gameView.setPad(pad);

        TextView vieView = (TextView) findViewById(R.id.vieView);
        gameView.setVieView(vieView);
    }

/*    public void onLeft(View view) {
        gameView.onLeft();
    }

    public void onRight(View view) {
        gameView.onRight();
    }
*/
    public void onFire(View view) {
        gameView.onFire();
    }
}
