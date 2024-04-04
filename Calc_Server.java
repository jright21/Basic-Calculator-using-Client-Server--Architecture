import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Calc_Server {
    public static void main(String args[]) {
        System.out.println("Welcome to our Calculator!!\n");
        System.out.println("Establishing connection.....\n");

        try {
            int port = 8081;
            // Step 1: Establish the socket connection.
          // Printing  server name.
            System.out.println("The server is hosted on: " + InetAddress.getLocalHost());
            ServerSocket ss = new ServerSocket(port);

            while (true) {
                // Step 2: Processing the request.
                Socket s = ss.accept();
                System.out.println("Connection established.\n");

                // Create a new thread for each client connection
                Thread clientThread = new Thread(new ClientHandler(s));
                clientThread.start();
            }
        } catch (Exception e) {
            System.out.println(" ");
        }
    }

    // Creating a client Handler.
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Processing the request for each client in a separate thread
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                // Send available functionalities to the client
                dos.writeUTF(getAvailableFunctionalities());

                while (true) {
                    // wait for input
                    String input = dis.readUTF();

                    if (input.equals("bye")) {
                        break;
                    }

                    System.out.println("Equation received: " + input);
                    double result = processEquation(input);
                    System.out.println("Sending the result...");

                    // send the result back to the client.
                    dos.writeUTF(Double.toString(result));
                }

                // Close the client socket after handling the request
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Rest of your existing code remains unchanged...

    private static String getAvailableFunctionalities() {
        return "\nBasic Arithmetic Functions:\n" +
                "  - Addition (+)\n" +
                "  - Subtraction (-)\n" +
                "  - Multiplication (*)\n" +
                "  - Division (/)\n" +
                "\nTrigonometric Functions:\n" +
                "  - Sine (sin)\n" +
                "  - Cosine (cos)\n" +
                "  - Tangent (tan)\n" +
                "  - Inverse Sine (asin or arcsin)\n" +
                "  - Inverse Cosine (acos or arccos)\n" +
                "  - Inverse Tangent (atan or arctan)\n" +
                "\nExponential and Logarithmic Functions:\n" +
                "  - Exponentiation (x^y or y^x)\n" +
                "  - Square Root (√x)\n" +
                "  - Natural Logarithm (ln)\n" +
                "  - Logarithm to the base 10 (log)\n" +
                "\nHyperbolic Functions:\n" +
                "  - Hyperbolic Sine (sinh)\n" +
                "  - Hyperbolic Cosine (cosh)\n" +
                "  - Hyperbolic Tangent (tanh)\n" +
                "\nStatistical Functions:\n" +
                "  - Mean (mean)\n" +
                "  - Standard Deviation (sd)\n" +
                "  - Variance (var)\n" +
                "  - Factorial (fact)\n" +
                "\nAngle Conversion:\n" +
                "  - Degree to Radian Conversion(degToRad)\n" +
                "  - Radian to Degree Conversion(radToDeg)\n" +
                "\nPowers and Roots:\n" +
                "  - Power of 2 (pow2)\n" +
                "  - Power of 3 (pow3)\n" +
                "  - Cube Root (cbrt)\n" +
                "  - Arbitrary Power- (y^x)\n" +
                "\nFraction Calculations:\n" +
                "  - Fraction Simplification(fracSimplify)\n" +
                "  - Mixed Number to Improper Fraction Conversion(mixedToImproper)\n" +
                "  - Improper Fraction to Mixed Number Conversion(improperToMixed)\n" +
                "\nCombinations:\n" +
                "  - Combination (nCr)\n";
    }

    private static double processEquation(String input) {
        // Use StringTokenizer to break the equation into function and operands
        StringTokenizer st = new StringTokenizer(input);
        String function = st.nextToken();

        switch (function) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "sin":
            case "cos":
            case "tan":
            case "asin":
            case "acos":
            case "atan":
            case "exp":
            case "sqrt":
            case "ln":
            case "log":
            case "sinh":
            case "cosh":
            case "tanh":
            case "mean":
            case "sd":
            case "var":
            case "fact":
            case "degToRad":
            case "radToDeg":
            case "pow2":
            case "pow3":
            case "cbrt":
            case "y^x":
            case "fracSimplify":
            case "mixedToImproper":
            case "improperToMixed":
            case "nCr":
                return applyFunction(st, function);
            default:
                return Double.NaN; // Indicate an invalid operation
        }
    }

    private static double applyFunction(StringTokenizer st, String function) {
        double operand1 = Double.parseDouble(st.nextToken());
        double operand2 = 0;

        if (st.hasMoreTokens()) {
            operand2 = Double.parseDouble(st.nextToken());
        }

        switch (function) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand2 != 0 ? operand1 / operand2 : Double.POSITIVE_INFINITY;
            case "sin":
                return Math.sin(Math.toRadians(operand1));
            case "cos":
                return Math.cos(Math.toRadians(operand1));
            case "tan":
                return Math.tan(Math.toRadians(operand1));
            case "asin":
                return Math.toDegrees(Math.asin(operand1));
            case "acos":
                return Math.toDegrees(Math.acos(operand1));
            case "atan":
                return Math.toDegrees(Math.atan(operand1));
            case "exp":
                return Math.pow(operand1, operand2);
            case "sqrt":
                return Math.sqrt(operand1);
            case "ln":
                return Math.log(operand1);
            case "log":
                return Math.log10(operand1);
            case "sinh":
                return Math.sinh(operand1);
            case "cosh":
                return Math.cosh(operand1);
            case "tanh":
                return Math.tanh(operand1);
            case "mean":
                return calculateMean(operand1, operand2);
            case "sd":
                return calculateStandardDeviation(operand1, operand2);
            case "var":
                return calculateVariance(operand1, operand2);
            case "fact":
                return calculateFactorial((int) operand1);
            case "degToRad":
                return Math.toRadians(operand1);
            case "radToDeg":
                return Math.toDegrees(operand1);
            case "pow2":
                return Math.pow(operand1, 2);
            case "pow3":
                return Math.pow(operand1, 3);
            case "cbrt":
                return Math.cbrt(operand1);
            case "y^x":
                return Math.pow(operand2, operand1);
            case "fracSimplify":
                return simplifyFraction(operand1, operand2);
            case "mixedToImproper":
                return mixedToImproper(operand1, operand2);
            case "improperToMixed":
                return improperToMixed(operand1, operand2);
            case "nCr":
                return nCr((int) operand1, (int) operand2);
            default:
                return Double.NaN; // Indicate an invalid operation
        }
    }
    // Function for some calculation operations.

    // For calculating mean.
    private static double calculateMean(double operand1, double operand2) {
        return (operand1 + operand2) / 2;
    }

    // Calculating Standard Deviation.
    private static double calculateStandardDeviation(double operand1, double operand2) {
        double mean = calculateMean(operand1, operand2);
        return Math.sqrt((Math.pow(operand1 - mean, 2) + Math.pow(operand2 - mean, 2)) / 2);
    }

    // Calculating Variance.
    private static double calculateVariance(double operand1, double operand2) {
        double mean = calculateMean(operand1, operand2);
        return (Math.pow(operand1 - mean, 2) + Math.pow(operand2 - mean, 2)) / 2;
    }

    // Calculating Factorial.
    private static int calculateFactorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        } else {
            return n * calculateFactorial(n - 1);
        }
    }

    // Simplify Fraction.
    private static double simplifyFraction(double numerator, double denominator) {
        return numerator / denominator;
    }

    // Convert mixed to improper fraction.
    private static double mixedToImproper(double whole, double fraction) {
        return whole + fraction;
    }

    // Convert Improper fraction to mixed fraction.
    private static double improperToMixed(double numerator, double denominator) {
        return numerator / denominator;
    }

    // Calculate nCr.
    private static double nCr(int n, int r) {
        return calculateFactorial(n) / (calculateFactorial(r) * calculateFactorial(n - r));
    }
}
