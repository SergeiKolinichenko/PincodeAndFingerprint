package sergeykolinichenko.name.pincodeandfingerprint.screens;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import sergeykolinichenko.name.pincodeandfingerprint.R;
import sergeykolinichenko.name.pincodeandfingerprint.helpers.EnterPinCodeHelper;
import sergeykolinichenko.name.pincodeandfingerprint.helpers.FingerprintHelper;
import sergeykolinichenko.name.pincodeandfingerprint.helpers.PinCodeCreateHelper;
import sergeykolinichenko.name.pincodeandfingerprint.helpers.PreferenceHelper;
import sergeykolinichenko.name.pincodeandfingerprint.utils.CryptoUtils;
import sergeykolinichenko.name.pincodeandfingerprint.utils.FingerprintUtils;

public class Presenter {

    private static Model model;
    private static Contract view;
    private Context context;
    private FingerprintHelper fingerprintHelper;
    private PreferenceHelper preferenceHelper;
    private static OnResultListener onResultListener;
    private static boolean correctPin = false;

    public Presenter(Model model, FingerprintHelper fingerprintHelper) {
        Presenter.model = model;
        this.fingerprintHelper = fingerprintHelper;
    }

    public interface OnResultListener {
        void onCompletedPin();
    }

    public void attachView(Contract view, Context context) {
        Presenter.view = view;
        this.context = context;
        onResultListener = (OnResultListener) context;
        preferenceHelper = PreferenceHelper.getInstance();
        preferenceHelper.init(context);
    }

    public void viewIsReady() {
        if (preferenceHelper.thereIsPreference(PreferenceHelper.PIN_CODE)) {
            if (FingerprintUtils.checkFingerprintCompatibility(context)) {
                view.setBottomSheetStateExpanded();
                prepareSensor();
            }
        } else {
            showText(context.getResources().getString(R.string.pin_code_text_enter_new_pin));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void prepareSensor() {
        if (FingerprintUtils.isSensorStateAt(FingerprintUtils.mSensorState.READY, context)) {
            FingerprintManagerCompat.CryptoObject cryptoObject = CryptoUtils.getCryptoObject();
            if (cryptoObject != null) {
                fingerprintHelper = new FingerprintHelper(context, preferenceHelper);
                fingerprintHelper.startAuth(cryptoObject);
            }
        }

    }


    public void onClickButton(int index) {
        if (index == ViewFragment.BUTTON_FINGERPRINT) { // если кнопка FINGERPRINT подьём bottom_sheet
            view.setBottomSheetStateExpanded();
            // если пин-кода нет, и пин-код еще не полностью набран, отправляем индекс кнопки в PinCodeCreateHelper
        } else if (!preferenceHelper.thereIsPreference(PreferenceHelper.PIN_CODE)
                && PinCodeCreateHelper.getStatusPinCode()) { // PinCodeCreateHelper,
            PinCodeCreateHelper.buttonClickHandler(index,               // а так же в колбэк,
                    (led, color) -> view.setColorToLed(led, color), // метод смены цвета индикатора
                    (this::savePin));    // и метод сохранения результата пин-кода

            // если пин код есть в настройках, отправляем пин код, кнопки и калбэк в EnterPinCodeHelper
        } else if (preferenceHelper.thereIsPreference(PreferenceHelper.PIN_CODE)) {
            EnterPinCodeHelper.handlerPinCode(model.getPinCode(), index, Presenter::handlerResult,
                    (led, color) -> view.setColorToLed(led, color));
        }
    }

    private void savePin(String pin) {
        if (pin.equals(PinCodeCreateHelper.MANY_SIMILAR_DIGITS)) {   // если это сообщение об одинаковых
            view.showToast(context.getResources()
                    .getString(R.string.pin_code_toast_many_similar_digits)); // ошибка, более 2 одинаковых цифр в пин-коде, выводим сообщение
        } else {    // или же сохраняем пин - код, анимируем индикацию ввода пин-кода, и выводим сообщение
            correctPin = true;
            model.savePin(pin);     // об успешном вводе пин-кода
            showToast(context.getResources().getString(R.string.pin_code_toast_saved_successfully));
            view.colorChangeAnimation(ViewFragment.COLOR_GREEN);
        }
    }

    public void onEndAnimation() {
        if (correctPin) {
            onResultListener.onCompletedPin();
            correctPin = false;
        }
    }

    public static void showToast(String message) {
        view.showToast(message);
    }

    public void detachView() {
        view = null;
    }

    private void showText(String text) {
        view.showText(text);
    }

    public static void handlerResult(String result) {
        if (result.equals(model.getPinCode())) {
            view.colorChangeAnimation(ViewFragment.COLOR_GREEN);
            correctPin = true;
        } else {
            view.colorChangeAnimation(ViewFragment.COLOR_RED);
            view.showToast(R.string.pin_code_toast_wrong_pin_code);
        }
    }
}
