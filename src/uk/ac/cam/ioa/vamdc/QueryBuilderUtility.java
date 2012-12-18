/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author aakram
 */
public class QueryBuilderUtility {

    String flworQuery = "";

    public String atomOnlyQuery(Object[] valuesArray, String databaseName) {
        String returnVar[] = new String[valuesArray.length];
        flworQuery = "";

        flworQuery = flworQuery + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n";
        flworQuery = flworQuery + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n";


        for (int i = 0; i < valuesArray.length; i++) {
            flworQuery = flworQuery + valuesArray[i];
            int index = valuesArray[i].toString().indexOf("$");
            int index2 = valuesArray[i].toString().indexOf(" ", index);

            returnVar[i] = valuesArray[i].toString().substring(index, index2);

            System.out.println(valuesArray[i].toString().substring(index, index2));

        }

        String returnStatement = "return concat(";

        for (int i = 0; i < returnVar.length; i++) {
            if (i == returnVar.length - 1) {
                returnStatement = returnStatement + returnVar[i] + ", \"\t\" ";
            } else {
                returnStatement = returnStatement + returnVar[i] + ", \"\t\", ";
            }
        }

        returnStatement = returnStatement + ")";
        flworQuery = flworQuery + returnStatement;

        return flworQuery;

    }

    public String atomRadiativeQuery(HashMap<String, String> XQueryMappingArray, String databaseName, boolean upperState) {
        
        Object[] keysArray = XQueryMappingArray.keySet().toArray();
        Object[] valuesArray = XQueryMappingArray.values().toArray();

        ArrayList<String> returnVar = new ArrayList<String>();

        ArrayList<String> forQuery = new ArrayList<String>();
        ArrayList<String> letQuery = new ArrayList<String>();

        flworQuery = "";

        flworQuery = flworQuery + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n";
        flworQuery = flworQuery + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes \n";

        flworQuery = flworQuery + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n";

        if (upperState) {
            flworQuery = flworQuery + " for $upperStateRef in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n";
            /*
            System.out.print(keysArray.length + " ");
            if (XQueryMappingArray.containsKey("RadTransFinalStateRef")) {
                XQueryMappingArray.remove("RadTransFinalStateRef");
            }
            System.out.println(keysArray.length + " ");
             *
             */
            for (int i = 0; i < keysArray.length; i++) {
                String tempReturnable = keysArray[i].toString();

                if (tempReturnable.startsWith("Atom")) {
                    String flworQueryValue = (String) XQueryMappingArray.get(tempReturnable);

                    if (flworQueryValue.trim().startsWith("for")) {
                        //System.out.println("for: " + flworQueryValue);
                        forQuery.add(flworQueryValue);
                    } else if (flworQueryValue.trim().startsWith("let")) {
                        //System.out.println("let: " + flworQueryValue);
                        letQuery.add(flworQueryValue);
                    }
                    

                    int index = flworQueryValue.toString().indexOf("$");
                    int index2 = flworQueryValue.toString().indexOf(" ", index);

                    returnVar.add(flworQueryValue.toString().substring(index, index2));
                } else if (tempReturnable.startsWith("RadTrans")) {

                    if (tempReturnable.equalsIgnoreCase("RadTransFinalStateRef")) {
                        // do nothing
                    } else {
                        String flworQueryValue = valuesArray[i].toString(); //XQueryMappingArray.get(tempReturnable);

                        //System.out.print(tempReturnable + " " + XQueryMappingArray.containsKey(tempReturnable) + " " );
                        //System.out.println(flworQueryValue);

                        String tempUpdatedQuery = getUpdatedRadTransQuery(flworQueryValue, " $upperStateRef/*");
                        if (tempUpdatedQuery.trim().startsWith("for")) {
                            //System.out.println("for: " + tempUpdatedQuery);
                            forQuery.add(tempUpdatedQuery);
                        } else if (tempUpdatedQuery.trim().startsWith("let")) {
                            //System.out.println("let: " + tempUpdatedQuery);
                            letQuery.add(tempUpdatedQuery);

                        }
                        //flworQuery = flworQuery + getUpdatedRadTransQuery(flworQueryValue, " $upperStateRef/*");


                        int index = flworQueryValue.toString().indexOf("$");
                        int index2 = flworQueryValue.toString().indexOf(" ", index);

                        returnVar.add(flworQueryValue.toString().substring(index, index2));
                    }
                }
            }

        } else if (!upperState) {
            flworQuery = flworQuery + " for $lowerStateRef in $radTrans/Radiative/RadiativeTransition[LowerStateRef=$stateID] \n";
            /*
            System.out.print(keysArray.length + " ");
            if (XQueryMappingArray.containsKey("RadTransInitialStateRef")) {
                XQueryMappingArray.remove("RadTransInitialStateRef");
                System.out.println(keysArray.length + " ");
            }
             */
            
            for (int i = 0; i < keysArray.length; i++) {
                String tempReturnable = keysArray[i].toString();

                if (tempReturnable.startsWith("Atom")) {
                    String flworQueryValue = (String) XQueryMappingArray.get(tempReturnable);

                    if (flworQueryValue.trim().startsWith("for")) {
                        System.out.println("for: " + flworQueryValue);
                        forQuery.add(flworQueryValue);
                    } else if (flworQueryValue.trim().startsWith("let")) {
                        System.out.println("let: " + flworQueryValue);
                        letQuery.add(flworQueryValue);

                    }

                    int index = flworQueryValue.toString().indexOf("$");
                    int index2 = flworQueryValue.toString().indexOf(" ", index);

                    returnVar.add(flworQueryValue.toString().substring(index, index2));
                } else if (tempReturnable.startsWith("RadTrans")) {
                    if (tempReturnable.equalsIgnoreCase("RadTransInitialStateRef")) {
                        // do nothing
                    } else {
                        String flworQueryValue = valuesArray[i].toString(); //XQueryMappingArray.get(tempReturnable);
                        //System.out.print(tempReturnable + " " );
                        //System.out.println(flworQueryValue);

                        String tempUpdatedQuery = getUpdatedRadTransQuery(flworQueryValue, " $lowerStateRef/*");

                        if (tempUpdatedQuery.trim().startsWith("for")) {
                            forQuery.add(tempUpdatedQuery);
                        } else if (tempUpdatedQuery.trim().startsWith("let")) {
                            letQuery.add(tempUpdatedQuery);
                        }

                        //flworQuery = flworQuery + getUpdatedRadTransQuery(flworQueryValue, " $lowerStateRef/*");


                        int index = flworQueryValue.toString().indexOf("$");
                        int index2 = flworQueryValue.toString().indexOf(" ", index);

                        returnVar.add(flworQueryValue.toString().substring(index, index2));
                    }

                }
            }
        }

        Object[] elements = forQuery.toArray();

        for (int i = 0; i < elements.length; i++) {
            flworQuery = flworQuery + elements[i].toString();
        }

        elements = letQuery.toArray();

        for (int i = 0; i < elements.length; i++) {
            flworQuery = flworQuery + elements[i].toString();
        }

        flworQuery = flworQuery + " where $xsams/Isotope/Ion/AtomicState/@stateID=$stateID \n";

        //System.out.println("*****  " + flworQuery);

        String returnStatement = "return concat(";

        //"return concat($stateID, \"\t\",  $speciesRef/../Probability/WeightedOscillatorStrength/Value, \"\t\", $speciesRef/../Probability/TransitionProbabilityA/Value )";

        for (int i = 0; i < returnVar.size(); i++) {
            if (i == returnVar.size() - 1) {
                returnStatement = returnStatement + returnVar.get(i) + ", \"\t\" ";
            } else {
                returnStatement = returnStatement + returnVar.get(i) + ", \"\t\", ";
            }
        }

        returnStatement = returnStatement + ")";
        flworQuery = flworQuery + returnStatement;

        return flworQuery;
    }

