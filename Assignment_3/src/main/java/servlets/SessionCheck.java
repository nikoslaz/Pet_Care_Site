package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author nikos
 */
public class SessionCheck extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("username") == null) {
            System.out.println("SessionCheck: No active session or user not logged in");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 status code
        } else {
            System.out.println("SessionCheck: Active session with user logged in");
            response.setStatus(HttpServletResponse.SC_OK); // 200 status code
        }
    }
}
