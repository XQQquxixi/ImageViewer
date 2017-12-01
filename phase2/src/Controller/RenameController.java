package Controller;

import Model.Image;
import Model.ImageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;

public class RenameController{

    /**
     * Label for show current name for this image.
     */
    @FXML
    private Label curName;

    /**
     * TextField to input new Name.
     */
    @FXML
    private TextField inputName;

    /**
     * ChoiceBox for choose one of previous names.
     */

    @FXML
    private ChoiceBox<String> prevNames;

    /**
     * Button for confirm user's change.
     */

    @FXML
    private Button ok;

    /**
     * Button for cancel the work for rename.
     */

    @FXML
    private Button cancel;

    /**
     * ImageView to show picture of this image file.
     */

    @FXML
    private ImageView pic;

    /**
     * This image.
     */

    private Image image;

    /**
     * The Controller which connected to this RenameController.
     */

    private Controller controller;

    /**
     * The ImageViewController which connected to this RenameController.
     */

    private ImageViewController imageViewController;

    /**
     * This image file's old name.
     */

    private String oldName;

    /**
     * Load the information of this image.
     * @param image
     * The image we want to load.
     */

    void getImage(Image image) {
        this.image = image;
        ArrayList<String> listOfPrevNames = ImageManager.getPastName(image.getFile().getAbsolutePath());
        if (listOfPrevNames != null) {
            prevNames.getItems().addAll(listOfPrevNames);
        }
        int i = image.getName().indexOf(" @");
        if(i != (-1)) {
            inputName.setText(image.getName().substring(0, i));
        } else {
            inputName.setText(image.getName());
        }
        curName.setText(image.getName());
//        oldName = image.getName() + image.getExtension();
        for (String key: Controller.nameToFile.keySet()){
            if (Controller.nameToFile.get(key).equals(image.getFile())){
                oldName = key;
            }
        }
        pic.setImage(new javafx.scene.image.Image(image.getFile().toURI().toString()));
    }

    /**
     * Let Label curName show inputName.
     */

    public void typeName() {
        curName.setText(inputName.getText() + image.getTagPartOfName());
    }

    /**
     * Choose one of previous name, then Label will change by user's decision.
     */

    public void choosePrevName() {
        if (prevNames.getValue() != null) {
            curName.setText(prevNames.getValue());
        }
    }

    /**
     * Pass a Controller into this RenameController.controller.
     * @param controller
     * The Controller we want to pass into.
     */

    void passController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Pass a ImageViewController into this RenameController.imageViewController.
     * @param controller
     * The ImageViewController we want to pass into.
     */

    void passIMController(ImageViewController controller) {
        this.imageViewController = controller;
    }

    /**
     * Update all change to this image's name into Controller and ImageViewController and close this stage.
     * @throws IOException if saving serialized file fails.
     */

    public void buttonOkAction() throws IOException {
        //image.setName(curName.getText());
        try {
            if (!(curName.getText() + image.getExtension()).equals(oldName)) {
                image = ImageManager.renameImage(image.getFile().getAbsolutePath(), curName.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Oops");
                alert.setHeaderText(null);
                alert.setContentText("You didn't change anything!");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.restoreTag(image.getName());
        Controller.nameToFile.remove(oldName);
        Controller.nameToFile.put(image.getName() + image.getExtension(), image.getFile());
        controller.initData(oldName, image.getName() + image.getExtension());
        imageViewController.getImage(image.getFile());
        imageViewController.initData(image);
        FXMLLoader loader = new FXMLLoader();
        loader.load(getClass().getResource("ImageView.fxml").openStream());
        ImageViewController controller = loader.getController();
        controller.getImage(image.getFile());
        Window window = ok.getScene().getWindow();
        Stage stage = (Stage) window;
        stage.close();
    }

    /**
     * Close this Stage.
     */

    public void buttonCancelAction() {
        ((Stage) cancel.getScene().getWindow()).close();
    }
}
