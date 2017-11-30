package Controller;

import Model.ImageManager;
import Model.TagManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller implements Initializable{

    /**
     * All codes with Alert are learned from website: http://code.makery.ch/blog/javafx-dialogs-official/
     */

    /**
     *
     */

    private static final double DISTANCE = 50.0;

    /**
     * Button for onAction ViewHistory
     */
    @FXML
    Button Log;

    /**
     * Button for open DirectoryChooser.
     */
    @FXML
    private Button btn1;

    /**
     * ImageView for show the image you click
     */

    @FXML
    private ImageView iv1;

    /**
     * ListView for names of these image files
     */

    @FXML
    private ListView<String> listView;

    /**
     * Button for clear all image files.
     */

    @FXML
    private Button clear;

    /**
     * Button for open ImageViewController to operate the image file user choose.
     */

    @FXML
    private Button ok;

    /**
     * Button for open TagManager.
     */

    @FXML
    private Button editTags;

    /**
     * Label show the path of image you click
     */

    @FXML
    private Label path;

    @FXML
    private TextField tags;

    /**
     * HashMap for store and record name and image File.
     */

    static Map<String, File> nameToFile = new HashMap<>();

    /**
     * Static ImageManager.
     */

    private static ImageManager imageManager;

    /**
     * Static TagManager.
     */

    private static TagManager tagManager;

    /**
     * Update Data when user change something in other Scene.
     * @param oldName
     * @param newName
     */

    public void initData(String oldName, String newName) {
        int position = listView.getItems().indexOf(oldName);
        listView.getItems().set(position, newName);
    }

    public void keyEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tags.getText().equals("")) {
                listView.getItems().clear();
                listView.getItems().addAll(nameToFile.keySet());
            } else {
                ArrayList<String> tagList = new ArrayList<>();
                for (String tag : tags.getText().split(",")) {
                    tagList.add(tag.trim());
                }
                listView.getItems().clear();
                listView.getItems().addAll(ImageManager.getImagesWithSameTags(tagList));
                System.out.println(ImageManager.getImagesWithSameTags(tagList));
            }
        }
    }

    public void buttonCancel(ActionEvent event) {
        listView.getItems().clear();
        listView.getItems().addAll(nameToFile.keySet());
    }

    public void ViewHistory() throws IOException{
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("History");
        alert.setContentText("Here is your all history changes of all files:");
        String history = ImageManager.getLogs();
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
    }

    /**
     * Open a directoryChooser to choose a directory. Then all image files in this directory will show in listView.
     * @param event
     * @throws Exception
     */

    public void Button1Action(ActionEvent event) throws Exception {
        DirectoryChooser dc = new DirectoryChooser();
        File directory = dc.showDialog(btn1.getScene().getWindow());
        if (directory != null) {
            Path dir = directory.toPath();
            int depth = Integer.MAX_VALUE;
            Stream<Path> pathStream1 = Files.find(dir, depth, ((path, basicFileAttributes) -> path.getFileName().toString().toLowerCase().endsWith(".png")));
            List<Path> paths = pathStream1.collect(Collectors.toList());
            Stream<Path> pathStream2 = Files.find(dir, depth, ((path, basicFileAttributes) -> path.getFileName().toString().toLowerCase().endsWith(".jpg")));
            Stream<Path> pathStream3 = Files.find(dir, depth, ((path, basicFileAttributes) -> path.getFileName().toString().toLowerCase().endsWith(".bmp")));
            Stream<Path> pathStream4 = Files.find(dir, depth, ((path, basicFileAttributes) -> path.getFileName().toString().toLowerCase().endsWith(".jpeg")));
            Stream<Path> pathStream5 = Files.find(dir, depth, ((path, basicFileAttributes) -> path.getFileName().toString().toLowerCase().endsWith(".gif")));
            paths.addAll(pathStream2.collect(Collectors.toList()));
            paths.addAll(pathStream3.collect(Collectors.toList()));
            paths.addAll(pathStream4.collect(Collectors.toList()));
            paths.addAll(pathStream5.collect(Collectors.toList()));
            for (Path path : paths) {
                File file = path.toFile();
                if (file.exists()) {
                    String name = file.getName();
                    if (nameToFile.containsKey(name)) {
                        File parentFile = nameToFile.get(name);
                        int i = dir.toString().length();
                        // Check whether the parentFile is belong to Main Directory.
                        if (!dir.toString().equals(parentFile.getParent())){
                            nameToFile.remove(name);
                            listView.getItems().remove(name);
                            nameToFile.put(parentFile.getAbsolutePath(), parentFile);
                            listView.getItems().add(parentFile.getAbsolutePath().substring(i));
                        }
                        name = file.getAbsolutePath().substring(i);
                    }
                    nameToFile.put(name, file);
                    Image image = new Image(file.toURI().toString());
                    Model.Image image1 = new Model.Image(file);
                    iv1.setImage(image);
                    listView.getItems().add(name);
                    ImageManager.checkKey(file);
                }
            }
        }
    }
