package controller;

import javafx.collections.FXCollections;
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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProduct implements Initializable {
    public TableView<Part> addProductAllPartsTable;
    public TableColumn<Part, Integer> addProductAllPartsIDCol;
    public TableColumn<Part, String> addProductAllPartsNameCol;
    public TableColumn<Part, Integer> addProductAllPartsInventoryCol;
    public TableColumn<Part, Double> addProductAllPartsPriceCol;
    public TableView<Part> addProductAssocPartsTable;
    public TableColumn<Part, Integer> addProductAssocPartsIDCol;
    public TableColumn<Part, String> addProductAssocPartsNameCol;
    public TableColumn<Part, Integer> addProductAssocPartsInventoryCol;
    public TableColumn<Part, Double> addProductAssocPartsPriceCol;
    public Button addProductAddButton;
    public Button addProductRemoveButton;
    public Button addProductSaveButton;
    public TextField addProductNameInput;
    public TextField addProductInventoryInput;
    public TextField addProductPriceInput;
    public TextField addProductMinInput;
    public TextField addProductMaxInput;
    public TextField addProductSearchBar;
    Stage stage;
    Scene scene;
    ObservableList<Part> currentAssocParts = FXCollections.observableArrayList();


    /**
     * Initializes tables to allParts and associatedParts respectively.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProductAllPartsTable.setItems(Inventory.getAllParts());
        addProductAllPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAllPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAllPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAllPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductAssocPartsTable.setItems(currentAssocParts);
        addProductAssocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAssocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAssocPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAssocPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Used to make later code more readable.
     */
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * First assigns variables to the text provided by the user in the text fields. If any of these variables have a
     * type mismatch due to invalid input an error is shown warning the user and all fields are cleared.
     * A logical check is used to ensure that minimum is less than maximum.
     * If everything checks out, an id is generated using the static atomic integer found in Inventory.
     * A new product is created and then added to the allProducts list.
     *
     * <p>
     * A temporary list is used in this class called currentAssocParts. The list is iterated over and every part is added
     * to the new products associated items list and the user is returned to the main menu.
     * </p>
     * @param event - click on the save button
     *
     */
    public void onAddProductSaveButtonAction(ActionEvent event) throws IOException {
        try {
            String name = addProductNameInput.getText();
            int stock = Integer.parseInt(addProductInventoryInput.getText());
            double price = Double.parseDouble(addProductPriceInput.getText());
            int max = Integer.parseInt(addProductMaxInput.getText());
            int min = Integer.parseInt(addProductMinInput.getText());
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Minimum Greater Than Maximum");
                alert.setContentText("Maximum Must Be Greater Than Minimum");
                alert.showAndWait();
            }
            else {
                // Constructing New Product.
                int id = Inventory.getAllProducts().size() + Inventory.productIdGenerator.addAndGet(3) + 1000;
                Product newProduct = new Product(id, name, price, stock, min, max);
                Inventory.addProduct(newProduct);

                // Adding Associated Parts.
                for (Part part : currentAssocParts) {
                    newProduct.addAssociatedPart(part);
                }
                returnToMainMenu(event);
            }
        }
        catch (Exception e) {
            addProductNameInput.setText("");
            addProductInventoryInput.setText("");
            addProductPriceInput.setText("");
            addProductMaxInput.setText("");
            addProductMinInput.setText("");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR INVALID INPUTS");
            alert.setHeaderText("Please Check Your Input Values");
            alert.setContentText("Price, Minimum, Maximum, and Inventory Must Be Numbers");
            alert.showAndWait();
        }
    }

    /**
     * Similar to all Searches found in this program:
     * First any selected items on the table are unselected. The searchterm is grabbed from the search bar and a List
     * containing the results of lookUpPart(searchTerm) is created.
     * If the above List is empty, then an idSearch is performed where the result is highlighted.
     * If the search still returns an empty list then an error message is shown to alert the user that
     * the item was not found. Otherwise, the List containing the results of lookUpPart(searchTerm) is set to the tableView.
     * @param event - a search term is entered into the search bar.
     */
    public void onAddProductSearchBarAction(ActionEvent event) {
        addProductAllPartsTable.getSelectionModel().clearSelection();
        String searchTerm = addProductSearchBar.getText();
        ObservableList<Part> searchResults = Inventory.lookUpPart(searchTerm);
        try {
            if (searchResults.size() == 0) {
                int idSearchTerm = Integer.parseInt(searchTerm);
                Part idSearchResult = Inventory.lookUpPart(idSearchTerm);
                if (idSearchResult.equals(null)) {
                    throw new Exception();
                }
                else {
                    addProductAllPartsTable.setItems(Inventory.getAllParts());
                    addProductAllPartsTable.getSelectionModel().select(idSearchResult);
                }
            }
            else {
                addProductAllPartsTable.setItems(searchResults);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Not Found");
            alert.setHeaderText("Part Does Not Exist");
            alert.setContentText("Press Okay To Return To Add Product Page");
            alert.showAndWait();
            addProductSearchBar.setText("");
        }
    }

    /**
     * Grabs the selected item and uses add() in order to append it to the given Products associated parts list.
     * @param event - the add button is clicked
     */
    public void onAddProductAddButtonAction(ActionEvent event) {
        Part selection = addProductAllPartsTable.getSelectionModel().getSelectedItem();
        currentAssocParts.add(selection);
    }

    /**
     * Grabs the selected item and displays a warning to the user.
     * If the user clicks okay, the remove() method is used to remove the selected item.
     * @param event - clicking the remove button
     */
    public void onAddProductRemoveButtonAction(ActionEvent event) {
        Part selection = addProductAssocPartsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING!!");
        alert.setHeaderText("Remove Part Confirmation");
        alert.setContentText("Are You Sure You Want To Remove " + selection.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            currentAssocParts.remove(selection);
        }
    }

    /**
     * Returns user to the main menu.
     * @param event - cancel button is clicked
     */
    public void onAddProductCancelAction(ActionEvent event) throws IOException {
        returnToMainMenu(event);
    }
}
