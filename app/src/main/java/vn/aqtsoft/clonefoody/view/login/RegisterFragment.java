package vn.aqtsoft.clonefoody.view.login;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vn.aqtsoft.clonefoody.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    @BindView(R.id.edt_Email)    EditText edt_Email;
    @BindView(R.id.edt_Password) EditText edt_Password;
    @BindView(R.id.edt_Password_config) EditText edt_Password_config;
    @BindView(R.id.edt_UserName) EditText edt_Username;

    private FirebaseAuth firebaseAuth;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_register_fragment, container, false);
        ButterKnife.bind(this,view);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @OnClick(R.id.btn_Register_Default)
    public void onRegisterDefaultClick(){
        final String email = edt_Email.getText().toString().trim();
        String password = edt_Password.getText().toString().trim();
        String password_config = edt_Password_config.getText().toString().trim();
        String username = edt_Username.getText().toString().trim();

        if (email.length() == 0){
            Toast.makeText(getActivity(), "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
        }else if (password.length() == 0){
            Toast.makeText(getActivity(), "Vui lòng nhập password!", Toast.LENGTH_SHORT).show();
        }else if (username.length() == 0){
            Toast.makeText(getActivity(), "Vui lòng nhập username!", Toast.LENGTH_SHORT).show();
        }else if (!password_config.equals(password)){
            Toast.makeText(getActivity(), "Password nhập lại không đúng!", Toast.LENGTH_SHORT).show();
        }else {
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(getContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    ((LoginActivity)getActivity()).callFragment(new LoginFragment());
                    ((LoginActivity)getActivity()).email = email;
                }
            });
        }
    }

}
