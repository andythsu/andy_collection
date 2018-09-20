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
import java.util.List;

/**
 * structure:
 * level 1: root
 * level 2: categories (folders)
 * level 3: locations
 */
public class Tree implements Parcelable{
    private Node root;
    public Tree() {
        root = new Node("root");
    }

    protected Tree(Parcel in) {
        root = in.readParcelable(Node.class.getClassLoader());
    }

    public static final Creator<Tree> CREATOR = new Creator<Tree>() {
        @Override
        public Tree createFromParcel(Parcel in) {
            return new Tree(in);
        }

        @Override
        public Tree[] newArray(int size) {
            return new Tree[size];
        }
    };

    public void traverseTree() {
        traverseTree(root);
    }
    public Node getRoot() {
        return root;
    }
    public Node getNodeByIndex(String index) {
        return getNodeByIndex(root, index);
    }

    /**
     * get all nodes that are category
     * @return
     */
    public List<Node> getAllCategoryNodes(){
        if(root != null && root.getChildren() != null) {
            return root.getChildren();
        }
        return null;
    }


    /**
     * adds collection element into tree
     * @param c
     */
    public void addCollectionElement(Collection c){
        // try to get level 2 parent node in tree
        Node parentNode = getLevel2NodeByCategory(c.getCategory());
        // if parent is not found
        if (parentNode == null) {
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


    /**
     * get level 2 parent node by category
     * @param name
     * @return
     */
    public Node getLevel2NodeByCategory(String name) {
        if(root != null && root.getChildren() != null) {
            for(int i=0; i<root.getChildren().size(); i++) {
                Node n = root.getChildren().get(i);
                // only check level 2s
                if(n.getName().equals(name)) {
                    return n;
                }
            }
        }
        return null;
    }
    /**
     * @param root
     * @param id
     */
    private Node getNodeByIndex(Node root, String id) {
        if(root != null) {
            if(id.equals(root.getData().getID())) {
                return root;
            }else {
                if(root.getChildren() != null) {
                    for(int i=0; i<root.getChildren().size(); i++) {
                        Node child = root.getChildren().get(i);
                        if(child.getData().getID() == id) return child;
                        traverseTree(child);
                    }
                }
            }
        }
        return null;
    }


    /**
     * @param root
     */
    private void traverseTree(Node root) {
        if(root != null) {
            if(root.getData() != null) {
                System.out.println(root.getData().toString());
            }else {
                // if it's a category, output it's name only
                System.out.println(root.toString());
            }
            if(root.getChildren() != null) {
                for(int i=0; i<root.getChildren().size(); i++) {
                    Node child = root.getChildren().get(i);
                    traverseTree(child);
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(root, flags);
    }

}
