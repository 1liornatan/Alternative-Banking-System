package client.screens.chat;

import chat.ChatAndVersion;
import http.constants.Constants;
import http.utils.HttpClientUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import okhttp3.*;
import users.UsersAndVersion;

import java.io.IOException;

public class ChatController {

    @FXML
    private ListView<String> usersList;

    @FXML
    private TextArea mainChatArea;

    @FXML
    private TextArea chatArea;
    private Thread updateThread;
    private int stopSignal;

    private final BooleanProperty shakeProperty;

    @FXML
    void sendChatButtonAction(ActionEvent event) {
        String text = chatArea.getText();
        addChatLine(text);
        chatArea.clear();
    }

    @FXML
    void initialize() {
        setupUpdateThread();
    }


    private void setupUpdateThread() {
        updateThread = new Thread(() -> {
            while(stopSignal != 1) {
                try {
                    updateData();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("check update: " + e.getMessage());
                }
            }
            stopSignal = 0;
        });
    }

    private void updateData() {
        updateChatLine();
        updateUsersList();
    }

    public void startUpdateThread() {
        updateThread.start();
    }

    public void stopUpdateThread() {
        stopSignal = 1;
    }

    private void updateChatLine() {
        new Thread( () -> {
            try {
                Request request = new Request.Builder()
                        .url(Constants.URL_CHAT)
                        .build();
                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();
                if(response.isSuccessful()) {
                    ChatAndVersion chatAndVersion = Constants.GSON_INSTANCE.fromJson(response.body().string(), ChatAndVersion.class);
                    int chatVersion = chatAndVersion.getChatVersion();

                    if(this.chatVersion == chatVersion)
                        return;

                    this.chatVersion = chatVersion;

                    Platform.runLater(() -> {
                        mainChatArea.setText(chatAndVersion.getChat());
                        checkShake(chatAndVersion.getChat());
                    });

                }
                response.close();

            } catch (IOException e) {
                System.out.println("Error Updating Chat: " + e.getMessage());
            }
        }).start();
    }

    private void checkShake(String chat) {
        if(chat.contains("Aviad"))
            shakeProperty.set(true);
    }

    private void updateUsersList() {
        new Thread( () -> {
            try {
                Request request = new Request.Builder()
                        .url(Constants.URL_USERS)
                        .build();
                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();
                if(response.isSuccessful()) {
                    UsersAndVersion usersAndVersion = Constants.GSON_INSTANCE.fromJson(response.body().string(), UsersAndVersion.class);
                    int usersVersion = usersAndVersion.getUsersVersion();

                    if(this.usersVersion == usersVersion)
                        return;

                    this.usersVersion = usersVersion;

                    Platform.runLater(() -> usersList.getItems().setAll(FXCollections.observableList(usersAndVersion.getUsersList())));

                }
                response.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void addChatLine(String textLine) {
        new Thread( () -> {
            try {
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = RequestBody.create(mediaType, textLine);
                Request request = new Request.Builder()
                        .url(Constants.URL_CHAT)
                        .method("POST", body)
                        .addHeader("Content-Type", "text/plain")
                        .build();
                Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute();
                response.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private int chatVersion;
    private int usersVersion;

    public ChatController() {
        chatVersion = 0;
        usersVersion = 0;
        stopSignal = 0;
        shakeProperty = new SimpleBooleanProperty(false);
    }

    public boolean isShakeProperty() {
        return shakeProperty.get();
    }

    public BooleanProperty shakePropertyProperty() {
        return shakeProperty;
    }
}
