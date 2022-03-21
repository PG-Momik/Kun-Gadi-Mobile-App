package com.example.sixth.Models;

import org.json.JSONArray;

import java.util.ArrayList;

public class RouteInfo {
    private String sn;
    private String id;
    private String route_no;
    private String path;
    private String start;
    private String node;
    private String end;
    private JSONArray coordinates;
    private String lat;
    private String lng;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoute_no() {
        return route_no;
    }

    public void setRoute_no(String route_no) {
        this.route_no = route_no;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public JSONArray getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(JSONArray coordinates) {
        this.coordinates = coordinates;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
