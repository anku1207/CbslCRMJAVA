package com.cbslprojects.crm.CRM.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbslprojects.crm.CRM.Model.User;
import com.cbslprojects.crm.R;
import com.cbslprojects.crm.CRM.Util.Constaints;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.UserDetailViewHolder> {

    private SharedPreferences sharedPreferences;
    private Context mContext;
    private ArrayList<User> users;


    public UserDetailAdapter(Context context, ArrayList<User> users) {
        this.mContext = context;
        this.users = users;
        sharedPreferences = mContext.getSharedPreferences(Constaints.USER_CREDENTIAL_DATA, 0);

    }

    @NonNull
    @Override
    public UserDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.rcv_user_detail_item_list, viewGroup, false);
        return new UserDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailViewHolder holder, final int i) {
        holder.tv_username.setText(users.get(i).getEmail());
        holder.tv_password.setText(users.get(i).getPassword());

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.remove(i);
                notifyDataSetChanged();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                String json = new Gson().toJson(users);
                editor.putString(Constaints.USER_CREDENTIAL_DATA, json);
                editor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username, tv_password;
        private ImageView img_delete;

        public UserDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_password = itemView.findViewById(R.id.tv_password);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
