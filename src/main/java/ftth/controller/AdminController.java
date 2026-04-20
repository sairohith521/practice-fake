package ftth.controller;

import java.util.List;
import java.util.Scanner;

import ftth.model.Customer;
import ftth.model.Plan;
import ftth.model.User;
import ftth.service.*;
import ftth.util.InputUtil;

public class AdminController {
    private final UserManagerService userManagerService;
    private final InventoryService inventoryService;
   
    private final PlanService planService;
    private final CustomerController customerController;
    private final CustomerConnectionController customerConnectionController;

    // 🔹 Constructor (Dependency Injection)
    public AdminController(CustomerController customerController,CustomerConnectionController customerConnectionController,PlanService planService,InventoryService inventoryService,UserManagerService userManagerService){
        this.userManagerService=userManagerService;
        this.inventoryService=inventoryService;
        this.planService=planService;
        this.customerController = customerController;
        this.customerConnectionController=customerConnectionController;
    }

    // 🔹 MAIN HANDLER (NO STATIC ❌)
    public boolean handle(String option, Scanner sc, User currentUser) {

        switch (option) {

            case "1":
                doAdd(sc,currentUser);
                return false;

            case "2":
                doMove(sc,currentUser);
                return false;

            case "3":
                doChange(sc,currentUser);
                return false;

            case "4":
                doDelete(sc,currentUser);
                return false;

            case "5":
                // doLookup(sc,currentUser);
                // return false;

            case "6":
                // doInventory(sc);
                // return false;

            case "7":
                // doMaint(sc);
                // return false;

            case "8":
                // doCapacity(sc);
                // return false;

            case "9":
                // doPlanAdmin(sc);
                // return false;

            case "A":
                // doUserMgmt(sc,userManagerService,currentUser);
                return false;

            case "0":
                System.out.println("Logged out.");
                return true;

            default:
                System.out.println("Invalid option.");
                return false;
        }
    }

    // =========================================================
    // 🔥 METHODS 
    // =========================================================

    private void doAdd(Scanner sc,User currUser) {
    customerConnectionController.handleAdd(sc,currUser);
}

private void doMove(Scanner sc,User currUser) {
    customerConnectionController.updateCustomerConnection(sc,currUser);
}

   private void doChange(Scanner sc,User currUser) {
    customerConnectionController.doChangePlan(sc, currUser);
   }
   private void doDelete(Scanner sc,User currUser) {
customerConnectionController.doDisconnect(sc, currUser);   
}
private void doLookup(Scanner sc,User currentUser) {
    customerController.menu(sc,currentUser);
}

    static void doInventory(Scanner sc) {
    InventoryController inventory = new InventoryController();
    inventory.menu(); 
}

    private void doMaint(Scanner sc) {

    System.out.println("\n--- Maintenance ---");
    System.out.println("  (Maintenance module — extend as needed.)");
    System.out.print("Press Enter to continue...");

    sc.nextLine();
}

    private void doCapacity(Scanner sc) {

    System.out.println("\n--- Capacity Dashboard ---");

    int[] pincodes = {560001, 560002, 110001};

    // 🔥 call service
    // inventoryService.showCapacity(pincodes);

    System.out.print("\nPress Enter to continue...");
    sc.nextLine();
}

     static void doPlanAdmin(Scanner sc) {
         PlanAdmin admin = new PlanAdmin(sc);
         admin.handleMenu();
    }
    private  void doUserMgmt(Scanner sc, UserManagerService um, String currentUser) {
        while (true) {
            System.out.println("\n--- User Management ---");
            // System.out.println("  [1] List Users");
            System.out.println("  [1] Add User");
            System.out.println("  [2] Change Password");
            System.out.println("  [3] Change Role");
            System.out.println("  [4] Delete User");
            System.out.println("  [5] Back");
            System.out.print("Choose: ");
            String sub = sc.nextLine().trim();

            switch (sub) {
                case "1": {
                    System.out.print("  New Username : ");
                    String uname = sc.nextLine().trim();
                    System.out.print("  Password     : ");
                    String pass  = sc.nextLine().trim();
                    System.out.println("  Role options : CSR | MAINT");
                    System.out.print("  Role         : ");
                    String role  = sc.nextLine().trim();
                    um.addUser(uname, pass, role);
                    break;
                }

                case "2": {
                    System.out.print("  Username     : ");
                    String uname    = sc.nextLine().trim();
                    System.out.print("  New Password : ");
                    String newPass  = sc.nextLine().trim();
                    boolean ok = um.changePassword(uname, newPass);
                    if (ok) System.out.println(" Password updated for '" + uname + "'.");
                    else    System.out.println(" Failed.");
                    break;
                }

                case "3": {
                    System.out.print("  Username     : ");
                    String uname   = sc.nextLine().trim();
                    System.out.println("  Role options :  CSR | MAINT");
                    System.out.print("  New Role     : ");
                    String newRole = sc.nextLine().trim();
                    boolean ok = um.changeRole(uname, newRole);
                    if (ok) System.out.println(" Role updated for '" + uname + "'.");
                    else    System.out.println(" Failed.");
                    break;
                }

                case "4": {
                    System.out.print("  Username to delete: ");
                    String uname = sc.nextLine().trim();
                    if (uname.equalsIgnoreCase(currentUser)) {
                        System.out.println(" You cannot delete your own account.");
                        break;
                    }
                    System.out.print("  Confirm delete '" + uname + "'? (y/n): ");
                    if (!sc.nextLine().equalsIgnoreCase("y")) break;
                    um.deleteUser(uname);
                    break;
                }

                case "5":
                    return;

                default:
                    System.out.println(" Invalid option.");
                    break;
            }
        }
    }
}