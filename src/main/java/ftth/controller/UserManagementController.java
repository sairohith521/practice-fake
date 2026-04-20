package ftth.controller;

import ftth.model.Role;
import ftth.model.User;
import ftth.service.UserManagerService;
import ftth.util.InputUtil;

import java.util.Scanner;

public class UserManagementController {

    private final UserManagerService userManagerService;

    public UserManagementController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    public User login(Scanner sc) {

        System.out.println("\n===== LOGIN =====");
        String username = InputUtil.readValidUsername(sc, "Username: ");
        String password = InputUtil.readPassword("Password: ");

        User user = userManagerService.login(username, password);

        if (user == null) {
            System.out.println("Invalid credentials.");
            return null;
        }

        Role role = userManagerService.getRole(user);
        System.out.println("Login successful. Role: " + role.getRoleCode());

        return user;
    }
}