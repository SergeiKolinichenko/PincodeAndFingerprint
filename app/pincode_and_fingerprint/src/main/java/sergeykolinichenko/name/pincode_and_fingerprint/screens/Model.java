package sergeykolinichenko.name.pincode_and_fingerprint.screens;

import sergeykolinichenko.name.pincode_and_fingerprint.helpers.PreferenceHelper;
import sergeykolinichenko.name.pincode_and_fingerprint.utils.CryptoUtils;

public class Model {

    private static PreferenceHelper preferenceHelper;

    public Model(PreferenceHelper preferenceHelper) {
        Model.preferenceHelper = preferenceHelper;
    }

    public String getPinCode() {
        if(preferenceHelper.thereIsPreference(PreferenceHelper.PIN_CODE)) {
            String pin = preferenceHelper.getString(PreferenceHelper.PIN_CODE);
            return CryptoUtils.decode(pin, CryptoUtils.getDecryptCipher());
        }
        return null;
    }

    public void savePin(String pin) {
        String encoded = CryptoUtils.encode(pin);
        preferenceHelper.putString(PreferenceHelper.PIN_CODE, encoded);
    }
}
