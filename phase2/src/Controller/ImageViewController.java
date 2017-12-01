package Controller;

import Model.Image;
import Model.ImageManager;
import Model.TagManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ImageViewController {

    /*
     * All codes with Alert are learned from website: http://code.makery.ch/blog/javafx-dialogs-official/
     */


    /**
     * Button for onAction moveFile.
     */
    @FXML
    Button Move;

    /**
     * Button for onAction goBack.
     */
    @FXML
    Button PREV;

    /**
     * Button for onAction goNext.
     */
    @FXML
    Button NEXT;

    /**
     * Button for onAction exit.
     */
    @FXML
    Button CLOSE;

    /**
     * Button for onAction deleteTag.
     */
    @FXML
    Button Delete;

    /**
     * Button for onAction rename.
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
     * ImageView to show this file's similar image.
     */
    @FXML
    javafx.scene.image.ImageView sim1;

    /**
     * ImageView to show this file's similar image.
     */
    @FXML
    javafx.scene.image.ImageView sim2;

    /**
     * ImageView to show this file's similar image.
     */
    @FXML
    javafx.scene.image.ImageView sim3;

    /**
     * Label to show name of curFile.
     */
    @FXML
    Label Name;
    // New Thing

    /**
     * ListView to show all tags of TagManager.
     */
    @FXML
    ListView<String> tags;

    /**
     * TextField to input new Tag.
     */
    @FXML
    TextField newTag;

    /**
     * Button for onAction buttonHistory.
     */
    @FXML
    Button history;

    /**
     * Label for displaying page number.
     */
    @FXML
    Label pageNum;

    /**
     * Label for displaying image name.
     */
    @FXML
    Label name1;

    /**
     * Label for displaying image name.
     */
    @FXML
    Label name2;

    /**
     * Label for displaying image name.
     */
    @FXML
    Label name3;

    /**
     * The current file which user is checking and operating.
     */
    private File curFile;

    /**
     * The current Image which user is checking and operating
     */
    private Image selectedImage;

    /**
     * The Controller which is passed from Main scene.
     */
    private Controller controller;

    /**
     * Container for containers which is for show similar Image with this current Image.
     */
    private ArrayList<ArrayList<javafx.scene.image.Image>> simDisplayList = new ArrayList<>();

    /**
     * Container for store similar Image with this current Image.
     */
    private ArrayList<ArrayList<Image>> simUseList = new ArrayList<>();

    /**
     * The page number of containers for show similar Image user is checking for now.
     */
    private static int curPage;


    /**
     * Pass a file and let selectedImage be the Image which is connected to this file. Then pass selectedImage into
     * initDate.
     *
     * @param image A file from Controller.
     */
    void getImage(File image) {
        curFile = image;
        try {
            selectedImage = ImageManager.checkKey(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initData(selectedImage);
    }

    /**
     * If user double click the tag, then user can change tag's content, and then, all images with this tag will change
     * the tag. If user right click, then user can choose to add this tag into this Image, or delete the tag
     * from tag pools.
     *
     * @param event Double click the tag in the right side.
     */
    public void doubleClickTags(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ObservableList<String> list = tags.getSelectionModel().getSelectedItems();
            TextInputDialog dialog = new TextInputDialog(list.get(0));
            dialog.setTitle("Text Input Dialog");
            dialog.setHeaderText("Look, a Text Input Dialog");
            dialog.setContentText("Please enter your name:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    String oldTag = list.get(0);
                    String newTag = result.get();
                    if (!tags.getItems().contains(newTag)) {
                        TagManager.editTag(oldTag, newTag);
                        if (selectedImage.getCurrentTags().contains(oldTag)) {
                            String oldName = selectedImage.getName() + selectedImage.getExtension();
                            for (String key : Controller.nameToFile.keySet()) {
                                if (Controller.nameToFile.get(key).equals(curFile)) {
                                    oldName = key;
                                }
                            }
                            selectedImage = ImageManager.deleteTag(selectedImage.getFile().getAbsolutePath(), oldTag);
                            selectedImage = ImageManager.addTag(selectedImage.getFile().getAbsolutePath(), newTag);
                            listView.getItems().clear();
                            listView.getItems().addAll(selectedImage.getCurrentTags());
                            Name.setText(selectedImage.getName());
                            String newName = selectedImage.getName() + selectedImage.getExtension();
                            Controller.nameToFile.remove(oldName);
                            Controller.nameToFile.put(newName, selectedImage.getFile());
                            Name.setText(selectedImage.getName());
                            controller.initData(oldName, newName);
                        }
                        Map<File, Image> map = ImageManager.getImages();
                        ArrayList<Image> images = new ArrayList<>(map.values());
                        ArrayList<Image> imWithTag = new ArrayList<>();
                        for (Image image : images) {
                            if (image.getCurrentTags().contains(oldTag)) {
                                imWithTag.add(image);
                            }
                        }
                        for (Image image : imWithTag) {
                            if (image.getCurrentTags().contains(oldTag)) {
                                String oldName = image.getName() + image.getExtension();
                                try {
                                    ImageManager.deleteTag(image.getFile().getAbsolutePath(), oldTag);
                                    ImageManager.addTag(image.getFile().getAbsolutePath(), newTag);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.tags.getItems().clear();
                this.tags.getItems().addAll(TagManager.getTagList());
            }
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            MenuItem delete = new MenuItem("delete");
            MenuItem add = new MenuItem("add");
            ContextMenu contextMenu = new ContextMenu(add, delete);
            contextMenu.show(listView, event.getScreenX(), event.getScreenY());
            add.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        addTags();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String tag = tags.getSelectionModel().getSelectedItems().get(0);
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
                                if (selectedImage.getCurrentTags().contains(tag)) {
                                    listView.getItems().remove(tag);
                                }
                                try {
                                    TagManager.removeTag(tag);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (Image image : imWithTag) {
                                if (image.getCurrentTags().contains(tag)) {
                                    String oldName = image.getName() + image.getExtension();
                                    try {
                                        ImageManager.deleteTag(image.getFile().getAbsolutePath(), tag);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
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
                            Name.setText(selectedImage.getName());
                            name1.setText(simUseList.get(0).get(0).getName());
                            name2.setText(simUseList.get(0).get(1).getName());
                            name3.setText(simUseList.get(0).get(2).getName());
                        }
                    }
                    tags.getItems().clear();
                    tags.getItems().addAll(TagManager.getTagList());
                }
            });
        }
    }

    /**
     * OnAction for Button NextPage.
     * The container of similar Image will go to next page.
     */

    public void buttonNextPage() {
        if (curPage != simDisplayList.size() - 1) {
            if (simDisplayList.get(curPage + 1).size() != 0) {
                sim1.setImage(simDisplayList.get(curPage + 1).get(0));
                name1.setText(simUseList.get(curPage + 1).get(0).getName());
            } else {
                sim1.setImage(null);
                name1.setText("");
            }
            if (simDisplayList.get(curPage + 1).size() >= 2) {
                sim2.setImage(simDisplayList.get(curPage + 1).get(1));
                name2.setText(simUseList.get(curPage + 1).get(1).getName());
            } else {
                sim2.setImage(null);
                name2.setText("");
            }
            if (simDisplayList.get(curPage + 1).size() == 3) {
                sim3.setImage(simDisplayList.get(curPage + 1).get(2));
                name3.setText(simUseList.get(curPage + 1).get(2).getName());
            } else {
                sim3.setImage(null);
                name3.setText("");
            }
            curPage += 1;
            pageNum.setText("Page " + (curPage + 1));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Oops");
            alert.setHeaderText(null);
            alert.setContentText("This is already the last page!");
            alert.showAndWait();
        }
    }

    /**
     * OnAction for Button prePage.
     * The container of similar Image will go to previous page.
     */

    public void buttonPrevPage() {
        if (curPage != 0) {
            if (simDisplayList.get(curPage - 1).size() != 0) {
                sim1.setImage(simDisplayList.get(curPage - 1).get(0));
                name1.setText(simUseList.get(curPage - 1).get(0).getName());
            } else {
                sim1.setImage(null);
                name1.setText("");
            }
            if (simDisplayList.get(curPage - 1).size() >= 2) {
                sim2.setImage(simDisplayList.get(curPage - 1).get(1));
                name2.setText(simUseList.get(curPage - 1).get(1).getName());
            } else {
                sim2.setImage(null);
                name2.setText("");
            }
            if (simDisplayList.get(curPage - 1).size() >= 2) {
                sim3.setImage(simDisplayList.get(curPage - 1).get(2));
                name3.setText(simUseList.get(curPage - 1).get(2).getName());
            } else {
                sim3.setImage(null);
                name3.setText("");
            }
            curPage -= 1;
            pageNum.setText("Page " + (curPage + 1));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Oops");
            alert.setHeaderText(null);
            alert.setContentText("This is already the first page!");
            alert.showAndWait();
        }
    }

    /**
     * When user double click one of three images for current page of container for similar Image, we can view and
     * operate the image which is double clicked by user.
     *
     * @param event Double click one of three images.
     */

    public void doubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (event.getSource().equals(sim1) || event.getSource().equals(name1)) {
                getImage(simUseList.get(curPage).get(0).getFile());
            } else if (event.getSource().equals(sim2) || event.getSource().equals(name2)) {
                getImage(simUseList.get(curPage).get(1).getFile());
            } else if (event.getSource().equals(sim3) || event.getSource().equals(name3)) {
                getImage(simUseList.get(curPage).get(2).getFile());
            }
        }
    }

    /**
     * Pass a Controller into this.controller.
     *
     * @param controller The Controller we want to pass into
     */
    void passController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Update information of this image.
     *
     * @param image The image file we update
     */
    void initData(Image image) {
        Name.setText(image.getName());
        Name.setText(image.getName());
        ArrayList<String> col = image.getCurrentTags();
        listView.getItems().clear();
        listView.getItems().addAll(col);
        Collection<String> tags = TagManager.getTagList();
        this.tags.getItems().clear();
        this.tags.getItems().addAll(tags);
        File imageFile = image.getFile();
        javafx.scene.image.Image image1 = new javafx.scene.image.Image(imageFile.toURI().toString());
        show.setImage(image1);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.tags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        int i = 0;
        ArrayList<javafx.scene.image.Image> list = new ArrayList<>();
        ArrayList<Image> list1 = new ArrayList<>();
        ArrayList<Image> simList = Model.Similarity.getSimilarImages(selectedImage.getFile().getAbsolutePath());
        simDisplayList.clear();
        simUseList.clear();
        for (Image simImage : simList) {
            javafx.scene.image.Image sim = new javafx.scene.image.Image(simImage.getFile().toURI().toString());
            list.add(sim);
            list1.add(simImage);
            i += 1;
            if (i % 3 == 0) {
                ArrayList<javafx.scene.image.Image> newList = new ArrayList<>();
                ArrayList<Image> newList1 = new ArrayList<>();
                newList.addAll(list);
                newList1.addAll(list1);
                simDisplayList.add(newList);
                simUseList.add(newList1);
                list.clear();
                list1.clear();
                i = 0;
            } else if (simList.indexOf(simImage) == (simList.size() - 1)) {
                ArrayList<javafx.scene.image.Image> newList = new ArrayList<>();
                ArrayList<Image> newList1 = new ArrayList<>();
                newList.addAll(list);
                newList1.addAll(list1);
                newList.add(null);
                if (newList.size() == 2) {
                    newList.add(null);
                }
                simDisplayList.add(newList);
                simUseList.add(newList1);
            }
        }
        sim1.setImage(simDisplayList.get(0).get(0));
        sim2.setImage(simDisplayList.get(0).get(1));
        sim3.setImage(simDisplayList.get(0).get(2));
        name1.setText(simUseList.get(0).get(0).getName());
        name2.setText(simUseList.get(0).get(1).getName());
        name3.setText(simUseList.get(0).get(2).getName());
        curPage = 0;
        pageNum.setText("Page " + (curPage + 1));
    }

    /**
     * Go back to last image in listView of Controller.
     *
     * @throws IOException If curFile is not in listView, there will be IOException.
     */
    public void goBack() throws IOException {
        try {
            getImage(controller.getPrevImage(curFile));
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
     *
     * @throws IOException if saving serialized file fails.
     */

    public void rename() throws IOException {
        Window window = rename.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("Rename.fxml").openStream());
        RenameController rename = loader.getController();
        rename.passController(controller);
        rename.passIMController(this);
        rename.getImage(selectedImage);
        primaryStage.setTitle("rename");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.showAndWait();
    }

    /**
     * Move this file.
     * It will open a directoryChooser, user can choose any directory which they prefer.
     *
     * @throws IOException if saving serialized file fails.
     */

    public void moveFile() throws IOException {
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
     *
     * @throws IOException If curFile is not in listView, there will be IOException.
     */

    public void goNext() throws IOException {
        try {
            getImage(controller.getNextImage(curFile));
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

    public void exit() {
        Stage stage = (Stage) CLOSE.getScene().getWindow();
        stage.close();
    }

    /**
     * Delete selected Tag of this image.
     *
     * @throws IOException if saving serialized file fails.
     */

    public void deleteTag() throws IOException {
        ObservableList<String> delete = listView.getSelectionModel().getSelectedItems();
        String oldName = selectedImage.getName() + selectedImage.getExtension();
        for (String key : Controller.nameToFile.keySet()) {
            if (Controller.nameToFile.get(key).equals(curFile)) {
                oldName = key;
            }
        }
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
     * Input the String in textField into new Tag of this Image file and TagManager. If the tag is already in
     * tags(or listView), the tags(or listView) will not change.
     *
     * @throws IOException if saving serialized file fails.
     */
    public void inputNewTag() throws IOException {
        String input = newTag.getText();
        boolean flag = false;
        if (input != null) {
            if (input.length() == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setContentText("Are you sure you want to add an empty tag?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK && !tags.getItems().contains(input)) {
                    // ... user chose OK
                    tags.getItems().add(input);
                    flag = true;
                }
            } else if (!tags.getItems().contains(input)) {
                tags.getItems().add(input);
                flag = true;
            }
            if (flag) {
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
    }

    /**
     * Add selected Tag for this image.
     *
     * @throws IOException if saving serialized file fails.
     */

    private void addTags() throws IOException {
        ObservableList<String> list = tags.getSelectionModel().getSelectedItems();
        String oldName = selectedImage.getName() + selectedImage.getExtension();
        for (String key : Controller.nameToFile.keySet()) {
            if (Controller.nameToFile.get(key).equals(curFile)) {
                oldName = key;
            }
        }
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
     * Open an alert to view all history of this Image file.
     */

    public void buttonHistory() {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("History");
            alert.setHeaderText("Here is your all history changes of this image:");
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

            alert.getDialogPane().setContent(expContent);

            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
