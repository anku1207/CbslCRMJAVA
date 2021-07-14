package com.cbslprojects.crm.RMMS.fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbslprojects.crm.R;
import com.cbslprojects.crm.RMMS.Interface.SearchSoldIdListener;
import com.cbslprojects.crm.RMMS.Util.ApiClient;
import com.cbslprojects.crm.RMMS.Util.Utility;
import com.cbslprojects.crm.RMMS.adapter.MachineAdapter;
import com.cbslprojects.crm.RMMS.model.Machine;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class RMMSFragment extends Fragment implements SearchSoldIdListener {

    private LinearLayout ll_machine, ll_online, ll_offline;
    private ImageView img_machine_sign, img_online_sign, img_offline_sign;
    private TextView tv_machine, tv_online, tv_offline;
    private ArrayList<Machine> arrayList_filter;
    private ArrayList<Machine> arrayList;
    private MachineAdapter machineAdapter;
    private ProgressDialog progressDialog;
    private String bankId;
    private TextView tv_no_data;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final String machine_total = "machine_total";
    private final String machine_online = "machine_online";
    private final String machine_offline = "machine_offline";
    private String filter_type = machine_total;

    public RMMSFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_rmms, container, false);
        Bundle bundle = savedInstanceState;
        ll_machine = v.findViewById(R.id.ll_machine);
        ll_online = v.findViewById(R.id.ll_online);
        ll_offline = v.findViewById(R.id.ll_offline);

        tv_machine = v.findViewById(R.id.tv_machine);
        tv_online = v.findViewById(R.id.tv_online);
        tv_offline = v.findViewById(R.id.tv_offline);

        img_machine_sign = v.findViewById(R.id.img_machine_sign);
        img_online_sign = v.findViewById(R.id.img_online_sign);
        img_offline_sign = v.findViewById(R.id.img_offline_sign);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);


        if (bundle == null)
            showMachine();

