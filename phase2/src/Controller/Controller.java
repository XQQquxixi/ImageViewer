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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller implements Initializable{

    /**
     * All codes with Alert are learned from website: http://code.makery.ch/blog/javafx-dialogs-official/
     */

    private static final double DISTANCE = 50.0;

    @FXML
    private Button btn1;

    @FXML
    private ImageView iv1;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField initDirectory;

    @FXML
    private Button ok;

    @FXML
    private Button editTags;

    @FXML
    private Label path;

    static Map<String, File> nameToFile = new HashMap<>();

    private static ImageManager imageManager;

    private static TagManager tagManager;

    public void initData(String oldName, String newName) {
        int position = listView.getItems().indexOf(oldName);
        listView.getItems().set(position, newName);
    }

    public void Button1Action(ActionEvent event) throws Exception {
        DirectoryChooser dc = new DirectoryChooser();
        File dire = dc.showDialog(btn1.getScene().getWindow());
        if (dire != null) {
            Path dir = dire.toPath();
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
                if (file.exists() && !listView.getItems().contains(file.getName())) {
                    nameToFile.put(file.getName(), file);
                    Image image = new Image(file.toURI().toString());
                    Model.Image image1 = new Model.Image(file);
                    iv1.setImage(image);
                    listView.getItems().add(file.getName());
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

    public void MouseClickList(MouseEvent event) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            Image image = new Image(nameToFile.get(listView.getSelectionModel().getSelectedItem()).toURI().toString());
            iv1.setImage(image);
            path.setText(nameToFile.get(listView.getSelectionModel().getSelectedItem()).getAbsolutePath());
        }
    }

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
                primaryStage.setScene(new Scene(root, 600, 400));
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

    public void ButtonEditTags(ActionEvent event) throws IOException {
        Window window = editTags.getScene().getWindow();
        Stage primaryStage = new Stage();
        primaryStage.initOwner(window);
        Parent root = FXMLLoader.load(getClass().getResource("TagsView.fxml"));
        primaryStage.setTitle("Edit All Tags");
        primaryStage.setScene(new Scene(root));
        primaryStage.showAndWait();
    }

    File getPrevImage(File curImage) throws IndexOutOfBoundsException {
        int curIndex = listView.getItems().indexOf(curImage.getName());
        if (curIndex != 0) {
            return nameToFile.get(listView.getItems().get(curIndex - 1));
        } else {
            throw new IndexOutOfBoundsException();
        }

    }

    File getNextImage(File curImage) throws IndexOutOfBoundsException {
        int curIndex = listView.getItems().indexOf(curImage.getName());
        if (curIndex != (listView.getItems().size() - 1)) {
            return nameToFile.get(listView.getItems().get(curIndex + 1));
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

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
