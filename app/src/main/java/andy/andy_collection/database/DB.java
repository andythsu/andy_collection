package andy.andy_collection;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import andy.andy_collection.adapters.MainRVAdapter;
import andy.andy_collection.structure.Collection;
import andy.andy_collection.structure.Tree;
import andy.andy_collection.util.Util;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.util.List;

public class DB extends AppCompatActivity {

    public void getAllDataFromDB(final MobileServiceClient mClient, ){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final List<Collection> results = mClient.getTable(Collection.class).execute().get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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

}
