package com.example.sendscribbles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class postsAdapter extends RecyclerView.Adapter<postsAdapter.ViewHolder> {

    // variables declaration
    private Context context;
    private List<Post> posts;

    // constructor
    public postsAdapter(Context context, List<Post> posts)
    {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        // bind the data
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> list){
        posts.addAll(list);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        // variables declaration
        private TextView tvUsername;
        // private ImageView ivImage;
        private TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // setting the variables
            tvUsername = itemView.findViewById(R.id.tv_postitem_UserTitle);
            //ivImage = itemView.findViewById(R.id.ivImage);
            tvContent = itemView.findViewById(R.id.tv_postitem_Caption);
        }

        public void bind(Post post) {
            // binding
            tvUsername.setText(post.getUser().getUsername());
            tvContent.setText(post.getDescription());
            // --------- TO DO (drawing) -----------------
        }
    }

}
