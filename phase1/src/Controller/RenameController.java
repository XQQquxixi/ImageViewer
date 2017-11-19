package Controller;

import Model.Image;
import Model.ImageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

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
    private Button ok;

    @FXML
    private Button cancel;

//    private ArrayList<String> listOfPrevNames = new ArrayList<>();

    private Image image;

    void getImage(Image image) {
        this.image = image;
        System.out.println(image.toString());
        ArrayList<String> listOfPrevNames = ImageManager.getPastName(image.getFile().getAbsolutePath());
        prevNames.getItems().addAll(listOfPrevNames);
        curName.setText(image.getName());
    }

    public void TypeName(ActionEvent event) {
        curName.setText(inputName.getText());
    }

    public void ChoosePrevName(ActionEvent event) {
        curName.setText(prevNames.getValue());
    }

    public void ButtonOkAction(ActionEvent event) throws IOException {
        //image.setName(curName.getText());
        try {
            image = ImageManager.renameImage(image.getFile().getAbsolutePath(), curName.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getResource("ImageView.fxml").openStream());
        ImageViewController controller = loader.getController();
        controller.initData(image);
        Window window = ok.getScene().getWindow();
        Stage stage = (Stage) window;
        stage.close();
    }

    public void ButtonCancelAction(ActionEvent event) {
        ((Stage) cancel.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: should get the list of previous names from model

        //        ArrayList<String> listOfPrevNames = image.getImageRenameObserver().getPastNames();

//        logs.remove(0);
//        for (String log : logs) {
//            listOfPrevNames.add(log.split(",")[0]);
//        }
//        listOfPrevNames.add("haha");
//        listOfPrevNames.add("hehe");
//        listOfPrevNames.add("heihei");

    }
}
