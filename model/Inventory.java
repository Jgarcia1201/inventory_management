package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.concurrent.atomic.AtomicInteger;


public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    public static AtomicInteger productIdGenerator = new AtomicInteger();
    public static AtomicInteger partIdGenerator = new AtomicInteger();

    /**
     * @param newPart is added to the observable list allParts defined above.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @param newProduct is added to observable list allProducts defined above.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     *
     * @param partId is used to compare against the id values of all other parts in allParts.
     * @return Part object where id is equal to partId parameter.
     */
    public static Part lookUpPart(int partId) {
        for (Part part: allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     *
     * @param productId is used to compare against the id values of all other products in allProducts.
     * @return Product object where id is equal to productId parameter.
     */
    public static Product lookUpProduct(int productId) {
        for (Product product: allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * @param partName is used to compare against the name values of all other parts in allParts.
     * @return Part object where name is equal to partName parameter.
     */
    public static ObservableList<Part> lookUpPart(String partName) {
        ObservableList<Part> partSearchResults = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                partSearchResults.add(part);
            }
        }
        return partSearchResults;
    }

    /**
     * @param productName is used to compare against the name values of all other products in allProducts.
     * @return Product object where name is equal to productName parameter.
     */
    public static ObservableList<Product> lookUpProduct(String productName) {
        ObservableList<Product> productSearchResults = FXCollections.observableArrayList();
        for (Product product: allProducts) {
            if (product.getName().contains(productName)) {
                productSearchResults.add(product);
            }
        }
        return productSearchResults;
    }

    /**
     *
     * @param index is used to identify the desired position of Part object in allParts.
     * @param selectedPart specifies what Part object to replace the current Part object with.
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     *
     * @param index is used to identify the desired position of Product object in allProduct.
     * @param selectedProduct specifies what Product object to replace the current Product object with.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     *
     * @param selectedPart - provides access to a specified part object and all of its methods to the function.
     * @return a boolean value. True if a part was successfully deleted. False if an error occurs.
     */
    public static boolean deletePart(Part selectedPart) {
        for (Part part : allParts) {
            if (part.getId() == selectedPart.getId()) {
                allParts.remove(selectedPart);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param selectedProduct provides access to a specified product object and all of its methods to the function.
     * @return a boolean value. True if a product was successfully deleted. False if an error occurs.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        for (Product product : allProducts) {
            if (product.getId() == selectedProduct.getId()) {
                allProducts.remove(selectedProduct);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return observable list allParts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     *
     * @return observable list allProducts.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Creates Sample Data and Adds it to allParts & allProducts respectively.
     */
    public static void addTestData() {
        InHouse core = new InHouse(1, "Core Reactor", 49.99, 10, 5, 25, 100);
        InHouse rearView = new InHouse(2, "Rear View Mirror", 89.99, 9, 4, 32, 105);
        Outsourced screen = new Outsourced(3, "Screen", 39.99, 10, 2, 30, "Walmart");
        Outsourced sound = new Outsourced(4, "Sound System", 40.99, 10, 10, 50, "JBL");
        allParts.add(core);
        allParts.add(rearView);
        allParts.add(screen);
        allParts.add(sound);

        Product flyingSaucer = new Product(1, "Flying Saucer", 10000.00, 10, 1, 10);
        Product rocketShip = new Product(2, "RocketShip", 15000.00, 2, 1, 10);
        Product hoverBoard = new Product(3, "Hover Board", 50000, 4, 2, 10);
        allProducts.add(flyingSaucer);
        allProducts.add(rocketShip);
        allProducts.add(hoverBoard);
    }

}
