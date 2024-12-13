package com.example.flousino;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjouterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjouterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AjouterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AjouterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AjouterFragment newInstance(String param1, String param2) {
        AjouterFragment fragment = new AjouterFragment();
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
    private DatabaseReference mIncomeDatabase;
    private FirebaseRecyclerAdapter adapter;

    private TextView incomeTotalSum;
    // Update edit text
    private EditText ammountEditText;
    private EditText typeEditText;
    private EditText noteEditText;
    // button for update and delete
    private Button btnUpdate;
    private Button btnDelete;
    // Data item value to update
    private int amount;
    private String type;
    private String note;
    private String post_key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_ajouter, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();
        incomeTotalSum=myview.findViewById(R.id.income_txt_result);
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        recyclerView=myview.findViewById(R.id.recycler_id_income);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalValue = 0;
                for(DataSnapshot mysnapshot:dataSnapshot.getChildren())
                {
                    Data data=mysnapshot.getValue(Data.class);
                    totalValue+=data.getAmount();
                    String stTotalValue=String.valueOf(totalValue);
                    incomeTotalSum.setText(stTotalValue+" DH");
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
                .setQuery(mIncomeDatabase, Data.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the layout for each item in the RecyclerView
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.income_recycler_data, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {
                // Bind the data from the model to the view holder
                holder.setAmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key=getRef(position).getKey();
                        amount=model.getAmount();
                        type=model.getType();
                        note=model.getNote();
                        updateDataItem();
                    }
                });

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
    private void updateDataItem()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getLayoutInflater();
        View myview=inflater.inflate(R.layout.update_data_item,null);
        builder.setView(myview);
        ammountEditText=myview.findViewById(R.id.amount_edt);
        typeEditText=myview.findViewById(R.id.type_edt);
        noteEditText=myview.findViewById(R.id.note_edt);

        btnUpdate=myview.findViewById(R.id.btnUpdate);
        btnDelete=myview.findViewById(R.id.btnDelete);
        AlertDialog dialog=builder.create();
        ammountEditText.setText(String.valueOf(amount));
        typeEditText.setText(type);
        typeEditText.setSelection(type.length());
        noteEditText.setText(note);
        noteEditText.setSelection(note.length());
        ammountEditText.setSelection(String.valueOf(amount).length());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = typeEditText.getText().toString().trim();
                note = noteEditText.getText().toString().trim();
                String mdammount =String.valueOf(amount);
                mdammount=ammountEditText.getText().toString().trim();
                int amount=Integer.parseInt(mdammount);
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data=new Data(amount,type,note,mDate,post_key);
                mIncomeDatabase.child(post_key).setValue(data, (error, ref) -> {
                    if (error == null) {
                        Toast.makeText(getActivity(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to update income data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomeDatabase.child(post_key).removeValue((error, ref) -> {
                    if (error == null) {
                        Toast.makeText(getActivity(), "data Deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to delete data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}


class MyViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
         void setDate(String date)
        {
            TextView mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
         void setType(String type)
        {
            TextView mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
         void setAmount(int amount)
        {
            TextView mAmount=mView.findViewById(R.id.ammount_txt_income);
            String stammount=String.valueOf(amount);
            mAmount.setText(stammount);
        }
         void setNote(String note)
        {
            TextView mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }


    }
