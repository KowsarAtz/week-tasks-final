package com.atzandroid.weektasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.atzandroid.weektasks.DbConstants.TO_DO_STATE;

public class NextDayFragment extends Fragment {

    private List<MyTask> myToDoTaskList;

    private ImageButton addTaskBtn;
    private FragmentTransaction mainActivityFragmentTransaction;

    private RecyclerView toDoTasksListRV;
    private ToDoTaskAdapter toDoTasksAdapter;
    private LinearLayoutManager toDoTasksLinearLayoutManager;

    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.next_day, container, false);
        mainActivity = (MainActivity) getActivity();

        addTaskBtn = view.findViewById(R.id.next_day_task_add_btn);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (mainActivityFragmentTransaction == null)
                    return;
                EditTaskFragment.activeObjectPK = 0;
                mainActivityFragmentTransaction.replace(R.id.day_activities_fragment, new EditTaskFragment());
                mainActivityFragmentTransaction.commit();
            }
        });

        myToDoTaskList = (new WeekTasksDBHelper(getContext())).getDayTasks(MainActivity.lastActiveFragmentDay, TO_DO_STATE);
        toDoTasksListRV = view.findViewById(R.id.todo_tasks_recyclerview_next_day);
        toDoTasksAdapter = new ToDoTaskAdapter(myToDoTaskList, mainActivity);
        toDoTasksLinearLayoutManager = new LinearLayoutManager(mainActivity, RecyclerView.VERTICAL, false);
        toDoTasksListRV.setLayoutManager(toDoTasksLinearLayoutManager);
        toDoTasksListRV.setAdapter(toDoTasksAdapter);


        return view;
    }
}
