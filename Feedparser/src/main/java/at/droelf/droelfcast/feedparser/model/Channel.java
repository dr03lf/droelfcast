package at.droelf.droelfcast.feedparser.model;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import at.droelf.droelfcast.feedparser.model.annotation.Atom;
import at.droelf.droelfcast.feedparser.model.annotation.Itunes;
import at.droelf.droelfcast.feedparser.model.annotation.Rss;
import at.droelf.droelfcast.feedparser.model.item.Item;


@Root(strict = false)
public class Channel {



    @Rss
    @Atom
    @ElementList(entry = "link", required = false, inline = true)
    private List<Link> links;

    @Rss
    @Itunes
    @ElementList(entry = "image", required = false, inline = true)
    private List<Image> images;


    @Itunes
    @Atom
    @ElementList(entry = "category", required = false, inline = true)
    private List<Category> categories;


    @Itunes
    @Atom
    @Rss
    @ElementList(entry = "item", required = false, inline = true)
    private List<Item> items;


    // Rss only stuff
    @Rss
    @Element
    private String title;

    @Rss
    @Element
    private String description;

    @Rss
    @Element(required = false)
    private String language;

    @Rss
    @Element(required = false)
    private String copyright;

    @Rss
    @Element(required = false)
    private String managingEditor;

    @Rss
    @Element(required = false)
    private String webMaster;

    @Rss
    @Element(required = false)
    private String pubDate;

    @Rss
    @Element(required = false)
    private String lastBuildDate;


    @Rss
    @Element(required = false)
    private String generator;

    @Rss
    @Element(required = false)
    private String docs;

    @Rss
    @Element(required = false)
    private String cloud;

    @Rss
    @Element(required = false)
    private String ttl;

    @Rss
    @Element(required = false)
    private String textInput;

    @Rss
    @Element(required = false)
    private String skipHours;

    @Rss
    @Element(required = false)
    private String skipDays;


    // itunes only stuff
    @Itunes
    @Element(required = false)
    private String author;

    @Itunes
    @Element(required = false)
    private String block;

    @Itunes
    @Element(required = false)
    private String explicit;

    @Itunes
    @Element(required = false)
    private String complete;

    @Itunes
    @Element(required = false, name = "new-feed-url")
    private String newFeedUrl;

    @Itunes
    @Element(required = false)
    private Owner owner;

    @Itunes
    @Element(required = false)
    private String subtitle;

    @Itunes
    @Element(required = false)
    private String summary;

}
