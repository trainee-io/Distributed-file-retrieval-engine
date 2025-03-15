package csc435.app;

public class FileRetrievalServer
{
    public static void main(String[] args)
    {
        // TO-DO change server port to a non-privileged port from args[0]
        int serverPort = 1;
        // TO-DO read the number of worker threads from args[1]
        int numWorkerThreads = 1;

        IndexStore store = new IndexStore();
        ServerProcessingEngine engine = new ServerProcessingEngine(store);
        ServerAppInterface appInterface = new ServerAppInterface(engine);
        
        // create a thread that runs the ZMQ Proxy and create the worker threads
        engine.initialize(serverPort, numWorkerThreads);

        // read commands from the user
        appInterface.readCommands();
    }
}
