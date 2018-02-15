package teamteam.graphing_calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by makloooo on 2/1/18.
 */


// LOOK UP PARCELABLE In order to return objects


public class LoginModule extends AppCompatActivity implements
        View.OnClickListener {


//    private AppCompatActivity context;

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    //private GoogleSignInAccount userAccount;
    //private GoogleSignInOptions mGoogleSignInOptions;
    private int RC_SIGN_IN = 779;
    private static final String TAG = MainActivity.class.getSimpleName();


    LoginModule() {
    }


    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_login);
       findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Configures options to request when signing in.
        // .requestIdToken("617175976667-ljedvh5ceofngqtmu18qt9e2gobgfn6h.apps.googleusercontent.com")
        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);
        mAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {

            Log.d("LOGINMODULE", "savedinstancestate is null");

            Bundle extras = getIntent().getExtras();

            if (extras == null) {
                Log.d("LOGINMODULE", "extras are null");

            } else {
                Log.d("LOGINMODULE", "extras not null");

                String userStatusCheck = extras.getString("userStatusCheck");
                String userStatus = extras.getString("userStatus");

                if ( userStatusCheck != null ) {
                    if (userStatusCheck.equals("checkUserStatus")) {
                        Log.d("LOGINMODULE", "checking user status");
                        checkUserStatus();
                    }
                }

                if (userStatus != null) {
                    if (userStatus.equals("signIn")) {
                        Log.d("LOGINMODULE", "signIn method is about to be called");
                        signIn();
                    } else if ( userStatus.equals("signOut")) {
                        Log.d("LOGINMODULE", "signOut method is about to be called");
                        signOut();
                    }
                }

                // Might have to do a catch here

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Returns null if no account exists
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

        Log.d("LOGINMODULE", "onStart()");

        FirebaseUser currentUser = mAuth.getCurrentUser();

//        updateUI(account);
        updateUI(currentUser);
    }

    private void checkUserStatus(){
        FirebaseUser currentUser = mAuth.getCurrentUser();

        boolean loginStatus;

        if (currentUser != null) {
           loginStatus = true;
        } else {
           loginStatus = false;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("loginStatus", loginStatus);
        setResult(RESULT_OK, intent);

        Log.d("LOGINMODULE", "Sending back activity result of checkUserStatus");

        finish();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:successful");

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginModule.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("LOGINMODULE", "inside onActivityResult");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                Log.d("LOGINMODULE", "Inside try statement");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                finish();
                updateUI(null);
            }

            //handleSignInResult(task);
        }
    }

    protected void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        Log.d("LOGINMODULE", "calling startActivityForResult");

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();

        Log.d("LOGINMODULE", "signing out");

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       //updateUI(null);
                        finish();
                    }
                });
    }

    //  GoogleSignInAccount user
    protected void updateUI(FirebaseUser account) {
        if (account != null) { // If there is someone signed in

            String nameAndEmail = account.getDisplayName() + "\n" + account.getEmail();
            //email.show();

            Toast toast = Toast.makeText(this, nameAndEmail, Toast.LENGTH_LONG);

            toast.show();

            Intent intent = new Intent(this, MainActivity.class);
            finish();
            //startActivity(intent);
        }

        else {
            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            Log.w("LOGINMODULEERROR", "Account is null");
            //finish();
        }

        /*
        userAccount = user;
        if (user == null) Toast.makeText(context, "Account is null", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, userAccount.getEmail(), Toast.LENGTH_SHORT).show();
        */
    }

    /*
    protected void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code="+ e.getStatusCode());
            updateUI(null);
        }
    }
    */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }



























}
