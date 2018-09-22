package andy.andy_collection;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import andy.andy_collection.adapters.AzureServiceAdapter;
import andy.andy_collection.adapters.MainRVAdapter;
import andy.andy_collection.structure.Collection;
import andy.andy_collection.structure.Node;
import andy.andy_collection.structure.Tree;
import com.microsoft.windowsazure.mobileservices.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static MobileServiceClient mClient;
    //    ListView parent_list_view;
    TextView exception_label;
    FloatingActionButton add_btn;

    RecyclerView rv;

//    ArrayAdapter<String> parent_list_adapter;

    Tree t = new Tree();
    Node root = t.getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_recycler_view);

        rv = (RecyclerView) findViewById(R.id.main_rv);
        exception_label = (TextView) findViewById(R.id.exception_label);

        rv.setLayoutManager(new LinearLayoutManager(this));

        init();

//        parent_list_view = (ListView) findViewById(R.id.parent_list);
//        add_btn = (FloatingActionButton) findViewById(R.id.add_btn);

//

        // add button clicked
//        add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Collection c = Collection.create("new", "new_loc", "new_cataaa");
//                insert(c);
//            }
//        });
    }

    private void insert(final Collection c) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mClient.getTable(Collection.class).insert(c).get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            t.addCollectionElement(c);
//                            parent_list_adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, t.getAllLevel2NodeNames());
//                            parent_list_view.setAdapter(parent_list_adapter);
//                            parent_list_view.setOnItemClickListener(MainActivity.this);
                            toast("Inserted successfully");
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exception_label.setText(e.getMessage());
                            exception_label.setVisibility(View.VISIBLE);
                        }
                    });
                }
                return null;
            }
        };

        runAsyncTask(task);

    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void getAllDataFromDB() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final List<Collection> results = mClient.getTable(Collection.class).execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initTree(results);
                            exception_label.setVisibility(View.INVISIBLE);
                            MainRVAdapter adapter = new MainRVAdapter(getApplicationContext(), t.getAllCategoryNodes());
                            rv.setAdapter(adapter);
//                            t.traverseTree();
//                            parent_list_adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, t.getAllLevel2NodeNames());
//                            parent_list_view.setAdapter(parent_list_adapter);
//                            parent_list_view.setOnItemClickListener(MainActivity.this);
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            exception_label.setText(e.getMessage());
                            exception_label.setVisibility(View.VISIBLE);
                        }
                    });
                }
                return null;
            }
        };

        runAsyncTask(task);

    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    private void init() {
        // sets the default screen message
        exception_label.setText("Establishing DB connection...");
        exception_label.setVisibility(View.VISIBLE);

        // getting instance of Azure Mobile Client & table
        setMobileClientInstance();

        // retrieving data from DB
        // getAllDataFromDB(); // old approach
        getAllDataFromDB();
    }


    private void refresh() {
        resetTree();
        getAllDataFromDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        refresh();
    }

    /**
     * reset tree structure because new items are being added to DB
     */
    private void resetTree() {
        t = new Tree();
        root = t.getRoot();
    }

    public void setMobileClientInstance() {
        if (mClient == null) {
            try {
                AzureServiceAdapter.Initialize(this);
                mClient = AzureServiceAdapter.getInstance().getClient();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * legacy methods
     */

    /*
    public void getAllDataFromDB() {
        try {
            mClient.getTable(Collection.class).execute(new TableQueryCallback<Collection>() {
                @Override
                public void onCompleted(List<Collection> result, int count, Exception exception, ServiceFilterResponse response) {
                    exception_label.setVisibility(View.INVISIBLE);
                    if (exception != null) {
                        exception_label.setText(exception.toString());
                        exception_label.setVisibility(View.VISIBLE);
                    }
                    initTree(result);
                    t.traverseTree();
                    ArrayAdapter<String> parent_list_adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, t.getAllLevel2NodeNames());
                    parent_list_view.setAdapter(parent_list_adapter);
                    parent_list_view.setOnItemClickListener(MainActivity.this);
                }
            });
        } catch (MobileServiceException e) {
            exception_label.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }
    */

    /*
    public void insert(Collection data) {
        mClient.getTable(Collection.class).insert(data, new TableOperationCallback<Collection>() {
            public void onCompleted(Collection entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    // Insert succeeded
                    Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                } else {
                    // Insert failed
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    exception.printStackTrace();
                }
            }
        });
    }
    */
    public void initTree(List<Collection> result) {
        if (result != null) {
            for (Collection c : result) {
                t.addCollectionElement(c);
            }
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        TextView text = (TextView) view;
//        String clickedText = text.getText().toString();
//        Intent i = new Intent(getBaseContext(), ChildrenActivity.class);
//
//        // set the animation when jumping to another intent
//        ActivityOptions animation = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left);
//
//        i.putExtra("node", t.getLevel2NodeByCategory(clickedText));
//
//        startActivity(i, animation.toBundle());
//    }
}

