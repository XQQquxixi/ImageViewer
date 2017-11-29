package Controller;

import Model.Image;
import Model.ImageManager;
import Model.TagManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
import java.util.List;
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

    private File curFile;

    private Image selectedImage;

//    private String path;

    private Controller controller;

    private ArrayList<ArrayList<javafx.scene.image.Image>> simDisplayList = new ArrayList<>();
    private ArrayList<ArrayList<Image>> simUseList = new ArrayList<>();

    private static int curPage;


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

    public void ButtonNextPage(ActionEvent event) {
        if (curPage != simDisplayList.size() - 1) {
            if (simDisplayList.get(curPage+1).size() != 0) {
                sim1.setImage(simDisplayList.get(curPage+1).get(0));
                name1.setText(simUseList.get(curPage+1).get(0).getName());
            } else {
                sim1.setImage(null);
                name1.setText("");
            }
            if (simDisplayList.get(curPage+1).size() >= 2) {
                sim2.setImage(simDisplayList.get(curPage+1).get(1));
                name2.setText(simUseList.get(curPage+1).get(1).getName());
            } else {
                sim2.setImage(null);
                name2.setText("");
            }
            if (simDisplayList.get(curPage+1).size() == 3) {
                sim3.setImage(simDisplayList.get(curPage+1).get(2));
                name3.setText(simUseList.get(curPage+1).get(2).getName());
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

    public void ButtonPrevPage(ActionEvent event) {
        if (curPage != 0) {
            if (simDisplayList.get(curPage - 1).size() != 0) {
                sim1.setImage(simDisplayList.get(curPage - 1).get(0));
                name1.setText(simUseList.get(curPage-1).get(0).getName());
            } else {
                sim1.setImage(null);
                name1.setText("");
            }
            if (simDisplayList.get(curPage - 1).size() >= 2) {
                sim2.setImage(simDisplayList.get(curPage - 1).get(1));
                name2.setText(simUseList.get(curPage-1).get(1).getName());
            } else {
                sim2.setImage(null);
                name2.setText("");
            }
            if (simDisplayList.get(curPage - 1).size() >= 2) {
                sim3.setImage(simDisplayList.get(curPage - 1).get(2));
                name3.setText(simUseList.get(curPage-1).get(2).getName());
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

    public void DoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (event.getSource().equals(sim1) || event.getSource().equals(name1)) {
                GetImage(simUseList.get(curPage).get(0).getFile());
            } else if (event.getSource().equals(sim2) || event.getSource().equals(name2)) {
                GetImage(simUseList.get(curPage).get(1).getFile());
            } else if (event.getSource().equals(sim3) || event.getSource().equals(name3)) {
                GetImage(simUseList.get(curPage).get(2).getFile());
            }
        }
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
        int i = 0;
        ArrayList<javafx.scene.image.Image> list = new ArrayList<>();
        ArrayList<Image> list1 = new ArrayList<>();
//        System.out.println("image:" + selectedImage);
        ArrayList<Image> simList = Model.Similarity.getSimilarImages(selectedImage.getFile().getAbsolutePath());
        System.out.println(simList);
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
                if (newList.size() == 1) {
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
//        System.out.println(simDisplayList);
        System.out.println(ImageManager.getImages());
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
            alert.setHeaderText("Here is your all history changes of this image:");
//            alert.setContentText("Here is your all history changes of this image:");
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

//            alert.getDialogPane().setExpandableContent(expContent);
            alert.getDialogPane().setContent(expContent);

            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
