import java.util.*;

public class BingoGamee {
    private static final int NUM_CARDS = 4;
    private static final int CARD_SIZE = 5;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayBanner();
        System.out.println("\nWelcome to Bingo Game!");
        System.out.println("Get ready to play and win!");

        displayMenu();
    }

    private static void displayBanner() {
        String ANSI_BLUE = "\u001B[34m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_RESET = "\u001B[0m";

        System.out.println(ANSI_BLUE + " \n");
        System.out.println(ANSI_BLUE + "********************************************");
        System.out.println(ANSI_BLUE + "*                                          *");
        System.out.println(ANSI_YELLOW + "*          B I N G O   G A M E             *");
        System.out.println(ANSI_BLUE + "*                                          *");
        System.out.println(ANSI_BLUE + "********************************************" + ANSI_RESET);
    }

    private static void displayMenu() {
        int choice;
        do {
            System.out.println("\n1. PLAY BINGO!");
            System.out.println("2. EXIT");
            System.out.print("Enter your input: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number. Enter [1] to PLAY BINGO or [2] to EXIT.");
                System.out.print("Enter your input: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    startBingoGame();
                    break;
                case 2:
                    System.out.println("Thanks for playing Bingo!");
                    break;
                default:
                    System.out.println("Invalid choice. Please Enter [1] to PLAY BINGO or [2] to EXIT.");
            }
        } while (choice != 2);
        scanner.close();
    }

    private static void startBingoGame() {
        final int TOTAL_NUMBERS = 75;
        int gameType;
        do {
            System.out.println("\nChoose your Game");
            System.out.println("1.TUMPAK BINGO GAME");
            System.out.println("2.JACKPOT BLACKOUT BINGO GAME");
            System.out.println("3.EXIT");
            System.out.print("Enter your Choice: ");
            gameType = scanner.nextInt();
            if (gameType == 3) {
                System.out.println("Exiting game.");
                return;
            }
            if (gameType != 1 && gameType != 2) {
                System.out.println("Invalid choice. Please choose again.");
            }
        } while (gameType != 1 && gameType != 2);

        boolean isBlackoutGame = (gameType == 2);

        String[][][] bingoCards = new String[NUM_CARDS][CARD_SIZE][CARD_SIZE];
        for (int i = 0; i < NUM_CARDS; i++) {
            bingoCards[i] = generateBingoCard(CARD_SIZE);
            System.out.println("\nPlayer's Bingo Card " + (i + 1) + ":");
            displayBingoCard(bingoCards[i]);
        }

        System.out.println("\nPress Enter to start drawing numbers...");
        scanner.nextLine();
        scanner.nextLine();
        Random random = new Random();
        Queue<Integer> drawnNumbers = new LinkedList<>(); 
        int drawnNumbersCount = 0;
        boolean bingoAchieved = false;

        while (drawnNumbers.size() < TOTAL_NUMBERS && !bingoAchieved) {
            int drawnNumber;
            do {
                drawnNumber = random.nextInt(TOTAL_NUMBERS) + 1;
            } while (drawnNumbers.contains(drawnNumber));
            drawnNumbers.offer(drawnNumber); 
            drawnNumbersCount++;

            System.out.println("\n---------------------------------");
            System.out.println("|    Drawn Number: " + String.format("%2d", drawnNumber) + "    |");
            System.out.println("---------------------------------");

         
            System.out.println("Drawn Numbers Queue: " + drawnNumbers);

            for (int i = 0; i < NUM_CARDS; i++) {
                System.out.println("\nPlayer's Bingo Card " + (i + 1) + ":");
                crossOutNumber(bingoCards[i], drawnNumber);
                displayBingoCard(bingoCards[i]);

                String bingoPattern = checkForBingoPattern(bingoCards[i], isBlackoutGame);
                if (!bingoPattern.equals("No Bingo Pattern")) {
                    bingoAchieved = true;
                    System.out.println("BINGO! Player's Bingo Card " + (i + 1) + " wins with " + bingoPattern + "!");
                    break;
                }
            }

            System.out.println("\nPress Enter to draw another number...");
            scanner.nextLine();
        }

        if (!bingoAchieved) {
            System.out.println("\nAll numbers drawn. No Bingo achieved.");
            for (int i = 0; i < NUM_CARDS; i++) {
                System.out.println("\nPlayer's Bingo Card " + (i + 1) + ":");
                String bingoPattern = checkForBingoPattern(bingoCards[i], isBlackoutGame);
                if (bingoPattern.equals("No Bingo Pattern")) {
                    System.out.println("Pattern in Player's Bingo Card " + (i + 1) + ": " + bingoPattern);
                }
            }
        }

        System.out.println("\nTotal numbers drawn: " + drawnNumbersCount);
    }

    private static String[][] generateBingoCard(int cardSize) {
        String[][] card = new String[cardSize][cardSize];
        final int[] LETTER_RANGES = {15, 15, 15, 15, 15};

        for (int col = 0; col < cardSize; col++) {
            int min = col * 15 + 1;
            int max = min + LETTER_RANGES[col] - 1;
            List<Integer> columnValues = new ArrayList<>();
            for (int i = min; i <= max; i++) {
                columnValues.add(i);
            }
            Collections.shuffle(columnValues);
            for (int row = 0; row < cardSize; row++) {
                if (row == cardSize / 2 && col == cardSize / 2) {
                    card[row][col] = "NA";
                } else {
                    card[row][col] = String.format("%2d", columnValues.get(row));
                }
            }
        }
        return card;
    }

    private static void displayBingoCard(String[][] card) {

        String ANSI_RED = "\u001B[31m";
        String ANSI_YELLOW = "\u001B[33m";
        String ANSI_WHITE = "\u001B[37m";
        String ANSI_RESET = "\u001B[0m";

        System.out.println(ANSI_RED + "_______________________________" + ANSI_RESET);
        System.out.println(ANSI_RED + "|_____________________________|" + ANSI_RESET);
        System.out.println(ANSI_RED + "|   B     I     N    G    O   |" + ANSI_RESET);
        System.out.println(ANSI_RED + "|_____________________________|" + ANSI_RESET);
        for (int i = 0; i < card.length; i++) {
            for (int j = 0; j < card[i].length; j++) {
                if (j == 0) {
                    System.out.print(ANSI_RED + "|" + ANSI_RESET);
                }
                String cellValue = card[i][j];
                if (i == CARD_SIZE / 2 && j == CARD_SIZE / 2) {
                    System.out.print(ANSI_YELLOW + "  " + cellValue + " |" + ANSI_RESET);
                } else {
                    if (cellValue.equals(" X ")) {
                        System.out.print(ANSI_WHITE + "  " + cellValue + ANSI_RESET + "|");
                    } else if (cellValue.equals("NA")) {
                        System.out.print(ANSI_WHITE + "  " + cellValue + ANSI_RESET + "|");
                    } else {
                        System.out.print(ANSI_WHITE + "  " + cellValue + " |" + ANSI_RESET);
                    }
                }
            }
            System.out.println("\n" + ANSI_RED + "|-----------------------------|" + ANSI_RESET);
        }
    }

    private static void crossOutNumber(String[][] card, int drawnNumber) {
        for (int row = 0; row < card.length; row++) {
            for (int col = 0; col < card[row].length; col++) {
                if (card[row][col].equals(String.valueOf(drawnNumber))) {
                    card[row][col] = " X ";
                }
            }
        }
    }

    private static String checkForBingoPattern(String[][] card, boolean isBlackoutGame) {
        if (isBlackoutGame) {
            boolean blackout = true;
            for (int row = 0; row < card.length; row++) {
                for (int col = 0; col < card[row].length; col++) {
                    if (!card[row][col].equals(" X ")) {
                        blackout = false;
                        break;
                    }
                }
                if (!blackout) {
                    break;
                }
            }
            if (blackout) {
                return "Blackout";
            }
        } else {

            for (int row = 0; row < card.length; row++) {
                boolean bingo = true;
                for (int col = 0; col < card[row].length; col++) {
                    if (!card[row][col].equals(" X ")) {
                        bingo = false;
                        break;
                    }
                }
                if (bingo) {
                    return "Straight Horizontal";
                }
            }


            for (int col = 0; col < card[0].length; col++) {
                boolean bingo = true;
                for (int row = 0; row < card.length; row++) {
                    if (!card[row][col].equals(" X ")) {
                        bingo = false;
                        break;
                    }
                }
                if (bingo) {
                    return "Straight Vertical";
                }
            }


            boolean leftDiagonalBingo = true;
            boolean rightDiagonalBingo = true;
            for (int i = 0; i < card.length; i++) {
                if (!card[i][i].equals(" X ")) {
                    leftDiagonalBingo = false;
                }
                if (!card[i][card.length - 1 - i].equals(" X ")) {
                    rightDiagonalBingo = false;
                }
            }
            if (leftDiagonalBingo || rightDiagonalBingo) {
                return "Diagonal";
            }
        }

        return "No Bingo Pattern";
    }
}
