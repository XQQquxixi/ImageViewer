import java.io.*;
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

  public Image(File file) {
    this.name = file.getName();
    this.file = file;
    currentTags = new ArrayList<>();
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
   if (currentTags.contains(tag)){
   System.out.println("This tag is already in here!");
   } else {
   currentTags.add(tag);
   this.setName(this.name + " @" + tag);
   if (!TagManager.tagList.contains(tag)){
   TagManager.addTag(tag);
   }
   }
   }

   public void deleteTag(String tag){
   if (currentTags.contains(tag)){
   currentTags.remove(tag);
   int index = name.lastIndexOf(" @"+tag);
   String newName = name.substring(0, index) +
   name.substring(index + (" @" + tag).length()+1, name.length());
   this.setName(newName);
   } else {
   System.out.println("No such tag in this photo!");
   }
   }


}

// how to add a tag to an image.
// if adding tags happens here, every time you instantiate Image, have to call tagmanager.
// if adding tags happens in tagmanager, have to pass Image as a param, image should implement correspondingn method that will eventually
// call setname.

