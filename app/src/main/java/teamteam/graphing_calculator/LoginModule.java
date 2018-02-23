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
    private int RC_SIGN_IN = 200;
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

//            Log.d("LOGINMODULE", "savedinstancestate is null");
            Bundle extras = getIntent().getExtras();

            actionsBasedOnItent(extras);

//            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Returns null if no account exists
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

//        Log.d("LOGINMODULE", "onStart()");

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null)
            Log.d("LOGINMODULE", "onStart(): currentUser is not null");
        else {
            Log.d("LOGINMODULE", "onStart(): currentUser is null");
//            finish();
        }

        updateUI(currentUser);
//        Log.d("LOGINMODULE", "finished updateUI() from onStart()");
    }

    private void actionsBasedOnItent(Bundle extras) {
        if (extras == null) {
                Log.d("ABOI", "extras are null");

            } else {
                Log.d("ABOI", "extras not null");

//                changeStatus(extras);
                getStatus(extras);
                changeStatus(extras);
            }
    }

    private void getStatus(Bundle extras) {

       String userStatus = extras.getString("userStatusCheck");

       if (userStatus != null) {
           if ( userStatus.equals("checkUserStatus")) {
               Log.d("getStatus", "checking user status");
               checkUserStatus();
//               finishActivity(100);
               finish();
           }
       }
       else {
           Log.d("getStatus","userStatus is null");
       }

    }

    private void changeStatus(Bundle extras) {

        String changeStatus = extras.getString("userStatus");

        if (changeStatus != null) {
            if (changeStatus.equals("signIn")) {
                signIn();
            } else if (changeStatus.equals("signOut")) {
                signOut();
                finish();
            }
        }
        else {
            Log.d("changeStatus", "changeStatus is null");
        }

    }

    protected void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        Log.d("signIn", "signIn()");

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Maybe extend this to be safe
        if (currentUser != null)
            Log.d("signOut", "signing out user " + currentUser.getDisplayName());

        mAuth.signOut();

//        FirebaseAuth.getInstance().signOut();

        Log.d("signOut", "signed out");
//        finish();

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
//                        finish();
                    }
                });
    }

    // function to check if the user is signed in or not
    private void checkUserStatus(){
        FirebaseUser currentUser = mAuth.getCurrentUser();

        boolean loginStatus;

        if (currentUser != null) {
           loginStatus = true;
           Log.d("checkUserStatus", "User - " + currentUser.getDisplayName());
        } else {
           loginStatus = false;
        }

        Log.d("checkUserStatus","" + loginStatus);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("loginStatus", loginStatus);
        setResult(RESULT_OK, intent);

//        finishActivity(100);
//        Log.d("LOGINMODULE", "Sending back activity result of checkUserStatus");

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebase", "firebaseAuthWithGoogle()");
        Log.d("firebase", "firebaseAuthWithGoogle: " + acct.getDisplayName());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("firebase", "\nsignInWithCredential:successful\n");

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
//                            finishActivity(RC_SIGN_IN);
                        } else {
                            Log.d("firebase", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginModule.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("LM:OnActivity", "inside");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                Log.d("LM:OnActivity", "Inside try statement");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.d(TAG, "Google sign in failed", e);
                updateUI(null);
            }

            //handleSignInResult(task);
        }
    }


    //  GoogleSignInAccount user
    protected void updateUI(FirebaseUser account) {
//        Log.d("LOGINEMUDLE", "inside updateUI()");

        if (account != null) { // If there is someone signed in

            String nameAndEmail = account.getDisplayName() + "\n" + account.getEmail();
            //email.show();
            Toast toast = Toast.makeText(this, nameAndEmail, Toast.LENGTH_LONG);
            toast.show();

//            Intent intent = new Intent(this, MainActivity.class);
            Log.d("LM:updateUI", "successful");
            finish();
            //startActivity(intent);
        }

        else {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            Log.d("LM:updateUI", "Account is null");
//            finish();
        }
    }

//    protected void updateUIMod(FirebaseUser account) {
//        if (account == null) {
//            Log.d("LM:updt2", "");
//            finish();
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
}
