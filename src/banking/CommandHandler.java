package banking;

import java.util.Scanner;

public class CommandHandler implements ICommandHandler {

    public int processCommandInput() {
        Scanner scanner = new Scanner(System.in);

        int input = Integer.parseInt(scanner.nextLine());
        System.out.println();

        return input;
    }

    public String processDialogInput() {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        return input;
    }
}
