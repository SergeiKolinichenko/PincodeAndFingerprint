package sergeykolinichenko.name.pincode_and_fingerprint.screens;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import sergeykolinichenko.name.pincode_and_fingerprint.R;
import sergeykolinichenko.name.pincode_and_fingerprint.helpers.FingerprintHelper;
import sergeykolinichenko.name.pincode_and_fingerprint.helpers.PinCodeCreateHelper;
import sergeykolinichenko.name.pincode_and_fingerprint.helpers.PreferenceHelper;


public class ViewFragment extends Fragment implements View.OnClickListener, Contract {

    // Цвета для индикации введённых цифр
    public static final int COLOR_GREEN = R.drawable.blank_circle_green_back;
    public static final int COLOR_YELLOW = R.drawable.blank_circle_yellow_back;
    public static final int COLOR_RED = R.drawable.blank_circle_red_back;
    public static final int COLOR_WIGHT = R.drawable.blank_circle_white_back;

    // Индексы кнопок
    public static final int BUTTON_0 = 0;
    public static final int BUTTON_1 = 1;
    public static final int BUTTON_2 = 2;
    public static final int BUTTON_3 = 3;
    public static final int BUTTON_4 = 4;
    public static final int BUTTON_5 = 5;
    public static final int BUTTON_6 = 6;
    public static final int BUTTON_7 = 7;
    public static final int BUTTON_8 = 8;
    public static final int BUTTON_9 = 9;
    public static final int BUTTON_BACK = 10;
    public static final int BUTTON_FINGERPRINT = 11;

    // Индексы индикаторов
    public static final int LED_1 = 1;
    public static final int LED_2 = 2;
    public static final int LED_3 = 3;
    public static final int LED_4 = 4;

    private Presenter presenter;
    private Context context;

    // Основная view
    private View rootView;

    // view'хи xml
    private TextView tvMessagePinCode;
    private CircleImageView cvFirstDigit, cvSecondDigit, cvThirdDigit, cvFourthDigit;
    private MaterialButton mbDigit_1, mbDigit_2, mbDigit_3, mbDigit_4, mbDigit_5, mbDigit_6,
            mbDigit_7, mbDigit_8, mbDigit_9, mbDigit_0, mbPinCodeBack;
    ImageButton ibFingerPrint;

    int colorAnimator;

