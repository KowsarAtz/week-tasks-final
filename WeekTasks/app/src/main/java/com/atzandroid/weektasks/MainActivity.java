package com.atzandroid.weektasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int NONE = -1, SAT = 0, SUN = 1, MON = 2, TUE = 3, WED = 4, THU = 5, FRI = 6;
    private static int[] days_buttons_ids = {R.id.day_sat, R.id.day_sun, R.id.day_mon, R.id.day_tue, R.id.day_wed, R.id.day_thu, R.id.day_fri};
    private static Button[] day_btns;
    private static final String[] days = {"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"};
    static final String[] daysFullname = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    static int today = NONE;
    private static int selected_day = NONE;
    private static final String TODAY = "Today";

    private LinearLayout queryLayout;
    private Button queryYesBtn, getQueryCancelBtn, aboutBtn, logoutBtn;
    private int queryPK = 0;

    private TextView loggedInName, loggedInUsername, loggedInEmail;

    FrameLayout fragmentLayout;

    LinearLayout menu_layout;
    ImageView options_menu_btn;
    private FragmentTransaction transaction;
    static int lastActiveFragmentDay = NONE;
    private int test = -1;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        initQueryLayout();
        queryLayout.setVisibility(View.VISIBLE);
        initDayBtns();
        setToday();

        WeekTasksDBHelper dbHelper = new WeekTasksDBHelper(this);
        if(dbHelper.getLastVisited() > SAT && today == SAT)
            dbHelper.deleteAllTask();
        dbHelper.setLastVisited(today);

        (new WeekTasksDBHelper(this)).overDuePreviousTasks(today);
        setDayButtonListeners();
        initOptionsMenu(getIntent().getExtras());
    }

    public void showQueryDialog(int pk){
        fragmentLayout.setVisibility(View.GONE);
        queryPK = pk;
    }

    private void initQueryLayout() {
        fragmentLayout = findViewById(R.id.day_activities_fragment);
        queryLayout = findViewById(R.id.ask_delete_dialog_layout);
        queryYesBtn = findViewById(R.id.ask_delete_dialog_yes_btn);
        getQueryCancelBtn = findViewById(R.id.ask_delete_dialog_cancel_btn);

        queryYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryPK == 0)
                    return;
                (new WeekTasksDBHelper(MainActivity.this)).deleteTask(queryPK);
                Toast.makeText(MainActivity.this, "Task removed!", Toast.LENGTH_LONG).show();
                updateFragment(lastActiveFragmentDay);
                fragmentLayout.setVisibility(View.VISIBLE);
                queryPK = 0;
            }
        });
        getQueryCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryPK == 0)
                    return;
                updateFragment(lastActiveFragmentDay);
                fragmentLayout.setVisibility(View.VISIBLE);
                queryPK = 0;
            }
        });
    }

    private void initOptionsMenu(Bundle bundle){
        menu_layout = findViewById(R.id.options_menu_layout);
        options_menu_btn = findViewById(R.id.options_menu_btn);
        menu_layout.setVisibility(View.GONE);
        options_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_layout.getVisibility() == View.GONE)
                    openMenu();
                else
                    closeMenu();
            }
        });
        aboutBtn = findViewById(R.id.about_btn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });

        logoutBtn = findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new WeekTasksDBHelper(MainActivity.this).updateLastToken("");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        loggedInName = findViewById(R.id.loggedin_name_tw);
        loggedInEmail = findViewById(R.id.loggedin_email_tw);
        loggedInUsername = findViewById(R.id.loggedin_usernamename_tw);
        String name = bundle.getString("first_name").replaceAll("^\"|\"$", "")+
                bundle.getString("last_name").replaceAll("^\"|\"$", "");
        loggedInName.setText(name);
        loggedInUsername.setText(bundle.getString("username").replaceAll("^\"|\"$", ""));
        loggedInEmail.setText(bundle.getString("email").replaceAll("^\"|\"$", ""));
    }

    private void initDayBtns() {
        day_btns = new Button[7];
        for (int day=SAT; day<= FRI; day++){
            day_btns[day] = findViewById(days_buttons_ids[day]);
        }
    }

    private void setToday(){
        int day = test; //for test
        if(test == -1){
            day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (day == 7)
                day = 0;
        }
        if (today != NONE) { //for test
            int temp = day - 1;
            if(temp == -1)
                temp = 6;
            day_btns[today].setText(days[temp]);
        }
        day_btns[day].setText(TODAY);
        select_button(day);
        today = day;
        updateFragment(today);
        lastActiveFragmentDay = day;
    }

    private void setDayButtonListeners(){
        Button btn;
        for(int day=SAT; day<=FRI; day++){
            btn = day_btns[day];
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt((String) v.getTag());
                    if(temp != selected_day) {
                        select_button(temp);
                        updateFragment(temp);
                        lastActiveFragmentDay = temp;
                    }
                }
            });
        }
    }

    public void updateFragment(int day) {
        transaction = getSupportFragmentManager().beginTransaction();
        if(day == today) {
            transaction.replace(R.id.day_activities_fragment, new TodayFragment());
            transaction.commit();
        }
        else if(day < today){
            transaction.replace(R.id.day_activities_fragment, new PrevDayFragment());
            transaction.commit();
        }
        else{
            transaction.replace(R.id.day_activities_fragment, new NextDayFragment());
            transaction.commit();
        }
    }

    private void select_button(int index){
        if (selected_day != NONE)
            deselect_button(selected_day);
        Button btn = day_btns[index];
        btn.setBackgroundResource(R.drawable.day_button_selected);
        btn.setTextColor(getResources().getColor(R.color.white));
        selected_day = index;
    }

    private void deselect_button(int index){
        Button btn = day_btns[index];
        btn.setBackgroundResource(R.drawable.day_button_unselected);
        btn.setTextColor(getResources().getColor(R.color.black));
    }

    private void openMenu(){
        final View view = (View) menu_layout;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", -3000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                options_menu_btn.setScaleY(-1);
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
                ObjectAnimator.ofFloat(view, "translationY", 0f).setDuration(250)
        );
        animatorSet.start();
    }

    private void closeMenu(){
        AnimatorSet animatorSet = new AnimatorSet();
        final View view = (View) menu_layout;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", -3000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                options_menu_btn.setScaleY(1);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.setDuration(250);

        animatorSet.playSequentially(
                ObjectAnimator.ofFloat(view, "translationY", 0f).setDuration(10),
                objectAnimator
        );
        animatorSet.start();
    }

}
