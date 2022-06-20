package abs.servlets;

import abs.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.manager.BankManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.transactions.TransactionsData;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name = "Transaction Servlet", urlPatterns = "/bank/transactions")
public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.getOutputStream().print("Not logged in yet.");

        }  else if (SessionUtils.isAdmin(request)) {
                response.getOutputStream().print("Only customers are authorized for this request.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
                TransactionsData transactions = bankManager.getTransactionsData(usernameFromSession);
                String jsonResponse = Constants.GSON_INSTANCE.toJson(transactions);

                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse);
                    out.flush();
                }
                logServerMessage("Transaction Response (" + usernameFromSession + "): " + jsonResponse);
        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
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
            int amount = Integer.parseInt(prop.getProperty(Constants.AMOUNT));
            String type = prop.getProperty(Constants.TYPE);

            if (amount <= 0 || type == null) {
                response.getOutputStream().print("Invalid Parameters!");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                try {
                    switch (type) {
                        case (Constants.TRANSACTION_WITHDRAW): {
                            bankManager.withdraw(usernameFromSession, amount, Constants.TRANSACTION_WITHDRAW);
                            break;
                        }
                        case (Constants.TRANSACTION_DEPOSIT): {
                            bankManager.deposit(usernameFromSession, amount, Constants.TRANSACTION_DEPOSIT);
                            break;
                        }
                    }
                } catch (Exception e) {
                    response.getOutputStream().print(e.getMessage());
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                }
            }
        }
    }


}
