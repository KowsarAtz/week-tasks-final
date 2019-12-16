package com.atzandroid.weektasks;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.atzandroid.weektasks.DbConstants.NOT_PASS_PROTECTED;
import static com.atzandroid.weektasks.DbConstants.NOT_SET;
import static com.atzandroid.weektasks.DbConstants.PASS_PROTECTED;

public class KeypadActivity extends AppCompatActivity {

    public static final int HIDE_DURATION = 300, OFFSET = 1000, SHOW_DURATION = 600;
    LinearLayout keypadLayout, passDecideDialogLayout, passEnterDialogLayout;
    ImageView icPasskey;
    Button noPassBtn, yesPassBtn, askLaterPassBtn;
    TextView enteringPassTW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keypad);
        keypadLayout = findViewById(R.id.keypad);
        icPasskey = findViewById(R.id.ic_passkey);
        passDecideDialogLayout = findViewById(R.id.decide_password_dialoge);
        noPassBtn = findViewById(R.id.decide_password_dialoge_no);
        yesPassBtn = findViewById(R.id.decide_password_dialoge_yes);
        askLaterPassBtn = findViewById(R.id.decide_password_dialoge_ask_later);
        passEnterDialogLayout = findViewById(R.id.entering_password_dialog);
        enteringPassTW = findViewById(R.id.entering_password_tw);
        final WeekTasksDBHelper dbHelper = new WeekTasksDBHelper(this);

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
//                dbHelper.changePassProtectedStatus(PASS_PROTECTED);
                  // to be completed . . .
                passDecideDialogLayout.setVisibility(View.GONE);
                icPasskey.setVisibility(View.GONE);
                showView(keypadLayout, OFFSET, SHOW_DURATION);
                showView(passEnterDialogLayout, -OFFSET, SHOW_DURATION);
                showView(icPasskey, -OFFSET, SHOW_DURATION);
            }
        });



        icPasskey.setVisibility(View.GONE);
        passDecideDialogLayout.setVisibility(View.GONE);
        passEnterDialogLayout.setVisibility(View.GONE);
        keypadLayout.setVisibility(View.GONE);

        int passProtectedStatus = dbHelper.passProtectedStatus();

        if (passProtectedStatus == NOT_SET) {
            keypadLayout.setVisibility(View.GONE);
            showView(passDecideDialogLayout, -OFFSET, SHOW_DURATION);
            showView(icPasskey, OFFSET, SHOW_DURATION);
        }
        else if (passProtectedStatus == NOT_PASS_PROTECTED) {
            startActivity(new Intent(KeypadActivity.this, MainActivity.class));
            finish();
        }
        else if (passProtectedStatus == PASS_PROTECTED){
            showView(keypadLayout, 1000, 1000);
        }

    }

    private void showView(View view, float offset, int duration){
        view.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                ObjectAnimator.ofFloat(view, "translationY", offset).setDuration(10),
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
}
