import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

class Client {
    static final int PORT = 8080;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private UserInput userInput;

    public Client() {
        userInput = new UserInput();
    }

    public void connect(String serverAddress, int serverPort) throws IOException {
        InetAddress addr = InetAddress.getByName(serverAddress);
        socket = new Socket(addr, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        System.out.println("Connected to server.");
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Connection closed.");
        }
    }

    public void process() throws IOException {
        while (true) {
            System.out.println("Do you have your bank account? y/n");
            String answer = userInput.readLine();

            if (answer.equals("n")) {
                createAccount();
            } else if (answer.equals("y")) {
                login();
            } else {
                System.out.println("Invalid input. Please enter 'y' or 'n'.");
                out.println("Proccess");
                out.println("Finish");
                out.println("END");
                break;
            }

            String response = in.readLine();
            System.out.println(response);
        }
        out.println("END");
    }

    private void createAccount() throws IOException {
        System.out.println("You can create your account.");
        String username = userInput.readUsername();
        String password = userInput.readPassword();
        out.println(username);
        out.println(password);
        out.println("REGISTER");
    }

    private void login() throws IOException {
        System.out.println("Input your username and password.");
        String username = userInput.readUsername();
        String password = userInput.readPassword();
        out.println(username);
        out.println(password);
        out.println("LOGIN");
    }
}

class UserInput {
    private BufferedReader userInputReader;

    public UserInput() {
        userInputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String readLine() throws IOException {
        return userInputReader.readLine();
    }

    public String readUsername() throws IOException {
        System.out.print("Enter username: ");
        return readLine();
    }

    public String readPassword() throws IOException {
        System.out.print("Enter password: ");
        return readLine();
    }
}

public class JabberClient {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connect("localhost", client.PORT);
            client.process();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}