package com.atzandroid.weektasks;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.atzandroid.weektasks.DbConstants.DONE_STATE;

public class ToDoTaskAdapter extends RecyclerView.Adapter<ToDoTaskAdapter.MyTaskViewHolder> {

    List<MyTask> myTaskList;
    MainActivity mainActivity;
    FragmentTransaction transaction;

    public ToDoTaskAdapter(List<MyTask> myTasks, MainActivity mainActivity) {
        this.myTaskList = myTasks;
        this.mainActivity = mainActivity;
    }

    @Override
    public MyTaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.to_do_task, viewGroup, false);
        MyTaskViewHolder pvh = new MyTaskViewHolder(v);
        return pvh;
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void onBindViewHolder(final MyTaskViewHolder viewHolder, final int i) {


        setListeners(viewHolder);

        viewHolder.title.setText(myTaskList.get(i).getTitle());
        viewHolder.body.setText(myTaskList.get(i).getBody());
        viewHolder.toDotime.setText(myTaskList.get(i).getToDoTime());
        viewHolder.pk = myTaskList.get(i).getPk();
        viewHolder.hasAlarm = myTaskList.get(i).getHas_alarm();

        if(viewHolder.hasAlarm == 1)
            viewHolder.alarmStatusLayout.setVisibility(View.VISIBLE);

    }

    public void setListeners(final MyTaskViewHolder viewHolder){

        final ObjectAnimator swipeLeftAnim = ObjectAnimator.ofFloat(viewHolder.toDoTaskLayer, "translationX", -170f)
                .setDuration(400);
        swipeLeftAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation){
                viewHolder.toDoTaskHiddenLayer.setVisibility(View.VISIBLE);
                viewHolder.swipeBtn.setScaleX(-1);
                viewHolder.swiped = Boolean.TRUE;
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

        final ObjectAnimator swipeRightAnim = ObjectAnimator.ofFloat(viewHolder.toDoTaskLayer, "translationX", 0f)
                .setDuration(400);
        swipeRightAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                viewHolder.toDoTaskHiddenLayer.setVisibility(View.GONE);
                viewHolder.swipeBtn.setScaleX(1);
                viewHolder.swiped = Boolean.FALSE;
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        final ObjectAnimator swipeRightAnimTickTask = ObjectAnimator.ofFloat(viewHolder.toDoTaskLayer, "translationX", 170f)
                .setDuration(400);
        swipeRightAnimTickTask.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    ObjectAnimator obj = ObjectAnimator.ofFloat(viewHolder.toDoTaskLayer, "translationX", 0f)
                            .setDuration(400);
                    obj.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            (new WeekTasksDBHelper(mainActivity)).updateTaskState(viewHolder.pk, DONE_STATE);
                            Toast.makeText(mainActivity, "Good Job!", Toast.LENGTH_LONG).show();
                            mainActivity.updateFragment(MainActivity.lastActiveFragmentDay);
                        }
                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }
                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    obj.start();
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                }
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });


        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(viewHolder.toDoTaskLayer.getContext()) {
            @Override
            public void onSwipeLeft() {
                if (viewHolder.swiped)
                    return;
                swipeLeftAnim.start();
            }
            @Override
            public void onSwipeRight(){
                if (viewHolder.swiped)
                    swipeRightAnim.start();
                else if (MainActivity.today == MainActivity.lastActiveFragmentDay){
                    swipeRightAnimTickTask.start();
                }
            }
        };

        viewHolder.toDoTaskLayer.setOnTouchListener(onSwipeTouchListener);
        viewHolder.swipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.swiped) {
                    swipeRightAnim.start();
                    return;
                }
                swipeLeftAnim.start();
            }
        });

        viewHolder.deleteTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showQueryDialog(viewHolder.pk);
            }
        });

        viewHolder.editTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTaskFragment.activeObjectPK = viewHolder.pk;
                transaction = mainActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.day_activities_fragment, new EditTaskFragment());
                transaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myTaskList.size();
    }

    public static class MyTaskViewHolder extends RecyclerView.ViewHolder {
        int pk, hasAlarm;
        TextView title, body, toDotime;
        LinearLayout toDoTaskLayer, toDoTaskHiddenLayer, toDoTaskTickHiddenLayer;
        ImageButton swipeBtn, addReminder, deleteTaskBtn, editTaskBtn;
        Boolean swiped;
        FrameLayout alarmStatusLayout;

        MyTaskViewHolder(View itemView) {
            super(itemView);
            toDoTaskLayer = itemView.findViewById(R.id.to_do_task_layer);
            title = itemView.findViewById(R.id.to_do_task_title);
            body = itemView.findViewById(R.id.to_do_task_text);
            toDotime = itemView.findViewById(R.id.to_do_task_time);
            toDoTaskHiddenLayer = itemView.findViewById(R.id.to_do_task_hidden_layer);
            toDoTaskTickHiddenLayer = itemView.findViewById(R.id.to_do_task_tick_hidden_layer);
            swipeBtn = itemView.findViewById(R.id.swipe_left_task_btn);
            addReminder = itemView.findViewById(R.id.add_reminder);
            deleteTaskBtn = itemView.findViewById(R.id.delete_task_btn);
            editTaskBtn = itemView.findViewById(R.id.edit_task_btn);
            alarmStatusLayout = itemView.findViewById(R.id.alarm_status_layout);
            swiped = Boolean.FALSE;
        }
    }

}

