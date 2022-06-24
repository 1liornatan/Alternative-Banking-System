package abs.servlets;

import abs.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
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

import static abs.constants.Constants.GSON_INSTANCE;

@WebServlet(name = "Payment Servlet", urlPatterns = "/bank/payments")
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        ServletOutputStream outputStream = response.getOutputStream();
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            outputStream.print("Not logged in yet.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        } else if (SessionUtils.isAdmin(request)) {
            outputStream.print("Only customers are authorized for this request.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            //user is already logged in
            Properties prop = new Properties();
            prop.load(request.getInputStream());
            String loanId = prop.getProperty(Constants.LOAN_DATA);
            String type = prop.getProperty(Constants.TYPE);

            if (loanId == null || type == null) {
                outputStream.print("Invalid Parameters!");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
                            outputStream.print("Invalid Amount!");
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            return;
                        }
                        bankManager.deriskLoanByAmount(loanId, amount);
                        break;
                    }
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
    }


    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
