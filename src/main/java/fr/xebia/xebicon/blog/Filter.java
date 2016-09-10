package fr.xebia.xebicon.blog;

import java.util.List;

public class Filter {
    public final String field;
    public final List<String> values;

    public Filter(String field, List<String> values) {
        this.field = field;
        this.values = values;
    }
}
