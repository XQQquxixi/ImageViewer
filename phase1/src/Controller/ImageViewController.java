package Controller;

import Model.Image;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;

public class ImageViewController {
    public Button Move;
    public Button PREV;
    public Button NEXT;
    public Button CLOSE;
    public Button Delete;
    public Button Add;
    private Image SelectedImage;
    @FXML
    private ListView<String> listView;
    @FXML
    private javafx.scene.image.ImageView show;

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
