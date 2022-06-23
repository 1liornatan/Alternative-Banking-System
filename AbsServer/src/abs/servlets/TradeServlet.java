package abs.servlets;

import http.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.investments.InvestmentData;
import manager.investments.InvestmentsSellData;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name = "Trade Servlet", urlPatterns = "/bank/trade")
public class TradeServlet extends HttpServlet {

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
            String type = request.getParameter(Constants.TYPE);
            if(type == null)
            {
                response.getOutputStream().print("Invalid Parameters!");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }

            String jsonResponse = null;

            switch(type) {
                case(Constants.INVESTMENTS_FOR_SELL): {
                    InvestmentsSellData investmentsForSell = bankManager.getInvestmentsForSell(usernameFromSession);
                    jsonResponse = Constants.GSON_INSTANCE.toJson(investmentsForSell);
                    break;
                }
                case(Constants.LISTED_INVESTMENTS): {
                    try {
                        InvestmentsSellData listedInvestments = bankManager.getCustomerInvestments(usernameFromSession);
                        jsonResponse = Constants.GSON_INSTANCE.toJson(listedInvestments);
                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.getOutputStream().print("Invalid parameters found!");
                    }
                    break;
                }
            }
                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse);
                    out.flush();
                    logServerMessage("Loan Trade Response (" + usernameFromSession + "): " + jsonResponse);
                } catch (Exception e) {
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

        }  else if (SessionUtils.isAdmin(request)) {
        response.getOutputStream().print("Only customers are authorized for this request.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        } else {
            Properties prop = new Properties();
            prop.load(request.getInputStream());
            String jsonRequest = prop.getProperty(Constants.INVESTMENT_DATA);
            String type = prop.getProperty(Constants.TYPE);
            InvestmentData investmentData = Constants.GSON_INSTANCE.fromJson(jsonRequest, InvestmentData.class);

            if(type == null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getOutputStream().print("Invalid parameters found!");
            }
            else {
                switch (type) {
                    case (Constants.BUY_INVESTMENT_REQUEST): {
                        try {
                            bankManager.investmentTrade(investmentData);
                            response.setStatus(HttpServletResponse.SC_OK);
                        } catch (Exception e) {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            response.getOutputStream().print(e.getMessage());
                        }
                        break;
                    }
                    case (Constants.LIST_INVESTMENT_REQUEST): {
                        try {
                            bankManager.listInvestment(investmentData);
                            response.setStatus(HttpServletResponse.SC_OK);
                        } catch (DataNotFoundException e) {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            response.getOutputStream().print(e.getMessage());
                        }
                        break;
                    }
                    case (Constants.UNLIST_INVESTMENT_REQUEST): {
                        try {
                            bankManager.unlistInvestment(investmentData);
                            response.setStatus(HttpServletResponse.SC_OK);
                        } catch (DataNotFoundException e) {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            response.getOutputStream().print(e.getMessage());
                        }
                        break;
                    }
                }
            }
        }
    }
    private void logServerMessage(String message) {
        System.out.println(message);
    }
}
