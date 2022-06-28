package http.utils;

public class Props {
    private String data;

    public Props() {
        data = "";
    }

    public void add(String key, String value) {
        data += key;
        data += '=';
        data += value;
        data += '\n';
    }

    @Override
    public String toString() {
        return data;
    }
}
