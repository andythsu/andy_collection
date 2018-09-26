package andy.andy_collection;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import andy.andy_collection.adapters.AzureServiceAdapter;
import andy.andy_collection.adapters.MainRVAdapter;
import andy.andy_collection.structure.Collection;
import andy.andy_collection.structure.Node;
import andy.andy_collection.structure.Tree;
import andy.andy_collection.util.Util;
import com.microsoft.windowsazure.mobileservices.*;

import java.net.MalformedURLException;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static MobileServiceClient mClient;
    TextView exception_label;
    FloatingActionButton add_btn;

    RecyclerView rv;

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

        add_btn = (FloatingActionButton) findViewById(R.id.add_btn);

        // add button clicked
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collection c = Collection.create("new", "new_loc", "test");
                insert(c);
            }
        });
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
                            Util.toast(MainActivity.this, "Inserted successfully");
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

        Util.runAsyncTask(task);

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

        Util.runAsyncTask(task);

    }

    private void init() {
        // sets the default screen message
        exception_label.setText("Establishing DB connection...");
        exception_label.setVisibility(View.VISIBLE);

        // getting instance of Azure Mobile Client & table
        setMobileClientInstance();

        // retrieving data from DB
        getAllDataFromDB();
    }


    private void refresh() {
        resetTree();
        getAllDataFromDB();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Tree t = new Tree();
        t.traverseTree(t.getCategoryNodeByName("new_cataaa"));
//        refresh();
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
                exception_label.setText(e.getMessage());
            }
        }
    }


    public void initTree(List<Collection> result) {
        if (result != null) {
            for (Collection c : result) {
                t.addCollectionElement(c);
            }
        }
    }

}

