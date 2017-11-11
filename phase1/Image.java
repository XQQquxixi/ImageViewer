import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Image extends Observable {
    private String name;
    private File file;     // Since Image is essentially a file.
    private ArrayList<Observer> observers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        String absolutePath = file.getAbsolutePath();
        String path = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
        File newFile = new File(path + File.separator + name);
        if(file.renameTo(newFile)){
            System.out.println("File rename success");;
        }else{
            System.out.println("File rename failed");
        }
        this.name = name;
        //file = newFile;
        setChanged();
        notifyObservers(oldName);
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers(String oldName) {
        for (Observer o: observers) {
            o.update(this, oldName);
        }
    }

    public String toString() {
        return name;
    }


}
