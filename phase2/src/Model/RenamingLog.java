package Model;

import Utility.FileManager;

import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;
import java.io.*;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RenamingLog implements Observer, Serializable {
    private static StringBuilder logs = new StringBuilder();
    /* A Logger. */
    private static final Logger logger = Logger.getLogger(Image.class.getName());
    /* A ConsoleHandler. */
    private static final Handler consoleHandler = new ConsoleHandler();
    private static final String path = "./logs.ser";


    public RenamingLog() throws IOException, ClassNotFoundException {
        //this.image.addObserver(this);
        File file = new File(path);
        logger.setLevel(Level.OFF);
        consoleHandler.setLevel(Level.OFF);
        logger.addHandler(consoleHandler);
        if (file.exists()) {
            readFromFile();
        } else {
            if (file.createNewFile()) {
                logger.log(Level.FINE, path);
            }
        }
    }

    /**
     * Read from the file with path.
     *
     * @throws ClassNotFoundException if the class path is not updated
     */
    public static void readFromFile() throws ClassNotFoundException {
        //Code adapted from Paul's slides
        //http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
        FileManager fm = new FileManager();
        try {
            ObjectInput input = fm.readFromFile(path);

            // deserialize the Map
            logs = (StringBuilder) input.readObject();
            input.close();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Cannot read from logs.ser");
        }
    }

    public void add(Image image, String nameOld, String date) {
        String newLog = nameOld + ", " + image.getName() + image.getExtension() + ", " + date;
        logs.append(newLog);
        logs.append(System.lineSeparator());

        // LogObserver the addition of a student.
       logger.log(Level.FINE, "Added a new renaming " + image.toString());
    }

    /**
     * Save the changes to the file with filePath.
     *
     * @throws IOException if stream to file with filePath cannot be written or closed
     */
    private static void saveToFile() throws IOException {
        //Code adapted from Paul's slides
        //http://www.teach.cs.toronto.edu/~csc207h/fall/lectures.shtml
        FileManager fm = new FileManager();
        ObjectOutput output = fm.saveToFile(path);

        // serialize the Map
        output.writeObject(logs);
        output.close();
    }

    public void addObservable(Image i) {
        i.addObserver(this);
    }


    public void update(Observable o, Object oldName) {
        Date now = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = dt.format(now);

        // add new log entry
        if (!((Image) o).getName().equals(oldName)) {
            add((Image) o, (String) oldName, date);
            try {
                saveToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Return the String representation of this ImageManager.
     *
     * @return the String representation of this ImageManager
     */
    @Override
    public String toString() {
        return logs.toString();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        RenamingLog rl=  new RenamingLog();
        System.out.println(rl.toString());
    }


}

