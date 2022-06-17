package abs.servlets;

import abs.utils.ServletUtils;
import abs.utils.SessionUtils;
import bank.logic.impl.exceptions.DataNotFoundException;
import bank.logic.manager.BankManager;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import manager.customers.CustomerData;

import java.io.IOException;
import java.util.List;

public class AccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usernameFromSession = SessionUtils.getUsername(req);
        BankManager bankManager = ServletUtils.getBankManager(getServletContext());

        ServletOutputStream outputStream = resp.getOutputStream();
        if(usernameFromSession == null) {

        }
        else {
            try {
                List<CustomerData> customerDataList = bankManager.getCustomersData().getCustomers();
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(customerDataList);
                outputStream.print(jsonResponse);
                //TODO FINISH
            } catch (DataNotFoundException e) {
                outputStream.print(e.getMessage());
            }
        }
    }
}
