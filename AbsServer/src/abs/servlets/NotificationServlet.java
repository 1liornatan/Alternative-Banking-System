package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
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

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.getOutputStream().print("Not logged in yet.");
        } else {
            //user is already logged in
            Gson gson = new Gson();
            NotificationsData notifications = null;
            try {
                notifications = bankManager.getNotificationsData(usernameFromSession);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonResponse = gson.toJson(notifications);

            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
            logServerMessage("Notifications Response (" + usernameFromSession + "): " + jsonResponse);
        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
