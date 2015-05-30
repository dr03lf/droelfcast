package at.droelf.droelfcast.feedparser.model.parsed.image;

import at.droelf.droelfcast.common.O;

public class ImageBundle {

    private final O<ImageItunes> imageItunes;
    private final O<ImageRss> imageRss;

    public ImageBundle(O<ImageItunes> imageItunes, O<ImageRss> imageRss) {
        this.imageItunes = imageItunes;
        this.imageRss = imageRss;
    }

    public O<ImageItunes> getImageItunes() {
        return imageItunes;
    }

    public O<ImageRss> getImageRss() {
        return imageRss;
    }
}
