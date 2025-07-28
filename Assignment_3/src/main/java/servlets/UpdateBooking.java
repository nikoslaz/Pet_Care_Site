package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import database.tables.EditBookingsTable;

public class UpdateBooking extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            String status = request.getParameter("status");
            EditBookingsTable edit = new EditBookingsTable();

            edit.updateBooking(bookingID, status);
            out.print("{\"message\":\"Booking status updated successfully.\"}");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }

}
