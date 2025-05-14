package User;


import fileManager.FileManager;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users = new ArrayList<>();
    private final FileManager fileManager;

    public UserManager(FileManager fileManager) {
        this.fileManager = fileManager;
        this.users = fileManager.loadUserList(); // 초기 로딩
    }

    public boolean addUser(User user) {
        if (isDuplicate(user.getId())) return false;
        users.add(user);
        fileManager.saveUserList(users);
        return true;
    }

    private boolean isDuplicate(String id) {
        return users.stream().anyMatch(u -> u.getId().equals(id));
    }

    private User findUserById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean login(String id, String password) {
        User user = findUserById(id);
        return user != null && user.getPw().equals(password);
    }

    public boolean removeUser(String id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (removed) fileManager.saveUserList(users);
        return removed;
    }

    public void reloadUsers() {
        this.users = fileManager.loadUserList();
    }

    public void saveUsers() {
        fileManager.saveUserList(users);
    }

    public List<User> getUsers() {
        return users;
    }
}
