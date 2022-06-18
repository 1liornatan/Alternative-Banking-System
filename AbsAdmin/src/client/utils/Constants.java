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
    public static final String TYPE_PARAM = "type";
    public static final String ALL_TRANSACTIONS = "all_transactions";
    public static final String ALL_CUSTOMERS = "all_customers";
    public static final String ALL_LOANS = "all_loans";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Abs";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;
    public static final String FORECAST_PAGE = FULL_SERVER_PATH + "/bank/forecast";
    public static final String LOAN_PAGE = FULL_SERVER_PATH + "/bank/loan";
    public static final String CUSTOMERS_PAGE = FULL_SERVER_PATH + "/bank/accounts";

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/bank/login/admin";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/bank/logout";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
