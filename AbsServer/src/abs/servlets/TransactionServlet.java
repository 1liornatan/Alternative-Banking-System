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
import manager.transactions.TransactionsData;
import java.io.IOException;
import java.io.PrintWriter;

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
            Gson gson = new Gson();
                TransactionsData transactions = bankManager.getTransactionsData(usernameFromSession);
                String jsonResponse = gson.toJson(transactions);

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
            int amount = ServletUtils.getIntParameter(request,Constants.AMOUNT);
            String type = request.getParameter(Constants.TYPE);

            if(amount <= 0 || type == null) {
                response.getOutputStream().print("Invalid Parameters!");
                return;
            }
            try {
               switch(type) {
                    case(Constants.TRANSACTION_WITHDRAW): {
                           bankManager.withdraw(usernameFromSession.trim(),amount,Constants.TRANSACTION_WITHDRAW);
                           break;
                    }
                     case(Constants.TRANSACTION_DEPOSIT): {
                          bankManager.charge(usernameFromSession.trim(),amount,Constants.TRANSACTION_DEPOSIT);
                          break;
                     }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
