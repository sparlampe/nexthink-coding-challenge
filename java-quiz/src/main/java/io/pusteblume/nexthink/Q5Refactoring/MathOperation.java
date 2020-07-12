package io.pusteblume.nexthink.Q5Refactoring;

public enum MathOperation {
    DIV("Divide", "/") {
        @Override
        public int calc(int val1, int val2) {
            if (val2 == 0) {
                throw new ArithmeticException("Cannot divide by 0.");
            }
            return val1 / val2;
        }
    },
    MINUS("Subtract", "-") {
        @Override
        public int calc(int val1, int val2) {
            return val1 - val2;
        }
    },
    PLUS("Add", "+") {
        @Override
        public int calc(int val1, int val2) {
            return val1 + val2;
        }
    },
    TIMES("Multiply", "*") {
        @Override
        public int calc(int val1, int val2) {
            return val1 * val2;
        }
    };
    public final String xmlTagName;
    public final String mathSign;

    public abstract int calc(int val1, int val2);

    public String toString() {
        return mathSign;
    }

    MathOperation(String xmlTagName, String mathSign) {
        this.xmlTagName = xmlTagName;
        this.mathSign = mathSign;
    }
}