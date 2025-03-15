package csc435.app;

import java.util.ArrayList;

class BenchmarkWorker implements Runnable {
    private ClientProcessingEngine engine;
    private String datasetPath;
    private double executionTime;

    public BenchmarkWorker(String serverIP, String serverPort, String datasetPath) {
        this.engine = new ClientProcessingEngine();
        this.datasetPath = datasetPath;
        this.engine.connect(serverIP, serverPort);
    }


    public void run() {
        long startTime = System.nanoTime();
        ClientProcessingEngine.IndexResult result = engine.indexFiles(datasetPath);
        long finishTime = System.nanoTime();
        this.executionTime = (finishTime - startTime) / 1_000_000_000.0;

        System.out.println("Completed indexing " + result.totalBytesRead + " bytes of data");
        System.out.println("Completed indexing in " + this.executionTime + " seconds");
    }

    public double getExecutionTime() {
        return this.executionTime;
    }

    public void search(String qry) {
        ArrayList<String> trms = new ArrayList<>();
        if (qry.contains(" AND ")) {
            String[] prt = qry.split(" AND ");
            for (String term : prt) {
                trms.add(term.trim());
            }
        } else {
            trms.add(qry.trim());
        }

        ClientProcessingEngine.SearchResult res = engine.searchFiles(trms);
        System.out.println("Searching " + qry);
        System.out.println("Search completed in " + res.executionTime + " seconds");
        System.out.println("Search results (top 10 out of " + res.total_match + "):");

        for (ClientProcessingEngine.DocPathFreqPair pair : res.doc_freq) {
            System.out.println("* " + pair.doc_path + ":" + pair.wordFrequency);
        }
    }

    public void disconnect() {
        engine.disconnect();
    }
}

public class FileRetrievalBenchmark {
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println(" correct format to enter is .....FileRetrievalBenchmark <server IP> <server port> <number of clients> <dataset path 1> ...");
            System.exit(1);
        }

        String srvr_ip = args[0];
        String srvr_prt = args[1];
        int no_of_clients = Integer.parseInt(args[2]);
        ArrayList<String> client_data_path = new ArrayList<>();

        for (int i = 0; i < no_of_clients; i++) {
            client_data_path.add(args[3 + i]);
        }

        long startTime = System.nanoTime();
        BenchmarkWorker[] wrkrs = new BenchmarkWorker[no_of_clients];
        Thread[] thrds = new Thread[no_of_clients];
        double max_exc_time = 0.0;

        for (int i = 0; i < no_of_clients; i++) {
            wrkrs[i] = new BenchmarkWorker(srvr_ip, srvr_prt, client_data_path.get(i));
            thrds[i] = new Thread(wrkrs[i]);
            thrds[i].start();
        }

        for (int i = 0; i < no_of_clients; i++) {
            try {
                thrds[i].join();

                double wrkr_exc_time = wrkrs[i].getExecutionTime();
                if (wrkr_exc_time > max_exc_time) {
                    max_exc_time = wrkr_exc_time;
                }
            } catch (InterruptedException e) {
                System.out.println("Error waiting for worker thread to finish.");
                e.printStackTrace();
            }
        }

        long finishTime = System.nanoTime();
        double totalExecutionTime = (finishTime - startTime) / 1_000_000_000.0;

        
        if (no_of_clients > 0) {
            wrkrs[0].search("the");
            wrkrs[0].search("child-like");
            wrkrs[0].search("distortion AND adaptation");
        }

        
        System.out.println("Total execution time: " + totalExecutionTime + " seconds");
        System.out.println("Wall time to index a dataset: " + max_exc_time + " seconds");

        
        for (int i = 0; i < no_of_clients; i++) {
            wrkrs[i].disconnect();
        }
    }
}
