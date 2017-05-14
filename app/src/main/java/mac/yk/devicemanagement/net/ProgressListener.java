package mac.yk.devicemanagement.net;

public interface ProgressListener {
        void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish);
    }