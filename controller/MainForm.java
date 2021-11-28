package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import model.Inventory;
import model.Part;
import model.Product;

public class MainForm implements Initializable {
    public TableView<Part> mainPartsTable;
    public TableColumn<Part, Integer> mainPartIDCol;
    public TableColumn<Part, String> mainPartNameCol;
    public TableColumn<Part, Integer> mainPartInventoryCol;
    public TableColumn<Part, Double> mainPartPriceCol;
    public Button mainPartsDelete;
    public Button mainPartsAdd;
    public Button mainPartsMod;
    public TextField mainPartsSearchBar;
    public TableView<Product> mainProductsTable;
    public TextField mainProductSearchBar;
    public TableColumn<Product, Integer> mainProductIDCol;
    public TableColumn<Product, String> mainProductNameCol;
    public TableColumn<Product, Integer> mainProductInventoryCol;
    public TableColumn<Product, Integer> mainProductPriceCol;
    public Button mainProductsDelete;
    private static int modifyPartIndex;
    private static int modifyProductIndex;
    private Stage stage;
    private Scene scene;

    /**
     * Sets up tableViews on initialization of MainForm.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initializing Parts Table:
        mainPartsTable.setItems(Inventory.getAllParts());
        mainPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Setting up Products Table:
        mainProductsTable.setItems(Inventory.getAllProducts());
        mainProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * @return modifyPartIndex - the index of the desired part.
     */
    public static int getPartToModify() {
        return modifyPartIndex;
    }

    /**
     *
     * @return modifyProductIndex - the index of the desired product.
     */
    public static int getProductToModify() {
        return modifyProductIndex;
    }

    /**
     * Loads Add Part view.
     * @param event - the click on Add button on the Parts pane.
     */
    public void onMainPartsAddAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * Loads ModifyPart controller. Once finished, passSelectedPart is called in order
     * to pass the selected part through to the ModifyPart controller.
     *
     * <p>
     * The ModifyPart view is then shown on the screen.
     * </p>
     * @param event - the click on Modify button on the Parts pane.
     */
    public void onMainPartsModAction(ActionEvent event) throws IOException {
        Part selection = mainPartsTable.getSelectionModel().getSelectedItem();
        try {
            modifyPartIndex = Inventory.getAllParts().indexOf(selection);
            // Loading
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
            Parent root = loader.load();
            // Passing Data
            ModifyPart mp = loader.getController();
            mp.passSelectedPart(selection);
            // Stage
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("WARNING!!");
            alert.setHeaderText("Select A Part To Modify");
            alert.setContentText("Please Select a Product and Try Again");
            alert.show();
        }
    }

    /**
     * Loads AddProduct view.
     * @param event - the click on the add button on the products pane.
     */
    public void onMainProductsAddAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads ModifyProduct controller. Once finished, passSelectedProduct is called in order
     * to pass the selected part through to the ModifyProduct controller.
     * The ModifyProduct view is then shown on the screen.
     * @param event - click on modify button on the products pane.
     */
    public void onMainProductsModAction(ActionEvent event) throws IOException {
        Product selection = mainProductsTable.getSelectionModel().getSelectedItem();
        try {
            modifyProductIndex = Inventory.getAllProducts().indexOf(selection);
            // Loading Controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
            Parent root = loader.load();
            // Passing Data
            ModifyProduct mp = loader.getController();
            mp.passSelectedProduct(selection);
            // Stage
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("WARNING!!");
            alert.setHeaderText("Select A Product");
            alert.setContentText("Please Select a Product and Try Again");
            alert.show();
        }
    }

