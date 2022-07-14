package bank.users;

import users.UsersAndVersion;

import java.util.*;

public class UserManager {

    private final Set<String> usersSet;
    private String userAdmin;
    private int usersVersion;

    public void addAdmin(String username) {
        userAdmin = username;
    }

    public boolean isAdminConnected() {
        return ! userAdmin.isEmpty();
    }

    public UserManager() {
        usersSet = new HashSet<>();
        userAdmin = "";
        usersVersion = 0;
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
        usersVersion++;
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
        usersVersion++;
    }

    public int getUsersVersion() {
        return usersVersion;
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public synchronized UsersAndVersion getUsersAndVersion() {
        List<String> usersList = new ArrayList<>();
        usersList.addAll(usersSet);

        return new UsersAndVersion(usersList, usersVersion);
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