    public String atomRadiativeQueryBoth(HashMap<String, String> XQueryMappingArray, String databaseName) {
        Object[] keysArray = XQueryMappingArray.keySet().toArray();
        Object[] valuesArray = XQueryMappingArray.values().toArray();

        ArrayList<String> returnVar = new ArrayList<String>();

        ArrayList<String> forQuery = new ArrayList<String>();
        ArrayList<String> letQuery = new ArrayList<String>();

        flworQuery = "";

        flworQuery = flworQuery + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species \n";
        flworQuery = flworQuery + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes/Radiative/RadiativeTransition \n";

        if (XQueryMappingArray.containsKey("RadTransFinalStateRef")) {
            //XQueryMappingArray.remove("RadTransFinalStateRef");
        }

        if (XQueryMappingArray.containsKey("RadTransInitialStateRef")) {
            //XQueryMappingArray.remove("RadTransInitialStateRef");
        }
        flworQuery = flworQuery + " for $radTransID in $radTrans/@id \n";
        flworQuery = flworQuery + " for $atoms in $xsams/Atoms/Atom \n";

        letQuery.add(" let $lowerStateRef := $radTrans/LowerStateRef \n");
        letQuery.add(" let $upperStateRef := $radTrans/UpperStateRef \n");
        
        letQuery.add(" let $lowerAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef] \n" );
        letQuery.add(" let $upperAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef] \n");

        for (int i = 0; i < keysArray.length; i++) {
            String tempReturnable = keysArray[i].toString();

            if (tempReturnable.startsWith("Atom")) {
                String flworQueryValue = (String) XQueryMappingArray.get(tempReturnable);
                if (tempReturnable.startsWith("AtomState")) {

                    String flworQueryValueUpper = flworQueryValue.replaceAll("\\$stateID", "\\$upperStateRef");
                    String flworQueryValueLower = flworQueryValue.replaceAll("\\$stateID", "\\$lowerStateRef");

                    String flworQueryValueUpperUpdated = getUpdatedAtomQueryNew(flworQueryValueUpper, "$upperAtomicState");
                    String flworQueryValueLowerUpdated = getUpdatedAtomQueryNew(flworQueryValueLower, "$lowerAtomicState");


                    int index = flworQueryValue.toString().indexOf("$");
                    int index2 = flworQueryValue.toString().indexOf(" ", index);

                    String tempReturnableVar = flworQueryValue.substring(index, index2);

                    System.out.println(tempReturnableVar);

                    flworQueryValueUpperUpdated = flworQueryValueUpperUpdated.replaceAll("\\" + tempReturnableVar, "\\" + tempReturnableVar + "Upper");
                    flworQueryValueLowerUpdated = flworQueryValueLowerUpdated.replaceAll("\\" + tempReturnableVar, "\\" + tempReturnableVar + "Lower");

                    if (flworQueryValueUpperUpdated.trim().startsWith("for")) {
                        System.out.print("for: " + flworQueryValueUpperUpdated);
                        forQuery.add(flworQueryValueUpperUpdated);
                    } else if (flworQueryValueUpperUpdated.trim().startsWith("let")) {
                        System.out.print("let: " + flworQueryValueUpperUpdated);
                        letQuery.add(flworQueryValueUpperUpdated);

                    }

                    if (flworQueryValueLowerUpdated.trim().startsWith("for")) {
                        System.out.println("for: " + flworQueryValueLowerUpdated);
                        forQuery.add(flworQueryValueLowerUpdated);
                    } else if (flworQueryValueLowerUpdated.trim().startsWith("let")) {
                        System.out.println("let: " + flworQueryValueLowerUpdated);
                        letQuery.add(flworQueryValueLowerUpdated);

                    }

                    returnVar.add(tempReturnableVar + "Upper");
                    returnVar.add(tempReturnableVar + "Lower");
                } else {
                    String flworQueryValueUpdated = "";
                    if(tempReturnable.endsWith("")){
                    flworQueryValueUpdated = getUpdatedRadTransQuery(flworQueryValue, "$atoms/*");
                    } else if(tempReturnable.endsWith("")){
                        
                    }if(tempReturnable.endsWith("")){
                        
                    }
                    
                    int index = flworQueryValue.toString().indexOf("$");
                    int index2 = flworQueryValue.toString().indexOf(" ", index);

                    String tempReturnableVar = flworQueryValue.substring(index, index2);
                    
                    if (flworQueryValueUpdated.trim().startsWith("for")) {
                        //System.out.println("for: " + flworQueryValueUpdated);
                        forQuery.add(flworQueryValueUpdated);
                    } else if (flworQueryValueUpdated.trim().startsWith("let")) {
                        //System.out.println("let: " + flworQueryValueUpdated);
                        letQuery.add(flworQueryValueUpdated);
                        
                    }
                    returnVar.add(tempReturnableVar);
                }
            } else if (tempReturnable.startsWith("RadTrans")) {
                if (tempReturnable.equalsIgnoreCase("RadTransInitialStateRef")) {
                    // do nothing
                } else if (tempReturnable.equalsIgnoreCase("RadTransFinalStateRef")) {
                    // do nothing
                } else {
                    String flworQueryValue = valuesArray[i].toString(); //XQueryMappingArray.get(tempReturnable);
                    //System.out.print(tempReturnable + " " );
                    //System.out.println(flworQueryValue);

                    String tempUpdatedQuery = getUpdatedRadTransQuery(flworQueryValue, " $radTrans/*");

                    if (tempUpdatedQuery.trim().startsWith("for")) {
                        forQuery.add(tempUpdatedQuery);
                    } else if (tempUpdatedQuery.trim().startsWith("let")) {
                        letQuery.add(tempUpdatedQuery);
                    }

                    //flworQuery = flworQuery + getUpdatedRadTransQuery(flworQueryValue, " $lowerStateRef/*");


                    int index = flworQueryValue.toString().indexOf("$");
                    int index2 = flworQueryValue.toString().indexOf(" ", index);

                    returnVar.add(flworQueryValue.toString().substring(index, index2));
                }
            }

        }

        Object[] elements = forQuery.toArray();

        for (int i = 0; i < elements.length; i++) {
            flworQuery = flworQuery + elements[i].toString();
        }

        elements = letQuery.toArray();

        for (int i = 0; i < elements.length; i++) {
            flworQuery = flworQuery + elements[i].toString();
        }

        flworQuery = flworQuery + " where $radTrans/@id=$radTransID and $atoms/Isotope/Ion/AtomicState/@stateID=$lowerStateRef \n";

        //System.out.println("*****  " + flworQuery);

        String returnStatement = "return concat(";

        //"return concat($stateID, \"\t\",  $speciesRef/../Probability/WeightedOscillatorStrength/Value, \"\t\", $speciesRef/../Probability/TransitionProbabilityA/Value )";

        for (int i = 0; i < returnVar.size(); i++) {
            if (i == returnVar.size() - 1) {
                returnStatement = returnStatement + returnVar.get(i) + ", \"\t\" ";
            } else {
                returnStatement = returnStatement + returnVar.get(i) + ", \"\t\", ";
            }
        }

        returnStatement = returnStatement + ")";
        flworQuery = flworQuery + returnStatement;
        return flworQuery;
    }
    // String tempUpdatedQuery = getUpdatedRadTransQuery(flworQueryValue, " $lowerStateRef/*");
    int counter = 0;

