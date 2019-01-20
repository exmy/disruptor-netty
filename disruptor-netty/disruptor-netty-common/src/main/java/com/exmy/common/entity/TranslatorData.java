package com.exmy.common.entity;

import java.io.Serializable;

/**
 * Created by yiyuan on 2019/1/17 16:38.
 */
public class TranslatorData implements Serializable {
    private static final long serialVersionUID = -5775885601059005075L;

    private String id;
    private String name;
    private String message;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
