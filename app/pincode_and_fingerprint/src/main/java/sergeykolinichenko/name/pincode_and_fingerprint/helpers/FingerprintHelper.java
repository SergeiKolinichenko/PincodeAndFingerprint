package sergeykolinichenko.name.pincode_and_fingerprint.helpers;

import android.content.Context;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import javax.crypto.Cipher;

import sergeykolinichenko.name.pincode_and_fingerprint.R;
import sergeykolinichenko.name.pincode_and_fingerprint.screens.Presenter;
import sergeykolinichenko.name.pincode_and_fingerprint.utils.CryptoUtils;

public class FingerprintHelper extends FingerprintManagerCompat.AuthenticationCallback {

    private final Context context;
    private final PreferenceHelper preferenceHelper;

    public FingerprintHelper(Context context, PreferenceHelper preferenceHelper) {
        this.context = context;
        this.preferenceHelper = preferenceHelper;
    }

    public void startAuth(FingerprintManagerCompat.CryptoObject cryptoObject) {
        CancellationSignal mCancellationSignal = new CancellationSignal();
        FingerprintManagerCompat manager = FingerprintManagerCompat.from(context);
        manager.authenticate(cryptoObject, 0, mCancellationSignal, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        Presenter.showToast(errString.toString());
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Presenter.showToast(helpString.toString());
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {

        Cipher cipher = result.getCryptoObject().getCipher();
        if (cipher != null) {
            String encoded = preferenceHelper.getString(PreferenceHelper.PIN_CODE);
            String decoded = CryptoUtils.decode(encoded, cipher);
            if(decoded != null) {
                Presenter.handlerResult(decoded);
            }
        }
    }

    @Override
    public void onAuthenticationFailed() {
        Presenter.showToast(context.getResources().getString(R.string.finger_pint_failed_to_recognize));
    }
}
