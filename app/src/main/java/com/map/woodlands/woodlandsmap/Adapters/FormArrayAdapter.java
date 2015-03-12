package com.map.woodlands.woodlandsmap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.R;

import java.util.ArrayList;

/**
 * Created by Jimmy on 3/11/2015.
 */
public class FormArrayAdapter extends ArrayAdapter<Form>{
    private LayoutInflater inflater;
    protected ViewHolder holder;
    private ArrayList<Form> mForms;

    public FormArrayAdapter(Context context, ArrayList<Form> forms) {
        super(context, R.layout.in_listview, forms);
        this.mForms = forms;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Form form = getItem(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.in_listview,parent,false);

            holder = new ViewHolder();
            holder.dateView = (TextView) convertView.findViewById(R.id.dateView);
            holder.statusView = (TextView) convertView.findViewById(R.id.statusView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.dateView.setText(mForms.get(position).INSP_DATE);
        holder.statusView.setText(mForms.get(position).STATUS);

        return convertView;
    }

    static class ViewHolder{
        TextView dateView;
        TextView statusView;
    }
}
