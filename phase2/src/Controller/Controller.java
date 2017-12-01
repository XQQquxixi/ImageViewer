package Controller;

import Model.ImageManager;
import Model.TagManager;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller implements Initializable{

    /*
     * All codes with Alert are learned from website: http://code.makery.ch/blog/javafx-dialogs-official/
     */


    /**
     * The distance between Controller scene and ImageViewController.
     */

    private static final double DISTANCE = 50.0;

    /**
     * Button for onAction viewHistory
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

    /**
     * TextField for search tag???????????.
     */

    @FXML
    private TextField tags;

    /**
     * HashMap for store and record name and image File.
     */

    static Map<String, File> nameToFile = new HashMap<>();

    /**
     * Static ImageManager.
     */

    static ImageManager imageManager;

    /**
     * Static TagManager.
     */

    static TagManager tagManager;

    /**
     * Update new name of one file in listViews when user change one Image's name in other Scene.
     * @param oldName
     * old name for image which is renamed.
     * @param newName
     * new name for image which is renamed.
     */

    void initData(String oldName, String newName) {
        int position = listView.getItems().indexOf(oldName);
        listView.getItems().set(position, newName);
        path.setText(nameToFile.get(newName).getAbsolutePath());
    }

    /**
     * Make listview display a list of images with the tags that the user typed.
     * User can type nothing and go back to original set of images.
     * @param event
     * the key on keyboard that user hit.
     */

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
            }
        }
    }

    /**
     * Open a Alert to view all logs for all files.
     * @throws IOException
     * If log doesn't exist.
     */

    public void viewHistory() throws IOException{
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
     * Open a directoryChooser to choose a directory. Then all image files in this directory will show in listView and
     * user can view and operate to this Image.
     *
     * If there is more than one image file with same name, their name will include parent directory path to show their
     * differences. But notice, if the file is in Main directory we choose, its name will stay constant.
     * @throws Exception If it can't find any files through a wrong path.
     */

    public void button1Action() throws Exception {
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
                            nameToFile.put(parentFile.getAbsolutePath().substring(i+1), parentFile);
                            listView.getItems().add(parentFile.getAbsolutePath().substring(i+1));
                        }
                    }
                    nameToFile.put(name, file);
                    Image image = new Image(file.toURI().toString());
//                    Model.Image image1 = new Model.Image(file);
                    iv1.setImage(image);
                    listView.getItems().add(name);
                    ImageManager.checkKey(file);
                }
            }
        }
    }

    /**
     * Select a Image file to view its detail.
     */
    public void mouseClickList() {
        String imageName = listView.getSelectionModel().getSelectedItem();
        if (imageName != null) {
            Image image = new Image(nameToFile.get(imageName).toURI().toString());
            iv1.setImage(image);
            path.setText(nameToFile.get(imageName).getAbsolutePath());
        }
    }

    /**
     * Clear all files in listView.
     */

    public void buttonClear() {
        listView.getItems().subList(0, listView.getItems().size()).clear();
        iv1.setImage(null);
        path.setText("Path");
        nameToFile = new HashMap<>();
    }

    /**
     * Open ImageViewController to operate Image file user selected.
     * @throws IOException If loader can't find resource to load.
     */
    public void buttonOkAction() throws IOException {
        Window window = ok.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        FXMLLoader loader = new FXMLLoader();
        Pane root = loader.load(getClass().getResource("ImageView.fxml").openStream());
        ImageViewController imageView = loader.getController();
        imageView.passController(this);
        String imageName = listView.getSelectionModel().getSelectedItem();
        if (imageName != null) {
            if (nameToFile.get(imageName).exists()) {
                imageView.getImage(nameToFile.get(imageName));
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

    }

    /**
     * Open TagManager for this App. Then, user can delete or add tags into Tags pool.
     * @throws IOException If loader can't find resource to load.
     */

    public void buttonEditTags() throws IOException {
        Window window = editTags.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("TagsView.fxml").openStream());
        TagsView tagsView = loader.getController();
        tagsView.passController(this);
        primaryStage.setTitle("Edit All tags");
        primaryStage.setScene(new Scene(root));
        primaryStage.showAndWait();
    }

    /**
     * Return the previous image for this curImage in listView.
     * @param curImage The image file user selected now.
     * @return Last image file in listView.
     * @throws IndexOutOfBoundsException If curImage is not in listView, there will be IndexOutOfBoundsException.
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
     * The image file user selected now.
     * @return Next image file in listView.
     * @throws IndexOutOfBoundsException
     * If curImage is not in listView, there will be IndexOutOfBoundsException.
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
     * @param location URL for the Controller
     * @param resources ResourceBundle for the Controller
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tagManager = new TagManager();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        try {
            imageManager = new ImageManager();
        }
        catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

}
