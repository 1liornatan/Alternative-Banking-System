package abs.servlets;

import bank.logic.impl.exceptions.DataNotFoundException;
import http.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.manager.BankManager;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.investments.PaymentsData;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name = "Forecast Servlet", urlPatterns = "/bank/forecast")
public class ForecastServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        ServletOutputStream outputStream = response.getOutputStream();
        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            outputStream.print("Not logged in yet.");
        } else if (!SessionUtils.isAdmin(request)) {
            try {
                PaymentsData data = bankManager.getPaymentsData(usernameFromSession);
                String jsonResponse = Constants.GSON_INSTANCE.toJson(data);

                outputStream.print(jsonResponse);

            } catch (Exception e) {
                outputStream.print("Data not found.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        } else {

            String type = request.getParameter(Constants.TYPE);
            if (type == null) {
                outputStream.print("Invalid Parameters!");
                return;
            }

            PaymentsData paymentsData = null;
            switch (type) {
                case (Constants.ALL_LOANS): {
                    paymentsData = bankManager.getAllLoansData();
                    break;
                }
                case (Constants.ALL_TRANSACTIONS): {
                    paymentsData = bankManager.getAllTransactionsData();
                    break;
                }
                case (Constants.ALL_CUSTOMERS): {
                    paymentsData = bankManager.getAllCustomersData();
                    break;
                }
            }
            String jsonResponse = Constants.GSON_INSTANCE.toJson(paymentsData);

            outputStream.print(jsonResponse);
            outputStream.flush();
            logServerMessage("Loan Trade Response (" + usernameFromSession + "): " + jsonResponse);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
