package Controller;

import Model.Image;
import Model.ImageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

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

    @FXML
    private ImageView pic;

//    private ArrayList<String> listOfPrevNames = new ArrayList<>();

    private Image image;

    private Controller controller;

    private ImageViewController imageViewController;

    private String oldName;

    void getImage(Image image) {
        this.image = image;
        ArrayList<String> listOfPrevNames = ImageManager.getPastName(image.getFile().getAbsolutePath());
        prevNames.getItems().addAll(listOfPrevNames);
        int i = image.getName().indexOf(" @");
        inputName.setText(image.getName().substring(0, i));
        curName.setText(image.getName());
        oldName = image.getName() + image.getExtension();
        pic.setImage(new javafx.scene.image.Image(image.getFile().toURI().toString()));
    }

    public void TypeName(KeyEvent event) {
        curName.setText(inputName.getText() + image.getTagPartOfName());
    }

    public void ChoosePrevName(ActionEvent event) {
        if (prevNames.getValue() != null) {
            curName.setText(prevNames.getValue());
        }
    }

    void passController(Controller controller) {
        this.controller = controller;
    }

    void passIMController(ImageViewController controller) {
        this.imageViewController = controller;
    }

    public void ButtonOkAction(ActionEvent event) throws IOException {
        //image.setName(curName.getText());
        try {
            if (!(curName.getText() + image.getExtension()).equals(oldName)) {
                image = ImageManager.renameImage(image.getFile().getAbsolutePath(), curName.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Oops");
                alert.setHeaderText(null);
                alert.setContentText("You didn't change anything!");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.initData(oldName, image.getName() + image.getExtension());
        imageViewController.GetImage(image.getFile());
        Controller.nameToFile.remove(oldName);
        Controller.nameToFile.put(image.getName() + image.getExtension(), image.getFile());
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


    }
}
