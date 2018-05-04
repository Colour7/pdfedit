package com.color.pdf.pdfsupport;

import java.io.Serializable;

/**
 * 用来保存关键字新建的对象
 * Created by Color on 2017/12/4.
 */
public class MatchItem implements Serializable{
    private static final long serialVersionUID = -8157143337136920338L;

    private Integer pageNum;
    private Float x;
    private Float y;
    private Float pageWidth;
    private Float pageHeight;
    private String content;

    private String newContent;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Float pageWidth) {
        this.pageWidth = pageWidth;
    }

    public Float getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Float pageHeight) {
        this.pageHeight = pageHeight;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    @Override
    public String toString() {
        return "MatchItem [pageNum=" + pageNum + ", x=" + x + ", y=" + y
                + ", pageWidth=" + pageWidth + ", pageHeight=" + pageHeight
                + ", content=" + content + "]";
    }
}
