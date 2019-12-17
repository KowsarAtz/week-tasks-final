package com.atzandroid.weektasks;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.atzandroid.weektasks.DbConstants.NOT_PASS_PROTECTED;
import static com.atzandroid.weektasks.DbConstants.NOT_SET;
import static com.atzandroid.weektasks.DbConstants.PASS_PROTECTED;

public class KeypadActivity extends AppCompatActivity {

    public static final int HIDE_DURATION = 300, OFFSET = 1000, SHOW_DURATION = 600
            , MIN_PASS_LENGTH = 4, MAX_PASS_LENGTH = 8, SHAKE_OFFSET = 50, SHAKE_DURATION = 1000
            , CHOOSING = 0, DEACTIVE = -1, ACTIVE = 1, REPEATING = 2;
    LinearLayout keypadLayout, passDecideDialogLayout, passEnterDialogLayout;
    ImageView icPasskey;
    Button noPassBtn, yesPassBtn, askLaterPassBtn;
    ImageButton icPassChoosed, icRemoveLastDigit;
    TextView enteringPassTW, choosePassTitleTW, choosePassInfoTW;
    WeekTasksDBHelper dbHelper;
    StringBuilder enteringPasswordString;
    int enteringPasswordStatus, tempPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypad);

        init();
        decidePassDialogButtonsSetListeners();
        keypadButtonsSetListeners();

        int passProtectedStatus = dbHelper.passProtectedStatus();

        if (passProtectedStatus == NOT_SET) {
            showView(passDecideDialogLayout, -OFFSET, SHOW_DURATION);
            showView(icPasskey, OFFSET, SHOW_DURATION);
            enteringPasswordStatus = DEACTIVE;
        }
        else if (passProtectedStatus == NOT_PASS_PROTECTED) {
            enteringPasswordStatus = DEACTIVE;
            startActivity(new Intent(KeypadActivity.this, MainActivity.class));
            finish();
        }
        else if (passProtectedStatus == PASS_PROTECTED){
            enteringPasswordStatus = ACTIVE;
            passDecideDialogLayout.setVisibility(View.GONE);
            icPasskey.setVisibility(View.GONE);

            choosePassTitleTW.setText("Enter password");
            choosePassInfoTW.setVisibility(View.GONE);

            showView(keypadLayout, OFFSET, SHOW_DURATION);
            showView(passEnterDialogLayout, -OFFSET, SHOW_DURATION);
            showView(icPasskey, -OFFSET, SHOW_DURATION);

            enteringPasswordString.setLength(0);
        }

    }

    private void init() {
        keypadLayout = findViewById(R.id.keypad);
        icPasskey = findViewById(R.id.ic_passkey);
        passDecideDialogLayout = findViewById(R.id.decide_password_dialoge);
        noPassBtn = findViewById(R.id.decide_password_dialoge_no);
        yesPassBtn = findViewById(R.id.decide_password_dialoge_yes);
        askLaterPassBtn = findViewById(R.id.decide_password_dialoge_ask_later);
        passEnterDialogLayout = findViewById(R.id.entering_password_dialog);
        enteringPassTW = findViewById(R.id.entering_password_tw);
        icPassChoosed = findViewById(R.id.ic_choose_pass);
        icRemoveLastDigit = findViewById(R.id.ic_remove_last_digit);
        choosePassInfoTW = findViewById(R.id.choose_pass_info_tv);
        choosePassTitleTW = findViewById(R.id.choose_pass_tv);
        dbHelper = new WeekTasksDBHelper(this);
        icPasskey.setVisibility(View.GONE);
        passDecideDialogLayout.setVisibility(View.GONE);
        passEnterDialogLayout.setVisibility(View.GONE);
        keypadLayout.setVisibility(View.GONE);
        enteringPasswordString = new StringBuilder();
    }


    private void keypadButtonsSetListeners() {
        icRemoveLastDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enteringPasswordString.length() == 0)
                    return;
                enteringPasswordString.setLength(enteringPasswordString.length() - 1);
                setEnteringPasswordTW();
            }
        });

        icPassChoosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteringPasswordStatus == CHOOSING){
                    if(enteringPasswordString.length() < MIN_PASS_LENGTH){
                        shakeView(enteringPassTW, "MINIMUM 4 DIGITS!", "- - - -" ,R.color.red_warning, Boolean.FALSE);
                        enteringPasswordString.setLength(0);
                        return;
                    }
                    if(enteringPasswordString.length() > MAX_PASS_LENGTH){
                        shakeView(enteringPassTW, "MAXIMUM 8 DIGITS!", "- - - -" ,R.color.red_warning, Boolean.FALSE);
                        enteringPasswordString.setLength(0);
                        return;
                    }
                    tempPass = enteringPasswordString.toString().hashCode();
                    shakeView(enteringPassTW, "REPEAT", "- - - -", R.color.green_approved, Boolean.FALSE);
                    enteringPasswordString.setLength(0);
                    enteringPasswordStatus = REPEATING;
                    return;
                }
                if (enteringPasswordStatus == REPEATING){
                    if (enteringPasswordString.toString().hashCode() != tempPass){
                        shakeView(enteringPassTW, "NO MATCH! TRY AGAIN", "- - - -" ,R.color.red_warning, Boolean.FALSE);
                        enteringPasswordString.setLength(0);
                        return;
                    }
                    dbHelper.changePassProtectedStatus(PASS_PROTECTED);
                    dbHelper.changePassword(tempPass);
                    shakeView(enteringPassTW, "PASSWORD SAVED", "" ,R.color.green_approved, Boolean.TRUE);
                    return;
                }
                if (enteringPasswordStatus == ACTIVE){
                    if (enteringPasswordString.toString().hashCode() != dbHelper.getPassHash()){
                        shakeView(enteringPassTW, "WRONG! TRY AGAIN", "- - - -" ,R.color.red_warning, Boolean.FALSE);
                        enteringPasswordString.setLength(0);
                        return;
                    }
                    startActivity(new Intent(KeypadActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void decidePassDialogButtonsSetListeners() {
        askLaterPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideView(passDecideDialogLayout, -OFFSET, HIDE_DURATION, Boolean.TRUE);
                hideView(icPasskey, OFFSET, HIDE_DURATION, Boolean.FALSE);
            }
        });

        noPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.changePassProtectedStatus(NOT_PASS_PROTECTED);
                hideView(passDecideDialogLayout, -OFFSET, HIDE_DURATION, Boolean.TRUE);
                hideView(icPasskey, OFFSET, HIDE_DURATION, Boolean.FALSE);
            }
        });

        yesPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passDecideDialogLayout.setVisibility(View.GONE);
                icPasskey.setVisibility(View.GONE);
                showView(keypadLayout, OFFSET, SHOW_DURATION);
                showView(passEnterDialogLayout, -OFFSET, SHOW_DURATION);
                showView(icPasskey, -OFFSET, SHOW_DURATION);

                enteringPasswordString.setLength(0);
                enteringPasswordStatus = CHOOSING;
            }
        });
    }

    private void showView(final View view, float offset, int duration){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", offset);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.setDuration(10);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                objectAnimator,
                ObjectAnimator.ofFloat(view, "translationY", 0f).setDuration(duration)
        );
        animatorSet.start();
    }

    private void hideView(final View view, float offset, int duration, final Boolean finishCurrentActivity){
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", offset);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                if (finishCurrentActivity) {
                    startActivity(new Intent(KeypadActivity.this, MainActivity.class));
                    finish();
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.setDuration(duration);

        animatorSet.playSequentially(
                ObjectAnimator.ofFloat(view, "translationY", 0f).setDuration(10),
                objectAnimator
        );
        animatorSet.start();
    }

    private void shakeView(final View view, final String message, final String defaultStr, final int color, final Boolean finishCurrentActivity){
        ObjectAnimator objectAnimatorStart = ObjectAnimator.ofFloat(view, "translationX", -SHAKE_OFFSET);
        objectAnimatorStart.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ((TextView) view).setText(message);
                ((TextView) view).setTextColor(getResources().getColor(color));
            }
            @Override
            public void onAnimationEnd(Animator animation) {
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        ObjectAnimator objectAnimatorEnd = ObjectAnimator.ofFloat(view, "translationX", 0f);
        objectAnimatorEnd.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                if (finishCurrentActivity){
                    startActivity(new Intent(KeypadActivity.this, MainActivity.class));
                    finish();
                    return;
                }
                ((TextView) view).setText(defaultStr);
                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        int t = SHAKE_DURATION/10;
        animatorSet.playSequentially(
                objectAnimatorStart,
                ObjectAnimator.ofFloat(view, "translationX", SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", -SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", -SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", -SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", 0f).setDuration(t),
                objectAnimatorEnd.setDuration(SHAKE_DURATION)
        );
        animatorSet.start();
    }

    private void shakeViewNextActivity(final View view, final String message, final String defaultStr, final int color){
        ObjectAnimator objectAnimatorStart = ObjectAnimator.ofFloat(view, "translationX", -SHAKE_OFFSET);
        objectAnimatorStart.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ((TextView) view).setText(message);
                ((TextView) view).setTextColor(getResources().getColor(color));
            }
            @Override
            public void onAnimationEnd(Animator animation) {
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        ObjectAnimator objectAnimatorEnd = ObjectAnimator.ofFloat(view, "translationX", 0f);
        objectAnimatorEnd.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                ((TextView) view).setText(defaultStr);
                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        int t = SHAKE_DURATION/10;
        animatorSet.playSequentially(
                objectAnimatorStart,
                ObjectAnimator.ofFloat(view, "translationX", SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", -SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", -SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", -SHAKE_OFFSET).setDuration(t),
                ObjectAnimator.ofFloat(view, "translationX", 0f).setDuration(t),
                objectAnimatorEnd.setDuration(SHAKE_DURATION)
        );
        animatorSet.start();
    }

    public void keypadButtonClick(View view){
        String num_char = ((Button) view).getText().toString();
        if (enteringPasswordStatus != DEACTIVE){
            if (enteringPasswordStatus == CHOOSING) {
                if (enteringPasswordString.length() >= MAX_PASS_LENGTH) {
                    shakeView(enteringPassTW, "MAXIMUM 8 DIGITS!", "- - - -", R.color.red_warning, Boolean.FALSE);
                    enteringPasswordString.setLength(0);
                    return;
                }
            }
            enteringPasswordString.append(num_char);
            setEnteringPasswordTW();
        }
    }

    private void setEnteringPasswordTW() {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < enteringPasswordString.length() ; i++){
            string.append("* ");
        }
        if(enteringPasswordString.length() < MIN_PASS_LENGTH){
            for(int i = 0; i < MIN_PASS_LENGTH - enteringPasswordString.length(); i++){
                string.append("- ");
            }
        }
        enteringPassTW.setText(string);
    }
}
