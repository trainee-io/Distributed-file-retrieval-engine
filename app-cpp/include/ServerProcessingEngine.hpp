#ifndef SERVER_SIDE_ENGINE_H
#define SERVER_SIDE_ENGINE_H

#include <zmq.h>

#include <memory>
#include <vector>
#include <string>

#include "IndexStore.hpp"

class ServerProcessingEngine {
    std::shared_ptr<IndexStore> store;
    // TO-DO keep track of the ZMQ context
    // TO-DO keep track of the ZMQ Proxy thread and worker threads

    public:
        // constructor
        ServerProcessingEngine(std::shared_ptr<IndexStore> store);

        // default virtual destructor
        virtual ~ServerProcessingEngine() = default;

        void initialize(int serverPort, int numWorkerThreads);

        void runZMQProxy(int serverPort);

        void runServerWorker();
        
        void shutdown();
};

#endif