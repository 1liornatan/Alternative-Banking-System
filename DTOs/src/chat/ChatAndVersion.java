package chat;

public class ChatAndVersion {
    String chat;
    int chatVersion;

    public ChatAndVersion(String chat, int chatVersion) {
        this.chat = chat;
        this.chatVersion = chatVersion;
    }

    public String getChat() {
        return chat;
    }

    public int getChatVersion() {
        return chatVersion;
    }
}
