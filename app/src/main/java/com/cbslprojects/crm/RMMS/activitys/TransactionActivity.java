package com.cbslprojects.crm.RMMS.activitys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.RMMS.adapter.TransactionAdapter;
import com.cbslprojects.crm.RMMS.model.Transaction;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    private ArrayList<Transaction> transactions;
    private ProgressDialog progressDialog;
    private String uid;
    private TextView tv_no_data;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        transactions = new ArrayList<>();
        transactions.add(new Transaction("1234656789", "jitendra"));
        transactions.add(new Transaction("1234656789", "jitendra"));
        transactions.add(new Transaction("1234656789", "jitendra"));

        setTitle("Transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_no_data = findViewById(R.id.tv_no_data);
        RecyclerView rcv_transaction_list = findViewById(R.id.rcv_transaction_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_transaction_list.setLayoutManager(linearLayoutManager);

//        Bundle bd = getArguments();
//        boolean status = false;
//        if (bd != null) {
//            status = bd.getBoolean(Constaints.WHOS_FRAGMENT_LOAD);
//        }

        TransactionAdapter transactionAdapter = new TransactionAdapter(this, transactions);
        rcv_transaction_list.setAdapter(transactionAdapter);
        rcv_transaction_list.setHasFixedSize(true);
        rcv_transaction_list.setNestedScrollingEnabled(true);
        rcv_transaction_list.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        swipeRefreshLayout = findViewById(R.id.swipeRefreshView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                // callApi("");
            }
        });
    }
}
