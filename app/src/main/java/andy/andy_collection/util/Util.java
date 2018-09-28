package andy.andy_collection.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;
import andy.andy_collection.adapters.AzureServiceAdapter;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

public class Util {

    public static AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public static void toast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static MobileServiceClient setMobileServiceClient(MobileServiceClient mClient, Context context){
        if(mClient == null){
            try {
                return AzureServiceAdapter.getClient(context);
            } catch (MalformedURLException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to initialize Mobile client")
                  .append("\n")
                  .append(e.getMessage());
            }
        }
        return mClient;
    }

}
