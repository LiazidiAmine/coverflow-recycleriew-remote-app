package info.androidhive.cardview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by amine on 01/11/16.
 */

public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

    }

    public void screenTapped(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
