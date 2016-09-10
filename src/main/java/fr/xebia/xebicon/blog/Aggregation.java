package fr.xebia.xebicon.blog;

import java.util.List;
import java.util.Objects;

public class Aggregation {
    public final String field;
    public final List<String> keys;

    public Aggregation(String field, List<String> values) {

        this.field = field;
        this.keys = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aggregation that = (Aggregation) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(keys, that.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, keys);
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "field='" + field + '\'' +
                ", keys=" + keys +
                '}';
    }
}
