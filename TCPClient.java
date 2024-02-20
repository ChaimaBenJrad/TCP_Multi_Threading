import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    private static final String SERVER_IP = "127.0.0.1"; // Adresse IP du serveur
    private static final int SERVER_PORT = 12345; // Port du serveur

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server. Enter text to reverse (type 'quit' to exit):");
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                if ("quit".equalsIgnoreCase(userInputLine)) {
                    break;
                }
                // Envoi de la chaîne au serveur
                output.println(userInputLine);
                // Réception de la réponse du serveur
                String serverResponse = input.readLine();
                System.out.println("Server response: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
