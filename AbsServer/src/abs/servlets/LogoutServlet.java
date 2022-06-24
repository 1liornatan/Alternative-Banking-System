package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.users.UserManager;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/bank/logout"})
public class LogoutServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        ServletOutputStream outputStream = response.getOutputStream();

        if (usernameFromSession != null) {
            System.out.println("Clearing session for " + usernameFromSession);
            if(SessionUtils.isAdmin(request))
                userManager.removeAdmin();

            userManager.removeUser(usernameFromSession);
            SessionUtils.clearSession(request);

            response.setStatus(HttpServletResponse.SC_OK);
            outputStream.print("Logged out successfully!");
        }
    }

}