    private LinearLayoutCompat llBottomSheetFingerPrint;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    public ViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pin_code, container, false);

        initView(); // получение ссылок на вьюхи
        initAction();   // получение листенера кнопок
        initBottomSheetBehavior(); // инициализация BottomSheet

        // Инизиализация обьекта настроек
        PreferenceHelper preferenceHelper = PreferenceHelper.getInstance();
        preferenceHelper.init(context); // Получение экземпляра нвстроек приложения

        Model model = new Model(preferenceHelper);
        FingerprintHelper fingerprintHelper = new FingerprintHelper(context, preferenceHelper);
        presenter = new Presenter(model, fingerprintHelper);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.attachView(this, context);    // подключаем вью к презентеру
        presenter.viewIsReady();    // вью готова
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView(); // отсоединение вьюхи от презентера
    }

    private void initView() {
        tvMessagePinCode = rootView.findViewById(R.id.tvMessagePinCode);
        cvFirstDigit = rootView.findViewById(R.id.cvFirstDigit);
        cvSecondDigit = rootView.findViewById(R.id.cvSecondDigit);
        cvThirdDigit = rootView.findViewById(R.id.cvThirdDigit);
        cvFourthDigit = rootView.findViewById(R.id.cvFourthDigit);
        mbDigit_1 = rootView.findViewById(R.id.mbDigit_1);
        mbDigit_2 = rootView.findViewById(R.id.mbDigit_2);
        mbDigit_3 = rootView.findViewById(R.id.mbDigit_3);
        mbDigit_4 = rootView.findViewById(R.id.mbDigit_4);
        mbDigit_5 = rootView.findViewById(R.id.mbDigit_5);
        mbDigit_6 = rootView.findViewById(R.id.mbDigit_6);
        mbDigit_7 = rootView.findViewById(R.id.mbDigit_7);
        mbDigit_8 = rootView.findViewById(R.id.mbDigit_8);
        mbDigit_9 = rootView.findViewById(R.id.mbDigit_9);
        mbDigit_0 = rootView.findViewById(R.id.mbDigit_0);
        mbPinCodeBack = rootView.findViewById(R.id.mbPinCodeBack);
        ibFingerPrint = rootView.findViewById(R.id.ibFingerPrint);
    }

    private void initAction() {
        mbDigit_1.setOnClickListener(this);
        mbDigit_2.setOnClickListener(this);
        mbDigit_3.setOnClickListener(this);
        mbDigit_4.setOnClickListener(this);
        mbDigit_5.setOnClickListener(this);
        mbDigit_6.setOnClickListener(this);
        mbDigit_7.setOnClickListener(this);
        mbDigit_8.setOnClickListener(this);
        mbDigit_9.setOnClickListener(this);
        mbDigit_0.setOnClickListener(this);
        mbPinCodeBack.setOnClickListener(this);
        ibFingerPrint.setOnClickListener(this);
    }

    private void initBottomSheetBehavior() {
        // настройка поведения нижнего экрана
        llBottomSheetFingerPrint = rootView.findViewById(R.id.llBottomSheetFingerPrint);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheetFingerPrint);
        // настройка состояний нижнего экрана
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN) {  // если BottomSheet, опущена
                    mbDigit_1.setEnabled(true); // включение кнопок
                    mbDigit_2.setEnabled(true);
                    mbDigit_3.setEnabled(true);
                    mbDigit_4.setEnabled(true);
                    mbDigit_5.setEnabled(true);
                    mbDigit_6.setEnabled(true);
                    mbDigit_7.setEnabled(true);
                    mbDigit_8.setEnabled(true);
                    mbDigit_9.setEnabled(true);
                    mbDigit_0.setEnabled(true);
                    mbPinCodeBack.setEnabled(true);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int index = PinCodeCreateHelper.NOT_PRESSED;
        if (v.getId() == R.id.mbDigit_0) {
            index = BUTTON_0;
        } else if (v.getId() == R.id.mbDigit_1) {
            index = BUTTON_1;
        } else if (v.getId() == R.id.mbDigit_2) {
            index = BUTTON_2;
        } else if (v.getId() == R.id.mbDigit_3) {
            index = BUTTON_3;
        } else if (v.getId() == R.id.mbDigit_4) {
            index = BUTTON_4;
        } else if (v.getId() == R.id.mbDigit_5) {
            index = BUTTON_5;
        } else if (v.getId() == R.id.mbDigit_6) {
            index = BUTTON_6;
        } else if (v.getId() == R.id.mbDigit_7) {
            index = BUTTON_7;
        } else if (v.getId() == R.id.mbDigit_8) {
            index = BUTTON_8;
        } else if (v.getId() == R.id.mbDigit_9) {
            index = BUTTON_9;
        } else if (v.getId() == R.id.mbPinCodeBack) {
            index = BUTTON_BACK;
        } else if (v.getId() == R.id.ibFingerPrint) {
            index = BUTTON_FINGERPRINT;
        }
        presenter.onClickButton(index);
    }

    @Override
    public void showToast(String message) {
        Toast toast = Toast.makeText(context,message , Toast.LENGTH_SHORT);
        TextView textViewToast = toast.getView().findViewById(android.R.id.message);
        if(textViewToast != null) {
            textViewToast.setGravity(Gravity.CENTER);
        }
        toast.show();
    }

    @Override
    public void showToast(int idMessage) {
        String message = getResources().getString(idMessage);
        showToast(message);
    }

    @Override
    public void showText(String text) {
        tvMessagePinCode.setText(text);
    }

    // отключение кнопок, подьем BottomSheet
    @Override
    public void setBottomSheetStateExpanded() {
        mbDigit_1.setEnabled(false);
        mbDigit_2.setEnabled(false);
        mbDigit_3.setEnabled(false);
        mbDigit_4.setEnabled(false);
        mbDigit_5.setEnabled(false);
        mbDigit_6.setEnabled(false);
        mbDigit_7.setEnabled(false);
        mbDigit_8.setEnabled(false);
        mbDigit_9.setEnabled(false);
        mbDigit_0.setEnabled(false);
        mbPinCodeBack.setEnabled(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    // установка цвета в один индикатор цифр
    @Override
    public void setColorToLed(int led, int color) {
        if (led == LED_1) {
            cvFirstDigit.setImageDrawable(ContextCompat.getDrawable(context, color));
        } else if (led == LED_2) {
            cvSecondDigit.setImageDrawable(ContextCompat.getDrawable(context, color));
        } else if (led == LED_3) {
            cvThirdDigit.setImageDrawable(ContextCompat.getDrawable(context, color));
        } else if (led == LED_4) {
            cvFourthDigit.setImageDrawable(ContextCompat.getDrawable(context, color));
        }
    }

    // установка цвета во все индикаторы цифр
    private void setColorToLeds(int color) {
        for (int i = 1; i <= LED_4; i++) {
            setColorToLed(i, color);
        }
    }

    @Override   // Анимация индикаторов цифр
    public void colorChangeAnimation(int color) {
        colorAnimator = color;
        LinearLayout llPinCodeLed = rootView.findViewById(R.id.llPinCodeLed);
        ObjectAnimator animationOne = ObjectAnimator.ofFloat(llPinCodeLed, "rotationX", 0.0f, 90f);
        animationOne.setDuration(250);
        animationOne.setInterpolator(new AccelerateDecelerateInterpolator());
        animationOne.addListener(animatorListenerStart);
        animationOne.start();

        ObjectAnimator animationTwo = ObjectAnimator.ofFloat(llPinCodeLed, "rotationX", 90.0f, 180f);
        animationTwo.setDuration(250);
        animationTwo.setInterpolator(new AccelerateDecelerateInterpolator());
        animationOne.addListener(animatorListenerEnd);
        animationTwo.start();
    }

    Animator.AnimatorListener animatorListenerStart = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            setColorToLeds(colorAnimator);
        }
        @Override
        public void onAnimationCancel(Animator animation) {
        }
        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };
    Animator.AnimatorListener animatorListenerEnd = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            new Thread(() -> {
                try {
                    Thread.sleep(750);

                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                presenter.onEndAnimation();
            }).start();
        }
        @Override
        public void onAnimationCancel(Animator animation) {
        }
        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };
}