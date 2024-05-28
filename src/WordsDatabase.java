import java.sql.*;

public class WordsDatabase {
    String url = "jdbc:sqlite::resource:words.db";

    public String getRandomWord(String difficulty) {
        String randomWordQuerry = String.format(
                "SELECT * FROM words WHERE word_difficulty = '%s' ORDER BY RANDOM() LIMIT 1", difficulty);

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(randomWordQuerry);
            rs.next();

            return rs.getString("word");
        } catch (SQLException e) {
            System.out.println("An error occurred. Please try again.");
            System.out.println("The word is none");
            return "none";
        }
    }

    public void deleteWord(String word) {
        String deleteQuerry = "DELETE FROM words WHERE word = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(deleteQuerry)) {

            pstmt.setString(1, word);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Word '" + word + "' deleted successfully.");
            } else {
                System.out.println("No word found with name '" + word + "'. ");
            }
        } catch (SQLException e) {
            System.out.println("The  word '" + word + "' does not exists. Please try again.");
        }
    }

    public void addWord(String word, String difficulty) {
        String addQuery = "INSERT INTO words (word, word_difficulty) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(addQuery)) {
           pstmt.setString(1, word);
           pstmt.setString(2, difficulty);
           pstmt.executeUpdate();
           System.out.println("Word '" + word + "' with difficulty '" + difficulty + "' added successfully.");
        } catch (SQLException e) {
            System.out.println("An error occurred. Please try again.");
        }
    }


}

