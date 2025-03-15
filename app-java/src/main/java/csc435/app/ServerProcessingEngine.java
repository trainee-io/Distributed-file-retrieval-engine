package csc435.app;

import java.util.ArrayList;
import org.zeromq.ZContext;

public class ServerProcessingEngine {
    private IndexStore store;
    private ZContext cnxt;
    private ZMQProxyWorker prxy_wrkr;
    private Thread prxy_thrd;
    private ArrayList<String> connect_clnts = new ArrayList<>();

    public ServerProcessingEngine(IndexStore store) {
        this.store = store;
        this.cnxt = new ZContext();
    }

    public void initialize(int srvr_prt, int num_wrkr_thrds) {
        prxy_wrkr = new ZMQProxyWorker(cnxt, "*", Integer.toString(srvr_prt), num_wrkr_thrds, this);
        prxy_thrd = new Thread(prxy_wrkr);
        prxy_thrd.start();
    }

    public void shutdown() {
        cnxt.destroy();
        try {
            prxy_thrd.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void listConnectedClients() {
        System.out.println("Connected Clients:");
        for (String clnt : connect_clnts) {
            System.out.println(clnt);
        }
    }

    public void addConnectedClient(String clientInfo) {
        connect_clnts.add(clientInfo);
    }
}
