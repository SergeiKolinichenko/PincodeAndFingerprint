package sergeykolinichenko.name.pincode_and_fingerprint.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import sergeykolinichenko.name.pincode_and_fingerprint.R;
import sergeykolinichenko.name.pincode_and_fingerprint.screens.Presenter;
import sergeykolinichenko.name.pincode_and_fingerprint.screens.ViewFragment;

public class MainActivity extends AppCompatActivity implements Presenter.OnResultListener {

    private static final String MY_LOG = "myLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startEnterFragment();
    }

    public void startEnterFragment() {
        ViewFragment viewFragment = new ViewFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, viewFragment, null);
        transaction.commit();
    }

    public void onCompletedPin() {
        ResultFragment resultFragment = new ResultFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_bottom);
        transaction.replace(R.id.container, resultFragment, null);
        transaction.commit();

    }
}