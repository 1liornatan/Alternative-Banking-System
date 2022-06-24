package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.loans.interest.exceptions.InvalidPercentException;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
import http.constants.Constants;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.investments.InvestmentsData;
import manager.investments.RequestDTO;
import manager.loans.LoansData;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name = "Loan Integration Servlet", urlPatterns = "/bank/loan/integration")
public class LoanIntegrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String usernameFromSession = SessionUtils.getUsername(request);
        ServletOutputStream outputStream = response.getOutputStream();
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            outputStream.print("Not logged in yet.");
        } else {
            Properties prop = new Properties();
            prop.load(request.getInputStream());
            String type = prop.getProperty(Constants.TYPE);
            String jsonRequest = prop.getProperty(Constants.DATA);

            switch(type) {
                case(Constants.INTEGRATION_REQUEST): {
                    RequestDTO requestDTO = Constants.GSON_INSTANCE.fromJson(jsonRequest,RequestDTO.class);
                    try {
                        LoansData integrationLoans = bankManager.getIntegrationLoans(requestDTO);
                        String jsonResponse = Constants.GSON_INSTANCE.toJson(integrationLoans);

                        try (PrintWriter out = response.getWriter()) {
                            out.print(jsonResponse);
                            out.flush();
                            response.setStatus(HttpServletResponse.SC_OK);
                        }
                        logServerMessage("Loan Integration Response (" + usernameFromSession + "): " + jsonRequest);
                    } catch (InvalidPercentException e) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        outputStream.print("Invalid parameters found!");
                    }
                    break;
                }
                case(Constants.INTEGRATION_SUBMIT): {
                    InvestmentsData investmentsData = Constants.GSON_INSTANCE.fromJson(jsonRequest, InvestmentsData.class);
                    try {
                        bankManager.setInvestment(investmentsData);
                        response.setStatus(HttpServletResponse.SC_OK);
                        outputStream.print("Submitted Successfully.");
                    } catch (Exception e) {
                        outputStream.print(e.getMessage());
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                    }
                    break;
                }
            }
        }
    }
    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
