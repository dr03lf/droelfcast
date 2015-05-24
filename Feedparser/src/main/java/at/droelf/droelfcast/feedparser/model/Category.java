package at.droelf.droelfcast.feedparser.model;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;


@Root(strict = false)
public class Category {

    @Attribute(required = false)
    private String text;

    @Text(required = false)
    private String value;

    //Nested doens't work ;(
    //    @ElementList(required = false, inline = true, entry = "category")
    //    private List<Category> subCategories;

}
