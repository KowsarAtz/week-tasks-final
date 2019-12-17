package com.atzandroid.weektasks;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoTaskAdapter extends RecyclerView.Adapter<ToDoTaskAdapter.MyTaskViewHolder> {

    List<MyTask> myTaskList;
    MainActivity mainActivity;

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

        viewHolder.title.setText(myTaskList.get(i).getTitle());
        viewHolder.text.setText(myTaskList.get(i).getText());
        viewHolder.time.setText(myTaskList.get(i).getTime());

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

    }

    @Override
    public int getItemCount() {
        return myTaskList.size();
    }

    public static class MyTaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, text, time;
        LinearLayout toDoTaskLayer, toDoTaskHiddenLayer;
        ImageButton swipeBtn;
        Boolean swiped;

        MyTaskViewHolder(View itemView) {
            super(itemView);
            toDoTaskLayer = itemView.findViewById(R.id.to_do_task_layer);
            title = itemView.findViewById(R.id.to_do_task_title);
            text = itemView.findViewById(R.id.to_do_task_text);
            time = itemView.findViewById(R.id.to_do_task_time);
            toDoTaskHiddenLayer = itemView.findViewById(R.id.to_do_task_hidden_layer);
            swipeBtn = itemView.findViewById(R.id.swipe_left_task_btn);
            swiped = Boolean.FALSE;
        }
    }

}

