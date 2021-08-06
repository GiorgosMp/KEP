package com.smilias.kep;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smilias.kep.databinding.FragmentLoginBinding;
import com.smilias.kep.model.MyRecyclerViewAdapter;
import com.smilias.kep.model.UserDetails;

import java.util.ArrayList;


public class LoginFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{
    private LinearLayoutManager layoutManager;
    private MyRecyclerViewAdapter adapter;
    private ArrayList<String> onlineStaff= new ArrayList<>();
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private int i;
    private FragmentLoginBinding binding;

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new MyRecyclerViewAdapter(getActivity(),onlineStaff);
        adapter.setClickListener(LoginFragment.this::onItemClick);
        binding.recyclerView.setAdapter(adapter);
        i=0;
        super.onResume();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot MainSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : MainSnapshot.child("OnlineStaff").getChildren()) {
                    if (!onlineStaff.contains(snapshot.getKey()))
                        onlineStaff.add(snapshot.getKey());
                }
                try {
                    if(i==0){
                        adapter = new MyRecyclerViewAdapter(getActivity(), onlineStaff);
                        adapter.setClickListener(LoginFragment.this::onItemClick);
                        binding.recyclerView.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Inflate the layout for this fragment
        layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        UserDetails.userName = LogInActivity.username;
        UserDetails.chatWith = adapter.getItem(position);
        startActivity(intent);

    }

    @Override
    public void onStop() {
        super.onStop();
        onlineStaff.clear();
    }
}