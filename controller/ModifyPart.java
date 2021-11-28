package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.*;
import model.Inventory;
import model.Part;
import model.InHouse;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPart implements Initializable {
    public TextField modPartToggleInput;
    public Label modPartToggleText;
    public TextField modPartIdInput;
    public TextField modPartNameInput;
    public TextField modPartInventoryInput;
    public TextField modPartPriceInput;
    public TextField modPartMaxInput;
    public TextField modPartMinInput;
    public RadioButton modPartOutsourceButton;
    public RadioButton modPartInHouseButton;
    int partIndex = MainForm.getPartToModify();
    Stage stage;
    Scene scene;

    private Part currentPart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Fills the text fields with the respected values from the Part object passed from the MainForm controller.
     * If the provided part is an InHouse object, the InHouse radio button will be checked and the last label will read
     * "Machine ID".
     *
     * If the provided part is an Outsource object, the outsourced radio button will be checked and the last label will
     * read "Company Name".
     * @param part - used to set currentPart value.
     */
    public void passSelectedPart(Part part) {
        this.currentPart = part;
        // Filling Text Fields
        modPartIdInput.setText(String.valueOf(currentPart.getId()));
        modPartNameInput.setText(String.valueOf(currentPart.getName()));
        modPartInventoryInput.setText(String.valueOf(currentPart.getStock()));
        modPartPriceInput.setText(String.valueOf(currentPart.getPrice()));
        modPartMaxInput.setText(String.valueOf(currentPart.getMax()));
        modPartMinInput.setText(String.valueOf(currentPart.getMin()));
        if (currentPart instanceof InHouse) {
            modPartToggleInput.setText(String.valueOf(((InHouse) currentPart).getMachineId()));
            modPartInHouseButton.setSelected(true);
            modPartToggleText.setText("Machine ID");
        }
        if (currentPart instanceof Outsourced) {
            modPartToggleInput.setText(String.valueOf(((Outsourced) currentPart).getCompanyName()));
            modPartOutsourceButton.setSelected(true);
            modPartToggleText.setText("Company Name");
        }
    }

    /**
     * Toggles the Label for the last text field to read: "Machine ID"
     * @param event - click on the InHouse Radio button
     */
    public void onModPartInHouseButtonAction(ActionEvent event) {
        modPartToggleText.setText("Machine ID");
    }

    /**
     * Toggles the Label for the last text field to read: "Company Name"
     * @param event - click on the Outsource Radio button
     */
    public void onModPartOutsourceButtonAction(ActionEvent event) {
        modPartToggleText.setText("Company Name");
    }

    /**
     * First variables are assigned values based on the text entered into the text fields by user. If there are any
     * type mismatches an error message is shown, and the text fields are cleared.
     * Next, a logical check ensures that the min is less than the max.
     *
     * The state of the radio buttons are then checked.
     * If the outsourced button is selected and the current part is an InHouse object, then a new Outsource object will have to be
     * created and added to the Parts list.
     *
     * If the InHouse button is selected and the current part is an Outsource object, then a new InHouse object will have
     * to be created and added to the Parts list.
     *
     * If the InHouse/Outsource status is not being changed, then the variables are assigned to the values provided by the
     * user in the text fields. An if statement then checks whether the object is an Inhouse or Outsource object:
     *  If an Inhouse, then the textfield is used to assign a value to MachineId.
     *  If an Outsource, then the textfield is used to assign a value to companyName.
     * @param event - click on the save button
     */
    public void onModPartSaveButtonAction(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(modPartIdInput.getText());
            String name = modPartNameInput.getText();
            int stock = Integer.parseInt(modPartInventoryInput.getText());
            double price = Double.parseDouble(modPartPriceInput.getText());
            int max = Integer.parseInt(modPartMaxInput.getText());
            int min = Integer.parseInt(modPartMinInput.getText());
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Minimum Greater Than Maximum");
                alert.setContentText("Maximum Must Be Greater Than Minimum");
                alert.showAndWait();
            } else {
                if (modPartOutsourceButton.isSelected() && currentPart instanceof InHouse) {
                    String company = modPartToggleInput.getText();
                    currentPart = new Outsourced(id, name, price, stock, min, max, company);
                }
                else if (modPartInHouseButton.isSelected() && currentPart instanceof Outsourced) {
                    int machineId = Integer.parseInt(modPartToggleInput.getText());
                    currentPart = new InHouse(id, name, price, stock, min, max, machineId);
                }
                else {
                    currentPart.setName(name);
                    currentPart.setPrice(price);
                    currentPart.setStock(stock);
                    currentPart.setMax(max);
                    currentPart.setMin(min);
                    if (currentPart instanceof InHouse) {
                        int machineId = Integer.parseInt(modPartToggleInput.getText());
                        ((InHouse)currentPart).setMachineId(machineId);
                    }
                    else {
                        String company = modPartToggleInput.getText();
                        ((Outsourced)currentPart).setCompanyName(company);
                    }
                }
                Inventory.updatePart(partIndex, currentPart);
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
     * Returns user to main menu using returnToMainMenu().
     * @param event - click on the cancel button
     */
    public void onModifyPartCancelAction(ActionEvent event) throws IOException {
        returnToMainMenu(event);
    }

    /**
     * created to improve code readability.
     */
    public void returnToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
