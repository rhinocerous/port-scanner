package models.output;

import models.BaseModel;

import java.util.List;

/**
 * Created by Aaron on 1/1/2015.
 */
public class ListResponse extends BaseModel
{
    protected boolean success;
    protected Integer page;
    protected Integer count;
    protected Integer totalItems;
    protected List<BaseModel> items;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public List<BaseModel> getItems() {
        return items;
    }

    public void setItems(List<BaseModel> items) {
        this.items = items;
    }
}
