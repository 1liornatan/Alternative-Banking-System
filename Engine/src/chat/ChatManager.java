package chat;

import chat.logic.ChatLogic;

public class ChatManager {
    private final ChatLogic chatLogic;

    public ChatManager() {
        chatLogic = new ChatLogic();
    }

    public ChatAndVersion getChat() {
        return chatLogic.getChatAndVersion();
    }

    public void addChatLine(String chatLine) {
        chatLogic.addLine(chatLine);
    }
}
