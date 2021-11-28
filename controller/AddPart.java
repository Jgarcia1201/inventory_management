package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPart implements Initializable {
    public RadioButton addPartInHouseButton;
    public RadioButton addPartOutsourceButton;
    public TextField addPartNameInput;
    public TextField addPartInventoryInput;
    public TextField addPartPriceInput;
    public TextField addPartMaxInput;
    public TextField addPartToggleInput;
    public Label addPartToggleLabel;
    public TextField addPartMinInput;
    public Button addPartSaveButton;
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * First variables are assigned values based on the text entered into the text fields by user. If there are any
     * type mismatches an error message is shown, and the text fields are cleared.
     * Next, a logical check ensures that the min is less than the max.
     *
     * The state of the radio buttons are then checked.
     * If the outsource button is checked then a new Outsourced object is made and then added to the allParts list.
     * Company name requires a string so the text from the respective text field is grabbed and used to initialize
     * companyName in the constructor.
     * If the inhouse button is checked then a new InHouse object is created and then added to the allParts list.
     * machineId requires an int so the text in the text field is converted to an integer first and then used to
     * initialize the machineId variable.
     *
     * @param event - click on the save button
     */
    public void onAddPartSaveButtonAction(ActionEvent event) throws IOException {
        try {
            int id = Inventory.getAllParts().size() + Inventory.partIdGenerator.addAndGet(2) + 1000;
            String name = addPartNameInput.getText();int stock = Integer.parseInt(addPartInventoryInput.getText());
            double price = Double.parseDouble(addPartPriceInput.getText());
            int max = Integer.parseInt(addPartMaxInput.getText());
            int min = Integer.parseInt(addPartMinInput.getText());
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Minimum Greater Than Maximum");
                alert.setContentText("Maximum Must Be Greater Than Minimum");
                alert.showAndWait();
            } else {
                if (addPartOutsourceButton.isSelected()) {
                    String company = addPartToggleInput.getText();
                    Part part = new Outsourced(id, name, price, stock, min, max, company);
                    Inventory.addPart(part);
                }
                else if (addPartInHouseButton.isSelected()) {
                    int machineId = Integer.parseInt(addPartToggleInput.getText());
                    Part part = new InHouse(id, name, price, stock, min, max, machineId);
                    Inventory.addPart(part);
                }
                returnToMainMenu(event);
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR INVALID INPUTS");
            alert.setHeaderText("Please Check Your Input Values");
            alert.setContentText("Price, Minimum, Maximum, and Inventory Must Be Numbers & In House or Outsourced MUST be selected");
            alert.showAndWait();
        }
    }

    /**
     * Toggles the Label for the last text field to read: "Machine ID"
     * @param event - click on the In House Radio button
     */
    public void onAddPartInHouseButtonAction(ActionEvent event) {
        addPartToggleLabel.setText("Machine ID");
    }

    /**
     * Toggles the Label for the last text field to read: "Company Name"
     * @param event - click on the Outsource Radio button
     */
    public void onAddPartOutsourceButtonAction(ActionEvent event) {
        addPartToggleLabel.setText("Company Name");
    }

    /**
     * returns user to the main menu using returnToMainMenu().
     * @param event - the cancel button is clicked.
     */
    public void onAddPartCancelAction(ActionEvent event) throws IOException {
        returnToMainMenu(event);
    }

    /**
     * Created to make code more readable.
     */
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