//        File directory = new File(initDirectory.getText().replaceAll("/", "//"));
//        FileChooser fc = new FileChooser();
//        if (directory.exists() && directory.isDirectory() || initDirectory.getText().equals("")) {
//            if (initDirectory.getText().equals("")) {
//                fc.setInitialDirectory(new File(System.getProperty("user.home")));
//            } else {
//                fc.setInitialDirectory(directory);
//            }
//            fc.getExtensionFilters().addAll(
//                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg", "*.bmp", "*.BMP", "*.PNG", "*.JPG", "*.JPEG"));
//
//            List<File> selectedFiles = fc.showOpenMultipleDialog(null);
//            if (selectedFiles != null) {
//                for (File file : selectedFiles) {
//                    if (file != null && !listView.getItems().contains(file.getName())) {
//                        nameToFile.put(file.getName(), file);
//                        Image image = new Image(file.toURI().toString());
//                        iv1.setImage(image);
//                        listView.getItems().add(file.getName());
//                    } else if (listView.getItems().contains(file.getName())) {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Uh-oh");
//                        alert.setContentText("This image is already in your list. QwQ");
//                        alert.showAndWait();
//                    }
//                }
//            }
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Uh-oh");
//            alert.setContentText("Wrong Directory!!! Please check and enter again.");
//            alert.showAndWait();
//        }
//    }

    /**
     * Select a Image file to check its detail.
     * @param event
     */
    public void MouseClickList(MouseEvent event) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            Image image = new Image(nameToFile.get(listView.getSelectionModel().getSelectedItem()).toURI().toString());
            iv1.setImage(image);
            path.setText(nameToFile.get(listView.getSelectionModel().getSelectedItem()).getAbsolutePath());
        }
    }

    /**
     * Clear all files in listView.
     * @param event
     */

    public void ButtonClear(ActionEvent event) {
        listView.getItems().subList(0, listView.getItems().size()).clear();
        iv1.setImage(null);
        path.setText("Path");
        nameToFile = new HashMap<>();
    }

    /**
     * Open ImageViewController to operate Image file user selected.
     * @param event
     * @throws IOException
     */
    public void ButtonOkAction(ActionEvent event) throws IOException {
//        ObservableList<String> pictures;
//        pictures = listView.getSelectionModel().getSelectedItems();
//        for (String pic : pictures) {
        Window window = ok.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("ImageView.fxml").openStream());
        ImageViewController imageView = loader.getController();
        imageView.passController(this);
        if (listView.getSelectionModel().getSelectedItem() != null) {
            if (nameToFile.get(listView.getSelectionModel().getSelectedItem()).exists()) {
                imageView.GetImage(nameToFile.get(listView.getSelectionModel().getSelectedItem()));
                primaryStage.setTitle("Image Viewer");
                primaryStage.setScene(new Scene(root, 600, 600));
                primaryStage.setX(window.getX() + DISTANCE);
                primaryStage.setY(window.getY() + DISTANCE);
                primaryStage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Uh-oh");
                alert.setContentText("This image does not exist anymore!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Uh-oh");
            alert.setContentText("Sorry, you need to select a image. QwQ");
            alert.showAndWait();
        }
//        }

    }

    /**
     * Open TagManager for this App.
     * @param event
     * @throws IOException
     */

    public void ButtonEditTags(ActionEvent event) throws IOException {
        Window window = editTags.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        Parent root = FXMLLoader.load(getClass().getResource("TagsView.fxml"));
        primaryStage.setTitle("Edit All tags");
        primaryStage.setScene(new Scene(root));
        primaryStage.showAndWait();
    }

    /**
     * Return the previous image for this curImage in listView.
     * @param curImage
     * @return
     * @throws IndexOutOfBoundsException
     */

    File getPrevImage(File curImage) throws IndexOutOfBoundsException {
        int curIndex = listView.getItems().indexOf(curImage.getName());
        if (curIndex != 0) {
            return nameToFile.get(listView.getItems().get(curIndex - 1));
        } else {
            throw new IndexOutOfBoundsException();
        }

    }

    /**
     * Return the next image for this curImage in listView.
     * @param curImage
     * @return
     * @throws IndexOutOfBoundsException
     */

    File getNextImage(File curImage) throws IndexOutOfBoundsException {
        int curIndex = listView.getItems().indexOf(curImage.getName());
        if (curIndex != (listView.getItems().size() - 1)) {
            return nameToFile.get(listView.getItems().get(curIndex + 1));
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Initialize Controller.
     * @param location
     * @param resources
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tagManager = new TagManager();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            imageManager = new ImageManager();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

}
