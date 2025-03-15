package csc435.app;

import java.util.Scanner;

public class ServerAppInterface {
    private ServerProcessingEngine engine;

    public ServerAppInterface(ServerProcessingEngine engine) {
        this.engine = engine;
    }

    public void readCommands() {
        Scanner sc = new Scanner(System.in);
        String command;

        while (true) {
            System.out.print("> ");
            command = sc.nextLine();

            if (command.compareTo("quit") == 0) {
                engine.shutdown();
                break;
            }

            if (command.compareTo("list") == 0) {
                engine.listConnectedClients();
                continue;
            }

            System.out.println("not a valid command");
        }

        sc.close();
    }
}
