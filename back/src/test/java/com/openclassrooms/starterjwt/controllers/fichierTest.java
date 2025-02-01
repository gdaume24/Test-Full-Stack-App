package com.openclassrooms.starterjwt.controllers;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class fichierTest {

    @Test
    public void testDateToString() throws Exception {
        // Créer une date avec un format spécifique
        Date date = new Date(1852063200000L);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormatted = dateFormat.format(date);

        // Afficher le résultat
        System.out.println("Date toString(): " + dateFormatted);
    }
    
}
