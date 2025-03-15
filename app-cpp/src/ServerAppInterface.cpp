#include "ServerAppInterface.hpp"

#include <iostream>
#include <vector>
#include <string>

ServerAppInterface::ServerAppInterface(std::shared_ptr<ServerProcessingEngine> engine) : engine(engine) { }

void ServerAppInterface::readCommands() {
    std::string command;
    
    while (true) {
        std::cout << "> ";
        
        // read from command line
        std::getline(std::cin, command);

        // if the command is quit, terminate the program       
        if (command == "quit") {
            engine->shutdown();
            break;
        }

        std::cout << "unrecognized command!" << std::endl;
    }
}