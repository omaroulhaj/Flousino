package com.example.flousino;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flousino.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
    // Floating button
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;
    // Floating button text
    private TextView fab_income_txt;
    private TextView fab_expense_txt;
    // Boolean
    private boolean isOpen=false;
    //animation
    private Animation FadeOpen,FadeClose;
    // Firebase ...

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dashboard, container, false);

       mAuth=FirebaseAuth.getInstance();
       FirebaseUser mUser=mAuth.getCurrentUser();
       String uid=mUser.getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        //Connect floating button to layout
        fab_main_btn = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_Ft_btn);
        fab_expense_btn=myview.findViewById(R.id.expense_Ft_btn);
        // Connect floating text
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);
        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        //Animation Connect
        FadeOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                addAnimation();
                addData();

            }
        });

        return myview;
    }
    // Floating Button Animation
    private void addAnimation() {
        if(isOpen)
        {
            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen=false;
        }
        else{
            fab_income_btn.startAnimation(FadeOpen);
            fab_expense_btn.startAnimation(FadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadeOpen);
            fab_expense_txt.startAnimation(FadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);

            isOpen=true;

        }
    }
    private void addData() {
        // Fab Button income ...
        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();

            }
        });
        // Fab Button expense ...
        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }
    public void incomeDataInsert()
    {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);
        final EditText edtAmount=myview.findViewById(R.id.amount_edt);
        final EditText edtType=myview.findViewById(R.id.type_edt);
        final EditText edtNote=myview.findViewById(R.id.note_edt);
        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type=edtType.getText().toString().trim();
                    String ammount=edtAmount.getText().toString().trim();
                    String note=edtNote.getText().toString().trim();
                    if(TextUtils.isEmpty(type))
                    {
                        edtType.setError("Type is required");
                        return;
                    }
                    if(TextUtils.isEmpty(ammount))
                    {
                        edtAmount.setError("Amount is required");
                        return;
                    }
                    int outammountint=Integer.parseInt(ammount);
                    if(TextUtils.isEmpty(note))
                    {
                        edtNote.setError("Note is required");
                        return;
                    }
                    String id=mIncomeDatabase.push().getKey();
                    String mDate=java.text.DateFormat.getDateInstance().format(java.util.Calendar.getInstance().getTime());
                    Data data = new Data(outammountint, type, mDate, note, id);
                    mIncomeDatabase.child(id).setValue(data, (error, ref) -> {
                        if (error == null) {
                            Toast.makeText(getActivity(), "Income data inserted successfully", Toast.LENGTH_SHORT).show();
                            addAnimation();
                        } else {
                            Toast.makeText(getActivity(), "Failed to insert income data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.dismiss();
                }
                });
        btnCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               addAnimation();
               dialog.dismiss();
           }
       });

        dialog.show();
    }

    public void expenseDataInsert()
    {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);
        final EditText ammount=myview.findViewById(R.id.amount_edt);
        final EditText type=myview.findViewById(R.id.type_edt);
        final EditText note=myview.findViewById(R.id.note_edt);
        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmtype=type.getText().toString().trim();
                String tmammount=ammount.getText().toString().trim();
                String tmnote=note.getText().toString().trim();
                if(TextUtils.isEmpty(tmtype))
                {
                    type.setError("Type is required");
                    return;
                }
                if(TextUtils.isEmpty(tmammount))
                {
                    ammount.setError("Amount is required");
                    return;
                }
                int outammountint=Integer.parseInt(tmammount);
                if(TextUtils.isEmpty(tmnote))
                {
                    note.setError("Note is required");
                    return;
                }
                String id=mExpenseDatabase.push().getKey();
                String mDate=java.text.DateFormat.getDateInstance().format(java.util.Calendar.getInstance().getTime());
                Data data = new Data(outammountint, tmtype, mDate, tmnote, id);
                mExpenseDatabase.child(id).setValue(data, (error, ref) -> {
                    if (error == null) {
                        Toast.makeText(getActivity(), "Expenses data inserted successfully", Toast.LENGTH_SHORT).show();
                        addAnimation();
                    } else {
                        Toast.makeText(getActivity(), "Failed to insert data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}