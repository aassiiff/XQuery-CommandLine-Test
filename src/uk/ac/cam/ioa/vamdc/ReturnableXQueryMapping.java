/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author aakram
 */
public class ReturnableXQueryMapping {
    
    private Properties properties;
    
    public ReturnableXQueryMapping() {
        initializeHashMap();
    }
    
    private void initializeHashMap() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("returnableMapping.properties"));
            System.out.println("properties.size(): " + properties.size());
            
            //System.out.println(properties.getProperty("AtomSymbol"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getPropertyValue(String property) {
        String value = properties.getProperty(property);
        //System.out.println(property + "  " + value);
        return value;
    }
    
    public HashMap<String, String> randomXQueryMapping(int number) {
        HashMap<String, String> XQueryMappingArray = new HashMap<String, String>();
        
        HashMap<String, String> propMap = new HashMap<String, String>((Map) properties);
        Object[] propertiesArray = propMap.keySet().toArray();
        
        Random generator = new Random();
        
        for (int i = 0; i < number; i++) {
            int randomIndex = generator.nextInt(properties.size());
            
            String tempKey = propertiesArray[randomIndex].toString();
            
            if (propMap.containsKey(tempKey)) {
                
                String tempValue = propMap.get(tempKey).toString();
                
                //System.out.print("randomXQueryMapping: " + tempKey + "  " +  tempValue);
                
                XQueryMappingArray.put(tempKey, tempValue);
                
                 //System.out.print("randomXQueryMapping: " + XQueryMappingArray.get(tempKey));
            }
        }
        XQueryMappingArray.put("AtomStateEnergy", propMap.get("AtomStateEnergy").toString());
        XQueryMappingArray.put("AtomSymbol", propMap.get("AtomSymbol").toString());
        XQueryMappingArray.put("AtomSpeciesID", propMap.get("AtomSpeciesID").toString());
        
        return XQueryMappingArray;
    }
    
    public HashMap<String, String> radTransXQueryMapping(int number) {
        HashMap<String, String> XQueryMappingArray = new HashMap<String, String>();
        
        HashMap<String, String> propMap = new HashMap<String, String>((Map) properties);
        Object[] propertiesArray = propMap.keySet().toArray();
        
        Random generator = new Random();
        
        for (int i = 0; i < number; i++) {
            int randomIndex = generator.nextInt(properties.size());
            
            String tempKey = propertiesArray[randomIndex].toString();
            
            if (propMap.containsKey(tempKey)) {
                
                String tempValue = propMap.get(tempKey).toString();
                
                System.out.print("radTransXQueryMapping: " + tempKey + "  " +  tempValue);
                
                XQueryMappingArray.put(tempKey, tempValue);           
                 //System.out.print("randomXQueryMapping: " + XQueryMappingArray.get(tempKey));
            }
        }
        XQueryMappingArray.put("RadTransEnergy", propMap.get("RadTransEnergy").toString());
        XQueryMappingArray.put("RadTransWavelength", propMap.get("RadTransWavelength").toString());
        //XQueryMappingArray.put("AtomSpeciesID", propMap.get("AtomSpeciesID").toString());
        
        return XQueryMappingArray;
    }
    
}
