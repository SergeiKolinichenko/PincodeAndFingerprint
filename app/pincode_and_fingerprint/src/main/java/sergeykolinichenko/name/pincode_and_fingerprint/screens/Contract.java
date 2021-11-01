package sergeykolinichenko.name.pincode_and_fingerprint.screens;

public interface Contract {

    void showToast(String message);

    void showToast(int idMessage);

    void showText(String text);

    void setBottomSheetStateExpanded();

    void setColorToLed(int led, int color); // установка цвета индикатора цифр

    void colorChangeAnimation(int color);

}
