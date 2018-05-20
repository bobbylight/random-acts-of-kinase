package org.sgc.rak.reps;

import java.util.Collections;
import java.util.List;

/**
 * Represents a page of data in a resource collection.
 *
 * @param <T> The type of data being paged through.
 */
public class PagedDataRep<T> {

    private List<T> data;
    private long start;
    private int count;
    private long total;

    public PagedDataRep() {
        this(Collections.emptyList(), 0, 0);
    }

    public PagedDataRep(List<T> data, long start, long total) {
        this.data = data;
        this.start = start;
        this.count = data.size();
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        this.count = data.size();
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
