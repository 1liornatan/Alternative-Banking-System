package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import chat.ChatAndVersion;
import chat.ChatManager;
import http.constants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.customers.CustomersData;
import manager.customers.CustomersWithVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "Chat Servlet", urlPatterns = "/chat")
public class ChatServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(req);
        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());

        ServletOutputStream outputStream = resp.getOutputStream();
        if(usernameFromSession == null) {
            outputStream.print("You must login first.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else if(SessionUtils.isAdmin(req)) {
            outputStream.print("Only customers are authorized for this request.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            ChatAndVersion chatAndVersion = chatManager.getChat();

            String jsonResponse = Constants.GSON_INSTANCE.toJson(chatAndVersion);
            outputStream.print(jsonResponse);
            outputStream.flush();
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(req);
        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());
        BufferedReader reader = req.getReader();

        ServletOutputStream outputStream = resp.getOutputStream();

        if(usernameFromSession == null) {
            outputStream.print("You must login first.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else if(SessionUtils.isAdmin(req)) {
            outputStream.print("Only customers are authorized for this request.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            String formattedDate = myDateObj.format(myFormatObj);
            String preLine = "[" + formattedDate + "] " + usernameFromSession + ":" + " ";
            String textLine;

            while((textLine = reader.readLine()) != null) {
                chatManager.addChatLine(preLine + textLine);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

}
