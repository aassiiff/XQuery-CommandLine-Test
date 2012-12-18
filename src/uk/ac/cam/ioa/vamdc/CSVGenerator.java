/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;



import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author aakram
 */
public class CSVGenerator {

    public static void main(String argv[]) throws Exception {
        String returnStatement = "";

        String[] returnVar = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};

        for (int i = 0; i < returnVar.length; i++) {
            if (i == returnVar.length - 1) {
                returnStatement = returnStatement + returnVar[i];
            } else {
                returnStatement = returnStatement + returnVar[i] + ", \"</td><td>\", ";
            }
        }

        System.out.println(returnStatement);

        //csvToJsonTest();
        
        csvToHTML();
    }

    public static void csvToJsonTest() throws Exception {
        
         String csv = "lastName,dob,age,hobby,firstName\n" + 
          "Kasireddi,Thu May 06 00:00:00 IST 2010,2,Singing,Sriram\n" + 
          "Kasireddi,Mon Sep 06 00:00:00 IST 1982,29,Painting,Sudhakar";

        StringBuilder content = new StringBuilder();

        BufferedReader in = new BufferedReader(new FileReader("/opt/jboss/VAMDCData/csv/aaf4fbd3-28cc-4cde-b9bc-f8793b63e9c2.csv"));

        String str;
        while ((str = in.readLine()) != null) {
            if(str.trim().length() > 0) {
                content.append(str + "\n");
            } else  {
                // do nothing
            }
        }
        
        
        //System.out.println(content.toString());

        JSONArray array = CDL.toJSONArray(content.toString());
        System.out.println(array.toString(2)); //pretty print with indent

    }
    
    private static void csvToHTML() throws Exception {
        
        StringBuilder content = new StringBuilder();

        BufferedReader in = new BufferedReader(new FileReader("/opt/jboss/VAMDCData/csv/aaf4fbd3-28cc-4cde-b9bc-f8793b63e9c2.csv"));
        
        String str;
        while ((str = in.readLine()) != null) {
            if(str.trim().length() > 0) {
                str = "<tr><td>" + str.replaceAll(",", "</td><td>") + "</td></tr> \n";
                content.append(str + "\n");
            } else  {
                // do nothing
            }
        }
        //System.out.println(content.toString());
    }
}
