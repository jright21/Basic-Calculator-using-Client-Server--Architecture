import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Calc_Client {
    public static void main(String[] args) {
        try {
            //Asking user to enter server name.
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the Server Name: ");
            String str = sc.nextLine();
            InetAddress ip = InetAddress.getByName(str);
            int port = 8081;

            // Step 1: Open the socket connection.
            Socket s = new Socket(ip, port);

            // Step 2: Communication-get the input and output stream
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            // Display available functionalities
            String availableFunctionalities = dis.readUTF();
            System.out.println("\nAvailable functionalities:\n"
                    + "{In fuction (function code) form}\n"
                    + availableFunctionalities);

            while (true) {
                // Enter the equation in the form-
                // "function operand1 operand2"
                System.out.print("Enter the equation in the form: \n");
                System.out.println("\n'function code and operands:'\n");
                System.out.println("(If no operand is required, simply enter function code)\n");
                System.out.println("{Maximum of 2 operands are allowed.}");

                String inp = sc.nextLine();

                if (inp.equals("bye"))
                    break;

                // send the equation to server
                dos.writeUTF(inp);

                // wait till request is processed and sent back to the client
                String ans = dis.readUTF();
                System.out.println("\n Answer = " + ans + "\n");
                System.out.println("-------------------------------------------------------\n");
            }
            sc.close();
        } catch (Exception e) {
            System.out.println(" "); //Avoid printig any kind of exception in terminal.
        }
    }
}
