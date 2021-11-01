package sergeykolinichenko.name.pincodeandfingerprint.helpers;

import android.annotation.SuppressLint;

import sergeykolinichenko.name.pincodeandfingerprint.screens.ViewFragment;

public class PinCodeCreateHelper {

    private static final String MY_LOG = "myLog";

    public static final String MANY_SIMILAR_DIGITS = "many_similar_digits";

    public static final int NOT_PRESSED = -1;

    // иницилизируем поля пин-кода
    private static int digit_1 = NOT_PRESSED;
    private static int digit_2 = NOT_PRESSED;
    private static int digit_3 = NOT_PRESSED;
    private static int digit_4 = NOT_PRESSED;

    // колбэк смены цвета индикатора цифр
    public interface ColorLedCallback {
        void setColorLed(int led, int color);
    }

    // колбэк записи результата, и попутно выведения сообщения о повторяющихся цифрах в пин-коде
    public interface ResultPinCodeCallback {
        void resultPinCode(String result);
    }

    // статус пин-кода - не заполнен
    public static boolean getStatusPinCode() {
        return digit_1 == NOT_PRESSED || digit_2 == NOT_PRESSED ||
                digit_3 == NOT_PRESSED || digit_4 == NOT_PRESSED;
    }

    // обработчик рапределяющий индекы кнопок цифр и не-цифр
    public static void buttonClickHandler(int button, ColorLedCallback ledCallback,
                                          ResultPinCodeCallback resultCallback) {
        if(button == ViewFragment.BUTTON_BACK) {
            backButtonHandler(ledCallback);
        } else if (button != ViewFragment.BUTTON_FINGERPRINT) {
            digitsButtonsHandler(button, ledCallback, resultCallback);
        }
    }

    // обработчик кнопок цифр
    private static void digitsButtonsHandler(int button, ColorLedCallback ledCallback,
                                             ResultPinCodeCallback resultCallback) {

        if (button != ViewFragment.BUTTON_BACK && button != ViewFragment.BUTTON_FINGERPRINT) {

            if (digit_1 == NOT_PRESSED) {
                digit_1 = button;
                ledCallback.setColorLed(ViewFragment.LED_1, ViewFragment.COLOR_YELLOW);

            } else if (digit_2 == NOT_PRESSED) {
                digit_2 = button;
                ledCallback.setColorLed(ViewFragment.LED_2, ViewFragment.COLOR_YELLOW);

            } else if (digit_3 == NOT_PRESSED) {

                if (digit_1 == digit_2 && button == digit_1) {
                    resultCallback.resultPinCode(MANY_SIMILAR_DIGITS);
                } else {
                    digit_3 = button;
                    ledCallback.setColorLed(ViewFragment.LED_3, ViewFragment.COLOR_YELLOW);
                }

            } else if (digit_4 == NOT_PRESSED) {

                if ((digit_1 == digit_2 && button == digit_1)
                        || (digit_1 == digit_3 && button == digit_3)
                        || (digit_2 == digit_3 && button == digit_3)) {

                    resultCallback.resultPinCode(MANY_SIMILAR_DIGITS);

                } else {
                    digit_4 = button;
                    ledCallback.setColorLed(ViewFragment.LED_4, ViewFragment.COLOR_YELLOW);
                    @SuppressLint("DefaultLocale") String pinCod =
                            String.format("%d%d%d%d", digit_1, digit_2, digit_3, digit_4);
                    resultCallback.resultPinCode(pinCod);
                }

            }
        }
    }

    // обработчик кнопок не-цифр
    private static void backButtonHandler(ColorLedCallback ledCallback) {
        if(digit_3 != NOT_PRESSED) {
            digit_3 = NOT_PRESSED;
            ledCallback.setColorLed(ViewFragment.LED_3, ViewFragment.COLOR_WIGHT);
        } else if (digit_2 != NOT_PRESSED) {
            digit_2 = NOT_PRESSED;
            ledCallback.setColorLed(ViewFragment.LED_2, ViewFragment.COLOR_WIGHT);
        } else if (digit_1 != NOT_PRESSED) {
            digit_1 = NOT_PRESSED;
            ledCallback.setColorLed(ViewFragment.LED_1, ViewFragment.COLOR_WIGHT);
        }
    }

}
