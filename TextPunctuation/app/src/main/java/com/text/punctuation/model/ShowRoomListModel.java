package com.text.punctuation.model;

import java.util.List;

/**
 * Created by bt on 2018/2/7.
 */

public class ShowRoomListModel {
    List<ShowRoomModel> showroomList;
    private String no;
    private String plan;
    private int imageWidth;//图片的原始宽度
    private int imageHeight;//图片的原始高度
    private int imageFullWidth;//适配屏幕后图片的宽度
    private int imageFullHeight;//适配屏幕后图片的高度

    public List<ShowRoomModel> getShowroomList() {
        return showroomList;
    }

    public void setShowroomList(List<ShowRoomModel> showroomList) {
        this.showroomList = showroomList;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageFullWidth() {
        return imageFullWidth;
    }

    public void setImageFullWidth(int imageFullWidth) {
        this.imageFullWidth = imageFullWidth;
    }

    public int getImageFullHeight() {
        return imageFullHeight;
    }

    public void setImageFullHeight(int imageFullHeight) {
        this.imageFullHeight = imageFullHeight;
    }
}
