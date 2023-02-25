package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.accounts.impl.exceptions.NonPositiveAmountException;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import http.constants.Constants;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "Time Servlet", urlPatterns = "/bank/time/rewind")
public class RewindServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        ServletOutputStream outputStream = response.getOutputStream();
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());
        ServletInputStream inputStream = request.getInputStream();

        if (usernameFromSession == null) { //user is not logged in yet
            outputStream.print("Not logged in yet.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (!SessionUtils.isAdmin(request)) {
            outputStream.print("You are not an admin!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            Properties prop = new Properties();
            prop.load(inputStream);
            String type = prop.getProperty(Constants.TYPE);


            switch(type) {
                case Constants.REWIND: {
                    int time = Integer.parseInt(prop.getProperty(Constants.AMOUNT));

                    if(time <= 0 || time > bankManager.getCurrentYaz()) {
                        outputStream.print(Constants.INVALID_TIME_MESSAGE);
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                    }
                    else {
                        bankManager.setRewind(time);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                    break;
                }
                case Constants.RESET: {
                    bankManager.resetRewind();
                    response.setStatus(HttpServletResponse.SC_OK);
                    break;
                }
                default: {
                    outputStream.print(Constants.NO_TYPE_MESSAGE);
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            }
        }
    }
}
