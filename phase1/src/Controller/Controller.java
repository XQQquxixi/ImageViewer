package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class Controller {
    @FXML
    private Button btn1;

    @FXML
    private ImageView iv1;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField initDirectory;

    private Map<String, File> nameToPath = new HashMap<>();

    public void Button1Action(ActionEvent event) throws Exception{
        File directory = new File(initDirectory.getText());
        if (directory.exists() && directory.isDirectory()) {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(directory);
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fc.showOpenDialog(null);
            if (selectedFile != null) {
                nameToPath.put(selectedFile.getName(), selectedFile);
                Image image = new Image(selectedFile.toURI().toString());
                iv1.setImage(image);
                listView.getItems().add(selectedFile.getName());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Uh-oh");
            alert.setContentText("Wrong Directory!!! Please check and enter again.");
            alert.showAndWait();
        }
    }

}
