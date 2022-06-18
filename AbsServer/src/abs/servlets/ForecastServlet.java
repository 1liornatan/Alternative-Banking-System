package abs.servlets;

import abs.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.investments.InvestmentsSellData;
import manager.investments.PaymentsData;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Forecast Servlet", urlPatterns = "/bank/forecast")
public class ForecastServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.getOutputStream().print("Not logged in yet.");
        } else if (SessionUtils.isAdmin(request)) {
            response.getOutputStream().print("Only customers are authorized for this request.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            String type = request.getParameter(Constants.TYPE);
            Gson gson = new Gson();
            if (type == null) {
                response.getOutputStream().print("Invalid Parameters!");
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
            String jsonResponse = gson.toJson(paymentsData);

            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
                logServerMessage("Loan Trade Response (" + usernameFromSession + "): " + jsonResponse);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getOutputStream().print("Invalid parameters found!");
            }
        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
