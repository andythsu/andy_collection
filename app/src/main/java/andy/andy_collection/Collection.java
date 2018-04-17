/**
 * @Author: Andy Su
 * @Date: Apr 10, 2018
 * @Description:
 */
package andy.andy_collection;


import android.os.Parcel;
import android.os.Parcelable;

public class Collection implements Parcelable{
//    @com.google.gson.annotations.SerializedName("ID")
    private String ID; // PK in the DB
//    @com.google.gson.annotations.SerializedName("Name")
    private String Name;
//    @com.google.gson.annotations.SerializedName("Location")
    private String Location;
//    @com.google.gson.annotations.SerializedName("Category")
    private String Category;


//    public static Collection create(String name, String location, String category){
//        return new Collection(name, location, category);
//    }
//
//    private Collection(String name, String location, String category){
//        this.id = Long.toString(new java.util.Date().getTime()); // use time stamp to get unique ID
//        this.Name = name;
//        this.Location = location;
//        this.Category = category;
//    }

    public Collection(String id, String name, String location, String category){
        this.ID = id;
        this.Name = name;
        this.Location = location;
        this.Category = category;
    }

    protected Collection(Parcel in) {
        ID = in.readString();
        Name = in.readString();
        Location = in.readString();
        Category = in.readString();
    }

    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };

    public void setName(String name) {
        this.Name = name;
    }
    public void setLocation(String location) {
        this.Location = location;
    }
    public void setIndex(String id) {
        this.ID = id;
    }
    public void setCategory(String category) {
        this.Category = category;
    }
    public String getName() {
        return Name;
    }
    public String getLocation() {
        return Location;
    }
    public String getID() {
        return ID;
    }
    public String getCategory() {
        return Category;
    }
    @Override
    public String toString() {
        return "ID: " + ID + "\n" + "Name: " + Name + "\n" + "Location: " + Location + "\n" +"Category: " + Category + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Name);
        dest.writeString(Location);
        dest.writeString(Category);
    }
}
