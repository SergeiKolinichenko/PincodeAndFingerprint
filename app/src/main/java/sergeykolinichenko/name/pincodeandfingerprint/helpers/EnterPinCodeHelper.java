package sergeykolinichenko.name.pincodeandfingerprint.helpers;

import sergeykolinichenko.name.pincodeandfingerprint.screens.ViewFragment;

public class EnterPinCodeHelper {

    private static final int NUMBER_OF_DIGITS = 4;

    private static int countDigits = 0;
    private static String thisPinCode = "";

    public interface ResultCallback {
        void onComplete(String result);
    }

    // колбэк смены цвета индикатора цифр
    public interface ColorLedCallback {
        void setColorLed(int digit, int color);
    }

    public static boolean handlerPinCode(String pinCode, int button, ResultCallback callback,
                                       ColorLedCallback ledCallback) {

        if(countDigits == 0 ) {
            for (int i = 0; i < NUMBER_OF_DIGITS; i++) {
                ledCallback.setColorLed(i + 1, ViewFragment.COLOR_WIGHT);
            }
        }

        if(button != ViewFragment.BUTTON_BACK && button != ViewFragment.BUTTON_FINGERPRINT) {
            if(countDigits != NUMBER_OF_DIGITS) {
                thisPinCode = thisPinCode + button;
                countDigits++;
                ledCallback.setColorLed(countDigits, ViewFragment.COLOR_YELLOW);
                if(countDigits == NUMBER_OF_DIGITS) {
                    checkPinCode(thisPinCode, pinCode, callback);
                }
            }
        } else if (button == ViewFragment.BUTTON_BACK && countDigits != 0) {
            thisPinCode = backEnterPinCode(thisPinCode, ledCallback);
        }
        return false;
    }

    private static String backEnterPinCode(String enterPinCode, ColorLedCallback ledCallback) {

        enterPinCode = enterPinCode.substring(0, enterPinCode.length()-1);
        ledCallback.setColorLed(countDigits, ViewFragment.COLOR_WIGHT);
        countDigits--;
        return enterPinCode;
    }

    private static void checkPinCode(String testPinCode, String pinCode, ResultCallback callback) {

        if(pinCode.equals(testPinCode)) {
            callback.onComplete(pinCode);
        } else {
            callback.onComplete(testPinCode);
        }
        countDigits = 0;
        thisPinCode = "";
    }

    public static int getLengthEnterPinCode() {
        return thisPinCode.length();
    }

}
