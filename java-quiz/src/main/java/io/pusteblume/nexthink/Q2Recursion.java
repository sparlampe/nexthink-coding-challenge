package io.pusteblume.nexthink;

import java.util.ArrayList;
import java.util.List;

public class Q2Recursion {
    public static class Payment {
        private final int amount;

        public Payment(int amount) {
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }
    }

    public static void main(String[] args) {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(100));
        payments.add(new Payment(50));
        System.out.println(sum(payments));
    }

    private static int sum(List<Payment> payments) {
        if (payments.isEmpty())
            return 0;
        else {
            Payment p = payments.remove(0);
            return p.getAmount() + sum(payments);
        }

    }
}