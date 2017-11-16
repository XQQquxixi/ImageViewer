package Model;

import java.io.*;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogObserver implements Observer {

  private Image image;
  private StringBuilder contents = new StringBuilder();
  private File file;
  private static final Logger logger =
          Logger.getLogger(Log.class.getName());
  private static final Handler consoleHandler = new ConsoleHandler();


  public Log(Image image) throws IOException, ClassNotFoundException {
    this.image = image;
    this.image.addObserver(this);
    logger.setLevel(Level.ALL);
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);

    //String absolutePath = "./logs";
    //String filePath = absolutePath + File.separator + image.getName() + ".txt";
    String filePath = "./" + image.getName() + ".txt";

    this.file = new File(filePath);
    if (file.exists()) {
      readFromFile(filePath);
    } else {
      file.createNewFile();
    }
  }

  public void readFromFile(String filePath) throws ClassNotFoundException {

    try {
      InputStream file = new FileInputStream(filePath);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);

      contents = (StringBuilder) input.readObject();
      input.close();
    } catch (IOException ex) {
      logger.log(Level.WARNING, "Cannot read from logs.");
    }
  }

  public void add(Image image, String nameOld, String time) {
    String newLog = nameOld + "," + image.getName() + "," + time;
    contents.append(newLog);
    contents.append(System.lineSeparator());

    // LogObserver the addition of a student.
    logger.log(Level.FINE, "Added a new renaming " + image.toString());
  }

  /**
   * Writes the students to file at filePath.
   *
   * @param filePath the file to write the records to
   */
  public void saveToFile(String filePath) throws IOException {

    OutputStream file = new FileOutputStream(filePath);
    OutputStream buffer = new BufferedOutputStream(file);
    ObjectOutput output = new ObjectOutputStream(buffer);

    // serialize the Map
    output.writeObject(contents);
    output.close();
  }


  public void update(Observable o, Object oldName) {
    Date now = new Date();
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String date = dt.format(now);

    // update log name
    String fileName = ((Image) o).getName() + ".txt";
    String newFilePath = "./" + fileName;
    File newFile = new File(newFilePath);
    if (file.renameTo(newFile)) {
      logger.log(Level.FINE, "log file updated successfully.");
    } else {
      logger.log(Level.WARNING, "failed to update log file");
    }
    file = newFile;

    add((Image) o, (String) oldName, date);
    try {
      saveToFile(newFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public String getLogs() {
    return contents.toString();
  }
}
