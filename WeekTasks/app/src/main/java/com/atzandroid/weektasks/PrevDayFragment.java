package com.atzandroid.weektasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.atzandroid.weektasks.DbConstants.DONE_STATE;
import static com.atzandroid.weektasks.DbConstants.OVER_DUE_STATE;

public class PrevDayFragment extends Fragment {

    private List<MyTask> myOverdueTaskList, myDoneTaskList;
    private MainActivity mainActivity;

    private RecyclerView myDoneTasksListRV;
    private DoneTaskAdapter myDoneTasksAdapter;
    private LinearLayoutManager doneTasksLinearLayoutManager;

    private RecyclerView myOverdueTasksListRV;
    private OverdueTaskAdapter myOverdueTasksAdapter;
    private LinearLayoutManager overdueTasksLinearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prev_day, container, false);

        mainActivity = (MainActivity) getActivity();

        myDoneTaskList = (new WeekTasksDBHelper(getContext())).getDayTasks(MainActivity.lastActiveFragmentDay, DONE_STATE);
        if (myDoneTaskList.size() != 0){
            (view.findViewById(R.id.done_task_list_empty_img_prev_day)).setVisibility(View.GONE);
        }
        myDoneTasksListRV = view.findViewById(R.id.done_tasks_recyclerview_prev_day);
        myDoneTasksAdapter = new DoneTaskAdapter(myDoneTaskList, mainActivity);
        doneTasksLinearLayoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
        myDoneTasksListRV.setLayoutManager(doneTasksLinearLayoutManager);
        myDoneTasksListRV.setAdapter(myDoneTasksAdapter);

        myOverdueTaskList = (new WeekTasksDBHelper(getContext())).getDayTasks(MainActivity.lastActiveFragmentDay, OVER_DUE_STATE);
        if (myOverdueTaskList.size() != 0){
            (view.findViewById(R.id.overdue_task_list_empty_img_prev_day)).setVisibility(View.GONE);
        }
        myOverdueTasksListRV = view.findViewById(R.id.overdue_tasks_recyclerview_prev_day);
        myOverdueTasksAdapter = new OverdueTaskAdapter(myOverdueTaskList, mainActivity);
        overdueTasksLinearLayoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
        myOverdueTasksListRV.setLayoutManager(overdueTasksLinearLayoutManager);
        myOverdueTasksListRV.setAdapter(myOverdueTasksAdapter);

        return view;
    }
}
