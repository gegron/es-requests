package fr.xebia.xebicon.blog;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Post {

    public static final String POST_INDEX = "xebia";
    public static final String POST_TYPE = "blog";

    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_CREATOR = "creator";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PUBDATE = "pubDate";
    public static final String FIELD_TITLE = "title";

    private List<String> category;
    private String content;
    private String creator;
    private String description;
    private String pubDate;
    private String title;

    @JsonCreator
    public Post(
            @JsonProperty(value = "category") List<String> category,
            @JsonProperty(value = "content") String content,
            @JsonProperty(value = "creator") String creator,
            @JsonProperty(value = "description") String description,
            @JsonProperty(value = "pubDate") String pubDate,
            @JsonProperty(value = "title") String title
    ) {
        this.category = category;
        this.content = content;
        this.creator = creator;
        this.description = description;
        this.pubDate = pubDate;
        this.title = title;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Post{" +
                "title='" + title + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }
}
