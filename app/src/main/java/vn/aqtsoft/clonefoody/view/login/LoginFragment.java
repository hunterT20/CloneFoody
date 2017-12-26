package vn.aqtsoft.clonefoody.view.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.aqtsoft.clonefoody.R;
import vn.aqtsoft.clonefoody.view.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener{
    public static int REQUEST_CODE_LOGIN_GOOGLE = 99;
    public static int CHECK_PROVIDER_LOGIN = 0;
    private GoogleApiClient client;
    private FirebaseAuth firebaseAuth;

    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> loginResult;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_login_fragment, container, false);
        ButterKnife.bind(this,view);

        firebaseAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();
        initFaceBook();
        LoginManager.getInstance().registerCallback(callbackManager, loginResult);

        addViews();
        createClientGoogle();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        client.stopAutoManage(getActivity());
        client.disconnect();
    }

    private void addViews() {

    }

    /**
     * Khởi tạo client Google
     */
    private void createClientGoogle(){
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();
    }


    /**
     * Mở Fragment đăng nhập google
     */
    private void LoginGoogle(){
        CHECK_PROVIDER_LOGIN = 1;
        Intent intentGoogle = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(intentGoogle,REQUEST_CODE_LOGIN_GOOGLE);
    }


    /**
     * Dùng tokenID để chứng thưc đăng nhập Firebase
     * @param tokenID: Token ID
     */
    private void onEventLoginFirebase(String tokenID){
        AuthCredential authCredential;

        switch (CHECK_PROVIDER_LOGIN){
            case 1:
                authCredential = GoogleAuthProvider.getCredential(tokenID, null);
                firebaseAuth.signInWithCredential(authCredential);
                break;
            case 2:
                authCredential = FacebookAuthProvider.getCredential(tokenID);
                firebaseAuth.signInWithCredential(authCredential);

        }
    }


    /**
     * Xử lý dữ liệu trả về từ Fragment đăng nhập google
     * @param requestCode:
     * @param resultCode: trạng thái result (true/false)
     * @param data: dữ liệu trả về
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN_GOOGLE){
            if (resultCode == Activity.RESULT_OK){
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount account = signInResult.getSignInAccount();
                assert account != null;
                String Token_ID = account.getIdToken();
                onEventLoginFirebase(Token_ID);
            }
        }else {
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }


    /**
     * Event click button login bằng gmail
     */
    @OnClick(R.id.btn_Login_Google)
    public void onLoginGoogleClick(){
        LoginGoogle();
    }


    private void initFaceBook() {
        loginResult = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                CHECK_PROVIDER_LOGIN = 2;
                onEventLoginFirebase(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.e("initFacebook", "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("initFacebook", "onError: ");
            }
        };
    }

    @OnClick(R.id.btn_Login_Face)
    public void onLoginFacebookClick(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }

    @OnClick(R.id.btn_DangKy)
    public void onLoginDefaultClick(){
        ((LoginActivity)getActivity()).callFragment(new RegisterFragment());
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * Bắt trạng thái đăng nhập của user (có/không)
     */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }

}
