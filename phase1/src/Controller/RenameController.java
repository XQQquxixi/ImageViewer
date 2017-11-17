package Controller;

import Model.Image;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RenameController implements Initializable{
    @FXML
    private Label curName;

    @FXML
    private TextField inputName;

    @FXML
    private ChoiceBox<String> prevNames;

    private ArrayList<String> listOfPrevNames = new ArrayList<>();

    private Image image;

    void getImage(Image image) {
        this.image = image;
    }

    public void TypeName(ActionEvent event) {
        curName.setText(inputName.getText());
    }

    public void ChoosePrevName(ActionEvent event) {
        curName.setText(prevNames.getValue());
    }

    public void ButtonOkAction(ActionEvent event) {

    }

    public void ButtonCancelAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: should get the list of previous names from model
        listOfPrevNames.add("haha");
        listOfPrevNames.add("hehe");
        listOfPrevNames.add("heihei");
        prevNames.getItems().addAll(listOfPrevNames);
    }
}
