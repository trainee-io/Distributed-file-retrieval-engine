#include "ServerProcessingEngine.hpp"

ServerProcessingEngine::ServerProcessingEngine(std::shared_ptr<IndexStore> store) : store(store) { }

void ServerProcessingEngine::initialize(int serverPort, int numWorkerThreads) {
    // TO-DO initialize the ZMQ context
    // TO-DO create and start the ZMQ Proxy thread
    // TO-DO create and start the worker threads
}

void ServerProcessingEngine::runZMQProxy(int serverPort) {
    // TO-DO create and bind router and dealer sockets
    // TO-DO create and start the ZMQ Proxy that will forward messages between the router and dealer sockets
    // TO-DO close the router and dealer sockets
    // TO-DO close the context
}

void ServerProcessingEngine::runServerWorker() {
    // TO-DO create a reply socket and connect it to the dealer
    // TO-DO receive a message from the client
    // TO-DO if the message is a REGISTER REQUEST, then
    //       generate a new client ID and return a REGISTER REPLY message containing the client ID
    // TO-DO if the message is an INDEX REQUEST, then
    //       extract the document path, client ID and word frequencies from the message(s)
    //       get the document number associated with the document path (call putDocument)
    //       update the index store with the word frequencies and the document number
    //       return an acknowledgement INDEX REPLY message
    // TO-DO if the message is a SEARCH REQUEST, then
    //       extract the terms from the message
    //       for each term get the pairs of documents and frequencies from the index store
    //       combine the returned documents and frequencies from all of the specified terms
    //       sort the document and frequency pairs and keep only the top 10
    //       for each document number get from the index store the document path
    //       return a SEARCH REPLY message containing the top 10 results
    // TO-DO if the message is a QUIT message, then finish running
    // TO-DO close the reply socket
}

void ServerProcessingEngine::shutdown() {
    // TO-DO shutdown the ZMQ context
    // TO-DO join the ZMQ Proxy and worker threads
}