package com.map.woodlands.woodlandsroad.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.map.woodlands.woodlandsroad.Data.RoadForm;
import com.map.woodlands.woodlandsroad.Fragments.RoadFormFragment;
import com.map.woodlands.woodlandsroad.R;

import java.util.ArrayList;

/**
 * Created by Jimmy on 3/11/2015.
 * Used for the listview in FormFragment
 */
public class RoadFormArrayAdapter extends ArrayAdapter<RoadForm>{
    private LayoutInflater inflater;
    protected ViewHolder holder;
    private ArrayList<RoadForm> mForms;
    private Context mContext;
    private RoadFormFragment mFormFragment;

    public RoadFormArrayAdapter(Context context, ArrayList<RoadForm> forms, RoadFormFragment formFragment) {
        super(context, R.layout.in_listview2, forms);
        this.mContext = context;
        this.mForms = forms;
        this.mFormFragment = formFragment;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoadForm form = mForms.get(position);

        if(convertView == null){
            convertView = inflater.inflate(R.layout.in_listview2,parent,false);

            holder = new ViewHolder();
            holder.positionView = (TextView) convertView.findViewById(R.id.position);
            holder.dateView = (TextView) convertView.findViewById(R.id.dateView);
            holder.timestampView = (TextView) convertView.findViewById(R.id.timestamp);
            holder.statusView = (TextView) convertView.findViewById(R.id.statusView);
            holder.warningIcon = (ImageView) convertView.findViewById(R.id.warningIcon);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

//            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
//                    mForms.get(getPosition).isSelected = buttonView.isChecked(); // Set the value of checkbox to maintain its state.
//                }
//            });

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(form.messages == null){
            holder.warningIcon.setVisibility(View.GONE);
            holder.statusView.setTextColor(mContext.getResources().getColor(R.color.green));
        }else{
            holder.warningIcon.setVisibility(View.VISIBLE);
            holder.statusView.setTextColor(mContext.getResources().getColor(R.color.red));
        }

//        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(mForms.get(position).isSelected);


        holder.positionView.setText(position+1+".");
        holder.dateView.setText(form.INSP_DATE);
        holder.statusView.setText(form.STATUS);
//        holder.warningIcon.setOnClickListener(new WarningView(mContext,form));
        return convertView;
    }

    static class ViewHolder{
        TextView positionView;
        TextView dateView;
        TextView timestampView;
        TextView statusView;
        ImageView warningIcon;
        CheckBox checkBox;

    }
}
