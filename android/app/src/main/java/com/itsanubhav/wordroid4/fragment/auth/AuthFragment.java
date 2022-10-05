package com.itsanubhav.wordroid4.fragment.auth;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.itsanubhav.wordroid4.Config;
import com.itsanubhav.wordroid4.ContainerActivity;
import com.itsanubhav.wordroid4.R;
import com.itsanubhav.wordroid4.fragment.webview.WebViewFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.itsanubhav.libdroid.model.auth.AuthResponse;
import com.itsanubhav.libdroid.repo.AuthRepository;

import java.util.Arrays;

import static android.app.Activity.RESULT_OK;


public class AuthFragment extends Fragment {

    private AuthRepository authRepository;
    private LiveData<AuthResponse> liveData = new MutableLiveData<>();
    private FirebaseAuth mAuth;
    private static final String TAG = "GoogleActivity";
    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    private MaterialButton otherLoginBtn;

    private MaterialButton loginBtn,signUpBtn,forgotPassBtn;

    private TextInputLayout usernameLayout,passLayout;

    public static AuthFragment newInstance(){
        return new AuthFragment();
    }

    public AuthFragment() {
        authRepository = AuthRepository.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_auth, container, false);

        TextView loginWithText = rootView.findViewById(R.id.loginWithText);

        try {
            ContainerActivity containerActivity = (ContainerActivity) getActivity();
            containerActivity.hideToolbar();
        }catch (Exception e){

        }



        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()==null){
            getActivity().finish();
        }

        otherLoginBtn = rootView.findViewById(R.id.otherLogin);

        usernameLayout = rootView.findViewById(R.id.username);
        passLayout = rootView.findViewById(R.id.password);

        loginBtn = rootView.findViewById(R.id.loginBtn);
        forgotPassBtn = rootView.findViewById(R.id.forgotPassword);
        signUpBtn = rootView.findViewById(R.id.signup);

        loginBtn.setOnClickListener(view -> loginBtnClicked());

        forgotPassBtn.setOnClickListener(view -> {
            forgotPassBtnClicked();
        });

        signUpBtn.setOnClickListener(view -> {
            signupBtnClicked();
        });

        otherLoginBtn.setOnClickListener(view -> signIn());

        if (!Config.ENABLE_WORDPRESS_LOGIN){
            loginWithText.setVisibility(View.GONE);
            otherLoginBtn.setVisibility(View.GONE);
            usernameLayout.setVisibility(View.GONE);
            passLayout.setVisibility(View.GONE);
            forgotPassBtn.setVisibility(View.GONE);
            loginBtn.setVisibility(View.GONE);
            signUpBtn.setVisibility(View.GONE);
            signIn();

        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    private void signupBtnClicked(){
        /*Intent intent = new Intent(getContext(),ContainerActivity.class);
        intent.putExtra("screen","webview");
        intent.putExtra("title","Register");
        intent.putExtra("url","https://dev.itsanubhav.com/wp-login.php?action=register");
        startActivity(intent);*/

        if (getContext() instanceof ContainerActivity){
            try {
                ContainerActivity a = (ContainerActivity) getActivity();
                a.addFragment(new WebViewFragment().newInstance("https://dev.itsanubhav.com/wp-login.php?action=register"),"Register");
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void forgotPassBtnClicked(){

    }

    private void loginBtnClicked(){
        String username = usernameLayout.getEditText().getText().toString();
        String password = passLayout.getEditText().getText().toString();
        if (!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
            authRepository.makeAuth(username,password).observe(this, authResponse -> {
                if (authResponse!=null){
                        if (authResponse.getToken()!=null){
                            Toast.makeText(getContext(),"Login successful "+authResponse.getUserDisplayName(),Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.defaultSharedPref, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token",authResponse.getToken());
                            editor.putString("name",authResponse.getUserDisplayName());
                            editor.putString("email",authResponse.getUserEmail());
                            editor.apply();
                            getActivity().onBackPressed();
                        }else {
                            Toast.makeText(getContext(),"Error: "+authResponse.getMessage(),Toast.LENGTH_LONG).show();
                        }
                }else {
                    Toast.makeText(getContext(),"Username or Password didn't match",Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(getContext(),"Username and Password are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(), //Google Login
                                new AuthUI.IdpConfig.PhoneBuilder().build())  //Phone Login
                        )
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                //startActivity(AuthActivity.createIntent(this, response));
                if (mAuth.getCurrentUser()!=null) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences(Config.defaultSharedPref, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (mAuth.getCurrentUser().getDisplayName()!=null)
                        editor.putString("name", mAuth.getCurrentUser().getDisplayName());
                    if (mAuth.getCurrentUser().getEmail()!=null)
                        editor.putString("email", mAuth.getCurrentUser().getEmail());
                    editor.apply();
                }
                getActivity().finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    //showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //showSnackbar(R.string.no_internet_connection);
                    return;
                }

                //showSnackbar(R.string.unknown_error);
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }



}
