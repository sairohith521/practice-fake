package ftth.controller;

import java.util.List;
import java.util.Scanner;

import ftth.service.FTTH;
import ftth.service.PlanService;
import ftth.util.InputUtil;
import ftth.service.EmailService;
import ftth.service.InventoryService;
import ftth.model.Customer;
import ftth.model.Plan;
import ftth.model.User;
import ftth.service.CustomerConnectionService;
import ftth.service.Customercreen;

public class CSRController {
    private CustomerConnectionService customerConnectionService;
    private final CustomerConnectionController customerConnectionController;
    private PlanService planService;
    private InventoryService inventoryService;

    public CSRController(CustomerConnectionController customerConnectionController,CustomerConnectionService customerConnectionService, PlanService planService, InventoryService inventoryService) {
        this.customerConnectionService = customerConnectionService;
        this.planService = planService;
        this.inventoryService = inventoryService;
        this.customerConnectionController=customerConnectionController;
    }

    // 🔹 MAIN HANDLER (NO STATIC ❌)
    public boolean handle(String option, Scanner sc,User currentUser) {

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
                // Customercreen.show(sc, ftth, email);
                // doLookup(sc);
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
    // 🔥 METHODS (move your logic here)
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
private void doLookup(Scanner sc) {

    System.out.println("\n--- Customer Lookup ---");
    System.out.println("  [1] Look up by Customer ID");
    System.out.println("  [2] List all customers");

    System.out.print("Choose: ");
    String sub = sc.nextLine().trim();

    if (sub.equals("1")) {
        System.out.print("Enter Customer ID: ");
        String custID = sc.nextLine().trim().toUpperCase();

        // 🔥 call service
        customerConnectionService.lookupCustomerById(custID);

    } else if (sub.equals("2")) {

        // 🔥 call service
        customerConnectionService.listAllCustomers();

    } else {
        System.out.println("Invalid choice.");
    }
}
}