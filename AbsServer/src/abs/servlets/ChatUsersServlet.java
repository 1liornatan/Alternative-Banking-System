package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.users.UserManager;
import chat.ChatAndVersion;
import chat.ChatManager;
import http.constants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UsersAndVersion;

import java.io.IOException;

@WebServlet(name = "Users List Servlet", urlPatterns = "/users")
public class ChatUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(req);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        ServletOutputStream outputStream = resp.getOutputStream();
        if (usernameFromSession == null) {
            outputStream.print("You must login first.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            UsersAndVersion usersAndVersion = userManager.getUsersAndVersion();

            String jsonResponse = Constants.GSON_INSTANCE.toJson(usersAndVersion);
            outputStream.print(jsonResponse);
            outputStream.flush();
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
