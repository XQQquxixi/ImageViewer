package Controller;

import Model.Image;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

public class ImageViewController {
    @FXML
    private Button Move;
    @FXML
    private Button PREV;
    @FXML
    private Button NEXT;
    @FXML
    private Button CLOSE;
    @FXML
    private Button Delete;
    @FXML
    private Button Add;
    @FXML
    private Button rename;
    @FXML
    private ListView<String> listView;
    @FXML
    private javafx.scene.image.ImageView show;

    private File curFile;

    private Image selectedImage;

    void GetImage(File image) {
        curFile = image;
        // TODO: what if there is already a Image instance for this file?
        selectedImage = new Image(curFile);
        //new Model.ImageRenameObserver(selectedImage);
        initData(selectedImage);
    }

    void initData(Image image){
//        selectedImage = image;
        Collection<String> col = image.getCurrentTags();
        listView.getItems().clear();
        listView.getItems().addAll(col);
        File imageFile = image.getFile();
        javafx.scene.image.Image image1 = new javafx.scene.image.Image(imageFile.toURI().toString());
        show.setImage(image1);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void GoBack() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getResource("sample.fxml").openStream());
        Controller controller = loader.getController();
        try {
            GetImage(controller.getPrevImage(curFile));
        } catch (IndexOutOfBoundsException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Oops");
            alert.setHeaderText(null);
            alert.setContentText("This is already the first image of the list!");
            alert.showAndWait();
        }
    }

    public void Rename(ActionEvent event) throws IOException {
        Window window = rename.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("Rename.fxml").openStream());
        RenameController rename = loader.getController();
        rename.getImage(selectedImage);
        primaryStage.setTitle("Rename");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.showAndWait();
    }

    public void MoveFile() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose directory which you wanna move into");
        File selectedDirectory =
                directoryChooser.showDialog(Move.getScene().getWindow());
        String path = selectedDirectory.getAbsolutePath();
        selectedImage.move(path);
    }

    public void GoNext() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getResource("sample.fxml").openStream());
        Controller controller = loader.getController();
        try {
            GetImage(controller.getNextImage(curFile));
        } catch (IndexOutOfBoundsException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Oops");
            alert.setHeaderText(null);
            alert.setContentText("This is already the last image of the list!");
            alert.showAndWait();
        }

    }

    public void Exit() {
        Stage stage = (Stage) CLOSE.getScene().getWindow();
        stage.close();
    }

    public void DeleteTag() throws MalformedURLException {
        ObservableList<String> delete = listView.getSelectionModel().getSelectedItems();
        for (String tag : delete) {
            selectedImage.deleteTag(tag);
        }
        initData(selectedImage);
    }

    public void OpenConfirmBox() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ConfirmBox.fxml"));
        Parent AddTags = loader.load();
        Scene Box = new Scene(AddTags);
        ConfirmBox controller = loader.getController();
        controller.initData(selectedImage);
        Stage Window = (Stage) Add.getScene().getWindow();
        Window.setScene(Box);
    }
}
