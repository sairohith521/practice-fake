package ftth.controller;

import ftth.model.Plan;
import ftth.service.PlanService;
import ftth.util.InputUtil;
import ftth.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class PlanAdmin {

    private final PlanService service;
    private final Scanner sc;
    public PlanAdmin(PlanService planService,Scanner sc) {
        service = planService;
        this.sc=sc;
    }

    public void handleMenu() {
        while (true) {
            try {
                System.out.println("\n--- PLAN ADMIN MENU ---");
                System.out.println("1. View Plans");
                System.out.println("2. Add Plan");
                System.out.println("3. Update Plan");
                System.out.println("4. Enable / Disable Plan");
                System.out.println("5. Delete Plan");
                System.out.println("6. Exit");

                System.out.print("Enter choice: ");

                if (!sc.hasNextLine()) return;
                String input = sc.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice.");
                    continue;
                }

                switch (choice) {
                    case 1: service.viewActivePlans(); break;
                    case 2: addPlanFlow(); break;
                    case 3: updatePlanFlow(); break;
                    case 4: togglePlanFlow(); break;
                    case 5: deletePlanFlow(); break;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (java.util.NoSuchElementException e) {
                return;
            } catch (RuntimeException e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }

    private void addPlanFlow() {

    // 1️⃣ Read inputs
    String planCode = InputUtil.readString(sc, "Enter Plan Code: ");
    String planName = readPlanName();
    String speedLabel = readSpeed();
    String dataLimitLabel = readDataLimit();

    System.out.print("Enter OTT Count: ");
    int ottCount = readInt();

    System.out.print("Enter Monthly Price: ");
    BigDecimal monthlyPrice = readBigDecimal(); // ✅ BigDecimal

    String oltType = readOltType();

    // 2️⃣ Show summary
    System.out.println("\n--- New Plan Summary ---");
    System.out.println("Code      : " + planCode);
    System.out.println("Name      : " + planName);
    System.out.println("Speed     : " + speedLabel);
    System.out.println("Data      : " + dataLimitLabel);
    System.out.println("OTTs      : " + ottCount);
    System.out.println("Price     : Rs." + monthlyPrice);
    System.out.println("OLT Type  : " + oltType);

    System.out.print("Confirm add? (y/n): ");
    if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.println("Cancelled.");
        return;
    }

    // 3️⃣ Create Plan object (FINAL MODEL)
    Plan plan = new Plan(
        planCode,
        planName,
        speedLabel,
        dataLimitLabel,
        ottCount,
        monthlyPrice,
        oltType,
        true
    );

    // 4️⃣ Call service
    service.createPlan(plan);

    System.out.println("✅ Plan added successfully.");
}
private BigDecimal readBigDecimal() {
    while (true) {
        try {
            return new BigDecimal(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.print("Invalid amount. Enter again: ");
        }
    }
}
private void printActivePlans(List<Plan> plans) {

    if (plans.isEmpty()) {
        System.out.println("No active plans available.");
        return;
    }

    for (Plan p : plans) {
        System.out.println(
            p.getPlanId() + ". " +
            p.getPlanName() +
            " | Speed: " + p.getSpeedLabel() +
            " | Data: " + p.getDataLimitLabel() +
            " | OTTs: " + p.getOttCount() +
            " | Rs." + p.getMonthlyPrice() +
            " | OLT: " + p.getOltType()
        );
    }
}

    private void updatePlanFlow() {
       // 1️⃣ Show active plans
    printActivePlans(service.getActivePlans());

    System.out.print("\nEnter Plan ID to update: ");
    long planId = readLong();

    // 2️⃣ Fetch existing plan
    Plan existing = service.findPlanById(planId);
    if (existing == null) {
        System.out.println("Plan not found.");
        return;
    }

    System.out.println("\nCurrent Plan:");
    System.out.println(existing.getPlanName()
        + " | Speed: " + existing.getSpeedLabel()
        + " | Data: " + existing.getDataLimitLabel()
        + " | OTTs: " + existing.getOttCount()
        + " | Rs." + existing.getMonthlyPrice()
        + " | OLT: " + existing.getOltType()
    );

    // 3️⃣ Read updated values
    String planName = readPlanName();
    String speedLabel = readSpeed();
    String dataLimitLabel = readDataLimit();

    System.out.print("Enter new OTT Count: ");
    int ottCount = readInt();

    System.out.print("Enter new Monthly Price: ");
    BigDecimal monthlyPrice = readBigDecimal(); // ✅ BigDecimal

    String oltType = readOltType();

    // 4️⃣ Summary
    System.out.println("\n--- Update Summary ---");
    System.out.println("Name      : " + planName);
    System.out.println("Speed     : " + speedLabel);
    System.out.println("Data      : " + dataLimitLabel);
    System.out.println("OTTs      : " + ottCount);
    System.out.println("Price     : Rs." + monthlyPrice);
    System.out.println("OLT Type  : " + oltType);

    System.out.print("Confirm update? (y/n): ");
    if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.println("Cancelled.");
        return;
    }

    // 5️⃣ Build updated Plan (KEEP planCode & active)
    Plan updated = new Plan(
        existing.getPlanCode(),        // plan_code unchanged
        planName,
        speedLabel,
        dataLimitLabel,
        ottCount,
        monthlyPrice,
        oltType,
        existing.isActive()
    );

    // 6️⃣ Call service
    service.updatePlan(planId, updated);

    System.out.println("✅ Plan updated successfully.");
}


    private void togglePlanFlow() {
        service.viewAllPlans();

        System.out.print("\nEnter Plan ID to Enable/Disable: ");
        long id = readLong();

        Plan plan = service.findPlanById(id);
        if (plan == null) {
            System.out.println("Plan not found.");
            return;
        }

        if (plan.isActive()) {
            System.out.print("Plan '" + plan.getPlanName() + "' is currently ENABLED. Disable it? (y/n): ");
        } else {
            System.out.print("Plan '" + plan.getPlanName() + "' is currently DISABLED. Enable it? (y/n): ");
        }

        String confirm = sc.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }

        boolean success = service.togglePlan(id);
        if (success) {
            String newState = plan.isActive() ? "DISABLED" : "ENABLED";
            System.out.println("Plan '" + plan.getPlanName() + "' is now " + newState + ".");
        } else {
            System.out.println("Failed to toggle plan status.");
        }
    }

    private void deletePlanFlow() {
        service.viewAllPlans();

        System.out.print("\nEnter Plan ID to delete: ");
        long id = readLong();

        Plan plan = service.findPlanById(id);
        if (plan == null) {
            System.out.println("Plan not found.");
            return;
        }

        System.out.println("Plan: " + plan);
        System.out.print("Are you sure you want to permanently delete '" + plan.getPlanName() + "'? (y/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("Cancelled.");
            return;
        }

        boolean success = service.deletePlan(id);
        if (success) System.out.println("Plan '" + plan.getPlanName() + "' deleted permanently.");
        else         System.out.println("Failed to delete plan.");
    }

    // --- Validated Inputs ---

    private String readPlanName() {
        while (true) {
            System.out.print("Enter Plan Name: ");
            String name = sc.nextLine().trim();
            if (ValidationUtil.isValidPlanName(name)) return name;
            System.out.println("Invalid name. Only letters, numbers and spaces allowed (2-50 chars).");
        }
    }

    private String readSpeed() {
        while (true) {
            System.out.print("Enter Speed (MBPS): ");
            String val = sc.nextLine().trim();
            if (ValidationUtil.isValidSpeed(val)) return val + "MBPS";
            System.out.println("Invalid speed. Enter a number only (e.g. 300, 500, 1000).");
        }
    }

    private String readDataLimit() {
        while (true) {
            System.out.print("Enter Data Limit (GB) or 'Unlimited': ");
            String val = sc.nextLine().trim();
            if (ValidationUtil.isValidDataLimit(val)) {
                if (val.equalsIgnoreCase("unlimited")) return "Unlimited";
                return val + "GB";
            }
            System.out.println("Invalid input. Enter a number (e.g. 60) or 'Unlimited'.");
        }
    }

    private String readOltType() {
        while (true) {
            System.out.print("Enter OLT Type [OLT300/OLT500]: ");
            String olt = sc.nextLine().trim().toUpperCase();
            if (ValidationUtil.isValidOltType(olt)) return olt;
            System.out.println("Invalid OLT type. Enter OLT300 or OLT500.");
        }
    }

    private int readInt() {
        while (true) {
            String value = sc.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    private long readLong() {
        while (true) {
            String value = sc.nextLine().trim();
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid id: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            String value = sc.nextLine().trim();
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid amount: ");
            }
        }
    }
}
