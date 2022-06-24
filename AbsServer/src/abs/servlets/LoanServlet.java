package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import bank.users.UserManager;
import com.google.gson.Gson;
import http.constants.Constants;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.loans.LoansData;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name = "Loans Servlet", urlPatterns = "/bank/loan")
public class LoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);
        ServletOutputStream outputStream = response.getOutputStream();
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            outputStream.print("Not logged in yet.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            //user is already logged in
            String type = request.getParameter(Constants.TYPE);
            if (type == null) {
                outputStream.print("Invalid Parameters!");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                String jsonResponse = null;

                if (SessionUtils.isAdmin(request)) {
                    if (type.equals(Constants.ALL_LOANS)) {
                        try {
                            LoansData allLoans = bankManager.getLoansData();
                            jsonResponse = Constants.GSON_INSTANCE.toJson(allLoans);
                        } catch (DataNotFoundException e) {
                            e.printStackTrace();
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                        }
                    } else {
                        outputStream.print("Only customers are authorized for this request.");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }

                } else {
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
                                response.setStatus(HttpServletResponse.SC_CONFLICT);
                            }
                            jsonResponse = Constants.GSON_INSTANCE.toJson(unFinishedLoans);
                            break;
                        }
                        case (Constants.ALL_LOANS): {
                            outputStream.print("Only admins are authorized for this request.");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }
                    }
                }

                logServerMessage("Loans Request (" + usernameFromSession + "): " + jsonResponse);

                outputStream.print(jsonResponse);
                outputStream.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
