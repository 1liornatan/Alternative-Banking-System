package abs.constants;

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
}