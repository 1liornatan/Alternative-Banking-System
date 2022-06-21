package abs.servlets;

import http.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.loans.interest.exceptions.InvalidPercentException;
import bank.logic.manager.BankManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.investments.InvestmentsData;
import manager.investments.RequestDTO;
import manager.loans.LoansData;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Loan Integration Servlet", urlPatterns = "/bank/loan/integration")
public class LoanIntegrationServlet extends HttpServlet {

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
            String jsonRequest = request.getInputStream().toString();

            if(jsonRequest == null)
                return;

            RequestDTO requestDTO = Constants.GSON_INSTANCE.fromJson(jsonRequest, RequestDTO.class);
            try {
                LoansData integrationLoans = bankManager.getIntegrationLoans(requestDTO);
                String jsonResponse = Constants.GSON_INSTANCE.toJson(integrationLoans);

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
            String jsonRequest = request.getInputStream().toString();
            InvestmentsData investmentsData = Constants.GSON_INSTANCE.fromJson(jsonRequest, InvestmentsData.class);
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
