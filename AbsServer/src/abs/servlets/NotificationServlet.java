package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
import http.constants.Constants;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.messages.NotificationsData;
import manager.transactions.TransactionsData;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Notification Servlet", urlPatterns = "/bank/notifications")
public class NotificationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);
        ServletOutputStream outputStream = response.getOutputStream();
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            outputStream.print("Not logged in yet.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        } else if (SessionUtils.isAdmin(request)) {
            outputStream.print("Only customers are authorized for this request.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            NotificationsData notifications = null;
            try {
                notifications = bankManager.getNotificationsData(usernameFromSession);
            } catch (Exception e) {
                outputStream.print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
            String jsonResponse = Constants.GSON_INSTANCE.toJson(notifications);

            outputStream.print(jsonResponse);
            outputStream.flush();
            response.setStatus(HttpServletResponse.SC_OK);
            logServerMessage("Notifications Response (" + usernameFromSession + "): " + jsonResponse);
        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
