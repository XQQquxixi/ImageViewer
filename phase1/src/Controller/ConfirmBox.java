package Controller;

import Model.Image;
import Model.TagManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfirmBox {
    public Button Confirm;
    public Button add;
    @FXML
    ListView<String> Tags;
    @FXML
    TextField newTag;
    private Image SelectedImage;

    void initData(Image image) {
        SelectedImage = image;
        Tags.getItems().clear();
        Tags.getItems().addAll(TagManager.getTagList());
        Tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void Confirm() throws IOException {
        ObservableList<String> list = Tags.getSelectionModel().getSelectedItems();
        for (String tag : list) {
            SelectedImage.addTag(tag);
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ImageView.fxml"));
        Parent GoBackImageView = loader.load();
        Scene ImageView = new Scene(GoBackImageView);
        ImageViewController controller = loader.getController();
        controller.initData(SelectedImage);
        Stage Window = (Stage) Confirm.getScene().getWindow();
        Window.setScene(ImageView);
    }

    public void AddNewTag() throws IOException {
        String input = newTag.getText();
        if (input != null) {
            SelectedImage.addTag(input);
        }
        initData(SelectedImage);
    }

}
