package vn.aqtsoft.clonefoody.view;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import vn.aqtsoft.clonefoody.R;

public class FlashScreenActivity extends AppCompatActivity{
    private TextView txtvVersionApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashscreen);

        txtvVersionApp = findViewById(R.id.txtvVersionApp);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            txtvVersionApp.setText(String.format("%s %s", getString(R.string.version_app), packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
