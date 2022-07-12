package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import bank.users.UserManager;
import http.constants.Constants;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.time.TimeData;

import java.io.IOException;

@WebServlet(name = "Rewind Servlet", urlPatterns = "/bank/time")
public class TimeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        ServletOutputStream outputStream = response.getOutputStream();

        if (usernameFromSession == null) {
            outputStream.print("You must login first.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            TimeData data = bankManager.getTimeData();
            String jsonResponse = Constants.GSON_INSTANCE.toJson(data);
            outputStream.print(jsonResponse);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        ServletOutputStream outputStream = response.getOutputStream();
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            outputStream.print("Not logged in yet.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (!SessionUtils.isAdmin(request)) {
            outputStream.print("You are not an admin!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            try {
                bankManager.increaseYaz();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (DataNotFoundException | NonPositiveAmountException e) {
                outputStream.print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }
}
