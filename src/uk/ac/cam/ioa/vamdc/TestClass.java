/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc;

/**
 *
 * @author aakram
 */
public class TestClass {

    public static void main(String argv[]) {

        String flworQueryValue = "AtomStateHyperfineMomentum=let $HyperfineMomentum := $xsams/Isotope/Ion/AtomicState[@stateID=$stateID]/AtomicQuantumNumbers/HyperfineMomentum \n";
        //"let $OscillatorStrength := $radTrans/Radiative/RadiativeTransition/Probability/OscillatorStrength/Value \n";

        // For Rad Rans
        /*
        int index = flworQueryValue.toString().indexOf("$");
        int index2 = flworQueryValue.toString().indexOf(" ", index);
        
        String tempQueryVariable = flworQueryValue.toString().substring(index+1, index2);
        
        System.out.println(tempQueryVariable);
        
        int tempQueryVariableIndex = flworQueryValue.indexOf(tempQueryVariable, index2);
        System.out.println(tempQueryVariableIndex);
        
        System.out.println(flworQueryValue.substring(index, index2 + 3));
        System.out.println(flworQueryValue.substring(tempQueryVariableIndex - 1));
        
        String newString = flworQueryValue.substring(0, index2 + 4) + " $radTrans/*" + flworQueryValue.substring(tempQueryVariableIndex - 1);
        
        System.out.append(newString);
         * 
         */

        String flworQueryValueUpper = flworQueryValue.replaceAll("\\$stateID", "\\$upperStateRef");
        String flworQueryValueLower = flworQueryValue.replaceAll("\\$stateID", "\\$lowerStateRef");

        //int index = flworQueryValueUpper.toString().indexOf("$xsams");
        //int index2 = flworQueryValueUpper.toString().indexOf("/", index);
        
        int index = flworQueryValueUpper.toString().indexOf("$");
        int index2 = flworQueryValueUpper.toString().indexOf(" ", index);

        String tempQueryVariable = flworQueryValueUpper.toString().substring(index + 1, index2);
        
        int tempQueryVariableIndex = flworQueryValueUpper.lastIndexOf("]", flworQueryValueUpper.length());
            
        String  updatedValue = flworQueryValueUpper.substring(0, index2 + 1) + "*" + flworQueryValueUpper.substring(tempQueryVariableIndex - 1);
        updatedValue = flworQueryValueUpper.substring(0, index2 + 4) + "$lowerAtomicState" + flworQueryValueUpper.substring(tempQueryVariableIndex +1);
        
        System.out.print(flworQueryValue);
        System.out.print(flworQueryValueUpper);
        System.out.println(updatedValue);
        
        flworQueryValue = "let $ElementSymbol := $xsams/ChemicalElement/ElementSymbol \n";
        index = flworQueryValue.toString().indexOf("$xsams");
        index2 = flworQueryValue.toString().indexOf("/", index);
        
        updatedValue = flworQueryValue.substring(0, index2 + 1) + "*" + flworQueryValue.substring(index2);
        
        System.out.print(flworQueryValue);
        System.out.print(updatedValue);
    }
    
    private static String updateQuery(String originalValue, String replacementValue, String match){
        String updatedValue = "";
        
        if (originalValue != null) {

            int index = originalValue.toString().indexOf("$");
            int index2 = originalValue.toString().indexOf(" ", index);

            String tempQueryVariable = originalValue.toString().substring(index + 1, index2);

            //int tempQueryVariableIndex = originalValue.lastIndexOf(tempQueryVariable, index2);

            // Starting from the end of query to find the last Reference
            int tempQueryVariableIndex = originalValue.lastIndexOf(tempQueryVariable, originalValue.length());
            // int tempQueryVariableIndex = originalValue.lastIndexOf("]", originalValue.length());

            updatedValue = originalValue.substring(0, index2 + 4) + replacementValue + originalValue.substring(tempQueryVariableIndex - 1);
        }
        
        return updatedValue;
    }
    
    private static String updateQuery2(String originalValue, String replacementValue, String match){
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
        
        return updatedValue;
    }
    
    private static String updateQuery3(String originalValue, String replacementValue, String match){
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
}
