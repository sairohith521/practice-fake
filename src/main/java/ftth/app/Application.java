package ftth.app;

import java.util.Scanner;
import ftth.service.*;
import ftth.util.InputUtil;
import ftth.controller.*;
import ftth.model.Role;
import ftth.model.User;
import ftth.repository.*;
/**
 * Main — Aaha Telecom FTTH Management System
 *
 * Roles & Menus
 * ─────────────
 * ADMIN : All options + User Management
 * CSR   : Add / Move / Change / Disconnect / Customer Lookup
 * MAINT : Inventory Admin / Maintenance / Capacity Dashboard / Plan Admin
 */

public class Application {
           private final Scanner sc;
           private User currentUser;
           // ===============================
           // Repositories
           // ===============================
           private final CustomerRepository customerRepository;
           private final CustomerConnectionRepository customerConnectionRepository;
           private final PlanRepository planRepository;
           private final ServiceAreaRepository serviceAreaRepository;
           private final InventoryRepository inventoryRepository;
           private final BillRepository billRepository;
           private final EmailLogRepository emailLogRepository;
           private final UserRepository userRepository;
           private final RoleRepository roleRepository;
           // ===============================
           // Services
           // ===============================
           private final PlanService planService;
           private final InventoryService inventoryService;
           private final EmailService emailService;
           private final UserManagerService userManagerService;
           private final CustomerConnectionService customerConnectionService;
           private final ServiceAreaService serviceAreaService;
           private final CustomerService customerService;
           private final BillService billService;
           // ===============================
           // Controllers
           // ===============================
           private final CustomerConnectionController customerConnectionController;
           private final AdminController adminController;
           private final CSRController csrController;
           private final MaintController maintController;
           private final UserManagementController userManagementController;
           private final CustomerController customerController;
           private final PlanAdmin planAdmin;
           // ===============================
           // Constructor (DI container)
           // ===============================
           public Application() {
               // ---------- shared utilities ----------
               this.sc = new Scanner(System.in);
               // ---------- repositories ----------
               this.customerRepository = new CustomerRepository();
               this.customerConnectionRepository = new CustomerConnectionRepository();
               this.planRepository = new PlanRepository();
               this.serviceAreaRepository = new ServiceAreaRepository();
               this.inventoryRepository = new InventoryRepository();
               this.billRepository = new BillRepository();
               this.emailLogRepository = new EmailLogRepository();
               this.userRepository = new UserRepository();
               this.roleRepository = new RoleRepository();
               // ---------- services ----------
               this.planService = new PlanService(planRepository);
               this.customerService=new CustomerService(customerRepository);
               this.inventoryService = new InventoryService(inventoryRepository,serviceAreaRepository);
               this.emailService = new EmailService(emailLogRepository);
               this.userManagerService = new UserManagerService(userRepository,roleRepository);
               this.serviceAreaService=new ServiceAreaService(serviceAreaRepository);
               this.billService=new BillService(billRepository);
               this.customerConnectionService = new CustomerConnectionService(customerService,customerRepository,customerConnectionRepository,planService,inventoryService,billRepository,serviceAreaService,emailService);
               // ---------- controllers ----------
                this.customerController=new CustomerController(customerService,billService, emailService, planService, customerConnectionService);
               this.customerConnectionController =new CustomerConnectionController(serviceAreaService,customerConnectionService,planService,inventoryService);
               this.adminController =new AdminController(customerController,customerConnectionController,planService,inventoryService,userManagerService);
               this.csrController =new CSRController(customerConnectionController,customerConnectionService,planService,inventoryService);
               this.maintController =new MaintController(inventoryService,planService);
               this.userManagementController=new UserManagementController(userManagerService);
               this.planAdmin=new PlanAdmin(planService,sc);
           }
    public void start() {
        while (true) {
            currentUser = userManagementController.login(sc);
            if (currentUser == null) continue;
            Role role = userManagerService.getRole(currentUser);
            boolean logout = false;
            while (!logout) {
                printMenu(role.getRoleCode());
                String option = InputUtil.readMenuOption(sc, "Option: ");
                switch (role.getRoleCode()){
                    case "ADMIN":
                        logout = adminController.handle(option, sc, currentUser);
                        break;
                    case "CSR":
                        // logout = csrController.handle(option, sc, currentUser);
                        break;
                    case "MAINT":
                        // logout = maintController.handle(option, sc, currentUser);
                        break;
                    default:
                        logout = true;
                }
            }
        }
    }


    // 🔹 MENU (belongs here)
    private void printMenu(String role) {
    System.out.println("\n============================================");
    System.out.println("          Welcome to Aaha Telecom");
    System.out.println("============================================");

    switch (role) {
        case "ADMIN":
            System.out.println("  [1] Add (New Install)");
            System.out.println("  [2] Move");
            System.out.println("  [3] Change Plan");
            System.out.println("  [4] Disconnect");
            System.out.println("  [5] Customers (Lookup / Config / Bill)");
            System.out.println("  [6] Inventory Admin");
            System.out.println("  [7] Maintenance");
            System.out.println("  [8] Capacity Dashboard");
            System.out.println("  [9] Plan Admin");
            System.out.println("  [A] User Management");
            System.out.println("  [0] Logout");
            break;

        case "CSR":
            System.out.println("  [1] Add (New Install)");
            System.out.println("  [2] Move");
            System.out.println("  [3] Change Plan");
            System.out.println("  [4] Disconnect");
            System.out.println("  [5] Customers (Lookup / Config / Bill)");
            System.out.println("  [0] Logout");
            break;

        case "MAINT":
            System.out.println("  [1] Inventory Admin");
            System.out.println("  [2] Maintenance");
            System.out.println("  [3] Capacity Dashboard");
            System.out.println("  [4] Plan Admin");
            System.out.println("  [0] Logout");
            break;
    }

    System.out.println("--------------------------------------------");
}
}