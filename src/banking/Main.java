package banking;

public class Main {
    public static void main(String[] args) {

        run(readPathToDB(args));
    }

    public static void run(String pathToDB) {
        ICardStore cardStoreController = new CardStore(pathToDB);
        BankSystem bankSystem = new BankSystem(cardStoreController);
        CommandHandler commandHandler = new CommandHandler();
        UserController userController = new UserController(bankSystem, commandHandler);

        userController.runMainMenu();
    }

    public static String readPathToDB(String[] inputArgs) {
        return inputArgs[1];
    }
}