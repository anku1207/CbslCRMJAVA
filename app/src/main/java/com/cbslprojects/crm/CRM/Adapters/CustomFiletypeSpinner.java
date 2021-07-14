package com.cbslprojects.crm.CRM.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cbslprojects.crm.CRM.Model.GetFileType;
import com.cbslprojects.crm.R;

import java.util.ArrayList;

public class CustomFiletypeSpinner extends BaseAdapter {

    private ArrayList<GetFileType> arrayList;
    private LayoutInflater inflter;


    public CustomFiletypeSpinner(Context context, ArrayList<GetFileType> arrayList) {
        this.arrayList = arrayList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        if (arrayList == null)
            return 0;
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.get_file_type, parent,false);

        GetFileType getFileType = arrayList.get(position);
        TextView textView_filetype = convertView.findViewById(R.id.file_list_text);
        textView_filetype.setText(getFileType.getFilename());
        return convertView;


    }
}
