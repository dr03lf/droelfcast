package at.droelf.droelfcast.common;

import java.io.File;
import java.io.IOException;

import timber.log.Timber;

public class FileUtils {

        public static void createFileDir(File fileDir) throws IOException {
            if(fileDir != null && fileDir.isDirectory() && !fileDir.exists()) {

                final boolean mkdirs = fileDir.mkdirs();
                if (!mkdirs) {
                    IOException ioException = new IOException("Failed to create dir: " + fileDir.getAbsolutePath());
                    Timber.e(ioException, "Error creating feed feedCache dirs");
                    throw ioException;
                }
            }
        }
}
