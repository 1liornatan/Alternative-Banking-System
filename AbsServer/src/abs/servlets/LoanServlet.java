package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import bank.users.UserManager;

import http.constants.Constants;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.loans.LoanData;
import manager.loans.LoansData;
import manager.loans.LoansWithVersion;


import java.io.IOException;

import java.util.Properties;

@WebServlet(name = "Loans Servlet", urlPatterns = "/bank/loan")
public class LoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);
        ServletOutputStream outputStream = response.getOutputStream();
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());
        LoansData data = null;
        int cVer, lVer;

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

                if (SessionUtils.isAdmin(request)) {
                    if (type.equals(Constants.ALL_LOANS)) {
                        try {
                            data = bankManager.getLoansData();
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
                            data = bankManager.getRequestedLoans(usernameFromSession);
                            break;
                        }
                        case (Constants.INVESTED_LOANS): {
                            data = bankManager.getInvestedLoans(usernameFromSession);
                            break;
                        }
                        case (Constants.UNFINISHED_LOANS): {
                            try {
                                data = bankManager.getUnFinishedLoans(usernameFromSession);
                            } catch (DataNotFoundException e) {
                                outputStream.print(e.getMessage());
                                response.setStatus(HttpServletResponse.SC_CONFLICT);
                            }
                            break;
                        }
                        case (Constants.ALL_LOANS): {
                            outputStream.print("Only admins are authorized for this request.");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }
                    }
                }

                if(data != null) {
                    cVer = bankManager.getCustomersVersion();
                    lVer = bankManager.getLoansVersion();

                    String jsonResponse = Constants.GSON_INSTANCE.toJson(new LoansWithVersion(data, cVer, lVer));
                    outputStream.print(jsonResponse);
                    outputStream.flush();
                    response.setStatus(HttpServletResponse.SC_OK);

                    //logServerMessage("Loans Request (" + usernameFromSession + "): " + jsonResponse);
                }
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String usernameFromSession = SessionUtils.getUsername(request);
        ServletOutputStream outputStream = response.getOutputStream();
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());
        ServletInputStream inputStream = request.getInputStream();

        if(usernameFromSession == null) {
            outputStream.print("You must login first.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else if(SessionUtils.isAdmin(request)) {
            outputStream.print("Loan Request is not for admins.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            try {
                Properties prop = new Properties();
                prop.load(inputStream);
                String jsonRequest = prop.getProperty(Constants.DATA);
                LoanData data = Constants.GSON_INSTANCE.fromJson(jsonRequest, LoanData.class);

                bankManager.createLoan(usernameFromSession, data);
                outputStream.print("Loan Created Successfully!");
                response.setStatus(HttpServletResponse.SC_OK);

            } catch (Exception e) {
                outputStream.print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }


    }

    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
