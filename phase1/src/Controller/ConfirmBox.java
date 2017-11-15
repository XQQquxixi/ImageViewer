package Controller;

import Model.Image;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ConfirmBox {
    public Button Confirm;
    public Button add;
    @FXML
    ListView Tags;
    @FXML
    TextField newTag;
    private Image SelectedImage;

    public void initDate(Image image) {
        SelectedImage = image;
        Tags.getItems().addAll(SelectedImage.getCurrentTags());
        Tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void Confirm() throws IOException {
        ObservableList<String> list = Tags.getSelectionModel().getSelectedItems();
        for (String tag : list) {
            SelectedImage.addTag(tag);
        }
        initDate(SelectedImage);
    }

    public void AddNewTag() throws IOException {
        String input = newTag.getText();
        if (input != null) {
            SelectedImage.addTag(input);
        }
    }

}
