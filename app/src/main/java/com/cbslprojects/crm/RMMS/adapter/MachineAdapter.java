package com.cbslprojects.crm.RMMS.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.cbslprojects.crm.CRM.Activitys.MainActivity;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.RMMS.Util.Constraints;
import com.cbslprojects.crm.RMMS.activitys.MachineDetailActivity;
import com.cbslprojects.crm.RMMS.dialog.ComplaintRegisterDialogFragment;
import com.cbslprojects.crm.RMMS.model.Machine;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MachineAdapter extends RecyclerView.Adapter<MachineViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<Machine> mBranchDetails;
    private ArrayList<Machine> mBranchDetailsFiltred;
    private String charString;

    public MachineAdapter(Object object, ArrayList<Machine> branchDetails) {
        mBranchDetails = branchDetails;
        mBranchDetailsFiltred = branchDetails;

        mContext = (Context) object;

    }


    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.rcv_machine_item_list, viewGroup, false);
        return new MachineViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineViewHolder holder, final int position) {

        final Machine machine = mBranchDetailsFiltred.get(position);

        if (machine.getBankName() != null)
            holder.tv_bank.setText("Bank : " + machine.getBankName());

        if (machine.getBranch_name() != null)
            holder.tv_branch.setText("Branch : " + machine.getBranch_name());

        if (machine.getBranchCode() != null)
            holder.tv_branch_code.setText("Branch Code : " + machine.getBranchCode());

        if (machine.getMachineNo() != null)
            holder.tv_machine_no.setText("Machine No : " + machine.getMachineNo());

        if (machine.getMachineNo() != null)
            holder.tv_city_name.setText("City Name : " + machine.getCityName());

        if (machine.getMachineNo() != null)
            holder.tv_state_name.setText("State Name : " + machine.getStateName());

        if (charString != null && charString.length() != 0 && machine.getBranch_name() != null)
            setHighLightCharacter("Branch : " + machine.getBranch_name(), charString, holder.tv_branch);

        if (charString != null && charString.length() != 0 && machine.getBankName() != null)
            setHighLightCharacter("Bank : " + machine.getBankName(), charString, holder.tv_bank);

        if (charString != null && charString.length() != 0 && machine.getBranchCode() != null)
            setHighLightCharacter("Branch Code : " + machine.getBranchCode(), charString, holder.tv_branch_code);

        if (charString != null && charString.length() != 0 && machine.getMachineNo() != null)
            setHighLightCharacter("Machine No : " + machine.getMachineNo(), charString, holder.tv_machine_no);

        if (charString != null && charString.length() != 0 && machine.getMachineNo() != null)
            setHighLightCharacter("City Name : " + machine.getCityName(), charString, holder.tv_city_name);

        if (charString != null && charString.length() != 0 && machine.getMachineNo() != null)
            setHighLightCharacter("State Name : " + machine.getStateName(), charString, holder.tv_state_name);

        if (machine.getConnectionStatus() != null) {
            if (machine.getConnectionStatus().trim().equals("Online")) {
                holder.tv_connected.setTextColor(Color.argb(255, 37, 128, 43));
                // holder.tv_connected.setTextColor(Color.alpha(0xFF55efc4));
                holder.tv_connected.setText("Status : Online");
                holder.btn_complaint_register.setVisibility(View.GONE);
            } else {
                holder.tv_connected.setTextColor(Color.RED);
                holder.tv_connected.setText("Status : offline");
                holder.btn_complaint_register.setVisibility(View.VISIBLE);
            }
        } else {
            holder.tv_connected.setTextColor(Color.RED);
            holder.tv_connected.setText("Status : not available");
            holder.btn_complaint_register.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bd = new Bundle();
                bd.putSerializable(Constraints.MACHINE_OBJ, mBranchDetailsFiltred.get(position));
                Intent intent = new Intent(mContext, MachineDetailActivity.class);
                intent.putExtras(bd);
                mContext.startActivity(intent);

//                Intent intent = new Intent(mContext, NotificationActivity.class);
//                mContext.startActivity(intent);

            }
        });

        holder.btn_complaint_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComplaintRegisterDialogFragment complaintRegisterDialogFragment = new ComplaintRegisterDialogFragment();
                Bundle bd = new Bundle();
                bd.putString(Constraints.BRANCH_CODE, machine.getBranchCode());
                bd.putString(Constraints.MACHINE_NO, machine.getMachineNo());
                complaintRegisterDialogFragment.setArguments(bd);
                complaintRegisterDialogFragment.show(((MainActivity) mContext).getSupportFragmentManager(), "ComplaintRegisterDialogFragment");
            }
        });

        holder.btn_total_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mContext.startActivity(new Intent(mContext, TransactionActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBranchDetailsFiltred.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mBranchDetailsFiltred = mBranchDetails;
                } else {
                    ArrayList<Machine> filteredList = new ArrayList<>();
                    for (Machine row : mBranchDetails) {
                        if (row.getMachineNo() != null && row.getBranchCode() != null && row.getBankName() != null && row.getBranch_name() != null) {
                            if (row.getMachineNo().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getBranchCode().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getBankName().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getBranch_name().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getCityName().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getStateName().toLowerCase().contains(charString.toLowerCase())

                            ) {
                                filteredList.add(row);
                            }
                        }
                    }

                    mBranchDetailsFiltred = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mBranchDetailsFiltred;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mBranchDetailsFiltred = (ArrayList<Machine>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    private void setHighLightCharacter(String desc, String query, TextView mTextView) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        Pattern word = Pattern.compile(query.toLowerCase());
        Matcher match = word.matcher(desc.toLowerCase());
        SpannableString redSpannable = new SpannableString(desc);
        while (match.find()) {
            redSpannable.setSpan(new BackgroundColorSpan(Color.YELLOW), match.start(), match.end(), 0);
        }
        builder.append(redSpannable);

        mTextView.setText(builder, TextView.BufferType.SPANNABLE);

    }

}