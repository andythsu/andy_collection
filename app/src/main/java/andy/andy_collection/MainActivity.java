package andy.andy_collection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    MainRVAdapter adapter;

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
//                Collection c = Collection.create("new", "new_loc", "test");
                showInsertDialog();
            }
        });
    }

    private void showInsertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText name_input = new EditText(this);
        final EditText location_input = new EditText(this);
        final EditText category_input = new EditText(this);

        final TextView name_label = new TextView(this);
        final TextView location_label = new TextView(this);
        final TextView category_label = new TextView(this);

        /* initializes the labels */
        name_label.setText("Name: ");
        name_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        location_label.setText("Location: ");
        location_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        category_label.setText("Category: ");
        category_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        /* add label and textfield to the layout and view */
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
                String name = name_input.getText().toString();
                String location = location_input.getText().toString();
                String category = category_input.getText().toString();

                Collection c = Collection.create(name, location, category);
                insert(c);
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }


    private void insert(final Collection data) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mClient.getTable(Collection.class).insert(data).get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Tree.addCollectionElement(data);
                            adapter.notifyDataSetChanged();
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
                            adapter = new MainRVAdapter(getApplicationContext(), Tree.getAllCategoryNodes());
                            rv.setAdapter(adapter);
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Unable to fetch data from DB")
                              .append("\n")
                              .append(e.getMessage())
                              .append("\n\n")
                              .append("Retrying...");
                            exception_label.setText(sb.toString());
                            exception_label.setVisibility(View.VISIBLE);
                            getAllDataFromDB();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
        setMobileClientInstance();
    }

    public void setMobileClientInstance() {
        if (mClient == null) {
            try {
                AzureServiceAdapter.Initialize(this);
                mClient = AzureServiceAdapter.getInstance().getClient();
            } catch (MalformedURLException e) {
                StringBuilder sb = new StringBuilder();
                sb.append(e.getMessage())
                        .append("\n\n")
                        .append("Retrying...")
                        .toString();
                exception_label.setText(sb);
                setMobileClientInstance();
            }
        }
    }


    public void initTree(List<Collection> result) {
        if (result != null) {
            for (Collection c : result) {
                Tree.addCollectionElement(c);
            }
        }
    }

}

