#include <iostream>
#include <memory>

#include "IndexStore.hpp"
#include "ServerProcessingEngine.hpp"
#include "ServerAppInterface.hpp"

int main(int argc, char** argv)
{
    // TO-DO change the server port to a non-privileged port from argv[1]
    int serverPort = 1;
    // TO-DO read the number of worker threads from argv[2]
    int numWorkerThreads = 1;

    std::shared_ptr<IndexStore> store = std::make_shared<IndexStore>();
    std::shared_ptr<ServerProcessingEngine> engine = std::make_shared<ServerProcessingEngine>(store);
    std::shared_ptr<ServerAppInterface> interface = std::make_shared<ServerAppInterface>(engine);

    // create a thread that runs the ZMQ Proxy and create the worker threads
    engine->initialize(serverPort, numWorkerThreads);

    // read commands from the user
    interface->readCommands();

    return 0;
}