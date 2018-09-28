package andy.andy_collection.database;


import andy.andy_collection.structure.Collection;

import java.util.List;

/**
 * Callback class that deals with any callback from DB
 */
public abstract class DBCallBack {
    public void getAllDataFromDB(final List<Collection> results) {}
    public void getException(final Exception e) {}
    public void insertSuccess() {}
    public void updateSuccess() {}
    public void deleteSuccess() {}
}