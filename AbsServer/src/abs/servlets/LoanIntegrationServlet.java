package abs.servlets;

import abs.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.loans.interest.exceptions.InvalidPercentException;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.getOutputStream().print("Not logged in yet.");
        } else {
            //user is already logged in
            Properties prop = new Properties();
            prop.load(request.getInputStream());
            Gson gson = new Gson();
            String jsonRequest = prop.getProperty(Constants.INTEGRATION_REQUEST);

            if(jsonRequest == null)
                return;

            RequestDTO requestDTO = gson.fromJson(jsonRequest, RequestDTO.class);
            try {
                LoansData integrationLoans = bankManager.getIntegrationLoans(requestDTO);
                String jsonResponse = gson.toJson(integrationLoans);

                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse);
                    out.flush();
                }
                logServerMessage("Loan Integration Response (" + usernameFromSession + "): " + jsonRequest);
            } catch (InvalidPercentException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getOutputStream().print("Invalid parameters found!");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.getOutputStream().print("Not logged in yet.");
        } else {
            //user is already logged in
            Properties prop = new Properties();
            prop.load(request.getInputStream());
            Gson gson = new Gson();
            String jsonRequest = prop.getProperty(Constants.INTEGRATION_SUBMIT);
            InvestmentsData investmentsData = gson.fromJson(jsonRequest, InvestmentsData.class);
            try {
                bankManager.setInvestment(investmentsData);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
            }
        }
    }
    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