    private String getUpdatedRadTransQuery(String originalValue, String replacementValue) {
        //System.out.append(counter++ + "  " + originalValue);
        String updatedValue = "";

        if (originalValue != null) {

            int index = originalValue.toString().indexOf("$");
            int index2 = originalValue.toString().indexOf(" ", index);

            String tempQueryVariable = originalValue.toString().substring(index + 1, index2);

            //int tempQueryVariableIndex = originalValue.lastIndexOf(tempQueryVariable, index2);

            // Starting from the end of query to find the last Reference
            int tempQueryVariableIndex = originalValue.lastIndexOf(tempQueryVariable, originalValue.length());
            
            String partAfterReplacement = originalValue.substring(tempQueryVariableIndex - 1);
            
            if(partAfterReplacement.startsWith("@")){
                partAfterReplacement = "/" + partAfterReplacement;
            }

            updatedValue = originalValue.substring(0, index2 + 4) + replacementValue + partAfterReplacement;
        }
        System.out.print("getUpdatedRadTransQuery: " + originalValue);
        System.out.println("getUpdatedRadTransQuery: " + updatedValue);
        return updatedValue;
    }
/*
    private String getUpdatedAtomQuery(String originalValue, String replacementValue) {
        //System.out.append(counter++ + "  " + originalValue);
        String updatedValue = "";

        int index = originalValue.toString().indexOf("$xsams");
        int index2 = originalValue.toString().indexOf("/", index);

        updatedValue = originalValue.substring(0, index2 + 1) + replacementValue + originalValue.substring(index2);
        return updatedValue;
    }
     * *
     */
    
