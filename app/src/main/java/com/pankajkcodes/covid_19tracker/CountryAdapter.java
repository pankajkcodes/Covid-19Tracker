package com.pankajkcodes.covid_19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pankajkcodes.covid_19tracker.api.CountryData;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private Context context;
    private List<CountryData> list;

    public CountryAdapter(Context context, List<CountryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_item_layout,parent,false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountryData data = list.get(position);
        holder.Total_cases.setText(NumberFormat.getInstance().format(Integer.parseInt(data.getCases())));
        holder.country_name.setText(data.getCountry());
        holder.serial.setText(String.valueOf(position+1));

        Map<String, String> img = data.getCountryInfo();
        Glide.with(context).load(img.get("flag")).into(holder.flag);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder{

        TextView serial,country_name,Total_cases;
        ImageView flag;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            serial  = itemView.findViewById(R.id.serial);
            country_name  = itemView.findViewById(R.id.country_item_name);
            Total_cases = itemView.findViewById(R.id.cases_item);
            flag  = itemView.findViewById(R.id.flag_item);

        }
    }
}
