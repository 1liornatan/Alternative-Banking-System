package users;

import java.util.List;

public class UsersAndVersion {
    private final List<String> usersList;
    private final int usersVersion;

    public UsersAndVersion(List<String> usersList, int usersVersion) {
        this.usersList = usersList;
        this.usersVersion = usersVersion;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public int getUsersVersion() {
        return usersVersion;
    }
}