    private String getUpdatedAtomQueryNew(String originalValue, String replacementValue) {
        //System.out.append(counter++ + "  " + originalValue);
        String updatedValue = "";

        if (originalValue != null) {

            int index = originalValue.toString().indexOf("$");
            int index2 = originalValue.toString().indexOf(" ", index);

            String tempQueryVariable = originalValue.toString().substring(index + 1, index2);

            //int tempQueryVariableIndex = originalValue.lastIndexOf(tempQueryVariable, index2);

            // Starting from the end of query to find the last Reference
            int tempQueryVariableIndex = originalValue.lastIndexOf("]", originalValue.length());

            updatedValue = originalValue.substring(0, index2 + 4) + replacementValue + originalValue.substring(tempQueryVariableIndex +1);
        }
        
        System.out.print("getUpdatedAtomQueryNew: " + originalValue);
        System.out.println("getUpdatedAtomQueryNew: " + updatedValue);
        return updatedValue;
    }
/*
    private String getUpdatedAtomStateQuery(String originalValue, String replacementValue) {
        //System.out.append(counter++ + "  " + originalValue);
        String updatedValue = "";

        if (originalValue != null) {

            //int index = originalValue.toString().indexOf("$xsams");
            int index = originalValue.toString().indexOf("$xsams");
            int index2 = originalValue.toString().indexOf("/", index);

            //String tempQueryVariable = originalValue.toString().substring(index + 1, index2);

            //int tempQueryVariableIndex = originalValue.lastIndexOf(tempQueryVariable, index2);

            // Starting from the end of query to find the last Reference
            int tempQueryVariableIndex = originalValue.lastIndexOf("AtomicState[", originalValue.length());

            updatedValue = originalValue.substring(0, index2 + 1) + replacementValue + originalValue.substring(tempQueryVariableIndex - 1);
        }

        return updatedValue;
    }
*/
    /*
    private boolean removeReturnableFromHashMap() {

        return false;
    }

    private boolean isReturnableSelected(Set<String> keys, String returnable) {

        Pattern pattern = Pattern.compile(returnable, Pattern.CASE_INSENSITIVE);

        Iterator<String> ite = keys.iterator();

        while (ite.hasNext()) {
            String candidate = ite.next();
            //System.out.println(candidate);
            Matcher m = pattern.matcher(candidate);
            if (m.find()) {
                return true;
            }

        }
        return false;
    }
     * 
     */
}
