package com.merati.project.geopost;

/**
 * Created by fawerg on 1/26/18.
 */

public class HistElement {
    public String status;
    public String tmst;

    public HistElement(String status, String tmst){
        this.status=status;
        this.tmst = tmst;
    }

    public String getStatus(){
        return status;
    }

    public String getTmst(){
        return tmst;
    }
}
