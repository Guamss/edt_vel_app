package com.example.edt.modele;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class JSONParser implements Serializable
{
    private String string;
    public JSONParser(String stringParam)
    {
        this.string = stringParam;
    }


    public ArrayList<Cours> toCoursArray() throws ParseException {
        HashMap<String, String> actualCours = new HashMap<>();
        ArrayList<Cours> coursList = new ArrayList<>();
        if (!this.string.equals("[]")) // Si le tableau reçu n'est pas vide
        {
            String[] objects = this.string.substring(2, string.length() - 1).replace("\"", "").split("\\},\\{"); // j'enlève les deux premier caractères "[{" et je split les différents objets
            String startHour = "";
            String endHour = "";
            String desc;
            String eventCategory;
            String modules;
            for (String object : objects) {
                actualCours.clear();
                String[] pairs = object.split(",");
                pairs[0] = null; // l'élément 0 concerne l'id du cours, il ne sert pas et m'empêche de parser correctement
                for (String pair : pairs) {
                    if (pair != null) {
                        if (Pattern.matches("start:.*", pair)) {
                            startHour = pair.substring(6, pair.length() - 1); // on enlève la partie "start:" pour garder uniquement l'heure
                            actualCours.put("start", startHour);
                        } else if (Pattern.matches("end:.*", pair)) {
                            endHour = pair.substring(4, pair.length() - 1); // on enlève la partie "end:" pour garder uniquement l'heure
                            actualCours.put("end", endHour);
                        } else {
                            String[] keyValue = pair.split(":"); // l'élément 0 est la clé et le 1 la valeur
                            actualCours.put(keyValue[0], keyValue[1]);
                        }
                    }
                }
                desc = actualCours.get("description");
                eventCategory = actualCours.get("eventCategory");
                modules = actualCours.get("modules");
                coursList.add(new Cours(startHour, endHour, desc, eventCategory, modules));
            }
        }
        return coursList;
    }

}