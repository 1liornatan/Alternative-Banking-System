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
import java.util.Properties;

import static abs.constants.Constants.GSON_INSTANCE;
import static abs.constants.Constants.USERNAME;

@WebServlet(name = "Loans Servlet", urlPatterns = "/bank/loan")
public class LoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);

        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.getOutputStream().print("Not logged in yet.");
        } else {
            //user is already logged in
            String type = request.getParameter(Constants.TYPE);
            if(type == null)
            {
                response.getOutputStream().print("Invalid Parameters!");
                return;
            }

            String jsonResponse = null;

            if(SessionUtils.isAdmin(request)) {
                if(type.equals(Constants.ALL_LOANS)) {
                    try {
                        LoansData allLoans = bankManager.getLoansData();
                        jsonResponse = Constants.GSON_INSTANCE.toJson(allLoans);
                    } catch (DataNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    response.getOutputStream().print("Only customers are authorized for this request.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }

            }
            else {
                switch (type) {
                    case (Constants.REQUESTED_LOANS): {
                        LoansData requestedLoans = bankManager.getRequestedLoans(usernameFromSession);
                        jsonResponse = Constants.GSON_INSTANCE.toJson(requestedLoans);
                        break;
                    }
                    case (Constants.INVESTED_LOANS): {
                        LoansData investedLoans = bankManager.getInvestedLoans(usernameFromSession);
                        jsonResponse = Constants.GSON_INSTANCE.toJson(investedLoans);
                        break;
                    }
                    case (Constants.UNFINISHED_LOANS): {
                        LoansData unFinishedLoans = new LoansData();
                        try {
                            unFinishedLoans.setLoans(bankManager.getUnFinishedLoans(usernameFromSession));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        jsonResponse = Constants.GSON_INSTANCE.toJson(unFinishedLoans);
                        break;
                    }
                    case (Constants.ALL_LOANS): {
                        response.getOutputStream().print("Only admins are authorized for this request.");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
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
