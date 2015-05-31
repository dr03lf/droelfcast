package at.droelf.droelfcast.backend.model;

import java.io.File;

public class FeedResponse{
        private File file;
        private String url;

        public FeedResponse(File file, String url) {
            this.file = file;
            this.url = url;
        }

        public File getFile() {
            return file;
        }

        public String getUrl() {
            return url;
        }
    }