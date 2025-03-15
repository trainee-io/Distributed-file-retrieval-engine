package csc435.app;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientProcessingEngine {
    private ZContext cnxt;
    private ZMQ.Socket sckt;
    private long clientId;

    public static class IndexResult {
        public double executionTime;
        public long totalBytesRead;

        public IndexResult(double executionTime, long totalBytesRead) {
            this.executionTime = executionTime;
            this.totalBytesRead = totalBytesRead;
        }
    }

    public static class DocPathFreqPair {
        public String doc_path;
        public long wordFrequency;

        public DocPathFreqPair(String documentPath, long wordFrequency) {
            this.doc_path = documentPath;
            this.wordFrequency = wordFrequency;
        }
    }

    public static class SearchResult {
        public double executionTime;
        public ArrayList<DocPathFreqPair> doc_freq;
        public int total_match;

        public SearchResult(double executionTime, ArrayList<DocPathFreqPair> documentFrequencies, int totalMatches) {
            this.executionTime = executionTime;
            this.doc_freq = documentFrequencies;
            this.total_match = totalMatches;
        }
    }

    public ClientProcessingEngine() {
        this.cnxt = new ZContext();
    }

    public IndexResult indexFiles(String folderPath) {
        IndexResult result = new IndexResult(0.0, 0);
        long startTime = System.nanoTime();

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path.");
            return result;
        }

        
        result.totalBytesRead = indexFolderRecursive(folder, result);

        long finishTime = System.nanoTime();
        result.executionTime = (finishTime - startTime) / 1_000_000_000.0;
        return result;
    }

    private long indexFolderRecursive(File folder, IndexResult result) {
        long totalBytesRead = 0;
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("Error accessing folder: " + folder.getPath());
            return totalBytesRead;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                
                totalBytesRead += indexFolderRecursive(file, result);
            } else if (file.isFile() && file.getName().endsWith(".txt")) {
                HashMap<String, Long> wordFrequencies = new HashMap<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] words = line.split("[^a-zA-Z0-9_-]+");
                        for (String word : words) {
                            if (word.length() > 3) {
                                wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0L) + 1);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error reading file: " + file.getName());
                    continue;
                }

                
                String message = "INDEX " + clientId + " " + file.getPath() + " " + serializeWordFreq(wordFrequencies);
                sckt.send(message.getBytes(ZMQ.CHARSET), 0);
                byte[] reply = sckt.recv(0);
                String replyMessage = new String(reply, ZMQ.CHARSET);
                if (!replyMessage.equals("INDEX_REPLY")) {
                    System.out.println("Error indexing file: " + file.getName());
                }

                totalBytesRead += file.length();
            }
        }

        return totalBytesRead;
    }

    public SearchResult searchFiles(ArrayList<String> terms) {
        SearchResult result = new SearchResult(0.0, new ArrayList<>(), 0);
        long startTime = System.nanoTime();

        
        String message = "SEARCH " + String.join(" AND ", terms);
        sckt.send(message.getBytes(ZMQ.CHARSET), 0);
        byte[] reply = sckt.recv(0);
        String replyMessage = new String(reply, ZMQ.CHARSET);

        if (replyMessage.startsWith("SEARCH_REPLY")) {
            String[] parts = replyMessage.split(" ", 3);
            result.total_match = Integer.parseInt(parts[1]);
            String[] results = parts[2].split(";");
            for (String res : results) {
                String[] pair = res.split(":");
                if (pair.length == 2) {
                    String formattedPath = formatDocumentPath(pair[0]);
                    result.doc_freq.add(new DocPathFreqPair(formattedPath, Long.parseLong(pair[1])));
                }
            }
        }

        long finishTime = System.nanoTime();
        result.executionTime = (finishTime - startTime) / 1_000_000_000.0;
        return result;
    }

    private String formatDocumentPath(String f_path) {
        
        String[] parts = f_path.split("/client_", 2);
        if (parts.length == 2) {
            String clientId = parts[1].split("/")[0];
            String relativePath = parts[1].substring(clientId.length() + 1);
            return "Client " + clientId + ":" + relativePath;
        }
        return f_path; 
    }

    public long getInfo() {
        return clientId;
    }

    public void connect(String serverIP, String serverPort) {
        sckt = cnxt.createSocket(SocketType.REQ);
        sckt.connect("tcp://" + serverIP + ":" + serverPort);

        
        sckt.send("REGISTER".getBytes(ZMQ.CHARSET), 0);
        byte[] reply = sckt.recv(0);
        String replyMessage = new String(reply, ZMQ.CHARSET);

        
        String[] parts = replyMessage.split(" ", 3); 
        if (parts.length >= 2) {
            clientId = Long.parseLong(parts[1]);
            if (parts.length >= 3) {
                System.out.println(parts[2]); 
            }
        } else {
            System.out.println("Error: Invalid REGISTER_REPLY from server.");
        }
    }

    public void disconnect() {
        sckt.send("QUIT".getBytes(ZMQ.CHARSET), 0);
        sckt.close();
        cnxt.destroy();
    }

    private String serializeWordFreq(HashMap<String, Long> wordFrequencies) {
        StringBuilder sb = new StringBuilder();
        for (String wrd : wordFrequencies.keySet()) {
            sb.append(wrd).append(":").append(wordFrequencies.get(wrd)).append("$");
        }
        return sb.toString();
    }
}