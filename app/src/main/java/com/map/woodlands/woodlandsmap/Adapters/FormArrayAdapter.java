package com.map.woodlands.woodlandsmap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.map.woodlands.woodlandsmap.Data.DeleteView;
import com.map.woodlands.woodlandsmap.Data.Form;
import com.map.woodlands.woodlandsmap.Data.WarningView;
import com.map.woodlands.woodlandsmap.Fragments.FormFragment;
import com.map.woodlands.woodlandsmap.R;

import java.util.ArrayList;

/**
 * Created by Jimmy on 3/11/2015.
 */
public class FormArrayAdapter extends ArrayAdapter<Form>{
    private LayoutInflater inflater;
    protected ViewHolder holder;
    private ArrayList<Form> mForms;
    private Context mContext;
    private FormFragment mFormFragment;

    public FormArrayAdapter(Context context, ArrayList<Form> forms, FormFragment formFragment) {
        super(context, R.layout.in_listview, forms);
        this.mContext = context;
        this.mForms = forms;
        this.mFormFragment = formFragment;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Form form = mForms.get(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.in_listview,parent,false);

            holder = new ViewHolder();
            holder.dateView = (TextView) convertView.findViewById(R.id.dateView);
            holder.timestampView = (TextView) convertView.findViewById(R.id.timestamp);
            holder.statusView = (TextView) convertView.findViewById(R.id.statusView);
            holder.warningIcon = (ImageView) convertView.findViewById(R.id.warningIcon);
            holder.deleteIcon = (ImageView) convertView.findViewById(R.id.deleteIcon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(form.messages == null){
            holder.warningIcon.setVisibility(View.GONE);
        }else{
            if(form.messages.size() != 0) {
                holder.warningIcon.setVisibility(View.VISIBLE);
            }else{
                holder.warningIcon.setVisibility(View.GONE);
            }
        }

        holder.dateView.setText(form.INSP_DATE);
        holder.timestampView.setText(form.timestamp);
        holder.statusView.setText(form.STATUS);
        holder.warningIcon.setOnClickListener(new WarningView(mContext,form));
        holder.deleteIcon.setOnClickListener(new DeleteView(mContext,form,mFormFragment));
        return convertView;
    }

    static class ViewHolder{
        TextView dateView;
        TextView timestampView;
        TextView statusView;
        ImageView warningIcon;
        ImageView deleteIcon;

    }
}
