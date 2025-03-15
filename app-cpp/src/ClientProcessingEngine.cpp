#include "ClientProcessingEngine.hpp"

ClientProcessingEngine::ClientProcessingEngine() { }

IndexResult ClientProcessingEngine::indexFolder(std::string folderPath) {
    IndexResult result = {0.0, 0};
    // TO-DO get the start time
    // TO-DO crawl the folder path and extrac all file paths
    // TO-DO for each file extract all words/terms and count their frequencies
    // TO-DO increment the total number of bytes read
    // TO-DO for each file prepare an INDEX REQUEST message and send to the server
    //       the document path, the client ID and the word frequencies
    // TO-DO receive for each INDEX REQUEST message an INDEX REPLY message
    // TO-DO get the stop time and calculate the execution time
    // TO-DO return the execution time and the total number of bytes read

    return result;
}

SearchResult ClientProcessingEngine::search(std::vector<std::string> terms) {
    SearchResult result = {0.0, { }};
    // TO-DO get the start time
    // TO-DO prepare a SEARCH REQUEST message that includes the search terms and send it to the server
    // TO-DO receive one or more SEARCH REPLY messages with the results of the search query
    // TO-DO get the stop time and calculate the execution time
    // TO-DO return the execution time and the top 10 documents and frequencies

    return std::move(result);
}

long ClientProcessingEngine::getInfo() {
    // TO-DO return the client ID

    return 0;
}

void ClientProcessingEngine::connect(std::string serverIP, std::string serverPort) {
    // TO-DO initialize the ZMQ context
    // TO-DO create the request socket and connect it to the server
    // send a REGISTER request message and receive a REGISTER reply message with the client ID
}

void ClientProcessingEngine::disconnect() {
    // TO-DO implement disconnect from server
    // TO-DO send a QUIT message to the server
    // close the request socket and the context
}