package io.pusteblume.nexthink.Q5Refactoring;

import java.io.IOException;

import static io.pusteblume.nexthink.Q5Refactoring.Expression.ArithmeticExpression.*;
import static io.pusteblume.nexthink.Q5Refactoring.MathOperation.*;

public class Q5Refactoring {

    public static void main(String[] args) {

        ArithmeticExpression expression =
                aexp(
                        aexp(
                                aexp(
                                        5,
                                        PLUS,
                                        4
                                ),
                                TIMES,
                                6
                        ),
                        DIV,
                        aexp(
                                3,
                                MINUS,
                                1
                        )
                );

        try {
            System.out.println(expression
                    + " = "
                    + expression.evaluate());
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Serializing XML to '"
                + "./expr1.xml"
                + "' ...");
        boolean success = expression.serializeToXml("./expr1.xml");
        System.out.println(success ? "Success!" : "An error occurred.");

        System.out.println("Press Enter to exit.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
