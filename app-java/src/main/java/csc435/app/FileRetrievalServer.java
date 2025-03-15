package csc435.app;

public class FileRetrievalServer {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("correct form is: FileRetrievalServer <port> <number of workers>");
            System.exit(1);
        }

        int srvr_prt = Integer.parseInt(args[0]);
        int num_wrkr_thrds = Integer.parseInt(args[1]);

        IndexStore store = new IndexStore();
        ServerProcessingEngine engine = new ServerProcessingEngine(store);
        ServerAppInterface appInterface = new ServerAppInterface(engine);

        
        engine.initialize(srvr_prt, num_wrkr_thrds);

        
        appInterface.readCommands();
    }
}
