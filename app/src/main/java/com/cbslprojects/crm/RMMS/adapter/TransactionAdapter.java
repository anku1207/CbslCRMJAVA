package com.cbslprojects.crm.RMMS.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.RMMS.model.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context mContext;
    private ArrayList<Transaction> transactions;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        this.mContext = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.rcv_transaction_item_list, viewGroup, false);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int i) {
        holder.tv_account_holder.setText("Account Holder Name : "+transactions.get(i).getAccountHolderName());
        holder.tv_account_number.setText("Account No. : "+transactions.get(i).getAccountNo());

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }


    class TransactionViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_account_holder, tv_account_number;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_account_holder = itemView.findViewById(R.id.tv_account_holder);
            tv_account_number = itemView.findViewById(R.id.tv_account_number);

        }


    }
}

