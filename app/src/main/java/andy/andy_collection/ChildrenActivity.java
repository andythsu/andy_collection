package andy.andy_collection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.widget.*;
import andy.andy_collection.adapters.AzureServiceAdapter;
import andy.andy_collection.adapters.ChildrenRVAdapter;
import andy.andy_collection.swiping.swipeButtonListener;
import andy.andy_collection.swiping.swipeController;
import andy.andy_collection.structure.Collection;
import andy.andy_collection.structure.Node;
import andy.andy_collection.util.Util;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.util.ArrayList;

public class ChildrenActivity extends AppCompatActivity {
    ListView children_list_view;
    Node parent_node;
    ArrayList<Node> children_node;
    RecyclerView rv;
    ChildrenRVAdapter adapter;
    private static MobileServiceClient mClient = AzureServiceAdapter.getInstance().getClient();


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

        adapter = new ChildrenRVAdapter(this, children_node);
        swipeController sc = new swipeController(new swipeButtonListener() {
            @Override
            public void onClicked(String op, int position) {
                Collection data = children_node.get(position).getData();
                if (op.equals("edit")) {
                    edit(data);
                } else if (op.equals("delete")) {
                    delete(data);
                }
            }
        });
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

    // overrides the animation when going back to previous intent
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void edit(final Collection data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String name = data.getName();
        String location = data.getLocation();
        String category = data.getCategory();

        final EditText name_input = new EditText(this);
        final EditText location_input = new EditText(this);
        final EditText category_input = new EditText(this);

        final TextView name_label = new TextView(this);
        final TextView location_label = new TextView(this);
        final TextView category_label = new TextView(this);

        /* initializes the input fields */
        name_input.setText(name);
        location_input.setText(location);
        category_input.setText(category);

        /* initializes the labels */
        name_label.setText("Name: ");
        name_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        location_label.setText("Location: ");
        location_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        category_label.setText("Category: ");
        category_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(name_label);
        ll.addView(name_input);
        ll.addView(location_label);
        ll.addView(location_input);
        ll.addView(category_label);
        ll.addView(category_input);

        builder.setView(ll);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            mClient.getTable(Collection.class).update(data).get();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.toast(ChildrenActivity.this, "Updated Successfully");
                                }
                            });
                        } catch (final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.toast(ChildrenActivity.this, e.getMessage());
                                }
                            });
                        }
                        return null;
                    }
                };
                Util.runAsyncTask(task);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public void delete(final Collection data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView label = new TextView(this);
        label.setText("Are you sure you want to delete?");
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        LinearLayout ll = new LinearLayout(this);

        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(label);
        builder.setView(ll);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
// perform delete
                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            mClient.getTable(Collection.class).delete(data).get();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.toast(ChildrenActivity.this, "Deleted Successfully");
                                }
                            });
                        } catch (final Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.toast(ChildrenActivity.this, e.getMessage());
                                }
                            });
                        }
                        return null;
                    }
                };
                Util.runAsyncTask(task);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        builder.show();

    }

}
