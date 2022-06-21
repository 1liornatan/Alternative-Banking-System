package abs.servlets;

import http.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.manager.BankManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Properties;

@WebServlet(name = "Payment Servlet", urlPatterns = "/bank/payments")
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.getOutputStream().print("Not logged in yet.");

        } else if (SessionUtils.isAdmin(request)) {
            response.getOutputStream().print("Only customers are authorized for this request.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            //user is already logged in
            Properties prop = new Properties();
            prop.load(request.getInputStream());
            String loanId = prop.getProperty(Constants.LOAN_DATA);
            String type = prop.getProperty(Constants.TYPE);

            if (loanId == null || type == null) {
                response.getOutputStream().print("Invalid Parameters!");
                return;
            }
            try {
                switch (type) {
                    case (Constants.CLOSE_LOAN_REQUEST): {
                        bankManager.closeLoan(loanId);
                        break;
                    }
                    case (Constants.PAY_CYCLE_REQUEST): {
                        bankManager.advanceCycle(loanId);
                        break;
                    }
                    case (Constants.PAY_DEBT_REQUEST): {
                        String amountRequest = prop.getProperty(Constants.AMOUNT);
                        int amount = Integer.parseInt(amountRequest);
                        if (amount <= 0) {
                            response.getOutputStream().print("Invalid Amount!");
                            return;
                        }
                        bankManager.deriskLoanByAmount(loanId, amount);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
