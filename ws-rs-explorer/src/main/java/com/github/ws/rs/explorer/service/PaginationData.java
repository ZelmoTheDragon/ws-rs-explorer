package com.github.ws.rs.explorer.service;

import java.util.ArrayList;
import java.util.List;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;

/**
 * Paginated search result.
 *
 * @param <T> Data type
 */
@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public final class PaginationData<T> {

    /**
     * Data.
     */
    @JsonbProperty("data")
    private List<T> data;

    /**
     * Total number of element.
     */
    @JsonbProperty("size")
    private long size;

    /**
     * Page size.
     */
    @JsonbProperty("pageSize")
    private long pageSize;

    /**
     * Page number.
     */
    @JsonbProperty("pageNumber")
    private long pageNumber;

    /**
     * Page count.
     */
    @JsonbProperty("pageCount")
    private long pageCount;

    /**
     * Default constructor.
     */
    public PaginationData() {
        this.data = new ArrayList<>();
    }

    // Getters and setters...

    public List<T> getData() {
        return List.copyOf(data);
    }

    public void setData(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }
}
