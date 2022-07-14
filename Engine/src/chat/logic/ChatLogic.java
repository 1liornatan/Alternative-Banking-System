package chat.logic;

import chat.ChatAndVersion;

public class ChatLogic {
    private String chatText;
    private int chatVersion;

    public ChatLogic() {
        chatText = "";
        chatVersion = 0;
    }

    public void addLine(String line) {
        chatText += "\n" + line;
        chatVersion++;
    }

    public ChatAndVersion getChatAndVersion() {
        return new ChatAndVersion(chatText, chatVersion);
    }

    public void clearChat() {
        chatText = "";
        chatVersion++;
    }
}
