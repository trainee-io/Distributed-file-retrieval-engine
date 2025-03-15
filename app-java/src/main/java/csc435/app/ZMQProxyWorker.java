package csc435.app;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ZMQProxyWorker implements Runnable {
    private ZContext cnxt;
    private String addr;
    private String prt;
    private int threads_num;
    private ServerProcessingEngine eng;

    public ZMQProxyWorker(ZContext context, String address, String port, int num_wrkr_thrd, ServerProcessingEngine engine) {
        this.cnxt = context;
        this.addr = address;
        this.prt = port;
        this.threads_num = num_wrkr_thrd;
        this.eng = engine;
    }

    
    public void run() {
        ZMQ.Socket soc_router = cnxt.createSocket(SocketType.ROUTER);
        ZMQ.Socket soc_dealer = cnxt.createSocket(SocketType.DEALER);

        soc_router.bind("tcp://" + addr + ":" + prt);
        soc_dealer.bind("inproc://workers");

        for (int i = 0; i < threads_num; i++) {
            Thread thread_wrkr = new Thread(new ServerWorker(new IndexStore(), cnxt, eng));
            thread_wrkr.start();
        }

        ZMQ.proxy(soc_router, soc_dealer, null);

        soc_router.close();
        soc_dealer.close();
    }
}
