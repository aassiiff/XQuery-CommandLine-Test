/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author aakram
 */
public class QueryBuilder {

    private String flworQuery;

    /**
     * Get the value of flworQuery
     *
     * @return the value of flworQuery
     */
    public String getFlworQuery() {
        return flworQuery;
    }

    /**
     * Set the value of flworQuery
     *
     * @param flworQuery new value of flworQuery
     */
    public void setFlworQuery(String flworQuery) {
        this.flworQuery = flworQuery;
    }

    public String buildQuery(HashMap<String, String> XQueryMappingArray, String databaseName) {

        flworQuery = "";


        //Object[] keysArray = XQueryMappingArray.keySet().toArray();
        // Object[] valuesArray = XQueryMappingArray.values().toArray();



        if (databaseName.endsWith(".dbxml")) {
        } else {
            databaseName = databaseName + ".dbxml";
        }

        boolean atoms = isReturnableSelected(XQueryMappingArray.keySet(), "Atom*");
        //System.out.println(atoms);

        boolean radTrans = isReturnableSelected(XQueryMappingArray.keySet(), "RadTrans*");
        //System.out.println(radTrans);

        //flworQuery(atoms, radTrans, XQueryMappingArray.keySet(), valuesArray, databaseName);

        if (atoms && !radTrans) {
            System.out.println("only atoms");
            //flworQuery = new QueryBuilderUtility().atomOnlyQuery(XQueryMappingArray.values().toArray(), databaseName);
        } 
        
        if ((!atoms && radTrans) || (atoms && radTrans)) {

            // It is is opposite due to use of !. Just made logic bit complicated 
            boolean upperRef = !isReturnableSelected(XQueryMappingArray.keySet(), "RadTransInitialStateRef");
            boolean lowerRef = !isReturnableSelected(XQueryMappingArray.keySet(), "RadTransFinalStateRef");

            if (upperRef && lowerRef) {
                System.out.println("atoms with upperRef && lowerRef");
                 flworQuery = new QueryBuilderUtility().atomRadiativeQueryBoth(XQueryMappingArray, databaseName);
            } else {
                System.out.println("atoms with either upperRef or lowerRef");
                flworQuery = new QueryBuilderUtility().atomRadiativeQuery(XQueryMappingArray, databaseName, upperRef);
            }
        }
        System.out.println("\n\n");
        return flworQuery;
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

    private String updateMappingQuery() {
        return "";
    }

    private String flworQuery(boolean atoms, boolean radTrans, Set<String> keys, Object[] valuesArray, String databaseName) {

        String returnVar[] = new String[valuesArray.length];
        String flworQueryLocal = "";
        if (atoms && !radTrans) {
            System.out.println("atoms && !radTrans");
            flworQueryLocal = flworQueryLocal + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n";
            flworQueryLocal = flworQueryLocal + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n";

            for (int i = 0; i < valuesArray.length; i++) {
                flworQuery = flworQuery + valuesArray[i];
                int index = valuesArray[i].toString().indexOf("$");
                int index2 = valuesArray[i].toString().indexOf(":");

                returnVar[i] = valuesArray[i].toString().substring(index, index2);

                System.out.println(valuesArray[i].toString().substring(index, index2));

            }
        } else if (atoms && radTrans) {
            System.out.println("atoms && radTrans");

            boolean upperRef = isReturnableSelected(keys, "RadTransFinalStateRef");
            boolean lowerRef = isReturnableSelected(keys, "RadTransInitialStateRef");

            System.out.println(upperRef + "  " + lowerRef);
            // Test if it has either upperRef or lowerRef or both Ref or none Ref

            flworQueryLocal = flworQueryLocal + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n";
            flworQueryLocal = flworQueryLocal + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes \n";

            flworQueryLocal = flworQueryLocal + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n";
            // flworQueryLocal = flworQueryLocal + " for $radTransID in $radTrans/Radiative/RadiativeTransition/@id \n";

            if (!lowerRef && !upperRef) {
                flworQueryLocal = flworQueryLocal + " for $upperStateRef in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n";

            } else if (lowerRef && !upperRef) {
                flworQueryLocal = flworQueryLocal + " for $lowerStateRef in $radTrans/Radiative/RadiativeTransition[LowerStateRef=$stateID] \n";

            } else if (!lowerRef && upperRef) {
                flworQueryLocal = flworQueryLocal + " for $upperStateRef in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n";


            } else if (lowerRef && upperRef) {
                flworQueryLocal = flworQueryLocal + " for $upperStateRef in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n";
                flworQueryLocal = flworQueryLocal + " for $lowerStateRef in $radTrans/Radiative/RadiativeTransition[LowerStateRef=$stateID] \n";
            }
            flworQueryLocal = flworQueryLocal + " for $upperStateRef in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n";
            //flworQueryLocal = flworQueryLocal + " let $upperStateRef := $radTrans/UpperStateRef \n";

            for (int i = 0; i < valuesArray.length; i++) {
                String tempValue = valuesArray[i].toString();
                //System.out.println(tempValue);

                tempValue = tempValue.replaceAll("\\$stateID", "\\$upperStateRef");

                /** 
                 */
                int index = tempValue.indexOf("$");
                int index2 = valuesArray[i].toString().indexOf(" ", index);

                String tempReturnable = tempValue.substring(index, index2);
                System.out.println(tempReturnable);

                String tempValue2 = tempValue.replaceAll("\\" + tempReturnable, "\\" + tempReturnable + 1);

                //System.out.print(tempValue);
                //System.out.println(tempValue2);
                flworQueryLocal = flworQueryLocal + tempValue;

            }



        } else if (!atoms && radTrans) {
            System.out.println("!atoms && radTrans");
            flworQueryLocal = flworQueryLocal + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes/Radiative/RadiativeTransition \n";
            flworQueryLocal = flworQueryLocal + " for $radTransID in $radTrans/@id \n";
        }

        String returnStatement = "return concat(";

        //"return concat($stateID, \"\t\",  $speciesRef/../Probability/WeightedOscillatorStrength/Value, \"\t\", $speciesRef/../Probability/TransitionProbabilityA/Value )";

        for (int i = 0; i < returnVar.length; i++) {
            if (i == returnVar.length - 1) {
                returnStatement = returnStatement + returnVar[i] + ", \"\t\" ";
            } else {
                returnStatement = returnStatement + returnVar[i] + ", \"\t\", ";
            }

        }

        returnStatement = returnStatement + ")";
        flworQueryLocal = flworQueryLocal + returnStatement;

        //System.out.println(flworQueryLocal);
        return flworQueryLocal;
    }
}
