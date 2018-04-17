/**
 * @Author: Andy Su
 * @Date: Apr 10, 2018
 * @Description:
 */
package andy.andy_collection;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Node implements Parcelable{
    private String name;
    private ArrayList<Node> children = new ArrayList<>();
    private Node parent;
    private Collection data;

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, Collection d) {
        this.data = d;
        this.name = name;
    }

    protected Node(Parcel in) {
        name = in.readString();
        children = in.createTypedArrayList(Node.CREATOR);
        parent = in.readParcelable(Node.class.getClassLoader());
        data = in.readParcelable(Collection.class.getClassLoader());
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };

    public void addChildren(Node n) {
        children.add(n);
    }

    // setters
    public void setParent(Node n) {
        parent = n;
    }
    public void setData(Collection d) {
        data = d;
    }

    public void setName(String name) {
        this.name = name;
    }

    // getters
    public Node getParent() {
        return parent;
    }
    public String getName() {
        return name;
    }
    public Collection getData() {
        return data;
    }

    public ArrayList<Node> getChildren(){
        return children;
    }

    @Override
    public String toString() {
        if(children != null) {
            return name;
        }else {
            return data.toString();
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(children);
        dest.writeParcelable(parent, flags);
        dest.writeParcelable((Parcelable) data, flags);
    }
}
