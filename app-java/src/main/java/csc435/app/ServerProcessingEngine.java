package csc435.app;

import org.zeromq.ZContext;

public class ServerProcessingEngine {
    private IndexStore store;
    // TO-DO keep track of the ZMQ context
    // TO-DO keep track of the ZMQ Proxy object
    // TO-DO keep track of the ZMQ Proxy thread and worker threads

    public ServerProcessingEngine(IndexStore store) {
        this.store = store;
    }

    public void initialize(int serverPort, int numWorkerThreads) {
        // TO-DO initialize the ZMQ context
        // TO-DO create a ZMQ Proxy object
        // TO-DO create and start the ZMQ Proxy thread
        // TO-DO create Server Worker objects
        // TO-DO create and start the worker threads
    }

    public void shutdown() {
        // TO-DO destroy the ZMQ context
        // TO-DO join the ZMQ Proxy and worker threads
    }
}
