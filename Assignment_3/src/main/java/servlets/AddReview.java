package servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import com.google.gson.Gson;
import mainClasses.Review;
import database.tables.EditReviewsTable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String requestBody = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            Gson gson = new Gson();
            Review review = gson.fromJson(requestBody, Review.class);

            EditReviewsTable reviewsTable = new EditReviewsTable();
            reviewsTable.createNewReview(review);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Review successfully added.");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Review.class.getName()).log(Level.SEVERE, null, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error connecting to the database.");
        } catch (Exception e) {
            Logger.getLogger(Review.class.getName()).log(Level.SEVERE, null, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error occurred.");
        }
    }
}
