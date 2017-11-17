package Controller;

import Model.Image;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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

    private Image SelectedImage;

    void GetImage(File image) {
        try {
            SelectedImage = new Image(image);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        initData(SelectedImage);
    }

    void initData(Image image){
        SelectedImage = image;
        Collection<String> col = image.getCurrentTags();
        listView.getItems().addAll(col);
        File imageFile = image.getFile();
        javafx.scene.image.Image image1 = new javafx.scene.image.Image(imageFile.toURI().toString());
        show.setImage(image1);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void GoBack() {
    }

    public void Rename(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("Rename.fxml").openStream());
        RenameController rename = loader.getController();
        rename.getImage();
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
        SelectedImage.move(path);
    }

    public void GoNext() {

    }

    public void Exit() {
        Stage stage = (Stage) CLOSE.getScene().getWindow();
        stage.close();
    }

    public void DeleteTag() throws MalformedURLException {
        ObservableList<String> delete = listView.getSelectionModel().getSelectedItems();
        for (String tag : delete) {
            SelectedImage.deleteTag(tag);
        }
        initData(SelectedImage);
    }

    public void OpenConfirmBox() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ConfirmBox.fxml"));
        Parent AddTags = loader.load();
        Scene Box = new Scene(AddTags);
        ConfirmBox controller = loader.getController();
        controller.initData(SelectedImage);
        Stage Window = (Stage) Add.getScene().getWindow();
        Window.setScene(Box);
    }
}
