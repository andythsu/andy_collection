package andy.andy_collection;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<Collection> list;

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
