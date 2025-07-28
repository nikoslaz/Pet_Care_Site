//package Rest_Api;
//
//import java.util.HashMap;
//import java.util.Map;
//import static spark.Spark.*;
//import com.google.gson.Gson;
//import database.DB_Connection;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import mainClasses.Pet;
//import java.util.Random;
//
//public class API {
//
//    private static final Map<String, Pet> pets = new HashMap<>();
//
//    public static void main(String[] args) {
//        options("/*", (request, response) -> {
//            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
//            if (accessControlRequestHeaders != null) {
//                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
//            }
//
//            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
//            if (accessControlRequestMethod != null) {
//                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
//            }
//
//            return "OK";
//        });
//
//        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
//
//        post("/pet", (request, response) -> {
//            Gson gson = new Gson();
//            Pet newPet = gson.fromJson(request.body(), Pet.class);
//            int petId = generatePetId();
//            newPet.setPet_id(petId);
//
//            if (!isValidPet(newPet)) {
//                response.status(400);
//                return "Invalid pet data.";
//            }
//
//            try (Connection conn = DB_Connection.getConnection()) {
//                Statement stmt = conn.createStatement();
//                String sql = "INSERT INTO pets (pet_id, owner_id, name, type, breed, gender, birthyear, weight, description, photo) VALUES ("
//                        + petId + ", "
//                        + newPet.getOwner_id() + ", "
//                        + "'" + newPet.getName() + "', "
//                        + "'" + newPet.getType() + "', "
//                        + "'" + newPet.getBreed() + "', "
//                        + "'" + newPet.getGender() + "', "
//                        + newPet.getBirthyear() + ", "
//                        + newPet.getWeight() + ", "
//                        + "'" + newPet.getDescription() + "', "
//                        + "'" + newPet.getPhoto() + "')";
//
//                stmt.executeUpdate(sql);
//            } catch (SQLException | ClassNotFoundException ex) {
//                response.status(500);
//                return "Database error: " + ex.getMessage();
//            }
//            pets.put(String.valueOf(petId), newPet);
//            response.status(201);
//            response.type("application/json");
//            return gson.toJson(newPet);
//        });
//
//        // GET
//        get("/pet/:pet_id", (request, response) -> {
//            String petId = request.params(":pet_id");
//
//            try (Connection conn = DB_Connection.getConnection()) {
//                // Assuming you have a "pets" table in your database
//                String sql = "SELECT * FROM pets WHERE pet_id = '" + petId + "'";
//
//                try (Statement stmt = conn.createStatement()) {
//                    ResultSet resultSet = stmt.executeQuery(sql);
//
//                    if (resultSet.next()) {
//                        // Retrieve pet information from the database
//                        int owner_id = resultSet.getInt("owner_id");
//                        String name = resultSet.getString("name");
//                        String type = resultSet.getString("type");
//                        String breed = resultSet.getString("breed");
//                        String gender = resultSet.getString("gender");
//                        int birthyear = resultSet.getInt("birthyear");
//                        double weight = resultSet.getDouble("weight");
//                        String description = resultSet.getString("description");
//                        String photo = resultSet.getString("photo");
//
//                        // Create a Pet object
//                        Pet pet = new Pet();
//                        pet.setPet_id(Integer.parseInt(petId));
//                        pet.setOwner_id(owner_id);
//                        pet.setName(name);
//                        pet.setType(type);
//                        pet.setBreed(breed);
//                        pet.setGender(gender);
//                        pet.setBirthyear(birthyear);
//                        pet.setWeight(weight);
//                        pet.setDescription(description);
//                        pet.setPhoto(photo);
//
//                        Gson gson = new Gson();
//                        response.status(200);
//                        response.type("application/json");
//                        return gson.toJson(pet);
//                    } else {
//                        response.status(404);
//                        return "Pet not found.";
//                    }
//                }
//            } catch (SQLException | ClassNotFoundException ex) {
//                response.status(500);
//                return "Database error: " + ex.getMessage();
//            }
//        });
//
//
//
//        // PUT
//        // PUT
//        put("/petWeight/:pet_id/:weight", (request, response) -> {
//            String petId = request.params(":pet_id");
//            double weight = Double.parseDouble(request.params(":weight"));
//
//            try (Connection conn = DB_Connection.getConnection()) {
//                String sql = "UPDATE pets SET weight = " + weight + " WHERE pet_id = '" + petId + "'";
//
//                try (Statement stmt = conn.createStatement()) {
//                    int rowsUpdated = stmt.executeUpdate(sql);
//
//                    if (rowsUpdated > 0) {
//                        response.status(200);
//                        return "Weight updated for pet with ID: " + petId;
//                    } else {
//                        response.status(404);
//                        return "Pet not found or wrong weight.";
//                    }
//                }
//            } catch (SQLException | ClassNotFoundException | NumberFormatException ex) {
//                response.status(500);
//                return "Database error: " + ex.getMessage();
//            }
//        });
//
//
//
//        // DELETE
//        delete("/petDeletion/:pet_id", (request, response) -> {
//            String petId = request.params(":pet_id");
//
//            try (Connection conn = DB_Connection.getConnection()) {
//                String sql = "DELETE FROM pets WHERE pet_id = '" + petId + "'";
//
//                try (Statement stmt = conn.createStatement()) {
//                    int rowsDeleted = stmt.executeUpdate(sql);
//
//                    if (rowsDeleted > 0) {
//                        response.status(200);
//                        return "Pet deleted with ID: " + petId;
//                    } else {
//                        response.status(404);
//                        return "Pet not found.";
//                    }
//                }
//            } catch (SQLException | ClassNotFoundException ex) {
//                response.status(500);
//                return "Database error: " + ex.getMessage();
//            }
//        });
//
//    }
//
//    private static boolean isValidPet(Pet pet) {
//        System.out.println("Pet ID length: " + (pet != null ? String.valueOf(pet.getPet_id()).length() : "null"));
//        System.out.println("Owner ID: " + (pet != null ? pet.getOwner_id() : "null"));
//        System.out.println("Name: " + (pet != null ? pet.getName() : "null"));
//        System.out.println("Type: " + (pet != null ? pet.getType() : "null"));
//        System.out.println("Breed: " + (pet != null ? pet.getBreed() : "null"));
//        System.out.println("Gender: " + (pet != null ? pet.getGender() : "null"));
//        System.out.println("Birthyear: " + (pet != null ? pet.getBirthyear() : "null"));
//        System.out.println("Weight: " + (pet != null ? pet.getWeight() : "null"));
//        System.out.println("Description: " + (pet != null ? pet.getDescription() : "null"));
//        System.out.println("Photo: " + (pet != null ? pet.getPhoto() : "null"));
//
//        boolean isValid = pet != null
//                && String.valueOf(pet.getPet_id()).length() == 10
//                && pet.getOwner_id() != 0
//                && pet.getName() != null && !pet.getName().isEmpty()
//                && pet.getType() != null && !pet.getType().isEmpty()
//                && pet.getBreed() != null && !pet.getBreed().isEmpty()
//                && pet.getGender() != null && !pet.getGender().isEmpty()
//                && pet.getBirthyear() > 2000
//                && pet.getWeight() > 0
//                && pet.getDescription() != null && !pet.getDescription().isEmpty()
//                && pet.getPhoto() != null && pet.getPhoto().startsWith("http");
//
//        return isValid;
//    }
//
//    private static int generatePetId() {
//        Random random = new Random();
//        int min = 1000000000;
//        int max = 2000000000;
//        return random.nextInt(max - min) + min;
//    }
//}
