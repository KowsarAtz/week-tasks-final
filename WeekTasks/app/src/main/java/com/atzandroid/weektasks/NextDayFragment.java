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

public class NextDayFragment extends Fragment {

    private ImageButton addTaskBtn;
    private FragmentTransaction mainActivityFragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.next_day, container, false);
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

        return view;
    }
}
