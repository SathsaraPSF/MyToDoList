package com.sathsata.myto_dolist.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sathsata.myto_dolist.Adapter.HomeTodoAdapter;
import com.sathsata.myto_dolist.MainActivity;
import com.sathsata.myto_dolist.Model.TodoModel;
import com.sathsata.myto_dolist.PersoanlTodoActivity;
import com.sathsata.myto_dolist.R;
import com.sathsata.myto_dolist.RecyclerItemTouchHelperThree;
import com.sathsata.myto_dolist.RecyclerItemTouchHelperTwo;
import com.sathsata.myto_dolist.Utils.DatabaseHandler;
import com.sathsata.myto_dolist.WorkToDoActivity;
import com.sathsata.myto_dolist.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CardView cardView,workTodo,personaTodo,familyTodo,travelTodo;
    TextView textView;
    ImageView imageView;
    private RecyclerView tasksRecyclerView;
    private DatabaseHandler db;
    private HomeTodoAdapter tasksAdapter;
    private List<TodoModel> taskList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTheme(R.style.AppThemes_StatusBar);
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cardView = root.findViewById(R.id.logout);
        textView = root.findViewById(R.id.name);
        imageView = root.findViewById(R.id.user);
        workTodo = root.findViewById(R.id.workTodo);
        personaTodo = root.findViewById(R.id.personalTodo);
        familyTodo = root.findViewById(R.id.familiyTodo);
        travelTodo = root.findViewById(R.id.travelTodo);
        tasksRecyclerView = root.findViewById(R.id.recyclerView);

        db = new DatabaseHandler(getContext());
        db.openDatabase();

        tasksRecyclerView = root.findViewById(R.id.recyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter = new HomeTodoAdapter(db, HomeFragment.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelperThree(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);



        taskList = db.getAllPersonalTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(getContext(),gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if( account != null){
            String Name = account.getDisplayName();
            Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(imageView);

            textView.setText(Name);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });

        workTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),WorkToDoActivity.class);
                startActivity(intent);
            }
        });

        personaTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PersoanlTodoActivity.class);
                startActivity(intent);
            }
        });





        return root;
    }

    private void SignOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public FragmentManager getSupportFragmentManager() {
        return null;
    }
}