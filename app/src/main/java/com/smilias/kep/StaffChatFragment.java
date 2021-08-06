package com.smilias.kep;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smilias.kep.databinding.FragmentStaffChatBinding;
import com.smilias.kep.model.MyRecyclerViewAdapter;
import com.smilias.kep.model.UserDetails;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class StaffChatFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    ArrayList<String> onlineStaff = new ArrayList<>();
    ArrayList<String> users = new ArrayList<>();
    FirebaseDatabase database;
    int i;
    private DatabaseReference myRef;
    private FragmentStaffChatBinding binding;


    public StaffChatFragment() {
        // Required empty public constructor
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
        adapter = new MyRecyclerViewAdapter(getActivity(), onlineStaff);
        adapter.setClickListener(StaffChatFragment.this::onItemClick);
        recyclerView.setAdapter(adapter);
        i = 0;
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
                for (DataSnapshot snapshot : MainSnapshot.child("Users").getChildren()) {
                    if (!onlineStaff.contains(snapshot.getKey())) {
                        users.add(snapshot.getKey());
                    }
                }
                try {
                    if (i == 0) {
                        adapter = new MyRecyclerViewAdapter(getActivity(), users);
                        adapter.setClickListener(StaffChatFragment.this::onItemClick);
                        recyclerView.setAdapter(adapter);
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
        binding = FragmentStaffChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
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
        users.clear();
    }
}