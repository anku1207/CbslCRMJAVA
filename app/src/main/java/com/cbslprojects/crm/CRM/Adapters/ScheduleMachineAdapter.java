package com.cbslprojects.crm.CRM.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cbslprojects.crm.CRM.Activitys.Schedule_Machine_List_Description;
import com.cbslprojects.crm.CRM.Model.BranchDetails;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.cbslprojects.crm.CRM.Util.Utility;
import com.cbslprojects.crm.CRM.fragments.ScheduleMachineFragment;

import java.util.ArrayList;


public class ScheduleMachineAdapter extends RecyclerView.Adapter<ScheduleMachineAdapter.RegisterComplaintViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<BranchDetails> mBranchDetails;
    private ArrayList<BranchDetails> mBranchDetailsFiltred;
    private String charString;
    private ScheduleMachineFragment scheduleMachineFragment;


    public ScheduleMachineAdapter(Context context, ScheduleMachineFragment scheduleMachineFragment, ArrayList<BranchDetails> branchDetails) {
        mBranchDetails = branchDetails;
        mBranchDetailsFiltred = branchDetails;
        mContext = context;
        this.scheduleMachineFragment = scheduleMachineFragment;
    }


    @NonNull
    @Override
    public RegisterComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.rcv_register_complaint_layout, viewGroup, false);
        return new RegisterComplaintViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterComplaintViewHolder holder, int position) {

        final BranchDetails branchDetails = mBranchDetailsFiltred.get(position);

        holder.bankname.setText("Bank Name: " + branchDetails.getBankName());
        holder.branchname.setText("Branch Name: " + branchDetails.getBranchName());
        holder.branchCode.setText("Sole Id: " + branchDetails.getSoleId());

//        if (branchDetails.getMachineNo() != null)
//            holder.machineNo.setText("Machine No: " + branchDetails.getMachineNo());

        if (charString != null && charString.length() != 0)
            Utility.setHighLightCharacter("Bank Name : " + branchDetails.getBankName(),
                    charString, holder.bankname);

        if (charString != null && charString.length() != 0)
            Utility.setHighLightCharacter("Sole Id : " + branchDetails.getSoleId(),
                    charString, holder.branchCode);

        if (charString != null && charString.length() != 0)
            Utility.setHighLightCharacter("Branch Name : " + branchDetails.getBranchName(),
                    charString, holder.branchname);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Schedule_Machine_List_Description.class);
                intent.putExtra(Constaints.SOLEID, branchDetails.getSoleId());
                mContext.startActivity(intent);
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
                    ArrayList<BranchDetails> filteredList = new ArrayList<>();
                    for (BranchDetails row : mBranchDetails) {

                        if (row.getSoleId().toLowerCase().contains(charString.toLowerCase())
                                || row.getBankName().toLowerCase().contains(charString.toLowerCase())
                                || row.getBranchName().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
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
                mBranchDetailsFiltred = (ArrayList<BranchDetails>) filterResults.values;

                scheduleMachineFragment.showTotalComplaintNumber(mBranchDetailsFiltred.size());

                notifyDataSetChanged();
            }
        };
    }

    class RegisterComplaintViewHolder extends RecyclerView.ViewHolder {
        private TextView bankname, branchCode, machineNo, branchname;

        RegisterComplaintViewHolder(@NonNull View convertView) {
            super(convertView);

            bankname = convertView.findViewById(R.id.bank_name_textview);
            branchname = convertView.findViewById(R.id.branch_name_textview);
            branchCode = convertView.findViewById(R.id.branch_code_textview);
            machineNo = convertView.findViewById(R.id.machine_no_textview);
        }


    }
}
