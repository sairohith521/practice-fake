package ftth.service;

import ftth.model.Plan;
import ftth.repository.PlanRepository;

import java.util.List;

public class PlanService {

    private final PlanRepository repo;

    public PlanService(PlanRepository repo) {
        this.repo = repo;
    }

    public boolean addPlan(Plan plan) {
        return repo.insertPlan(plan);
    }
    public void createPlan(Plan plan) {

    boolean inserted = repo.insertPlan(plan);

    if (!inserted) {
        throw new RuntimeException("Plan creation failed");
    }
}

    public List<Plan> getAllPlans() {
        return repo.findAllPlans();
    }

    public List<Plan> getActivePlans() {
        return repo.findActivePlans();
    }
    public void printActivePlans(List<Plan> plans) {

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



    public void viewActivePlans() {
        List<Plan> plans = repo.findActivePlans();
        if (plans.isEmpty()) {
            System.out.println("No enabled plans available.");
            return;
        }
        System.out.println("---- ENABLED PLANS ----");
        for (Plan p : plans) {
            System.out.println(p);
        }
    }
    private void showActivePlans() {
    List<Plan> plans =getActivePlans();
    printActivePlans(plans);
}

    public void viewAllPlans() {
        List<Plan> plans = repo.findAllPlans();
        if (plans.isEmpty()) {
            System.out.println("No plans available.");
            return;
        }
        System.out.println("---- ALL PLANS ----");
        for (Plan p : plans) {
            System.out.println(p);
        }
    }

    public Plan findPlanById(long id) {
        return repo.findPlanById(id);
    }

    public void updatePlan(long planId, Plan updatedPlan) {

    Plan existingPlan = repo.findById(planId);

    if (existingPlan == null) {
        throw new RuntimeException("Plan not found with id=" + planId);
    }

    boolean updated = repo.updatePlan(planId, updatedPlan);

    if (!updated) {
        throw new RuntimeException("Plan update failed for id=" + planId);
    }
}

    public boolean togglePlan(long id) {
        Plan plan = repo.findPlanById(id);
        if (plan == null) return false;
        boolean newStatus = !plan.isActive();
        return repo.togglePlanStatus(id, newStatus);
    }

    public boolean deletePlan(long id) {
        return repo.deletePlan(id);
    }
      /**
     * Get an active plan by plan ID.
     * Throws exception if plan does not exist or is inactive.
     */
    public Plan getActivePlan(Long planId) {

        if (planId == null) {
            throw new IllegalArgumentException("Plan ID cannot be null");
        }

        Plan plan = repo.findById(planId);

        if (plan == null) {
            throw new RuntimeException("Plan not found with ID: " + planId);
        }

        if (!plan.isActive()) {
            throw new RuntimeException(
                "Plan with ID " + planId + " is not active"
            );
        }

        return plan;
    }

    /**
     * Find plan by ID (no active check).
     */
    public Plan findPlanById(Long planId) {
        return repo.findById(planId);
    }
}

