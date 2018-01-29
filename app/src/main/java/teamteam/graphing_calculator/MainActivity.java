package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sign_in = findViewById(R.id.gotoauth);
        sign_in.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int targetId = view.getId();
        if (targetId == R.id.gotoauth) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}
