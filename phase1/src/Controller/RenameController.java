package Controller;

import Model.Image;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    @FXML
    private Button cancel;

//    private ArrayList<String> listOfPrevNames = new ArrayList<>();

    private Image image;

    void getImage(Image image) {
        this.image = image;
        System.out.println(image.toString());
    }

    public void TypeName(ActionEvent event) {
        curName.setText(inputName.getText());
    }

    public void ChoosePrevName(ActionEvent event) {
        curName.setText(prevNames.getValue());
    }

    public void ButtonOkAction(ActionEvent event) {
        image.setName(curName.getText());
    }

    public void ButtonCancelAction(ActionEvent event) {
        ((Stage) cancel.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: should get the list of previous names from model
        Image listOfPrevNames = image;
        //        ArrayList<String> listOfPrevNames = image.getImageRenameObserver().getPastNames();

//        logs.remove(0);
//        for (String log : logs) {
//            listOfPrevNames.add(log.split(",")[0]);
//        }
//        listOfPrevNames.add("haha");
//        listOfPrevNames.add("hehe");
//        listOfPrevNames.add("heihei");
//        prevNames.getItems().addAll(listOfPrevNames);
    }
}
