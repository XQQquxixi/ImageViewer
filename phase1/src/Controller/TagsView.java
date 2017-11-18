package Controller;

import Model.TagManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.io.IOException;

public class TagsView {
    public Button Delete;
    public Button Add;
    private TagManager MyTags;

    @FXML
    TextField newTag;
    @FXML
    ListView<String> listOfTags;

    private void initData(TagManager tagManager) {
        MyTags = tagManager;
        listOfTags.getItems().clear();
        listOfTags.getItems().addAll(MyTags.getTagList());
        listOfTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public void DeleteSelectedTags() throws IOException {
        ObservableList<String> listForDelete = listOfTags.getSelectionModel().getSelectedItems();
        for (String tag : listForDelete) {
            TagManager.removeTag(tag);
        }
        initData(MyTags);
    }

    public void AddNewTag() throws IOException {
        String input = newTag.getText();
        if (input != null) {
            TagManager.addTag(input);
        }
        initData(MyTags);
    }
}
