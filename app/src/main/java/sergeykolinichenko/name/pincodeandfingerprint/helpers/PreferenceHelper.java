package sergeykolinichenko.name.pincodeandfingerprint.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    public static final String FINGERPRINT_CODE = "fingerprint_code";
    public static final String PIN_CODE = "pin_code";
    public static final String IS_PIN_CODE_ENTRY = "is_pin_cod_entry";

    private static PreferenceHelper instance;
    private static SharedPreferences preferences;

    private PreferenceHelper() {
    }

    // В методе получаем обьект PreferenceHelper instance
    public static PreferenceHelper getInstance() {
        if (instance == null) {
            instance = new PreferenceHelper();
        }
        return instance;
    }

    public void init(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean thereIsPreference(String alias) {
        return preferences.contains(alias);
    }

    // сохраняем настройки тип Boolean
    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // сохраняем настройки тип String
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value); // putBoolean(key, value);
        editor.apply();
    }

    // возвращает настройки тип Boolean, ранее сохраненые
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    // возвращает настройки тип String, ранее сохраненые
    public String getString(String key) {
        return preferences.getString(key, "");
    }
}
