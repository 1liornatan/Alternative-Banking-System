package abs.servlets;

import abs.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.investments.PaymentsData;
import manager.loans.LoanData;
import manager.loans.LoansData;
import manager.transactions.TransactionsData;

import java.io.IOException;
import java.io.PrintWriter;
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

            String loanName = prop.getProperty(Constants.LOAN_DATA);
            String type = prop.getProperty(Constants.TYPE);

            if (loanName == null || type == null) {
                response.getOutputStream().print("Invalid Parameters!");
                return;
            }
            try {
                switch (type) {
                    case (Constants.CLOSE_LOAN_REQUEST): {
                        bankManager.closeLoan(loanName);
                        break;
                    }
                    case (Constants.PAY_CYCLE_REQUEST): {
                        bankManager.advanceCycle(loanName);
                        break;
                    }
                    case (Constants.PAY_DEBT_REQUEST): {
                        String amountRequest = prop.getProperty(Constants.AMOUNT);
                        int amount = Integer.parseInt(amountRequest);
                        if (amount <= 0) {
                            response.getOutputStream().print("Invalid Parameters!");
                            return;
                        }
                        bankManager.deriskLoanByAmount(loanName, amount);
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
