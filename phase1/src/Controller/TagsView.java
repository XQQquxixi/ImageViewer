
package Controller;

import Model.TagManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TagsView implements Initializable{
    public Button Delete;
    public Button Add;
    static TagManager tagManager;

    @FXML
    TextField newTag;
    @FXML
    ListView<String> listOfTags;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tagManager = new TagManager();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listOfTags.getItems().clear();
        listOfTags.getItems().addAll(TagManager.getTagList());
        listOfTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initData(){
        listOfTags.getItems().clear();
        listOfTags.getItems().addAll(TagManager.getTagList());
        listOfTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public void DeleteSelectedTags() throws IOException {
        ObservableList<String> listForDelete = listOfTags.getSelectionModel().getSelectedItems();
        for (String tag : listForDelete) {
            TagManager.removeTag(tag);
        }
        initData();
    }

    public void AddNewTag() throws IOException {
        String input = newTag.getText();
        if (input != null) {
            TagManager.addTag(input);
        }
        initData();
    }
}
