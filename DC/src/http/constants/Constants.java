package http.constants;

import com.google.gson.Gson;

public class Constants {
    public static final String USERNAME = "username";
    public static final String USER_NAME_ERROR = "username_error";
    public static final String REQUESTED_LOANS = "requested_loans";
    public static final String INVESTED_LOANS = "invested_loans";
    public static final String UNFINISHED_LOANS = "unfinished_loans";
    public static final String ALL_LOANS = "all_loans";
    public static final String ALL_TRANSACTIONS = "all_transactions";
    public static final String ALL_CUSTOMERS = "all_customers";
    public static final String TYPE = "type";

    public static final int INT_PARAMETER_ERROR = Integer.MIN_VALUE;
    public static final String INTEGRATION_REQUEST = "request";
    public static final String INTEGRATION_SUBMIT = "submit";
    public static final String LOAN_DATA = "loan_data";
    public static final String CLOSE_LOAN_REQUEST = "close_loan_request";
    public static final String PAY_DEBT_REQUEST = "pay_debt_request";
    public static final String PAY_CYCLE_REQUEST = "pay_cycle_request";

    public static final String INVESTMENT_DATA = "investment_data";
    public static final String LIST_INVESTMENT_REQUEST = "list_investment";
    public static final String UNLIST_INVESTMENT_REQUEST = "unlist_investment";
    public static final String INVESTMENTS_FOR_SELL = "investments_for_sell";
    public static final String LISTED_INVESTMENTS = "listed_investments";
    public static final String BUY_INVESTMENT_REQUEST = "buy_investment";
    public static final String AMOUNT = "amount";
    public static final String TRANSACTION_WITHDRAW = "withdraw";
    public static final String TRANSACTION_DEPOSIT = "deposit";
    public static final String IS_ADMIN = "is_admin";
    public static final Gson GSON_INSTANCE = new Gson();

    public static final String URL_BASE = "http://localhost:8080/Abs";
    public static final String URL_TRADE = URL_BASE + "/bank/trade";
    public static final String URL_LOAN = URL_BASE + "/bank/loan";
    public static final String URL_INTEGRATION = URL_LOAN + "/integration";
    public static final String URL_PAYMENTS = URL_BASE + "/bank/payment";
    public static final String URL_INFO = URL_BASE + "/bank/info";
    public static final String URL_TRANSACTIONS = URL_BASE + "/bank/transactions";

    public static final String URL_NOTIFICATIONS = URL_BASE + "/bank/notifications";
    public static final String URL_FORECAST = URL_BASE + "/bank/forecast";
    public static final String FORECAST_PAYMENTS = "forecast_customer";
    public static final String URL_LOGOUT = URL_BASE + "/bank/logout";
    public static final String URL_CUSTOMERS = URL_BASE + "/bank/accounts";
    public static final String URL_LOGIN = URL_BASE + "/bank/login";
    public static final String URL_LOGIN_ADMIN = URL_LOGIN + "/admin";
    public static final String DATA = "data";
}