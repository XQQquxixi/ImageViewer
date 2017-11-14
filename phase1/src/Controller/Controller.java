package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller {
    @FXML
    private Button btn1;

    @FXML
    private ImageView iv1;

    @FXML
    private ListView listView;

    public void Button1Action(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("//Users//yangxueqing//Desktop//ProImageTry"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            iv1.setImage(image);
            listView.getItems().add(selectedFile.getName());
        } else {
            System.out.println("not valid");
        }
    }

}
