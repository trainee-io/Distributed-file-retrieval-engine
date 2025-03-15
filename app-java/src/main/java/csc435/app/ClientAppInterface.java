package csc435.app;

import java.util.Scanner;
import java.util.ArrayList;

public class ClientAppInterface {
    private ClientProcessingEngine engine;

    public ClientAppInterface(ClientProcessingEngine engine) {
        this.engine = engine;
    }

    public void readCommands() {
        Scanner sc = new Scanner(System.in);
        String command;

        while (true) {
            System.out.print("> ");
            command = sc.nextLine();

            if (command.compareTo("quit") == 0) {
                engine.disconnect();
                break;
            }

            if (command.length() >= 7 && command.substring(0, 7).compareTo("connect") == 0) {
                String[] prt = command.split(" ");
                if (prt.length == 3) {
                    engine.connect(prt[1], prt[2]);
                } else {
                    System.out.println("Invalid connect command. Usage: connect <server IP> <server port>");
                }
                continue;
            }

            if (command.length() >= 8 && command.substring(0, 8).compareTo("get_info") == 0) {
                long clientId = engine.getInfo();
                System.out.println("Client ID: " + clientId);
                continue;
            }

            if (command.length() >= 5 && command.substring(0, 5).compareTo("index") == 0) {
                String folderPath = command.substring(6);
                ClientProcessingEngine.IndexResult res = engine.indexFiles(folderPath);
                System.out.println("Completed Indexing " + res.totalBytesRead + " bytes of data");
                System.out.println("Completed Indexing in " + res.executionTime + " seconds.");
                continue;
            }

            if (command.length() >= 6 && command.substring(0, 6).compareTo("search") == 0) {
                String query = command.substring(7);
                String[] terms = query.split(" AND ");
                ArrayList<String> termList = new ArrayList<>();
                for (int i = 0; i < terms.length; i++) {
                    termList.add(terms[i].trim());
                }
                ClientProcessingEngine.SearchResult res = engine.searchFiles(termList);
                System.out.println("Search completed in " + res.executionTime + " seconds");
                System.out.println("Search results (top 10 out of " + res.total_match + "):");
                for (int i = 0; i < res.doc_freq.size(); i++) {
                    ClientProcessingEngine.DocPathFreqPair pair = res.doc_freq.get(i);
                    System.out.println("* " + pair.doc_path + ":" + pair.wordFrequency);
                }
                continue;
            }

            System.out.println("Unrecognized command!");
        }

        sc.close();
    }
}