package csc435.app;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerWorker implements Runnable {
    private IndexStore str;
    private ZContext cnxt;
    private ServerProcessingEngine eng;
    private static long nxt_clnt_id = 1; 

    public ServerWorker(IndexStore store, ZContext context, ServerProcessingEngine engine) {
        this.str = store;
        this.cnxt = context;
        this.eng = engine;
    }

    @Override
    public void run() {
        ZMQ.Socket sckt = cnxt.createSocket(SocketType.REP);
        sckt.connect("inproc://workers");

        while (true) {
            byte[] bffr = sckt.recv(0);
            String msg = new String(bffr, ZMQ.CHARSET);

            if (msg.startsWith("REGISTER")) {
              
                long client_id = nxt_clnt_id++;
                String info_client = "Client " + client_id;
                eng.addConnectedClient(info_client); 
                String reply = "REGISTER_REPLY " + client_id + " Connection successful!";
                sckt.send(reply.getBytes(ZMQ.CHARSET), 0);
            } else if (msg.startsWith("INDEX")) {
             
                String[] prt = msg.split(" ", 4);
                long id_client = Long.parseLong(prt[1]);
                String doc_path = prt[2];
                HashMap<String, Long> wrd_frq = deserializeWordFreq(prt[3]);

                long doc_num = str.putDocument(id_client + "_" + doc_path);
                str.updateIndex(doc_num, wrd_frq);

                sckt.send("INDEX_REPLY".getBytes(ZMQ.CHARSET), 0);
            } else if (msg.startsWith("SEARCH")) {
           
                String[] prt = msg.split(" ", 2);
                String[] tmr = prt[1].split(" AND ");

                ArrayList<DocFreqPair> total_resukts = new ArrayList<>();
                for (String term : tmr) {
                    ArrayList<DocFreqPair> results_wrds = str.lookupIndex(term);
                    if (total_resukts.isEmpty()) {
                        total_resukts = results_wrds;
                    } else {
                        total_resukts = intersectResults(total_resukts, results_wrds);
                    }
                }

                total_resukts.sort((a, b) -> Long.compare(b.wrd_frq, a.wrd_frq));

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Math.min(10, total_resukts.size()); i++) {
                    String doc_path = str.getDocument(total_resukts.get(i).doc_num);
                    sb.append(doc_path).append(":").append(total_resukts.get(i).wrd_frq).append(";");
                }

                sckt.send(("SEARCH_REPLY " + total_resukts.size() + " " + sb.toString()).getBytes(ZMQ.CHARSET), 0);
            } else if (msg.startsWith("QUIT")) {
                break;
            } else {
                sckt.send("ERROR".getBytes(ZMQ.CHARSET), 0);
            }
        }

        sckt.close();
    }

    private HashMap<String, Long> deserializeWordFreq(String serialize) {
        HashMap<String, Long> word_frq = new HashMap<>();
        String[] pairs = serialize.split("\\$");
        for (String pairss : pairs) {
            String[] value_key = pairss.split(":");
            if (value_key.length == 2) {
                word_frq.put(value_key[0], Long.parseLong(value_key[1]));
            }
        }
        return word_frq;
    }

    private ArrayList<DocFreqPair> intersectResults(ArrayList<DocFreqPair> l1, ArrayList<DocFreqPair> l2) {
        ArrayList<DocFreqPair> res = new ArrayList<>();
        for (DocFreqPair p1 : l1) {
            for (DocFreqPair p2 : l2) {
                if (p1.doc_num == p2.doc_num) {
                    res.add(new DocFreqPair(p1.doc_num, p1.wrd_frq + p2.wrd_frq));
                }
            }
        }
        return res;
    }
}
