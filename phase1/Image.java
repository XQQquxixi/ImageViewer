import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class Image extends Observable implements Serializable {

  /**
   * the name part of file's name.
   */
  private String name;

  /**
   * The full name of file.
   */
  private String fullName;

  /**
   * this image file.
   */
  private File file;

  /**
   * An ArrayList of all the tags of this image.
   */
  private ArrayList<String> currentTags;


  /**
   * The observers.
   */
  private ArrayList<Observer> observers = new ArrayList<>();

  public Image(File file) {
    this.file = file;
    currentTags = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    String oldName = this.name;
    String absolutePath = file.getAbsolutePath();
    String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    File newFile = new File(path + File.separator + name);
    if (file.renameTo(newFile)) {
      System.out.println("File rename success");
    } else {
      System.out.println("File rename failed");
    }
    this.name = name;
    file = newFile;
    setChanged();
    notifyObservers(oldName);
  }


  public void notifyObservers(String oldName) {
    for (Observer o : observers) {
      o.update(this, oldName);
    }
  }

  public String toString() {
    return name;
  }


}

// how to add a tag to an image.
// if adding tags happens here, every time you instantiate Image, have to call tagmanager.
// if adding tags happens in tagmanager, have to pass Image as a param, image should implement correspondingn method that will eventually
// call setname.

