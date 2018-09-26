package andy.andy_collection.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import andy.andy_collection.ChildrenActivity;
import andy.andy_collection.MainActivity;
import andy.andy_collection.structure.Collection;
import andy.andy_collection.R;
import andy.andy_collection.structure.Node;
import andy.andy_collection.structure.Tree;

import java.util.ArrayList;
import java.util.List;

public class MainRVAdapter extends RecyclerView.Adapter<MainRVAdapter.MainViewHolder> {

    private List<Node> parent_nodes;
    private Context context;


    public static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView location;
        TextView category;

        public MainViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.main_cv);
            name = (TextView) itemView.findViewById(R.id.name);
//            location = (TextView)itemView.findViewById(R.id.location);
//            category = (TextView)itemView.findViewById(R.id.category);
        }
    }

    public MainRVAdapter(Context context, List<Node> parent_nodes) {
        this.context = context;
        this.parent_nodes = parent_nodes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_view, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, final int position) {
        holder.name.setText(parent_nodes.get(position).getName());
//        holder.location.setText(list.get(position).getLocation());
//        holder.category.setText(list.get(position).getCategory());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Node parent_node = parent_nodes.get(position);
                Intent i = new Intent(context, ChildrenActivity.class);
                ActivityOptions animation = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.slide_out_left);
                i.putExtra("node", parent_node);
                v.getContext().startActivity(i, animation.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return parent_nodes.size();
    }

}
