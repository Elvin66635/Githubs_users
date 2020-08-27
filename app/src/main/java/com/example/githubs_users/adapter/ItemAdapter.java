package com.example.githubs_users.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.githubs_users.model.CustomItemClickListener;
import com.example.githubs_users.R;
import com.example.githubs_users.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Item> userList;
    private ArrayList<Item> filteredUserList;
    private Context context;
    private CustomItemClickListener customItemClickListener;
    public ItemAdapter(Context context, ArrayList<Item> userArrayList, CustomItemClickListener customItemClickListener) {
        this.context = context;
        this.userList = userArrayList;
        this.filteredUserList = userArrayList;
        this.customItemClickListener = customItemClickListener;
    }

    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customItemClickListener.onItemClick(filteredUserList.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());

            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.MyViewHolder viewHolder, int position) {

        viewHolder.title.setText(filteredUserList.get(position).getLogin());
        viewHolder.githubLink1.setText(filteredUserList.get(position).getHtml_url());

        Picasso.with(context)
                .load(userList.get(position).getAvatar_url())
                .placeholder(R.drawable.progress_animation)
                .into(viewHolder.image_detail);
    }
    @Override
    public int getItemCount() {
        return filteredUserList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchString = charSequence.toString();
                if (searchString.isEmpty()) {
                    filteredUserList = userList;
                } else {
                    ArrayList<Item> tempFilteredList = new ArrayList<>();
                    for (Item item : userList) {
                        // search for user name
                        if (item.getLogin().toLowerCase().contains(searchString)) {
                            tempFilteredList.add(item);
                        }
                    }
                    filteredUserList = tempFilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUserList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredUserList = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, githubLink1;
        ImageView image_detail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) itemView.findViewById(R.id.title_row);
            githubLink1 = (TextView) itemView.findViewById(R.id.githubLink1);
            image_detail = (ImageView) itemView.findViewById(R.id.cover);
        }
    }
}