package andy.andy_collection;

import android.app.ActivityOptions;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceJsonTable;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableJsonQueryCallback;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.microsoft.windowsazure.mobileservices.table.query.ExecutableQuery;
import com.microsoft.windowsazure.mobileservices.table.query.Query;

import org.json.JSONArray;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static MobileServiceClient mClient;
    ListView parent_list_view;
    TextView exception_label;
    FloatingActionButton add_btn;

    Tree t = new Tree();
    Node root = t.getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent_list_view = (ListView) findViewById(R.id.parent_list);
        exception_label = (TextView) findViewById(R.id.exception_label);
        add_btn = (FloatingActionButton) findViewById(R.id.add_btn);

        init();

        // add button clicked
        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("button clicked");
                Collection newData = Collection.create("testing_09_19_1", "testing_location", "testing_category");
                insert(newData);
            }
        });
    }

    private void init() {
        // sets the default screen message
        exception_label.setText("Establishing DB connection...");
        exception_label.setVisibility(View.VISIBLE);

        // getting instance of Azure Mobile Client
        getMobileClientInstance();
        ////////////////////////////////////////

        // retrieving data from DB
        getDataFromDB();
        /////////////////////////////////////////
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetTree();
        getDataFromDB();
    }

    /**
     * reset tree structure because new items are being added to DB
     */
    private void resetTree() {
        t = new Tree();
        root = t.getRoot();
    }

    public void getMobileClientInstance() {
        if(mClient == null){
            try {
                AzureServiceAdapter.Initialize(this);
                mClient = AzureServiceAdapter.getInstance().getClient();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getDataFromDB() {
        try {
            mClient.getTable(Collection.class).execute(new TableQueryCallback<Collection>() {
                @Override
                public void onCompleted(List<Collection> result, int count, Exception exception, ServiceFilterResponse response) {
                    exception_label.setVisibility(View.INVISIBLE);
                    if(exception != null){
                        exception_label.setText(exception.toString());
                        exception_label.setVisibility(View.VISIBLE);
                    }
                    initTree(result);
                    t.traverseTree();
                    ArrayAdapter<String> parent_list_adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1, t.getAllLevel2NodeNames());
                    parent_list_view.setAdapter(parent_list_adapter);
                    parent_list_view.setOnItemClickListener(MainActivity.this);
                }
            });
        } catch (MobileServiceException e) {
            exception_label.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }


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

    public void initTree(List<Collection> result){
        if(result != null){
            for(Collection c : result){
                // try to get level 2 parent node in tree
                Node parentNode = t.getLevel2NodeByCategory(c.getCategory());
                // if parent is not found
                if(parentNode == null) {
                    // create missing level 2 parent
                    parentNode = new Node(c.getCategory());
                    // add to root node (level 1)
                    root.addChildren(parentNode);
                }
                // create level 3 node
                Node newNode = new Node(c.getCategory(), c);
                // add to level 2
                parentNode.addChildren(newNode);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView text = (TextView) view;
        String clickedText = text.getText().toString();
        Intent i = new Intent(getBaseContext(), ChildrenActivity.class);

        // set the animation when jumping to another intent
        ActivityOptions animation = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left);

        i.putExtra("node", t.getLevel2NodeByCategory(clickedText));

        startActivity(i, animation.toBundle());
    }
}

