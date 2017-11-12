import java.io.*;
import java.util.ArrayList;

public class TagManager {
    private ArrayList<String> tagList;
    private static final String filePath =  "./tagManager.ser";

    public TagManager() throws ClassNotFoundException, IOException {
        tagList = new ArrayList<>();
        File file = new File(filePath);
        if (file.exists()) {
            readFromFile(filePath);
        } else {
            file.createNewFile();
        }
    }

    public ArrayList<String> getTagList() {
        return new ArrayList<>(tagList);
    }

    public void addTag(String tag) throws IOException {
        if(!tagList.contains(tag)) {
            tagList.add(tag);
            saveToFile(filePath);
        } else {
            System.out.println("This tag already existed.");
        }

    }

    public void removeTag(String tag) throws IOException {
        if(tagList.contains(tag)) {
            tagList.remove(tag);
            saveToFile(filePath);
        } else {
            System.out.println("No such tag.");
        }

    }

    public void saveToFile(String filePath) throws IOException {

        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        // serialize the Map
        output.writeObject(tagList);
        output.close();
    }

    public void readFromFile(String path) throws ClassNotFoundException {

        try {
            InputStream file = new FileInputStream(path);
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);


            tagList = (ArrayList<String>) input.readObject();
            input.close();
        } catch (IOException ex) {
            System.out.println("Cannot find the file.");
        }
    }
}
