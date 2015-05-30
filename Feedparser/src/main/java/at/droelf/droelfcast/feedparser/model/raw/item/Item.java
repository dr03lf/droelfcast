package at.droelf.droelfcast.feedparser.model.raw.item;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import at.droelf.droelfcast.feedparser.model.raw.Image;
import at.droelf.droelfcast.feedparser.model.raw.RawLink;
import at.droelf.droelfcast.feedparser.model.raw.annotation.Atom;
import at.droelf.droelfcast.feedparser.model.raw.annotation.Itunes;
import at.droelf.droelfcast.feedparser.model.raw.annotation.Podlove;
import at.droelf.droelfcast.feedparser.model.raw.annotation.Rss;

@Root(strict = false)
public class Item {

    @Rss
    @Atom
    @ElementList(entry = "link", required = false, inline = true)
    private List<RawLink> links;


    //Rss only
    @Rss
    @Element(required = false)
    private String title;

    @Rss
    @Element(required = false)
    private String description;

    @Rss
    @Element(required = false)
    private String author;

    @Rss
    @Element(required = false)
    private String category;

    @Rss
    @Element(required = false)
    private String comments;

    @Rss
    @Element(required = false)
    private Enclosure enclosure;

    @Rss
    @Element(required = false)
    private at.droelf.droelfcast.feedparser.model.raw.item.Guid guid;

    @Rss
    @Element(required =  false)
    private String pubDate;

    @Rss
    @Element(required = false)
    private at.droelf.droelfcast.feedparser.model.raw.item.Source source;

    //iTunes
    @Itunes
    @Element(required = false)
    private String block;

    @Itunes
    @ElementList(entry = "image", required = false, inline = true)
    private List<Image> images;

    @Itunes
    @Element(required = false)
    private String duration;

    @Itunes
    @Element(required = false)
    private String explicit;

    @Itunes
    @Element(required = false)
    private String subtitle;

    @Itunes
    @Element(required = false)
    private String summary;


    //Podlove Simple Chapters
    @Podlove
    @ElementList(required = false, entry = "chapters")
    private List<at.droelf.droelfcast.feedparser.model.raw.item.Chapter> chapters;


    public List<RawLink> getLinks() {
        return links;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getComments() {
        return comments;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public at.droelf.droelfcast.feedparser.model.raw.item.Guid getGuid() {
        return guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public at.droelf.droelfcast.feedparser.model.raw.item.Source getSource() {
        return source;
    }

    public String getBlock() {
        return block;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getDuration() {
        return duration;
    }

    public String getExplicit() {
        return explicit;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getSummary() {
        return summary;
    }

    public List<at.droelf.droelfcast.feedparser.model.raw.item.Chapter> getChapters() {
        return chapters;
    }
}
