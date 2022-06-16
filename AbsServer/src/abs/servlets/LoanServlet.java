package abs.servlets;

import abs.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import bank.users.UserManager;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.loans.LoansData;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static abs.constants.Constants.USERNAME;

@WebServlet(name = "Loans Servlet", urlPatterns = "/bank/loan")
public class LoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.getOutputStream().print("Not logged in yet.");
        } else {
            //user is already logged in
            String type = request.getParameter(Constants.TYPE);
            Gson gson = new Gson();
            if(type == null)
            {
                response.getOutputStream().print("Invalid Parameters!");
                return;
            }

            String jsonResponse = null;

            switch(type) {
                case(Constants.REQUESTED_LOANS): {
                    LoansData requestedLoans = bankManager.getRequestedLoans(usernameFromSession.trim());
                    jsonResponse = gson.toJson(requestedLoans);
                    break;
                }
                case(Constants.INVESTED_LOANS): {
                    LoansData investedLoans = bankManager.getInvestedLoans(usernameFromSession.trim());
                    jsonResponse = gson.toJson(investedLoans);
                    break;
                }
                case(Constants.UNFINISHED_LOANS): {
                    LoansData unFinishedLoans = new LoansData();
                    try {
                        unFinishedLoans.setLoans(bankManager.getUnFinishedLoans(usernameFromSession.trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jsonResponse = gson.toJson(unFinishedLoans);
                    break;
                }
            }

            logServerMessage("Loans Request (" + usernameFromSession + "): " + jsonResponse);

            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse);
                out.flush();
            }
        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
