/**
 * @Author: Andy Su
 * @Date: Apr 10, 2018
 * @Description:
 */
package andy.andy_collection.structure;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {
    private String name;
    private ArrayList<Node> children = new ArrayList<>();
    private Node parent;
    private Collection data;

    public Node(String name) {
        this.name = name;
    }

    public Node(String name, Collection d) {
        this.name = name;
        this.data = d;
    }

    public void remove(Collection data) {
        if (children != null) {
            for (Node n : children) {
                if (n.getData().equals(data)) {
                    children.remove(n);
                }
            }
            Tree.cleanNode(this);
        }
    }

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

    public ArrayList<Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        if (children != null) {
            return name;
        } else {
            return data.toString();
        }
    }

}
