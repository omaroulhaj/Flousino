package com.example.flousino;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.flousino.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    // Firebase database
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private DatabaseReference mExpenseDatabase;
    private FirebaseRecyclerAdapter adapter;

    private TextView expenseTotalSum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_expense, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();
        expenseTotalSum=myview.findViewById(R.id.expense_txt_result);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        recyclerView=myview.findViewById(R.id.recycler_id_expense);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalValue = 0;
                for(DataSnapshot mysnapshot:dataSnapshot.getChildren())
                {
                    Data data=mysnapshot.getValue(Data.class);
                    totalValue+=data.getAmount();
                    String stTotalValue=String.valueOf(totalValue);
                    expenseTotalSum.setText(stTotalValue+" DH");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return myview;

    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpenseDatabase, Data.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Data, MyViewHoldere>(options) {

            @NonNull
            @Override
            public MyViewHoldere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the layout for each item in the RecyclerView
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.expense_recycler_data, parent, false);
                return new MyViewHoldere(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHoldere holder, int position, @NonNull Data model) {
                // Bind the data from the model to the view holder
                holder.setAmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
            }
        };

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        // Start listening for changes in Firebase
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}


class MyViewHoldere extends RecyclerView.ViewHolder
{
    View mView;
    public MyViewHoldere(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }
    void setDate(String date)
    {
        TextView mDate=mView.findViewById(R.id.date_txt_expense);
        mDate.setText(date);
    }
    void setType(String type)
    {
        TextView mType=mView.findViewById(R.id.type_txt_expense);
        mType.setText(type);
    }
    void setAmount(int amount)
    {
        TextView mAmount=mView.findViewById(R.id.ammount_txt_expense);
        String stammount=String.valueOf(amount);
        mAmount.setText(stammount);
    }
    void setNote(String note)
    {
        TextView mNote=mView.findViewById(R.id.note_txt_expense);
        mNote.setText(note);
    }


}
