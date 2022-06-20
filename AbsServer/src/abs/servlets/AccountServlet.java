package abs.servlets;

import abs.constants.Constants;
import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.customers.CustomerData;
import manager.customers.CustomersData;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Accounts Servlet", urlPatterns = "/bank/accounts")
public class AccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(req);
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        ServletOutputStream outputStream = resp.getOutputStream();
        if(usernameFromSession == null) {

        }
        else if(!SessionUtils.isAdmin(req)) {
            outputStream.print("Only admins are authorized for this request.");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                CustomersData customerDataList = bankManager.getCustomersData();
                String jsonResponse = Constants.GSON_INSTANCE.toJson(customerDataList);
                try (PrintWriter out = resp.getWriter()) {
                    out.print(jsonResponse);
                    out.flush();
                }
            } catch (DataNotFoundException e) {
                outputStream.print(e.getMessage());
            }
        }
    }
}
