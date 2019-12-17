package com.atzandroid.weektasks;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        viewHolder.title.setText(myTaskList.get(i).getTitle());
        viewHolder.text.setText(myTaskList.get(i).getText());
        viewHolder.time.setText(myTaskList.get(i).getTime());

        OnSwipeTouchListener onSwipeTouchListener = new OnSwipeTouchListener(viewHolder.toDoTaskLayer.getContext()) {
            @Override
            public void onSwipeLeft() {
                ObjectAnimator obj = ObjectAnimator.ofFloat(viewHolder.toDoTaskLayer, "translationX", -300f)
                        .setDuration(400);
                obj.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation){
                        viewHolder.toDoTaskHiddenLayer.setVisibility(View.VISIBLE);
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
                obj.start();
            }
            @Override
            public void onSwipeRight(){
                ObjectAnimator obj = ObjectAnimator.ofFloat(viewHolder.toDoTaskLayer, "translationX", 0f)
                        .setDuration(400);
                obj.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewHolder.toDoTaskHiddenLayer.setVisibility(View.GONE);
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
        };

        viewHolder.toDoTaskLayer.setOnTouchListener(onSwipeTouchListener);
//        viewHolder.title.setOnTouchListener(onSwipeTouchListener);
//        viewHolder.text.setOnTouchListener(onSwipeTouchListener);
    }

    @Override
    public int getItemCount() {
        return myTaskList.size();
    }

    public static class MyTaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView text;
        TextView time;
        LinearLayout toDoTaskLayer, toDoTaskHiddenLayer;

        MyTaskViewHolder(View itemView) {
            super(itemView);
            toDoTaskLayer = itemView.findViewById(R.id.to_do_task_layer);
            title = itemView.findViewById(R.id.to_do_task_title);
            text = itemView.findViewById(R.id.to_do_task_text);
            time = itemView.findViewById(R.id.to_do_task_time);
            toDoTaskHiddenLayer = itemView.findViewById(R.id.to_do_task_hidden_layer);
        }
    }

}

