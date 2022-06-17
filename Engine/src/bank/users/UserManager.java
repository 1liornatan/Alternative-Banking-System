package bank.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private final Set<String> usersSet;
    private String userAdmin;

    public void addAdmin(String username) {
        userAdmin = username;
    }

    public boolean isAdminConnected() {
        return ! userAdmin.isEmpty();
    }

    public UserManager() {
        usersSet = new HashSet<>();
        userAdmin = "";
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }

    public String getAdmin() {
        return userAdmin;
    }

    public void removeAdmin() {
        userAdmin = "";
    }
}