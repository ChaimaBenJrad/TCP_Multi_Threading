import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    private static final int PORT = 12345;
    private static final int MAX_THREADS = 10;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = input.readLine()) != null) {
                    // Reverse the input string
                    String reversedString = reverseString(inputLine);
                    // Simulate some processing time
                    Thread.sleep(1000);
                    // Send the reversed string back to the client
                    output.println(reversedString);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String reverseString(String input) {
            return new StringBuilder(input).reverse().toString();
        }
    }
}
