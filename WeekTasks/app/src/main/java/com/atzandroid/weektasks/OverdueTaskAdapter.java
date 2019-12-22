package com.atzandroid.weektasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OverdueTaskAdapter extends RecyclerView.Adapter<OverdueTaskAdapter.MyTaskViewHolder> {

    List<MyTask> myTaskList;
    MainActivity mainActivity;

    public OverdueTaskAdapter(List<MyTask> myTasks, MainActivity mainActivity) {
        this.myTaskList = myTasks;
        this.mainActivity = mainActivity;
    }

    @Override
    public MyTaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.overdue_task, viewGroup, false);
        MyTaskViewHolder pvh = new MyTaskViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final MyTaskViewHolder viewHolder, final int i) {

        viewHolder.title.setText(myTaskList.get(i).getTitle());

    }

    @Override
    public int getItemCount() {
        return myTaskList.size();
    }

    public static class MyTaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        MyTaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.done_task_title);
        }
    }

}