//        Bundle bd = getArguments();
//        if (bd != null) {
//            bankId = bd.getString(Constraints.ZONE_ID);
//            getMachineDetailByBankID(bankId, machine_total);
//        } else {
        //  bankId = MyPrefences.getInstance(getContext()).getString(MyPrefences.ZONE_ID, null);
        bankId = "0";
        // }


        ll_machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMachine();
                filterMachine(machine_total);
                filter_type = machine_total;
            }
        });

        ll_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_machine_sign.setVisibility(View.INVISIBLE);
                img_online_sign.setVisibility(View.VISIBLE);
                img_offline_sign.setVisibility(View.INVISIBLE);
                ll_machine.setBackgroundColor(Color.WHITE);
                ll_online.setBackgroundColor(Color.GRAY);
                ll_offline.setBackgroundColor(Color.WHITE);

                filterMachine(machine_online);
                filter_type = machine_online;
            }
        });

        ll_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_machine_sign.setVisibility(View.INVISIBLE);
                img_online_sign.setVisibility(View.INVISIBLE);
                img_offline_sign.setVisibility(View.VISIBLE);
                ll_machine.setBackgroundColor(Color.WHITE);
                ll_online.setBackgroundColor(Color.WHITE);
                ll_offline.setBackgroundColor(Color.GRAY);

                filterMachine(machine_offline);
                filter_type = machine_offline;
            }
        });


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        arrayList_filter = new ArrayList<>();
        arrayList = new ArrayList<>();

        tv_no_data = v.findViewById(R.id.tv_no_data);
        RecyclerView rcv_machine_list = v.findViewById(R.id.rcv_machine_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcv_machine_list.setLayoutManager(linearLayoutManager);

        machineAdapter = new MachineAdapter(getContext(), arrayList_filter);
        rcv_machine_list.setAdapter(machineAdapter);
        rcv_machine_list.setHasFixedSize(true);
        //  rcv_machine_list.addItemDecoration(new DividerItemDecoration((getContext()), DividerItemDecoration.VERTICAL));
        rcv_machine_list.setNestedScrollingEnabled(true);


        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshView);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isNetworkAvailable(getContext())) {

                    getMachineDetail(bankId, filter_type);
                } else {
//                    Toast.makeText(getContext(),
//                            "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                    Utility.callFragment(getActivity(), new NetworkCheckFragment());
                }

            }
        });
        if (arrayList_filter.size() == 0) {
            if (Utility.isNetworkAvailable(getContext())) {
                if (savedInstanceState == null)
                    getMachineDetail(bankId, machine_total);
            } else {
//                Toast.makeText(getContext(),
//                        "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                Utility.callFragment(getActivity(), new NetworkCheckFragment());
            }
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }



    private void showMachine() {
        img_machine_sign.setVisibility(View.VISIBLE);
        img_online_sign.setVisibility(View.INVISIBLE);
        img_offline_sign.setVisibility(View.INVISIBLE);
        ll_machine.setBackgroundColor(Color.GRAY);
        ll_online.setBackgroundColor(Color.WHITE);
        ll_offline.setBackgroundColor(Color.WHITE);
    }


    private void getMachineDetail(String bankId, final String type) {
        progressDialog.show();

        Call<Machine> listCall = ApiClient.callJsonRetrofit().getMachineDetail(bankId);

        listCall.enqueue(new Callback<Machine>() {
            @Override
            public void onResponse(@NonNull Call<Machine> call, @NonNull retrofit2.Response<Machine> response) {
                try {
                    Machine machine = response.body();

                    if (machine == null) {
                        Toast.makeText(getContext(), "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }
                    arrayList_filter.clear();
                    arrayList.clear();

                    if (machine.getStatus().equals("Success")) {
                        arrayList.addAll(machine.getData());
                        arrayList_filter.addAll(machine.getData());

                        filterMachine(type);
                    } else {
                        Toast.makeText(getContext(), machine.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (arrayList_filter.size() == 0) {
                        tv_no_data.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_data.setVisibility(View.GONE);
                    }
                    progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Machine> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMachineDetailByZoneID(String zoneId, final String type) {
        progressDialog.show();

        Call<Machine> listCall = ApiClient.callJsonRetrofit().getMachineDetailByBankID(zoneId);

        listCall.enqueue(new Callback<Machine>() {
            @Override
            public void onResponse(@NonNull Call<Machine> call, @NonNull retrofit2.Response<Machine> response) {
                try {
                    Machine machine = response.body();

                    if (machine == null) {
                        Toast.makeText(getContext(), "Error : " + response.message(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }
                    arrayList_filter.clear();
                    arrayList.clear();

                    if (machine.getStatus().equals("Success")) {
                        arrayList.addAll(machine.getData());
                        arrayList_filter.addAll(machine.getData());

                        filterMachine(type);
                    } else {
                        Toast.makeText(getContext(), machine.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (arrayList_filter.size() == 0) {
                        tv_no_data.setVisibility(View.VISIBLE);
                    } else {
                        tv_no_data.setVisibility(View.GONE);
                    }
                    progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Machine> call, @NonNull Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void filterMachine(String type) {

        ArrayList<Machine> online_machines = new ArrayList<>();
        ArrayList<Machine> offline_machines = new ArrayList<>();


        for (Machine row : arrayList) {
            if (row.getConnectionStatus() != null) {
                if (row.getConnectionStatus().equals("Online")) {
                    online_machines.add(row);
                } else {
                    offline_machines.add(row);
                }
            } else {
                offline_machines.add(row);
            }
        }
        arrayList_filter.clear();
        switch (type) {
            case machine_total:
                arrayList_filter.addAll(arrayList);
                break;
            case machine_online:
                arrayList_filter.addAll(online_machines);
                break;
            case machine_offline:
                arrayList_filter.addAll(offline_machines);
                break;
        }
        machineAdapter.notifyDataSetChanged();
        tv_machine.setText("" + arrayList.size());
        tv_online.setText("" + online_machines.size());
        tv_offline.setText("" + offline_machines.size());

        if (arrayList_filter.size() == 0) {
            tv_no_data.setVisibility(View.VISIBLE);
        } else {
            tv_no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public void SearchSoldId(String query) {
        machineAdapter.getFilter().filter(query);
    }

    @Override
    public void SearchSoldIdOnline(String query) {
        boolean b = Pattern.compile("-?[0-9]+").matcher(query).matches();

        if (Utility.isNetworkAvailable(getContext())) {
            if (query.equals("0")) {
                if (arrayList_filter.size() == 0)
                    getMachineDetail(query, filter_type);
            } else {
                if (b) {
                    getMachineDetailByZoneID(query, filter_type);
                } else {
                    if(!query.isEmpty())
                    Toast.makeText(getContext(), "Zone Id not valid", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getContext(),
                    "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
