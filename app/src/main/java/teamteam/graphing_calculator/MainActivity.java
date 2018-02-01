package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LoginModule mLoginModule;

    MainActivity() { mLoginModule = new LoginModule(this); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginModule.onCreate();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLoginModule.onStart();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.sign_in_button) mLoginModule.signIn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginModule.onActivityResult(requestCode, data);
    }
}
