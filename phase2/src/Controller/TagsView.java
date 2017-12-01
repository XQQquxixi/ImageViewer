package Controller;

import Model.Image;
import Model.ImageManager;
import Model.TagManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class TagsView implements Initializable{

    /**
     * Button for delete some particular tags.
     */
    @FXML
    Button delete;

    /**
     * Button for add some particular tags.
     */
    @FXML
    Button add;

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

    private Controller controller;

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
    public void deleteSelectedTags() throws IOException {
        ObservableList<String> listForDelete = listOfTags.getSelectionModel().getSelectedItems();
        for (String tag : listForDelete) {
            Map<File, Image> map = ImageManager.getImages();
            ArrayList<Image> images = new ArrayList<>(map.values());
            ArrayList<Image> imWithTag = new ArrayList<>();
            for (Image image : images) {
                if (image.getCurrentTags().contains(tag)) {
                    imWithTag.add(image);
                }
            }
            if (imWithTag.size() != 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("Are you sure you want to delete " + tag + "?");
                alert.setContentText(tag + " involved in " + imWithTag.toString() + " will also be deleted.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == ButtonType.OK) {
                        TagManager.removeTag(tag);
                        for (Image image : imWithTag) {
                            String oldName = image.getName() + image.getExtension();
                            ImageManager.deleteTag(image.getFile().getAbsolutePath(), tag);
                            for (String key : Controller.nameToFile.keySet()) {
                                if (Controller.nameToFile.get(key).equals(image.getFile())) {
                                    oldName = key;
                                }
                            }
                            if (Controller.nameToFile.containsKey(oldName)) {
                                String newName = image.getName() + image.getExtension();
                                Controller.nameToFile.remove(oldName);
                                Controller.nameToFile.put(newName, image.getFile());
                                controller.initData(oldName, newName);
                            }
                        }
                    }
                }
            }
        }
        initData();
    }

    /**
     * Pass a Controller into this.controller.
     * @param controller The controller we want to pass into.
     */
    void passController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Add new Tag into TagManager.
     * @throws IOException
     * When user add Tag, there might be IOException.
     */
    public void addNewTag() throws IOException {
        String input = newTag.getText();
        if (input != null) {
            TagManager.addTag(input);
        }
        newTag.clear();
        initData();
    }
}
