package teamteam.graphing_calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Created by makloooo on 2/1/18.
 */

public class LoginModule {

    private AppCompatActivity context;

    GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount userAccount;
    private GoogleSignInOptions mGoogleSignInOptions;
    private int RC_SIGN_IN = 779;
    private static final String TAG = MainActivity.class.getSimpleName();

    LoginModule(AppCompatActivity activity) {
        context = activity;
    }

    protected void onCreate() {
        // Configures options to request when signing in.
        // .requestIdToken("617175976667-ljedvh5ceofngqtmu18qt9e2gobgfn6h.apps.googleusercontent.com")
        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, mGoogleSignInOptions);
    }

    protected void onStart() {
        // Returns null if no account exists
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        updateUI(account);
    }

    protected void onActivityResult(int requestCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    protected void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        context.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void updateUI(GoogleSignInAccount user) {
        userAccount = user;
        if (user == null) Toast.makeText(context, "Account is null", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, userAccount.getEmail(), Toast.LENGTH_SHORT).show();
    }

    protected void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code="+ e.getStatusCode());
            updateUI(null);
        }
    }
}
