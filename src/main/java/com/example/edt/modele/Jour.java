package com.example.edt.modele;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Jour implements Serializable
{
    private ArrayList<Cours> coursList;
    private String federationId;
    private String endDate;
    private String startDate;
    private final String resType = "103"; // par défaut
    private final String calView = "agendaDay"; // pour récupérer le jour
    private final String colourScheme = "3"; // par défaut

    public Jour(String federationIdParam, String startDateParam, String endDateParam) throws Exception
    {

        // Préparation des paramètres
        this.coursList = new ArrayList<>();
        this.startDate = startDateParam;
        this.endDate = endDateParam;
        this.federationId = federationIdParam;

        Map<String, String> params = new HashMap<>();
        params.put("start", startDate);
        params.put("end", endDate);
        params.put("resType", "103");
        params.put("calView", "agendaDay");
        params.put("federationIds[]", federationId);
        params.put("colourScheme", "3");
        StringJoiner sj = new StringJoiner("&");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);


        URL url = new URL("https://edt.iut-velizy.uvsq.fr/Home/GetCalendarData"); // Envoi d'une requête POST à /Home/GetCalendarData
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01"); // Réponse sous format JSON

        // Envoi de la requête POST
        try (OutputStream os = con.getOutputStream()) {
            os.write(out);
        }

        // Lecture de la réponse du serveur
        int status = con.getResponseCode();
        if (status == 200)
        {
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

            this.coursList = new JSONParser(response.toString()).toCoursArray();
            Collections.sort(this.coursList);

        }
        else
        {
            throw new Exception("Erreur de communication avec le serveur : " + status);
        }
    }

    public ArrayList<Cours> getCoursList()
    {
        return this.coursList;
    }

    public String getFederationId()
    {
        return this.federationId;
    }

    public String getEndDate()
    {
        return this.endDate;
    }

    public String getStartDate()
    {
        return this.startDate;
    }

    public String getResType()
    {
        return this.resType;
    }

    public String getCalView()
    {
        return this.calView;
    }

    public String getColourScheme()
    {
        return this.colourScheme;
    }
}