    /**
     * Exits the program.
     * @param event - click on the exit button.
     */
    public void onExitMainAction(ActionEvent event) {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * This method first clears the selection, gets the text from the search bar, and then creates a list called
     * partSearchResults using the returned values from lookUpPart(String).
     * If the list is empty after this event, lookUpPart is called again this time using an integer parameter.
     * If the part is found, the table is set to display allParts, and the selected item is highlighted.
     * If the part is not found, then an error is thrown to let the user know that the part does not exist.
     * If none of these conditions happen, then lookUpPart(String) did return a non-empty list and the table is set
     * to display those returned values.
     * @param event - entering a search term into the search bar.
     */
    public void onPartSearchAction(ActionEvent event) {
        mainPartsTable.getSelectionModel().clearSelection();
        String searchTerm = mainPartsSearchBar.getText();
        ObservableList<Part> partSearchResults = Inventory.lookUpPart(searchTerm);
        try {
            if (partSearchResults.size() == 0) {
                int idSearch = Integer.parseInt(searchTerm);
                Part idSearchResults = Inventory.lookUpPart(idSearch);
                if (idSearchResults.equals(null)) {
                    throw new Exception();
                }
                else {
                    mainPartsTable.setItems(Inventory.getAllParts());
                    mainPartsTable.getSelectionModel().select(Inventory.lookUpPart(idSearch));
                }
            }
            else {
                mainPartsTable.setItems(partSearchResults);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Not Found");
            alert.setHeaderText("Part Does Not Exist");
            alert.setContentText("Press Okay To Return To Main Menu");
            alert.showAndWait();
            mainPartsSearchBar.setText("");
        }
    }

    /**
     * This method first clears the selection, gets the text from the search bar, and then creates a list called
     * productSearchResults using the returned values from lookUpProduct(String).
     * If the list is empty after this event, lookUpProduct is called again this time using an integer parameter.
     * If the part is found, the table is set to display allProduct, and the selected item is highlighted.
     * If the part is not found, then an error is thrown to let the user know that the part does not exist.
     * If none of these conditions happen, then lookUpProduct(String) did return a non-empty list and the table is set
     * to display those returned values.
     *
     * @param event - entering a search term into the search bar.
     */
    public void onProductSearchAction(ActionEvent event) {
        mainProductsTable.getSelectionModel().clearSelection();
        String searchTerm = mainProductSearchBar.getText();
        ObservableList<Product> productSearchResults = Inventory.lookUpProduct(searchTerm);
        try {
            if (productSearchResults.size() == 0) {
                int idSearch = Integer.parseInt(searchTerm);
                Product idSearchResults = Inventory.lookUpProduct(idSearch);
                if (idSearchResults.equals(null)) {
                    throw new Exception();
                }
                else {
                    mainProductsTable.setItems(Inventory.getAllProducts());
                    mainProductsTable.getSelectionModel().select(Inventory.lookUpProduct(idSearch));
                }
            }
            else {
                mainProductsTable.setItems(productSearchResults);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Product Not Found");
            alert.setHeaderText("Product Does Not Exist");
            alert.setContentText("Press Okay To Return To Main Menu");
            alert.showAndWait();
            mainProductSearchBar.setText("");
        }
    }

    /**
     * First the highlighted part on the table is selected an assigned to a variable selection.
     * Then the allParts List is called.
     * If the selection is null, the rest of the function is ignored as there is nothing to delete.
     *
     * If the selection does exist, then an alert is displayed confirming the user's desire to delete the selected item.
     * If the user confirms this action then the product is deleted from allParts, otherwise the program will return
     * to the main menu.
     * @param event - the delete button being click on the parts pane.
     */
    public void onMainPartsDeleteAction(ActionEvent event) {
        Part selection = (Part) mainPartsTable.getSelectionModel().getSelectedItem();
        ObservableList<Part> allParts = Inventory.getAllParts();
        if (selection == null) {
            return;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("WARNING!");
            alert.setContentText("Are You Sure You Want To Delete " + selection.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selection);
            }
        }
        mainPartsTable.setItems(allParts);
    }

    /**
     * First the highlighted product on the table is selected an assigned to a variable selection.
     * Then the allProducts List is called.
     * If the selection is null, the rest of the function is ignored as there is nothing to delete.
     *
     * If the selection does exist, then an alert is displayed confirming the user's desire to delete the selected item.
     * If the user confirms this action then the product is deleted from allProducts, otherwise the program will return
     * to the main menu.
     * @param event - the delete button being click on the products pane.
     */
    public void onMainProductsDeleteAction(ActionEvent event) {
        Product selection = (Product) mainProductsTable.getSelectionModel().getSelectedItem();
        ObservableList<Product> allProds = Inventory.getAllProducts();
        if (selection == null) {
            return;
        }
        else {
            if (selection.getAllAssociatedParts().size() == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("WARNING!");
                alert.setContentText("Are You Sure You Want To Delete " + selection.getName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deleteProduct(selection);
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("CANNOT BE DELETED");
                alert.setHeaderText("Cannot Delete Item");
                alert.setContentText("Products With Associated Parts Cannot Be Deleted");
                alert.show();
            }
        }
        mainProductsTable.setItems(allProds);
    }
}
