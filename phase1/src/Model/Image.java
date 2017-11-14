package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class Image extends Observable implements Serializable {

  /**
   * the name of file.
   */
  private String name;

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

  public Image(File file) throws IOException, ClassNotFoundException {
    this.file = file;
    currentTags = new ArrayList<>();
    this.name = file.getName();

    if (file.getName().contains("@")) {
      // Modified before
      String path = file.getAbsolutePath();
      File ser =new File(
          path.substring(0, path.lastIndexOf(File.separator) + 1)+this.name+".ser");
      if (ser.exists() && ser.isFile()) {
        // There is a .ser file
        Image reload = readFromFile(ser);
        this.name = reload.name;
        this.currentTags = reload.currentTags;
      } else {
        // There isn't one but the file name retains info of tags
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(file.getName().split("@")));
        this.name = tags.get(0).replaceAll("\\s", "");
        currentTags.addAll(tags.subList(1, tags.size()));
        writeToFile();
      }
    }
  }

  private void writeToFile() {
    try {
      ObjectOutputStream serOut =
          new ObjectOutputStream(new FileOutputStream("name" + ".ser"));
      serOut.writeObject(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Image readFromFile(File ser) throws IOException, ClassNotFoundException {
    ObjectInputStream serIn = new ObjectInputStream(new FileInputStream(ser));
    Image result = (Image) serIn.readObject();
    serIn.close();
    return result;
  }


  public String getName() {
    return name;
  }

  public File getFile() {
    return file;
  }

  public void setName(String name) {
    String oldName = this.name;
    String absolutePath = file.getAbsolutePath();
    String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    File newFile = new File(path + File.separator + name);
    int i = 1;
    while (newFile.exists()) {
      path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
      newFile = new File(path + File.separator + name + Integer.toString(i++));
    }
    if (file.renameTo(newFile)) {
      System.out.println("File rename success");
    } else {
      System.out.println("File rename failed");
    }
    this.name = name;
    this.file = newFile;
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

  public void addTag(String tag) throws IOException {
    if (currentTags.contains(tag)) {
      System.out.println("This tag is already in here!");
    } else {
      currentTags.add(tag);
      this.setName(this.name + " @" + tag);
      if (!TagManager.tagList.contains(tag)) {
        TagManager.addTag(tag);
      }
    }
  }

  public void deleteTag(String tag) {
    if (currentTags.contains(tag)) {
      currentTags.remove(tag);
      int index = name.lastIndexOf(" @" + tag);
      String newName = name.substring(0, index) +
          name.substring(index + (" @" + tag).length() + 1, name.length());
      this.setName(newName);
    } else {
      System.out.println("No such tag in this photo!");
    }
  }

  public void move(String newPath) throws IOException {
    Path oldPath = file.toPath();
    Files.move(oldPath, Paths.get(newPath));
    file = new File(newPath);
    String serPath =
        oldPath.toString().substring(0, oldPath.toString().lastIndexOf(File.separator)+1)
            + name + ".ser";
    Files.delete(Paths.get(serPath));
    notifyObservers(name);
  }


}

// how to add a tag to an image.
// if adding tags happens here, every time you instantiate Image, have to call tagmanager.
// if adding tags happens in tagmanager, have to pass Image as a param, image should implement correspondingn method that will eventually
// call setname.

