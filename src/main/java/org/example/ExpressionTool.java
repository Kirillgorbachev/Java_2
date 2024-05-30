package org.example;

import java.util.*;


public class ExpressionTool {


    public static double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");

        if (!validateExpression(expression)) {
            System.out.println("Error: Invalid expression!");
            return Double.NaN;
        }

        Set<String> variables = extractVariables(expression);
        Scanner scanner = new Scanner(System.in);

        Map<String, Double> variableValues = new HashMap<>();
        for (String variable : variables) {
            System.out.print("Enter the value for variable " + variable + ": ");
            double value = scanner.nextDouble();
            variableValues.put(variable, value);
        }

        for (Map.Entry<String, Double> entry : variableValues.entrySet()) {
            expression = expression.replaceAll(entry.getKey(), Double.toString(entry.getValue()));
        }

        return computeExpression(expression);
    }


    private static boolean validateExpression(String expression) {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
            }
        }
        return balance == 0;
    }


    private static Set<String> extractVariables(String expression) {
        Set<String> variables = new HashSet<>();
        StringBuilder variableBuilder = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (Character.isLetter(c)) {
                variableBuilder.append(c);
            } else {
                if (variableBuilder.length() > 0) {
                    variables.add(variableBuilder.toString());
                    variableBuilder.setLength(0);
                }
            }
        }
        if (variableBuilder.length() > 0) {
            variables.add(variableBuilder.toString());
        }
        return variables;
    }


    private static double computeExpression(String expression) {
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    processOperator(operands, operators);
                }
                operators.pop();
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && hasHigherPrecedence(operators.peek(), ch)) {
                    processOperator(operands, operators);
                }
                operators.push(ch);
            } else {
                StringBuilder operandBuilder = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    operandBuilder.append(expression.charAt(i++));
                }
                i--;
                operands.push(Double.parseDouble(operandBuilder.toString()));
            }
        }

        while (!operators.isEmpty()) {
            processOperator(operands, operators);
        }

        return operands.pop();
    }


    private static void processOperator(Stack<Double> operands, Stack<Character> operators) {
        char op = operators.pop();
        double operand2 = operands.pop();
        double operand1 = operands.pop();
        double result = performOperation(operand1, operand2, op);
        operands.push(result);
    }


    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }


    private static boolean hasHigherPrecedence(char op1, char op2) {
        return (op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-');
    }


    private static double performOperation(double operand1, double operand2, char operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero!");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
