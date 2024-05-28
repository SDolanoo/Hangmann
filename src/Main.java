        import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // make and populate ArrayList for difficultyArray easy / medium / hard
        ArrayList<Character> difficultyArray = new ArrayList<>();
        difficultyArray.add('e');
        difficultyArray.add('m');
        difficultyArray.add('h');

        // make empty array list for results
        ArrayList<String> playerResults = new ArrayList<>();

        System.out.println("Welcome to the hangman game!!!");
        WordsDatabase wordsDatabase = new WordsDatabase();

        int numberOfGames = 0;

        while (true) {
            Scanner userInput = new Scanner(System.in);
            System.out.println(" ");
            System.out.println("Possible options: play the game // results // add new word //  delete a word // quit");
            System.out.println("What do you want to do:");
            char userAction = userInput.next().toLowerCase().charAt(0);
            if (userAction == 'p') {
                numberOfGames += 1;
                playTheGame(difficultyArray, numberOfGames, playerResults);
            } else if (userAction == 'a') {
                Scanner scanner = new Scanner(System.in);
                System.out.println(" ");
                System.out.println("Enter a word to add:");
                String wordToAdd =  scanner.next();
                wordsDatabase.addWord(wordToAdd, setDifficulty(difficultyArray));
            } else if (userAction == 'd') {
                Scanner scanner = new Scanner(System.in);
                System.out.println(" ");
                System.out.println("Enter a word to delete:");
                String wordToDelete =  scanner.next();
                wordsDatabase.deleteWord(wordToDelete);
            } else if (userAction == 'q') {
                System.exit(0);
            } else if (userAction == 'r') {
                if (playerResults.isEmpty()) {
                    System.out.println("No game hasn't been played yet. Please play the game and try again.");
                } else {
                    for (String item : playerResults){
                        System.out.println(item);
                    }
                }
            }else {
                System.out.println("Wrong input, try again.");
            }
        }
    }

    private static void  playTheGame(ArrayList<Character> difficultyArray, int numberOfGames, ArrayList<String> playerResults) {
        // user settings
        String difficulty = setDifficulty(difficultyArray);

        String theWord = "";
        WordsDatabase wordsDatabase = new WordsDatabase();
        if (difficulty.equals("hard")) {
            theWord = wordsDatabase.getRandomWord("medium");
        } else {
            theWord = wordsDatabase.getRandomWord(difficulty);
        }

        ArrayList<Character> playerGuesses = new ArrayList<>();
        printWordState(theWord, playerGuesses);

        int wrongGuessesCount = 0;

        // game in progress while loop
        while (checkWinner(theWord, playerGuesses, wrongGuessesCount)) {
            Scanner userGuess = new Scanner(System.in);
            System.out.println(" ");
            System.out.println("Enter a letter:");
            char letterInput = userGuess.next().toLowerCase().charAt(0);
            if (checkCorrectGuess(theWord, letterInput, playerGuesses)) {
                if (difficulty.equals("easy") || difficulty.equals("medium")) {
                    wrongGuessesCount += 1;
                } else {
                    wrongGuessesCount += 2;
                }
            }

            printWrongGuessesState(wrongGuessesCount);
            printWordState(theWord, playerGuesses);
        }
        playerResults.add(getResults(wrongGuessesCount, difficulty, numberOfGames));
    }

    private static String getResults(int wrongGuessesCount, String difficulty, int numberOfGames){
        String winOrLose = "";
        if (wrongGuessesCount == 8) {
            winOrLose += "lose";
        } else {
            winOrLose += "win";
        }
        // Game 1 - difficulty: easy - Win - No. wrong guesses: 6
        return "Game "+ numberOfGames + " - " + "difficulty: " + difficulty + " - " + winOrLose + " - No. wrong quesses: " + wrongGuessesCount;

    }

    private static String setDifficulty(ArrayList<Character> difficultyArray) {
        // a function to return difficulty based on user Input from SetDiffInput() function
        char difficultyInput = setDiffInput(difficultyArray);
        if (difficultyInput == 'e') {
            return "easy";
        } else if (difficultyInput == 'm') {
            return "medium";
        } else {
            return "hard";
        }
    }

    private static Character setDiffInput(ArrayList<Character> difficultyArray) {
        // a function made to determine what difficulty the User want's to set and
        // return a first character of a word to avoid spelling mistakes
        System.out.print("Choose the difficulty (easy / medium / hard): ");
        Scanner scannerDifficulty = new Scanner(System.in);
        while (true){
            char difficultyInput = scannerDifficulty.next().toLowerCase().charAt(0);
            if (difficultyArray.contains(difficultyInput)){
                return difficultyInput;
            } else {
                System.out.print("Wrong input... Try again. Type (easy / medium / hard): ");
            }
        }

    }

    private static boolean checkCorrectGuess(String newRandomWord ,char letter, ArrayList<Character> playerGuesses) {
        // a function to check if the userInput was correct or not and adds the letter to playerGuesses ArrayList,
        // returns true if the guess was not correct and false if the guess was correct, also if the guess was correct,
        // but the letter was guessed correctly before returns false
        System.out.println(" ");
        int occursInWord = newRandomWord.indexOf(letter);
        if (occursInWord < 0) {
            System.out.println("Nope! Try again.");
            playerGuesses.add(letter);
            return true;
        }else if (playerGuesses.contains(letter)){
            System.out.println("Correct, but you already guessed this letter :/");
            return false;
        }else {
            System.out.println("Correct!");
            playerGuesses.add(letter);
            return false;
        }
    }

    private static boolean checkWinner(String newRandomWord, ArrayList<Character> playerGuesses, int wrongGuessesCount) {
        // a function to check if the game has ended whether by
        // reaching the max wrong guesses count (8) made or correctly guessing a word
        // in both ways the function returns false (to end a main while loop) and announces the result of the game
        // or returns true and the game continues
        if (wrongGuessesCount == 8) {
            announceLooser(newRandomWord);
            return false;
        }
        int count = 0;
        for (int i = 0; i < newRandomWord.length(); i++) {
            if ( playerGuesses.contains(newRandomWord.charAt(i)) ) {
                count += 1;
            }
        }
        if (count == newRandomWord.length()) {
            announceWinner();
            return false;
        } else {
            return true;
        }
    }

    private static void announceWinner() {
        // a function to print winning message
        System.out.println(" ");
        System.out.println("You win!");
    }

    private static void announceLooser(String newRandomWord) {
        // a function to print loosing message
        System.out.println(" ");
        System.out.println("You lose... The correct word was: " + newRandomWord);
    }

    private static void printWordState(String newRandomWord, ArrayList<Character> playerGuesses) {
        // a function that prints the state of currently guessed word e.g. -i--er (dinner)
        for (int i = 0; i < newRandomWord.length(); i++) {
            if (playerGuesses.contains(newRandomWord.charAt(i))) {
                System.out.print(newRandomWord.charAt(i));
            } else {
                System.out.print('-');
            }
        }
    }

    private static void printWrongGuessesState(int wrongGuessesCount) {
        // a function that print a hangman based on wrongGuessesCount
        switch (wrongGuessesCount) {
            case 1:
                System.out.println(" ________");
                break;
            case 2:
                System.out.println(" ________");
                System.out.println(" |      |");
                break;
            case 3:
                System.out.println(" ________");
                System.out.println(" |      |");
                System.out.println(" O");
                break;
            case 4:
                System.out.println(" ________");
                System.out.println(" |      |");
                System.out.println(" O");
                System.out.println(" |");
                break;
            case 5:
                System.out.println(" ________");
                System.out.println(" |      |");
                System.out.println(" O");
                System.out.println("/|");
                break;
            case 6:
                System.out.println(" ________");
                System.out.println(" |      |");
                System.out.println(" O");
                System.out.println("/|\\");
                break;
            case 7:
                System.out.println(" ________");
                System.out.println(" |      |");
                System.out.println(" O");
                System.out.println("/|\\");
                System.out.println("/");
                break;
            case 8:
                System.out.println(" ________");
                System.out.println(" |      |");
                System.out.println(" O");
                System.out.println("/|\\");
                System.out.println("/ \\");
                break;
        }
    }

}