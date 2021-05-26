package banking;

public class RenderAccountMenuUI implements IRenderUI {

    public void renderFrame() {
        System.out.println("1. Balance\n" +
                           "2. Add income\n" +
                           "3. Do transfer\n" +
                           "4. Close account\n" +
                           "5. Log out\n" +
                           "0. Exit");

        System.out.println();
    }

    public void showLogOutStatus() {
        System.out.println("You have successfully logged out!\n");
    }
}
