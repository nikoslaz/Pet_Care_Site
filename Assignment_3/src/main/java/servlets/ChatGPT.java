package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatGPT extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String petType = request.getParameter("petType");
        String breed = request.getParameter("breed");

        try (PrintWriter out = response.getWriter()) {
            String chatResponse = chatGPT(petType, breed);
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Pet Care Chat Response</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Response:</h2>");
            out.println("<p>" + chatResponse + "</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String chatGPT(String petType, String breed) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";  // Replace with your actual API key
        String model = "gpt-3.5-turbo";

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            String prompt = String.format("Give me information about how to take care of a %s, specifically a %s.", petType, breed);
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";

            System.out.println("Sending request to ChatGPT API...");
            System.out.println("Request body: " + body);

            connection.setDoOutput(true);
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(body);
                writer.flush();
            }

            System.out.println("Request sent. Reading response...");

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            System.out.println("Response received: " + response.toString());
            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            if (e instanceof HttpRetryException) {
                HttpRetryException httpRetryException = (HttpRetryException) e;
                if (httpRetryException.responseCode() == 429) {
                    // Handle rate limit error
                    System.err.println("Rate limit reached. Waiting before retrying...");
                    try {
                        Thread.sleep(10000); // Wait for 10 seconds before retrying
                        return chatGPT(petType, breed); // Retry the request
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt(); // Restore interrupted status
                        return "Error: Interrupted while waiting to retry.";
                    }
                }
            }
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }


    private String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }
}
