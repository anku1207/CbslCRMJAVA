package com.cbslprojects.crm.RMMS.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cbslprojects.crm.R;


public class MachineViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_bank_id, tv_bank, tv_branch, tv_branch_code, tv_machine_no, tv_connected,tv_city_name,tv_state_name, tv_total_transaction;
    public Button btn_complaint_register,btn_total_transaction;

    public MachineViewHolder(@NonNull View convertView) {
        super(convertView);

        tv_bank_id = convertView.findViewById(R.id.tv_zone_id);
        tv_bank = convertView.findViewById(R.id.tv_zone);

        tv_branch = convertView.findViewById(R.id.tv_branch);
        tv_branch_code = convertView.findViewById(R.id.tv_branch_code);
        tv_machine_no = convertView.findViewById(R.id.tv_machine_no);
        tv_connected = convertView.findViewById(R.id.tv_connected);
        tv_total_transaction = convertView.findViewById(R.id.tv_total_transaction);
        btn_complaint_register = convertView.findViewById(R.id.btn_complaint_register);
        btn_total_transaction = convertView.findViewById(R.id.btn_total_transaction);
        tv_city_name = convertView.findViewById(R.id.tv_city_name);
        tv_state_name = convertView.findViewById(R.id.tv_state_name);
    }


}
