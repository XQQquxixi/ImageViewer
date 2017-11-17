package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    private static final double DISTANCE = 50.0;

    @FXML
    private Button btn1;

    @FXML
    private ImageView iv1;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField initDirectory;

    @FXML
    private Button ok;

    @FXML
    private Button editTags;

    private Map<String, File> nameToFile = new HashMap<>();

    public void Button1Action(ActionEvent event) throws Exception{
        File directory = new File(initDirectory.getText());
        if (directory.exists() && directory.isDirectory()) {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(directory);
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fc.showOpenDialog(null);
            if (selectedFile != null) {
                nameToFile.put(selectedFile.getName(), selectedFile);
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

    public void MouseClickList(MouseEvent event) {
        Image image = new Image(nameToFile.get(listView.getSelectionModel().getSelectedItem()).toURI().toString());
        iv1.setImage(image);
    }

    public void ButtonOkAction(ActionEvent event) throws IOException {
//        ObservableList<String> pictures;
//        pictures = listView.getSelectionModel().getSelectedItems();
//        for (String pic : pictures) {
        Window window = ok.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("ImageViewer.fxml").openStream());
        ImageViewController imageView = loader.getController();
        imageView.GetImage(nameToFile.get(listView.getSelectionModel().getSelectedItem()));
        primaryStage.setTitle("Image Viewer");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setX(window.getX() + DISTANCE);
        primaryStage.setY(window.getY() + DISTANCE);
        primaryStage.show();
//        }

    }

    public void ButtonEditTags(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("TagsView.fxml"));
        primaryStage.setTitle("Edit All Tags");
        primaryStage.setScene(new Scene(root, 280, 500));
        primaryStage.showAndWait();
    }

    File getPrevImage(File curImage) throws IndexOutOfBoundsException {
        int curIndex = listView.getItems().indexOf(curImage.getName());
        return nameToFile.get(listView.getItems().get(curIndex - 1));
    }

    File getNextImage(File curImage) throws IndexOutOfBoundsException {
        int curIndex = listView.getItems().indexOf(curImage.getName());
        return nameToFile.get(listView.getItems().get(curIndex + 1));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

}
