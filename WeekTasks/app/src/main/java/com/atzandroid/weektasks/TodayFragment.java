package com.atzandroid.weektasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.atzandroid.weektasks.DbConstants.TO_DO_STATE;

public class TodayFragment extends Fragment {
    private List<MyTask> myToDoTaskList, myDoneTaskList;
    private MainActivity mainActivity;

//    private RecyclerView myDoneTasksListRV;
//    private DoneTaskAdapter myDoneTasksAdapter;
//    private LinearLayoutManager doneTasksLinearLayoutManager;

    private RecyclerView toDoTasksListRV;
    private ToDoTaskAdapter toDoTasksAdapter;
    private LinearLayoutManager toDoTasksLinearLayoutManager;

    private ImageButton addTaskBtn;
    private FragmentTransaction mainActivityFragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.to_day, container, false);

        mainActivity = (MainActivity) getActivity();

//        myDoneTasksListRV = view.findViewById(R.id.done_tasks_recyclerview);
//        myDoneTasksAdapter = new DoneTaskAdapter(myTaskList, mainActivity);
//        doneTasksLinearLayoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
//        myDoneTasksListRV.setLayoutManager(doneTasksLinearLayoutManager);
//        myDoneTasksListRV.setAdapter(myDoneTasksAdapter);

        myToDoTaskList = (new WeekTasksDBHelper(getContext())).getDayTasks(MainActivity.lastActiveFragmentDay, TO_DO_STATE);
        toDoTasksListRV = view.findViewById(R.id.todo_tasks_recyclerview);
        toDoTasksAdapter = new ToDoTaskAdapter(myToDoTaskList, mainActivity);
        toDoTasksLinearLayoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
        toDoTasksListRV.setLayoutManager(toDoTasksLinearLayoutManager);
        toDoTasksListRV.setAdapter(toDoTasksAdapter);

        addTaskBtn = view.findViewById(R.id.today_task_add_btn);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (mainActivityFragmentTransaction == null)
                    return;
                mainActivityFragmentTransaction.replace(R.id.day_activities_fragment, new EditTaskFragment());
                mainActivityFragmentTransaction.commit();
            }
        });

        return view;
    }
}
