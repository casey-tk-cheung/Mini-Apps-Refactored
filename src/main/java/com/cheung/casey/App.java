package com.cheung.casey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Scanner;

public class App 
{
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("P4CS Mini Applications\n"+
                "----------------------");

        MenuChoice menuChoice; // variable to store the MenuChoice enum
        int choice;
        do {
            System.out.println("Please select an option:\n"+
                    "1) Keep Counting Game\n"+
                    "2) Square Root Calculator\n"+
                    "3) Check-Digit Generator\n"+
                    "4) Check-Digit Checker\n"+
                    "9) Quit");
            choice = keyboard.nextInt(); keyboard.nextLine(); // consumes the new line
            menuChoice = MenuChoice.fromInt(choice); // converts choice to a menuChoice
            int number;

            switch (menuChoice) {
                case KEEP_COUNTING:
                    System.out.println("Keep Counting\n"+
                            "-------------\n"+
                            "You will be presented with 10 addition questions.\n"+
                            "After the first question, the left-hand operand is the result of the previous addition.\n"+
                            "Press enter to start...");
                    keyboard.nextLine();

                    keepCounting(keyboard);
                    break;

                case SQUARE_ROOT:
                    System.out.println("Square Root Calculator\n"+
                            "----------------------\n"+
                            "Please enter a positive number:");
                    number = keyboard.nextInt();
                    System.out.println("How many decimal places do you want the solution calculated to:");
                    int decimalPlaces = keyboard.nextInt();
                    if (decimalPlaces<1||decimalPlaces>7) {
                        System.out.println("Out of range.\n");
                        break;
                    }

                    // https://docs.oracle.com/javase/8/docs/api/java/math/BigDecimal.html
                    // calculates the accuracy needed
                    BigDecimal accuracy = new BigDecimal(1/(Math.pow(10,decimalPlaces))).setScale(decimalPlaces, RoundingMode.DOWN);
                    int lowerBound = 0;
                    int upperBound = number;

                    // calculates the square root and rounds it to the correct amount of decimal places
                    BigDecimal sqrt = BigDecimal.valueOf(squareRoot(lowerBound,upperBound,number,accuracy,decimalPlaces)).setScale(decimalPlaces, RoundingMode.DOWN);
                    System.out.println("The square root of "+number+" to "+decimalPlaces+" decimal places is\n"+sqrt+"\n");
                    break;

                case CHECK_DIGIT_GENERATOR:
                    System.out.println("Check-Digit Calculator\n"+
                            "----------------------\n"+
                            "This calculator will take a 5-digit number and generate a trailing 6th check digit.");
                    int cdNum;

                    do {
                        System.out.println("Please enter 5-digit number to generate final code:");
                        number = keyboard.nextInt();
                        cdNum = checkDigitGen(number); //generates check-digit appended to the number

                        // checks that all digits are within range
                        if (cdNum==-1) {
                            System.out.println("Error, all individual digits must be in the range of 1 and 9 inclusive, and 5 digits.\n");
                        }
                    } while (cdNum==-1);

                    System.out.println("The 6-digit final number is: "+cdNum+"\n");
                    break;

                case CHECK_DIGIT_CHECKER:
                    System.out.println("Check-Digit Checker\n"+
                            "------------------------------------\n"+
                            "Please enter 6-digit number to check:");
                    number = keyboard.nextInt();

                    if (number==checkDigitGen(number/10)) // checks if the check-digit is correct
                        System.out.println("This number is valid.\n");
                    else
                        System.out.println("This number is invalid.\n");
                    break;

                case QUIT:
                    System.out.println("Thank you for using P4CS Mini Applications.");
                    return;

                default:
                    System.out.println("Invalid choice, please re-enter.\n");
            }
        } while (choice!=9);
    }

    private static void keepCounting(Scanner keyboard) {
        final int MAX = 10; // maximum random number
        final int MIN = 1; // minimum random number
        final int NUMQ = 10; // number of questions

        Random random = new Random();
        long startTime = System.currentTimeMillis();
        int firstOperand = random.nextInt((MAX - MIN) + 1) + MIN; // generates first operand between 1 and 10

        for (int i=0;i<NUMQ;i++) {
            int secondOperand = random.nextInt((MAX - MIN) + 1) + MIN; // generates second operand between 1 and 10
            // https://www.tutorialspoint.com/java/util/random_nextboolean.htm
            String operator = random.nextBoolean() ? "+" : "-"; // generates a random true false value to select the operator
            System.out.println("Question "+(i+1)+": "+firstOperand+" "+operator+" "+secondOperand+" =");
            int guess = keyboard.nextInt();

            if (operator=="+") {
                firstOperand += secondOperand;
            }
            else if (operator=="-") {
                firstOperand -= secondOperand;
            }

            if (guess!=firstOperand) {
                System.out.println("The answer was "+firstOperand+".\n");
                break;
            }

            if (i==9) {
                long endTime = System.currentTimeMillis();
                long timeTaken = (endTime - startTime)/1000;
                System.out.println("Questions complete in "+timeTaken+" seconds.\n");
            }
        }
    }

    private static double squareRoot(double lb, double ub, double num, BigDecimal accuracy, int dp) {
        // binary search to find the square root
        double avg = (lb+ub)/2; // average of lower and upper bound

        // checks if the difference between the target and average matches the accuracy needed
        if ((BigDecimal.valueOf(Math.abs(num-avg*avg)).setScale(dp, RoundingMode.DOWN)).compareTo(accuracy) <= 0) {
            return avg;
        }
        else if (num>(avg*avg)) {
            return squareRoot(avg+1,ub,num,accuracy,dp); // calls itself, changing lower bound to average+1
        }
        else if (num<(avg*avg)) {
            return squareRoot(lb,avg-1,num,accuracy,dp); // calls itself, changing upper bound to average-1
        }
        return avg;
    }

    /* Check-Digit Generator Test Plan
     * Inputs  Data type      Expected result  Actual result
     * 13337   valid          133375           133375
     * 26358   valid          263586           263586
     * 99999   valid extreme  999997           999997
     * 11111   valid extreme  111113           111113
     * 10101   invalid        Error            Error
     * 00000   invalid        Error            Error
     * 123456  erroneous      Error            Error
     * 1       erroneous      Error            Error
     */
    private static int checkDigitGen(int number) {
        int[] digits = new int[5];
        int count = 0;
        int temp = number; // temporary variable to store number
        while (temp>0||count<5) {
            if (temp%10==0||count==5) { // check if digits are in range
                return -1;
            }
            else {
                digits[4-count] = temp % 10; // adds the lowest digit of temp to the last empty element in digits
                temp = temp / 10;
                ++count;
            }
        }

        int even = 0;
        int odd = 0;

        // loops for the length of the array digits
        for (int i=0;i<digits.length;i++) {
            if ((i+1)%2==0) {
                even += digits[i]; // adds up all the even numbers
            }
            else {
                odd += digits[i]; // adds up all the odd numbers
            }
        }

        int checkDigit = ((7*odd)+(3*even)); // applies the rules to generate the check-digit

        if (checkDigit%10==0) { // if the remainder is 0, check-digit is 0
            checkDigit = 0;
        }
        else {
            checkDigit = 10-(checkDigit%10);
        }
        number = (number*10)+checkDigit; // appends the check digit to the number
        return number;
    }
}
