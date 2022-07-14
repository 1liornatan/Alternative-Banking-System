package abs.utils;

import bank.logic.manager.BankManager;
import bank.users.UserManager;
import chat.ChatManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static http.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String BANK_MANAGER_ATTRIBUTE_NAME = "bankManager";
	private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object bankManagerLock = new Object();
	private static final Object chatManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static BankManager getBankManager(ServletContext servletContext) {
		synchronized (bankManagerLock) {
			if (servletContext.getAttribute(BANK_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(BANK_MANAGER_ATTRIBUTE_NAME, new BankManager());
			}
		}
		return (BankManager) servletContext.getAttribute(BANK_MANAGER_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}

	public static ChatManager getChatManager(ServletContext servletContext) {
		synchronized (chatManagerLock) {
			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new ChatManager());
			}
		}
		return (ChatManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
	}
}
