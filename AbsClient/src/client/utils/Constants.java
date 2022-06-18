package client.utils;

import com.google.gson.Gson;

public class Constants {

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public static final String PROP_AMOUNT = "amount";

    public static final String TYPE_REQUEST = "requested_loans";
    public static final String TYPE_INVEST = "invested_loans";
    public static final String TYPE_UNFINISHED = "unfinished_loans";
    public static final String TYPE_DEPOSIT = "deposit";
    public static final String TYPE_WITHDRAW = "withdraw";

    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Abs";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public static final String TRANSACTIONS_URL = FULL_SERVER_PATH + "/bank/transactions";
    public static final String LOAN_URL = FULL_SERVER_PATH + "/bank/loan";
    public static final String NOTIFICATIONS_URL = FULL_SERVER_PATH + "/bank/notifications";

    public final static String INTEGRATION_REQUEST_URL = FULL_SERVER_PATH + "/bank/loan/integration";
    public static final String PAYMENTS_URL = FULL_SERVER_PATH + "/bank/payments";
    public static final String CLOSE_LOAN_REQUEST = "close_loan_request";
    public static final String PAY_DEBT_REQUEST = "pay_debt_request";
    public static final String PAY_CYCLE_REQUEST = "pay_cycle_request";
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/bank/login";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/bank/logout";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";
    public static final String TYPE_CLOSE = "type_close";
    public static final String TYPE_DEBT = "type_debt";
    public static final String TYPE_CYCLE = "type_cycle";
    public static final String PROP_TYPE = "type";
    public static final String PROP_LOAN_ID = "loan_data";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
