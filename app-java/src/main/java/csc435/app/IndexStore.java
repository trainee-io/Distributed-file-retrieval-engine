package csc435.app;

import java.util.ArrayList;
import java.util.HashMap;

class DocFreqPair {
    public long doc_num;
    public long wrd_frq;

    public DocFreqPair(long documentNumber, long wordFrequency) {
        this.doc_num = documentNumber;
        this.wrd_frq = wordFrequency;
    }
}

public class IndexStore {
    private HashMap<String, Long> documentMap;
    private HashMap<String, ArrayList<DocFreqPair>> word_invrt_indx;
    private final Object docLock = new Object();
    private final Object termIndexLock = new Object();

    public IndexStore() {
        documentMap = new HashMap<>();
        word_invrt_indx = new HashMap<>();
    }

    public long putDocument(String documentPath) {
        synchronized (docLock) {
            if (documentMap.containsKey(documentPath)) {
                return documentMap.get(documentPath);
            }

            long documentNumber = documentMap.size() + 1;
            documentMap.put(documentPath, documentNumber);
            return documentNumber;
        }
    }

    public String getDocument(long documentNumber) {
        synchronized (docLock) {
            for (String path : documentMap.keySet()) {
                if (documentMap.get(path) == documentNumber) {
                    return path;
                }
            }
            return "";
        }
    }

    public void updateIndex(long doc_num, HashMap<String, Long> wrd_frq) {
        synchronized (termIndexLock) {
            for (String trm : wrd_frq.keySet()) {
                ArrayList<DocFreqPair> prs = word_invrt_indx.getOrDefault(trm, new ArrayList<>());
                prs.add(new DocFreqPair(doc_num, wrd_frq.get(trm)));
                word_invrt_indx.put(trm, prs);
            }
        }
    }

    public ArrayList<DocFreqPair> lookupIndex(String term) {
        synchronized (termIndexLock) {
            return word_invrt_indx.getOrDefault(term, new ArrayList<>());
        }
    }
}