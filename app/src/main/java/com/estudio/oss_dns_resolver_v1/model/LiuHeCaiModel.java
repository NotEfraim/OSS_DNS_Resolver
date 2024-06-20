package com.estudio.oss_dns_resolver_v1.model;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LiuHeCaiModel implements Serializable {
    private List<String> reds = new ArrayList<>();
    private List<String> blue = new ArrayList<>();
    private List<String> green = new ArrayList<>();

    public List<String> getReds() {
        return reds;
    }

    public void setReds(List<String> reds) {
        this.reds = reds;
    }

    public List<String> getBlue() {
        return blue;
    }

    public void setBlue(List<String> blue) {
        this.blue = blue;
    }

    public List<String> getGreen() {
        return green;
    }

    public void setGreen(List<String> green) {
        this.green = green;
    }

}
