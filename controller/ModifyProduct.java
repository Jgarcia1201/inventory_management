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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyProduct implements Initializable {
    public TableView<Part> modProductAllPartsTable;
    public TableColumn<Part, Integer> modProductAllPartsIDCol;
    public TableColumn<Part, String> modProductAllPartsNameCol;
    public TableColumn<Part, Integer> modProductAllPartsInventoryCol;
    public TableColumn<Part, Double> modProductAllPartsPriceCol;
    public TableView<Part> modProductAssocPartsTable;
    public TableColumn<Part, Integer> modProductAssocPartsIDCol;
    public TableColumn<Part, String> modProductAssocPartsNameCol;
    public TableColumn<Part, Integer> modProductAssocPartsInventoryCol;
    public TableColumn<Part, Double> modProductAssocPartsPriceCol;
    public Button modProductAddButton;
    public Button modProductRemoveButton;
    public Button modProductSaveButton;
    public TextField modProductIDInput;
    public TextField modProductNameInput;
    public TextField modProductInventoryInput;
    public TextField modProductPriceInput;
    public TextField modProductMinInput;
    public TextField modProductMaxInput;
    public TextField modProductSearchBar;
    Stage stage;
    Scene scene;

    private Product currentProduct;
    int productIndex = MainForm.getProductToModify();

    /**
     *
     *  Initializes the AllParts Table. The Second table is purposely excluded.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProductAllPartsTable.setItems(Inventory.getAllParts());
        modProductAllPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductAllPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductAllPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProductAllPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     *
     * Made this to make returning to the main menu more readable.
     */
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the second table to display the associated parts by using the values of the provided product object.
     * In addition, the text fields are filled with the values of the parameter's variables.
     * @param prod - the product that is being passed from the MainForm controller.
     */
    public void passSelectedProduct(Product prod) {
        this.currentProduct = prod;
        // Table
        modProductAssocPartsTable.setItems(currentProduct.getAllAssociatedParts());
        modProductAssocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductAssocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductAssocPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProductAssocPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        // Text Fields
        modProductIDInput.setText(String.valueOf(currentProduct.getId()));
        modProductNameInput.setText(currentProduct.getName());
        modProductInventoryInput.setText(String.valueOf(currentProduct.getStock()));
        modProductPriceInput.setText(String.valueOf(currentProduct.getPrice()));
        modProductMaxInput.setText(String.valueOf(currentProduct.getMax()));
        modProductMinInput.setText(String.valueOf(currentProduct.getMin()));
    }

    /**
     * First assigns interval values based on the inputted text in text fields.
     * If there are any type mismatches here from invalid input, the catch block will display an error message.
     * A logic check ensures that the minimum is less than the maximum.
     * If everything checks out, the variables are assigned to their respective values within the
     * private currentProduct object and the updateProduct method is used to change the item contained at the given
     * index. The user is then returned to the main menu upon a successful save.
     * @param event - click on Save button
     */
    public void onModProductSaveButtonAction(ActionEvent event) throws IOException {
        try {
            String name = modProductNameInput.getText();
            int stock = Integer.parseInt(modProductInventoryInput.getText());
            double price = Double.parseDouble(modProductPriceInput.getText());
            int max = Integer.parseInt(modProductMaxInput.getText());
            int min = Integer.parseInt(modProductMinInput.getText());
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Minimum Greater Than Maximum");
                alert.setContentText("Maximum Must Be Greater Than Minimum");
                alert.showAndWait();
            }
            else {
                // Constructing New Product.
                currentProduct.setName(name);
                currentProduct.setPrice(price);
                currentProduct.setStock(stock);
                currentProduct.setMax(max);
                currentProduct.setMin(min);
                Inventory.updateProduct(productIndex, currentProduct);
                returnToMainMenu(event);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR INVALID INPUTS");
            alert.setHeaderText("Please Check Your Input Values");
            alert.setContentText("Price, Minimum, Maximum, and Inventory Must Be Numbers");
            alert.showAndWait();
        }
    }

    /**
     * First any selected items on the table are unselected. The searchterm is grabbed from the search bar and a List
     * containing the results of lookUpPart(searchTerm) is created.
     * If the above List is empty, then an idSearch is performed where the result is highlighted.
     * If the search still returns an empty list then an error message is shown to alert the user that
     * the item was not found. Otherwise, the List containing the results of lookUpPart(searchTerm) is set to the tableView.
     * @param event - text being entered into the search form.
     */
    public void onModProductSearchBarAction(ActionEvent event) {
        modProductAllPartsTable.getSelectionModel().clearSelection();
        String searchTerm = modProductSearchBar.getText();
        ObservableList<Part> searchResults = Inventory.lookUpPart(searchTerm);
        try {
            if (searchResults.size() == 0) {
                int idSearchTerm = Integer.parseInt(searchTerm);
                Part idSearchResult = Inventory.lookUpPart(idSearchTerm);
                if (idSearchResult.equals(null)) {
                    throw new Exception();
                }
                else {
                    modProductAllPartsTable.setItems(Inventory.getAllParts());
                    modProductAllPartsTable.getSelectionModel().select(idSearchResult);
                }
            }
            else {
                modProductAllPartsTable.setItems(searchResults);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Not Found");
            alert.setHeaderText("Part Does Not Exist");
            alert.setContentText("Press Okay To Return To Add Product Page");
            alert.showAndWait();
            modProductSearchBar.setText("");
        }
    }

    /**
     * This method grabs the selected item from the table and adds it to the product's associated parts list
     * using the addAssociatedParts() method.
     * @param event - click on the Add button
     */
    public void onModProductAddButtonAction(ActionEvent event) {
        Part selection = modProductAllPartsTable.getSelectionModel().getSelectedItem();
        currentProduct.addAssociatedPart(selection);
    }

    /**
     * First, the selected item is grabbed, then an alert is shown to the user in order to confirm the removal
     * of the selected part from the Product's associatedParts list.
     * if the user clicks okay, then the selected product is deleted from the associatedParts list.
     * @param event - click on the remove button
     */
    public void onModProductRemoveButtonAction(ActionEvent event) {
        Part selection = modProductAssocPartsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("WARNING!!");
        alert.setHeaderText("Remove Part Confirmation");
        alert.setContentText("Are You Sure You Want To Remove " + selection.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            currentProduct.deleteAssociatedPart(selection);
        }
    }

    /**
     * Returns the user to the main menu using returnToMainMenu().
     * @param event - click on the cancel button
     */
    public void onModifyPartCancelAction(ActionEvent event) throws IOException {
        returnToMainMenu(event);
    }
}
