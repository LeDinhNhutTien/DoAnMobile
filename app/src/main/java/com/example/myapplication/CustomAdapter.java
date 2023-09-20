package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<DocBao> {

    private ArrayList<DocBao> originalList;
    private ArrayList<DocBao> filteredList;
    private ItemFilter itemFilter = new ItemFilter();

    public CustomAdapter(@NonNull Context context, int resource, List<DocBao> items) {
        super(context, resource,items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if (view == null){
            LayoutInflater inflater =  LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.dong_layout_listview, null);
        }
        DocBao p = getItem(position);
        if (p != null){
            // Anh xa + Gan gia tri
            TextView txtTitle = (TextView) view.findViewById(R.id.textViewTitle);
            txtTitle.setText(p.title);

            TextView txtDate = (TextView) view.findViewById(R.id.textViewDate);
            txtDate.setText(p.getDate());

            ImageView imageView = view.findViewById(R.id.imageView);
            Picasso.get().load(p.image).into(imageView);
        }
        return view;
    }
    public CustomAdapter(Context context, int resource, ArrayList<DocBao> objects) {
        super(context, resource, objects);
        originalList = objects;
        filteredList = objects;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public DocBao getItem(int position) {
        return filteredList.get(position);
    }


    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            ArrayList<DocBao> tempList = new ArrayList<>();

            for (DocBao item : originalList) {
                if (item.getTitle().toLowerCase().contains(query)) {
                    tempList.add(item);
                }
            }
            results.count = tempList.size();
            results.values = tempList;

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<DocBao>) results.values;
            notifyDataSetChanged();
        }
    }
}
