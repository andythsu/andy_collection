package andy.andy_collection;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CatViewHolder> {

    private List<Collection> list;
    private Context context;

    public static class CatViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name;
        TextView location;
        TextView category;
        public CatViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.main_cv);
            name = (TextView)itemView.findViewById(R.id.name);
            location = (TextView)itemView.findViewById(R.id.location);
            category = (TextView)itemView.findViewById(R.id.category);
        }
    }

    public RVAdapter(Context context, List<Collection> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RVAdapter.CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_view, parent, false);
        return new RVAdapter.CatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.CatViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.location.setText(list.get(position).getLocation());
        holder.category.setText(list.get(position).getCategory());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked" + list.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
