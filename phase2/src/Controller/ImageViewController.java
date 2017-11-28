package Controller;

import Model.Image;
import Model.ImageManager;
import Model.TagManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ImageViewController {

    /**
     * All codes with Alert are learned from website: http://code.makery.ch/blog/javafx-dialogs-official/
     */


    /**
     * Button for onAction MoveFile.
     */
    @FXML
    Button Move;

    /**
     * Button for onAction GoBack.
     */
    @FXML
    Button PREV;

    /**
     * Button for onAction GoNext.
     */
    @FXML
    Button NEXT;

    /**
     * Button for onAction Exit.
     */
    @FXML
    Button CLOSE;

    /**
     * Button for onAction DeleteTag.
     */
    @FXML
    Button Delete;

    /**
     * Button for onAction AddTags.
     */
    @FXML
    Button Add;

    /**
     * Button for onAction Rename.
     */
    @FXML
    Button rename;

    /**
     * ListView to show all tags of this Image(selectedImage).
     */
    @FXML
    ListView<String> listView;

    /**
     * ImageView to show this file(curFile).
     */
    @FXML
    javafx.scene.image.ImageView show;

    /**
     * Label to show name of curFile.
     */
    @FXML
    Label Name;
    // New Thing

    /**
     * ListView to show all Tags of TagManager.
     */
    @FXML
    ListView<String> Tags;

    /**
     * TextField to input new Tag.
     */
    @FXML
    TextField newTag;

    /**
     * Button for onAction ButtonHistory.
     */
    @FXML
    Button history;

    private File curFile;

    private Image selectedImage;

    private String path;

    private Controller controller;


    /**
     * Pass a file and let selectedImage be the Image which is connected to this file. Then pass selectedImage into
     * initDate.
     * @param image
     * A file from Controller.
     */
    void GetImage(File image) {
        curFile = image;
        try {
            selectedImage = ImageManager.checkKey(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initData(selectedImage);
    }

    /**
     * Pass a Controller into this.controller.
     * @param controller
     */
    void passController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Update information of this image.
     * @param image
     */
    void initData(Image image){
        Name.setText(image.getName());
        Name.setText(image.getName());
        ArrayList<String> col = image.getCurrentTags();
        listView.getItems().clear();
        listView.getItems().addAll(col);
        Collection<String> tags = TagManager.getTagList();
        Tags.getItems().clear();
        Tags.getItems().addAll(tags);
        File imageFile = image.getFile();
        javafx.scene.image.Image image1 = new javafx.scene.image.Image(imageFile.toURI().toString());
        show.setImage(image1);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Go back to last image in listView of Controller.
     * @throws IOException
     */

    public void GoBack() throws IOException {
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

    /**
     * Open RenameController to rename of this image file.
     * @param event
     * Click rename Button.
     * @throws IOException
     */

    public void Rename(ActionEvent event) throws IOException {
        Window window = rename.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("Rename.fxml").openStream());
        RenameController rename = loader.getController();
        rename.passController(controller);
        rename.passIMController(this);
        rename.getImage(selectedImage);
        primaryStage.setTitle("Rename");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.showAndWait();
    }

    /**
     * Move this file.
     * @throws IOException
     */

    public void MoveFile() throws IOException {
        // Part of codes in this method is copied from http://java-buddy.blogspot.ca/2013/03/javafx-simple-example-of.html
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose directory which you wanna move into");
        File selectedDirectory =
                directoryChooser.showDialog(Move.getScene().getWindow());
        if (selectedDirectory != null) {
            String path = selectedDirectory.getAbsolutePath();
            selectedImage = ImageManager.move(selectedImage.getFile().getAbsolutePath(), path + "/" + selectedImage.getName() + selectedImage.getExtension());
        }
        Controller.nameToFile.put(selectedImage.getName() + selectedImage.getExtension(), selectedImage.getFile());
    }

    /**
     * Go to next image file in listView of Controller.
     * @throws IOException
     */

    public void GoNext() throws IOException {
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

    /**
     * Close this Stage.
     */

    public void Exit() {
        Stage stage = (Stage) CLOSE.getScene().getWindow();
        stage.close();
    }

    /**
     * Delete selected Tag of this image.
     * @throws IOException
     */

    public void DeleteTag() throws IOException {
        ObservableList<String> delete = listView.getSelectionModel().getSelectedItems();
        String oldName = selectedImage.getName() + selectedImage.getExtension();

        for (String tag : delete) {
            selectedImage = ImageManager.deleteTag(selectedImage.getFile().getAbsolutePath(), tag);
            listView.getItems().remove(tag);
        }
        String newName = selectedImage.getName() + selectedImage.getExtension();
        Controller.nameToFile.remove(oldName);
        Controller.nameToFile.put(newName, selectedImage.getFile());
        Name.setText(selectedImage.getName());
        controller.initData(oldName, newName);
    }


    /**
     *
     * @throws IOException
     */
    public void InputNewTag() throws IOException {
        String input = newTag.getText();
        if (input != null) {
            if (input.length() == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Are you sure you want to add an empty tag?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK && !Tags.getItems().contains(input)) {
                    // ... user chose OK
                    Tags.getItems().add(input);
                }
            } else if (!Tags.getItems().contains(input)) {
                Tags.getItems().add(input);
            }
            String oldName = selectedImage.getName() + selectedImage.getExtension();

            selectedImage = ImageManager.addTag(selectedImage.getFile().getAbsolutePath(), input);
            if (!listView.getItems().contains(input)) {
                listView.getItems().add(input);
            }
            String newName = selectedImage.getName() + selectedImage.getExtension();
            Controller.nameToFile.remove(oldName);
            Controller.nameToFile.put(newName, selectedImage.getFile());
            Name.setText(selectedImage.getName());
            controller.initData(oldName, newName);
            newTag.clear();
        }
    }

    /**
     * Add selected Tag for this image.
     * @throws IOException
     */

    public void AddTags() throws IOException{
        ObservableList<String> list = Tags.getSelectionModel().getSelectedItems();
        String oldName = selectedImage.getName() + selectedImage.getExtension();
        for (String tag : list) {
            selectedImage = ImageManager.addTag(selectedImage.getFile().getAbsolutePath(), tag);
            if (!listView.getItems().contains(tag)) {
                listView.getItems().add(tag);
            }
        }
        String newName = selectedImage.getName() + selectedImage.getExtension();
        Controller.nameToFile.remove(oldName);
        Controller.nameToFile.put(newName, selectedImage.getFile());
        Name.setText(selectedImage.getName());
        controller.initData(oldName, newName);
    }

    /**
     * Open an alert to view all history of this Image.
     * @param event
     */

    public void ButtonHistory(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("History");
            alert.setContentText("Here is your all history changes of this image:");
            String history = ImageManager.getLog(selectedImage.getFile().getAbsolutePath());
            TextArea textArea = new TextArea(history);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(textArea, 0, 0);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
