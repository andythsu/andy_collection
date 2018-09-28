package andy.andy_collection.database;

import android.os.AsyncTask;
import andy.andy_collection.structure.Collection;
import andy.andy_collection.util.Util;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.util.List;

public class DB extends DBCallBack {

    final MobileServiceClient mClient;

    public DB(MobileServiceClient mClient) {
        this.mClient = mClient;
    }

    public void getAllDataFromDB(final DBCallBack callback) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final List<Collection> results = mClient.getTable(Collection.class).execute().get();
                    callback.getAllDataFromDB(results);
                } catch (final Exception e) {
                    callback.getException(e);
                }
                return null;
            }
        };

        Util.runAsyncTask(task);

    }

    public void insert(final Collection data, final DBCallBack callback) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mClient.getTable(Collection.class).insert(data).get();
                    callback.insertSuccess();
                } catch (final Exception e) {
                    callback.getException(e);
                }
                return null;
            }
        };

        Util.runAsyncTask(task);

    }

    public void update(final Collection data, final DBCallBack callback) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mClient.getTable(Collection.class).update(data).get();
                    callback.updateSuccess();
                } catch (final Exception e) {

                }
                return null;
            }
        };

        Util.runAsyncTask(task);

    }

    public void delete(final Collection data, final DBCallBack callback){
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mClient.getTable(Collection.class).delete(data).get();
                    callback.deleteSuccess();
                } catch (final Exception e) {
                   callback.getException(e);
                }
                return null;
            }
        };

        Util.runAsyncTask(task);

    }
}
