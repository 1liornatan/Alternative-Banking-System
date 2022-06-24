package abs.servlets;

import http.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.info.ClientInfoData;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Information Update Servlet", urlPatterns = "/bank/info")
public class InfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        PrintWriter writer = response.getWriter();
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("User is not logged in.");

        } else {
            try {
                ClientInfoData clientInfo = bankManager.getClientInfo(usernameFromSession);
                String jsonResponse = Constants.GSON_INSTANCE.toJson(clientInfo, ClientInfoData.class);
                writer.print(jsonResponse);
                response.setStatus(HttpServletResponse.SC_OK);

            } catch (DataNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                writer.print(e.getMessage());
            }
        }
    }

}
