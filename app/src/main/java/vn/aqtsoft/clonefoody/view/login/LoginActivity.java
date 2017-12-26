package vn.aqtsoft.clonefoody.view.login;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import vn.aqtsoft.clonefoody.R;

public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        callFragment(new LoginFragment());
    }

    public void callFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frm_login,fragment)
                .commit();
    }
}
