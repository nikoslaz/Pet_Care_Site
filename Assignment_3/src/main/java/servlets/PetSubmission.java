package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mainClasses.Pet; // Importing the Pet class
import database.DB_Connection; // Assuming DB_Connection is in mainClasses
import database.tables.EditPetsTable;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PetSubmission extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Extracting pet details from the request
        int petId = Integer.parseInt(request.getParameter("pet_id"));
        int ownerId = Integer.parseInt(request.getParameter("owner_id"));
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String breed = request.getParameter("breed");
        String gender = request.getParameter("gender");
        int birthyear = Integer.parseInt(request.getParameter("birthyear"));
        double weight = Double.parseDouble(request.getParameter("weight"));
        String description = request.getParameter("description");
        String photo = request.getParameter("photo");

        // Instantiating a Pet object
        Pet pet = new Pet();
        pet.setPet_id(petId);
        pet.setOwner_id(ownerId);
        pet.setName(name);
        pet.setType(type);
        pet.setBreed(breed);
        pet.setGender(gender);
        pet.setBirthyear(birthyear);
        pet.setWeight(weight);
        pet.setDescription(description);
        pet.setPhoto(photo);

        // Adding the pet to the database
        try {
            createNewPet(pet);
            response.getWriter().println("Pet added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Failed to add pet.");
        }
    }

    // Method to add a new pet in the database
    public void createNewPet(Pet bt) throws ClassNotFoundException {
        try {
            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String insertQuery = "INSERT INTO pets (pet_id, owner_id, name, type, breed, gender, birthyear, weight, description, photo) "
                    + "VALUES ('" + bt.getPet_id() + "', '" + bt.getOwner_id() + "', '" + bt.getName() + "', '"
                    + bt.getType() + "', '" + bt.getBreed() + "', '" + bt.getGender() + "', '" + bt.getBirthyear() + "', '"
                    + bt.getWeight() + "', '" + bt.getDescription() + "', '" + bt.getPhoto() + "')";
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The pet was successfully added in the database.");
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditPetsTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
