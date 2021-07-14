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

import com.cbslprojects.crm.CRM.Activitys.Resolve_Complaint;
import com.cbslprojects.crm.CRM.Model.ReloveComplaint;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.Utility;
import com.cbslprojects.crm.CRM.fragments.ResolveComplaintFragment;

import java.util.ArrayList;


public class ResolveComplaintAdapter extends RecyclerView.Adapter<ResolveComplaintAdapter.RegisterComplaintViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<ReloveComplaint> mReloveComplaintArrayList;
    private ArrayList<ReloveComplaint> mReloveComplaintArrayListFiltred;
    private String charString;
    private ResolveComplaintFragment resolveComplaintFragment;

    public ResolveComplaintAdapter(Context context, ResolveComplaintFragment resolveComplaintFragment, ArrayList<ReloveComplaint> reloveComplaints) {
        mReloveComplaintArrayList = reloveComplaints;
        mReloveComplaintArrayListFiltred = reloveComplaints;
        mContext = context;
        this.resolveComplaintFragment=resolveComplaintFragment;
    }


    @NonNull
    @Override
    public RegisterComplaintViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.list_resolve_complaint, viewGroup, false);
        return new RegisterComplaintViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterComplaintViewHolder holder, int position) {
        final ReloveComplaint list = mReloveComplaintArrayListFiltred.get(position);

        holder.bankname.setText("Bank : " + list.getBankName());
        holder.branchName.setText("Branch : " + list.getBranchName());
        holder.complaintdate.setText("Date : " + list.getComplaintDate());
        holder.sole_id.setText("Sole ID : " + list.getSoleid());
        holder.complainttype.setText("Complaint type : " + list.getComplaintType());
        holder.complaintno.setText("Complaint No : " + list.getComplaintNumber());
        holder.projectname.setText("" + list.getProjectname());

        if (charString!=null&&charString.length() != 0)
            Utility.setHighLightCharacter("Sole ID : "+list.getSoleid(),
                    charString, holder.sole_id);

        if (charString!=null&&charString.length() != 0)
            Utility.setHighLightCharacter("Bank : "+list.getBankName(),
                    charString, holder.bankname);

        if (charString!=null&&charString.length() != 0)
            Utility.setHighLightCharacter(""+list.getProjectname(),
                    charString, holder.projectname);

        if (charString!=null&&charString.length() != 0)
            Utility.setHighLightCharacter("Branch : "+list.getBranchName(),
                    charString, holder.branchName);

        if (charString!=null&&charString.length() != 0)
            Utility.setHighLightCharacter("Complaint type : "+list.getComplaintType(),
                    charString, holder.complainttype);

        if (charString!=null&&charString.length() != 0)
            Utility.setHighLightCharacter("Complaint No : "+list.getComplaintNumber(),
                    charString, holder.complaintno);

        if (charString!=null&&charString.length() != 0)
            Utility.setHighLightCharacter("Date : "+list.getComplaintDate(),
                    charString, holder.complaintdate);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Resolve_Complaint.class);
                intent.putExtra("complaintNo", list.getComplaintNumber());
                intent.putExtra("machineID", list.getMachindId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReloveComplaintArrayListFiltred.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                 charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mReloveComplaintArrayListFiltred = mReloveComplaintArrayList;
                } else {
                    ArrayList<ReloveComplaint> filteredList = new ArrayList<>();
                    for (ReloveComplaint row : mReloveComplaintArrayList) {
                        if (row.getSoleid().toLowerCase().contains(charString.toLowerCase())
                               || row.getBankName().toLowerCase().contains(charString.toLowerCase())
                                || row.getProjectname().toLowerCase().contains(charString.toLowerCase())
                                || row.getBranchName().toLowerCase().contains(charString.toLowerCase())
                                || row.getComplaintType().toLowerCase().contains(charString.toLowerCase())
                                || row.getComplaintNumber().toLowerCase().contains(charString.toLowerCase())
                                || row.getComplaintDate().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
                        }
                    }

                    mReloveComplaintArrayListFiltred = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mReloveComplaintArrayListFiltred;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mReloveComplaintArrayListFiltred = (ArrayList<ReloveComplaint>) filterResults.values;

              //  if (charString!=null&&charString.length() != 0)
                    resolveComplaintFragment.showTotalComplaintNumber(mReloveComplaintArrayListFiltred.size());

                notifyDataSetChanged();
            }
        };
    }

    class RegisterComplaintViewHolder extends RecyclerView.ViewHolder {
        TextView bankname, branchName, complainttype, sole_id, complaintno, complaintdate, projectname;

        RegisterComplaintViewHolder(@NonNull View convertView) {
            super(convertView);
            bankname = convertView.findViewById(R.id.list_bank_name);
            branchName = convertView.findViewById(R.id.list_branch_name);
            complaintdate = convertView.findViewById(R.id.list_date);
            sole_id = convertView.findViewById(R.id.list_sole_id);
            complaintno = convertView.findViewById(R.id.list_complaint_no);
            complainttype = convertView.findViewById(R.id.list_complaint_type);
            projectname = convertView.findViewById(R.id.list_project_name);

        }


    }
}
