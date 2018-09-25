package andy.andy_collection;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.*;
import andy.andy_collection.adapters.ChildrenRVAdapter;
import andy.andy_collection.controllers.swipeController;
import andy.andy_collection.structure.Collection;
import andy.andy_collection.structure.Node;

import java.util.ArrayList;

public class ChildrenActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView children_list_view;
    Node parent_node;
    ArrayList<Node> children_node;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.children_recycler_view);

//        children_list_view = (ListView) findViewById(R.id.children_list);

        // getting data passed in from MainActivity
//        Node n = (Node) getIntent().getParcelableExtra("node");

        parent_node = getIntent().getParcelableExtra("node");

        children_node = parent_node.getChildren();

        rv = (RecyclerView) findViewById(R.id.children_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ChildrenRVAdapter adapter = new ChildrenRVAdapter(this, children_node);
        swipeController sc = new swipeController();
        ItemTouchHelper touchHelper = new ItemTouchHelper(sc);

        rv.setAdapter(adapter);
        touchHelper.attachToRecyclerView(rv);
        
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        // getting all names and store as array to display to ListView
//        String[] children_node_names = new String[children_node.size()];
//
//        for(int i=0; i<children_node_names.length; i++){
//            children_node_names[i] = children_node.get(i).getData().getName();
//        }
        ////////////////////////////////////////////////////////////////

//        ArrayAdapter<String> parent_list_adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, children_node_names);
//        children_list_view.setAdapter(parent_list_adapter);
//        children_list_view.setOnItemClickListener(this);
    }

    // sets the event listener when clicking on the list item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Collection data = children_node.get(position).getData();
        StringBuffer buffer = new StringBuffer();
        TextView text = new TextView(this);
        text.setText(data.toString());
        text.setTextIsSelectable(true);
        text.setTextSize(16);
        text.setPadding(15, 10, 0, 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(text)
                .setTitle("Data")
                .setCancelable(true)
                .show();
    }

    // overrides the animation when going back to previous intent
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
