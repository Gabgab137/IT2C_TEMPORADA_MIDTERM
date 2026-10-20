package com.gabriela.gym.ui;

import com.gabriela.gym.dao.ScenarioDao;
import com.gabriela.gym.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {

    private final ScenarioDao dao;
    private final Scanner scanner;

    public ScenarioMenu() {
        dao = new ScenarioDao();
        scanner = new Scanner(System.in);
    }

    public void start() {
        int choice;

        do {
            displayMenu();
            choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addMember();
                case 2 -> viewMembers();
                case 3 -> System.out.println("Exiting Gym Membership Management. Goodbye!");
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }

        } while (choice != 3);
    }

    private void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("       GYM MEMBERSHIP MANAGEMENT");
        System.out.println("========================================");
        System.out.println("[1] Add New Member");
        System.out.println("[2] View All Members");
        System.out.println("[3] Exit System");
        System.out.println("========================================");
    }

    private void addMember() {
        System.out.println("\n--- Add New Member ---");

        String fullName;

        while (true) {
            System.out.print("Enter full name: ");
            fullName = scanner.nextLine().trim();

            if (!fullName.isBlank()) {
                break;
            }

            System.out.println("Full name cannot be empty. Please try again.");
        }

        String tier;

        while (true) {
            System.out.print("Enter membership tier (BASIC/PREMIUM/VIP): ");
            tier = scanner.nextLine().trim().toUpperCase();

            if (tier.equals("BASIC") ||
                    tier.equals("PREMIUM") ||
                    tier.equals("VIP")) {
                break;
            }

            System.out.println("Invalid tier. Please enter BASIC, PREMIUM, or VIP.");
        }

        double monthlyFee = readPositiveFee();

        String status;

        while (true) {
            System.out.print("Enter status (ACTIVE/INACTIVE): ");
            status = scanner.nextLine().trim().toUpperCase();

            if (status.equals("ACTIVE") || status.equals("INACTIVE")) {
                break;
            }

            System.out.println("Invalid status. Please enter ACTIVE or INACTIVE.");
        }

        ScenarioEntity member = new ScenarioEntity(
                0, fullName, tier, monthlyFee, status
        );

        if (dao.addMember(member)) {
            System.out.println("Member added successfully!");
        } else {
            System.out.println("Failed to add member. Please try again.");
        }
    }

    private double readPositiveFee() {
        while (true) {
            System.out.print("Enter monthly fee: ");
            String input = scanner.nextLine().trim();

            try {
                double fee = Double.parseDouble(input);

                if (Double.isFinite(fee) && fee > 0) {
                    return fee;
                }

                System.out.println("Monthly fee must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid fee. Please enter a number.");
            }
        }
    }

    private void viewMembers() {
        List<ScenarioEntity> members = dao.getAllMembers();

        System.out.println("\n==========================================================");
        System.out.println("                    ALL GYM MEMBERS");
        System.out.println("==========================================================");

        System.out.printf(
                "+----+----------------------+---------+-------------+----------+%n"
        );
        System.out.printf(
                "| %-2s | %-20s | %-7s | %-11s | %-8s |%n",
                "ID", "FULL NAME", "TIER", "MONTHLY FEE", "STATUS"
        );
        System.out.printf(
                "+----+----------------------+---------+-------------+----------+%n"
        );

        for (ScenarioEntity member : members) {
            System.out.printf(
                    "| %-2d | %-20.20s | %-7s | %11.2f | %-8s |%n",
                    member.id(),
                    member.fullName(),
                    member.tier(),
                    member.monthlyFee(),
                    member.status()
            );
        }

        System.out.printf(
                "+----+----------------------+---------+-------------+----------+%n"
        );
        System.out.println("Total Records Displayed: " + members.size());
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }
}
