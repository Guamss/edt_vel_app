package com.example.edt.modele;

import android.text.Html;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cours implements Serializable
{
    private Date startHour;
    private Date endHour;
    private String desc;
    private String eventCategory;
    private String modules;

    public Cours(String startHourParam, String endHourParam, String descParam, String eventCategoryParam, String modulesParam) throws ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.startHour = format.parse(startHourParam);
        this.endHour = format.parse(endHourParam);
        this.desc = Html.fromHtml(descParam, Html.FROM_HTML_MODE_LEGACY).toString();
        this.desc = this.desc.replaceAll("\\\\r\\\\n", "").replaceAll("<br />", "\n").trim(); // On enlève les caractères indésirable
        this.eventCategory = eventCategoryParam;
        this.modules = modulesParam;
    }

    public Date getStartHour() {
        return startHour;
    }

    public Date getEndHour() {
        return endHour;
    }

    public String getDesc() {
        return desc;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getModules() {
        return modules;
    }

    public String toString()
    {
        return "------------------------------------------------------------------\n" + this.startHour.getHours() + ":" + this.startHour.getMinutes() + " - " + this.endHour.getHours() + ":" + this.endHour.getMinutes() + "\n" + this.getDesc();
    }
}