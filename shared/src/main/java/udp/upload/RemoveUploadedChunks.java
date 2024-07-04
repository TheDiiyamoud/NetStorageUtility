package udp.upload;

import java.io.File;

public class RemoveUploadedChunks implements Runnable{

    private final String[] chunkNames;
    public RemoveUploadedChunks(String[] chunkNames) {
        this.chunkNames = chunkNames;
    }

    @Override
    public void run() {
        for (String chunkName: chunkNames) {
            File f = new File(chunkName);
            f.delete();

        }
    }
}
