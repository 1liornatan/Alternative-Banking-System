package files.saver;

public interface Saver {
    void saveToFile(String path);

    void loadFile(String path);
}
