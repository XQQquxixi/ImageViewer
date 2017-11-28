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

    /**
     * Button for delete some particular tags.
     */
    @FXML
    Button Delete;

    /**
     * Button for add some particular tags.
     */
    @FXML
    Button Add;

    /**
     * TextField for input new tag.
     */
    @FXML
    TextField newTag;

    /**
     * ListView to show list of all tags.
     */
    @FXML
    ListView<String> listOfTags;

    /**
     * Initialize TagsView.
     * @param location
     * URL location
     * @param resources
     * ResourceBundle resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listOfTags.getItems().clear();
        listOfTags.getItems().addAll(TagManager.getTagList());
        listOfTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Update data for any change in listOfTags.
     */
    private void initData(){
        listOfTags.getItems().clear();
        listOfTags.getItems().addAll(TagManager.getTagList());
        listOfTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    /**
     * Delete selected tags.
     * @throws IOException
     * When user delete Tag, there might be IOException.
     */
    public void DeleteSelectedTags() throws IOException {
        ObservableList<String> listForDelete = listOfTags.getSelectionModel().getSelectedItems();
        for (String tag : listForDelete) {
            TagManager.removeTag(tag);
        }
        initData();
    }

    /**
     * Add new Tag into TagManager.
     * @throws IOException
     * When user add Tag, there might be IOException.
     */
    public void AddNewTag() throws IOException {
        String input = newTag.getText();
        if (input != null) {
            TagManager.addTag(input);
        }
        newTag.clear();
        initData();
    }
}
