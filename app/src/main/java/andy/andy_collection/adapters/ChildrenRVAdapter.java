package andy.andy_collection.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import andy.andy_collection.R;
import andy.andy_collection.structure.Collection;
import andy.andy_collection.structure.Node;
import andy.andy_collection.structure.Tree;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChildrenRVAdapter extends RecyclerView.Adapter<ChildrenRVAdapter.ChildrenViewHolder> {

    List<Node> list;
    Context context;

    public static class ChildrenViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView location;
        TextView category;

        public ChildrenViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.main_cv);
            name = (TextView) itemView.findViewById(R.id.children_name);
            location = (TextView) itemView.findViewById(R.id.children_location);
            category = (TextView) itemView.findViewById(R.id.children_category);
        }
    }

    public ChildrenRVAdapter(Context context, List<Node> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ChildrenRVAdapter.ChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.children_card_view, parent, false);
        return new ChildrenRVAdapter.ChildrenViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildrenViewHolder holder, int position) {
        holder.name.setText(list.get(position).getData().getName());
        holder.location.setText(list.get(position).getData().getLocation());
        holder.category.setText(list.get(position).getData().getCategory());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
