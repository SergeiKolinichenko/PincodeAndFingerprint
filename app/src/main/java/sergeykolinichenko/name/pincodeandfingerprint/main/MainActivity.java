package sergeykolinichenko.name.pincodeandfingerprint.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import sergeykolinichenko.name.pincodeandfingerprint.R;
import sergeykolinichenko.name.pincodeandfingerprint.screens.Presenter;
import sergeykolinichenko.name.pincodeandfingerprint.screens.ViewFragment;

public class MainActivity extends AppCompatActivity implements Presenter.OnResultListener {

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