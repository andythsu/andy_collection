/**
 * @Author: Andy Su
 * @Date: Apr 10, 2018
 * @Description:
 */
package andy.andy_collection.structure;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * structure:
 * level 1: root
 * level 2: categories (folders)
 * level 3: locations
 */
public class Tree {
    public static Node root = new Node("root");

    public static void traverseTree() {
        traverseTree(root);
    }
    public static Node getRoot() {
        return root;
    }

    /**
     * get all nodes that are category
     * @return
     */
    public static List<Node> getAllCategoryNodes(){
        if(root != null && root.getChildren() != null) {
            return root.getChildren();
        }
        return null;
    }


    /**
     * adds collection element into tree
     * @param c
     */
    public static void addCollectionElement(Collection c){
        // try to get level 2 parent node in tree
        Node parentNode = getCategoryNodeByName(c.getCategory());
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
     * get level 2 parent node by category name
     * @param name
     * @return
     */
    public static Node getCategoryNodeByName(String name) {
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
     * cleans level 2 parent node if there are no children left
     */
    public static void cleanNode(Node n){
        if(n.getChildren().size() == 0){
            root.getChildren().remove(n);
        }
    }


    /**
     * @param root
     */
    public static void traverseTree(Node root) {
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


}
