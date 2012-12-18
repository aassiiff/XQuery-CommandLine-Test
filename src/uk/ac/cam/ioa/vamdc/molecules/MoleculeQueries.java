/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc.molecules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author aakram
 */
public class MoleculeQueries {

    private String flworQuery;
    private String[] caseReturnablesArray = {"ElecStateLabel", "v1", "v2", "v3", "J", "Ka", "Kc", "F1", "F2", "li", "l",
        "F", "FJ", "r", "parity", "asSym", "l2", "kronigParity", "v", "elecRefl", "S",
        "J", "Lambda", "Sigma", "Omega", "elecInv", "SpinComponentLabel", "N", "vibInv", "vibSym",
        "rotSym", "rovibSym", "K", "I", "vibInv", "vibSym"};
    private String databaseName = "hitran-container.dbxml";

    public MoleculeQueries(String databaseNameValue) {
        this.databaseName = databaseNameValue;
    }

    public String buildQuery(HashMap<String, String> XQueryMappingArray) {
        flworQuery = "";

        boolean molecules = isReturnableSelected(XQueryMappingArray.keySet(), "Molecule*");
        boolean radTrans = isReturnableSelected(XQueryMappingArray.keySet(), "RadTrans*");

        if (molecules && !radTrans) {
            System.out.println("only Molecules");
            flworQuery = atomOnlyQuery(XQueryMappingArray.values().toArray());
            //flworQuery = new QueryBuilderUtility().atomOnlyQuery(XQueryMappingArray.values().toArray(), databaseName);
        }
        if (!molecules && radTrans) {
            System.out.println("only RadTrans");
            //flworQuery = new QueryBuilderUtility().atomOnlyQuery(XQueryMappingArray.values().toArray(), databaseName);
        }

        return flworQuery;
    }

    public String atomOnlyQuery(Object[] valuesArray) {
        //String returnVar[] = new String[valuesArray.length];
        
        List<String> returnVar = new ArrayList<String>();
        List<String> caseVarriables=new ArrayList<String>();
        
        flworQuery = "";

        flworQuery = flworQuery + " for $molecule in collection('" + databaseName + "')/XSAMSData/Species/Molecules/Molecule \n";
        flworQuery = flworQuery + " for $moleculeState at $index in $molecule/MolecularState \n";

        flworQuery = flworQuery + " let $cases := $moleculeState/Case \n";
        flworQuery = flworQuery + " let $caseID := $cases/@caseID \n";


        for (int i = 0; i < valuesArray.length; i++) {
            //System.out.println(valuesArray[i].toString());
            if (!valuesArray[i].toString().contains("__")) {
                flworQuery = flworQuery + valuesArray[i];
                int index = valuesArray[i].toString().indexOf("$");
                int index2 = valuesArray[i].toString().indexOf(" ", index);

                returnVar.add(valuesArray[i].toString().substring(index, index2));

                //System.out.println(valuesArray[i].toString().substring(index, index2));
            } else {
                // MoleculeQNFj=case__Fj
                int index = valuesArray[i].toString().lastIndexOf("_");
                String tempCaseVariable = valuesArray[i].toString().substring(index +1);
                
                caseVarriables.add(tempCaseVariable);
                
                //returnVar[i].addtempCaseVariable.trim();
                
                //String tempStringArray[] = new String[1];
                //tempStringArray[0] = tempCaseVariable.trim();
                
                //flworQuery = flworQuery + case_nltcs(Arrays.asList(tempStringArray));
                     
                System.out.println(tempCaseVariable);
            }

        }
        
        flworQuery = flworQuery + case_nltcs(caseVarriables);  
        //System.out.println(case_nltcs_return(caseVarriables));
        
        flworQuery = flworQuery + case_ltcs(caseVarriables);
        //System.out.println(case_ltcs_return(caseVarriables));
       
        flworQuery = flworQuery + (case_dcs(caseVarriables));
        //System.out.println(case_dcs_return(caseVarriables));
        
        flworQuery = flworQuery + (case_hunda(caseVarriables));
        //System.out.println(case_hunda_return(caseVarriables));
        
        flworQuery = flworQuery + (case_hundb(caseVarriables));
        //System.out.println(case_hundb_return(caseVarriables));
        
        flworQuery = flworQuery + (case_stcs(caseVarriables));
        //System.out.println(case_stcs_return(caseVarriables));
        
        flworQuery = flworQuery + (case_lpcs(caseVarriables));
        //System.out.println(case_lpcs_return(caseVarriables));
        
        flworQuery = flworQuery + (case_asymcs(caseVarriables));
        //System.out.println(case_asymcs_return(caseVarriables));
        
        flworQuery = flworQuery + (case_asymos(caseVarriables));
        //System.out.println(case_asymos_return(caseVarriables));
        
        flworQuery = flworQuery + (case_sphcs(caseVarriables));
        //System.out.println(case_sphcs_return(caseVarriables));
        
        flworQuery = flworQuery + (case_sphos(caseVarriables));
        //System.out.println(case_sphos_return(caseVarriables));
        
        flworQuery = flworQuery + (case_ltos(caseVarriables));
        //System.out.println(case_ltos_return(caseVarriables));
        
        flworQuery = flworQuery + (case_lpos(caseVarriables));
        //System.out.println(case_lpos_return(caseVarriables));
        
        flworQuery = flworQuery + (case_nltos(caseVarriables));
        //System.out.println(case_nltos_return(caseVarriables));
        

        String returnStatement = "return  ";
        
        returnStatement = returnStatement + "if($caseID=\"nltcs\") \n then " + case_nltcs_return(returnVar, caseVarriables);
       
        /**/
        returnStatement = returnStatement + "else if($caseID=\"ltcs\") \n then " + case_ltcs_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"dcs\") \n then " + case_dcs_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"hunda\") \n then " + case_hunda_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"hundb\") \n then " + case_hundb_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"stcs\") \n then " + case_stcs_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"lpcs\") \n then " + case_lpcs_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"asymcs\") \n then " + case_asymcs_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"sphcs\") \n then " + case_sphcs_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"sphos\") \n then " + case_sphos_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"ltos\") \n then " + case_ltos_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"lpos\") \n then " + case_lpos_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + "else if($caseID=\"nltos\") \n then " + case_nltos_return(returnVar, caseVarriables);
        
        returnStatement = returnStatement + " else concat( ";

        Iterator<String> iterator = returnVar.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();
            
            returnStatement = returnStatement + tempValue + ", \"\t\",  ";
            
        }
        
        iterator = caseVarriables.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();
            
            returnStatement = returnStatement + " \"----\" , \"\t\" ";
            
            if(iterator.hasNext()){
                returnStatement = returnStatement + ", ";
            }
            
        }

        returnStatement = returnStatement + ")";
        flworQuery = flworQuery + returnStatement;

        return flworQuery;

    }
    public String queryFlowr1 = ""
             + "for $molecule in collection('hitran-container.dbxml')/XSAMSData/Species/Molecules/Molecule \n"
        + " for $moleculeState at $index in $molecule/MolecularState \n"
        + " let $NuclearStatisticalWeight := $moleculeState/MolecularStateCharacterisation/NuclearStatisticalWeight \n"
        + " let $IonCharge := $molecule/MolecularChemicalSpecies/IonCharge \n"
        + " let $HarmonicFrequency := $molecule/MolecularChemicalSpecies/NormalModes/NormalMode/HarmonicFrequency/Value \n"
        + " let $electronicStateRef := $molecule/MolecularChemicalSpecies/NormalModes/NormalMode/@electronicStateRef \n"
        + " return concat($NuclearStatisticalWeight,  \"\t\", $IonCharge,  \"\t\", $HarmonicFrequency,  \"\t\", $electronicStateRef)\n";
            /*
            + " for $molecule in collection('" + this.databaseName + "')/XSAMSData/Species/Molecules/Molecule \n "
            //+ " for $cases at $index in $molecule/MolecularState/Case \n"
            + " for $moleculeState at $index in $molecule/MolecularState \n"
            + " let $case := $moleculeState/Case \n"
            + " let $stateID := $moleculeState/@stateID \n"
            + " let $speciesID := $molecule/@speciesID \n"
            + " let $caseID := $case/@caseID \n"
            + "return concat($stateID,  \"\t\",$index,  \"\t\",$caseID,  \"\t\", $speciesID) \n";*/

    /*
    + " let $caseJ := $cases/ltcs:QNs/ltcs:J \n"
    + " let $casev1 := $cases/ltcs:QNs/ltcs:v1 \n"
    + " let $casev2 := $cases/ltcs:QNs/ltcs:v2 \n"
    + " let $casev3 := $cases/ltcs:QNs/ltcs:v3 \n"
    + " let $casel2 := $cases/ltcs:QNs/ltcs:l2 \n"
    + " let $caser := $cases/ltcs:QNs/ltcs:r \n"
    + " let $casekronigParity := $cases/ltcs:QNs/ltcs:kronigParity \n"
    + " let $caseElecStateLabel := $cases/ltcs:QNs/ltcs:ElecStateLabel \n"
    
    + " let $caseElecStateLabel_dcs := $cases/dcs:QNs/dcs:ElecStateLabel \n"
    + " let $caseJ_dcs := $cases/dcs:QNs/dcs:J \n"
    + " let $casev_dcs := $cases/dcs:QNs/dcs:v \n"
    + " let $caseF_dcs := $cases/dcs:QNs/dcs:F \n"
    + " let $caseF1_dcs := $cases/dcs:QNs/dcs:F1 \n"
    + " let $caseparity_dcs := $cases/dcs:QNs/dcs:parity \n"
    + " let $caseasSym_dcs := $cases/dcs:QNs/dcs:asSym \n"
    + " let $caser_dcs := $cases/ltcs:QNs/dcs:r \n"
    
    + " return if($caseID=\"ltcs\") \n"
    + "     then " + case_ltcs_return1()
    + "     else if($caseID=\"dcs\") \n then "  + case_dcs_return1()
    + "     else " + case_nltcs_return1();
     * 
     */
    public String moleculesOnlyQuery() {
        String flworQuery = "";

        return flworQuery;
    }

    public String createReturnablesList(int number) {

        String tempNameString = "";
        Random generator = new Random();
        String tempStringArray[] = new String[number];

        for (int i = 0; i < number; i++) {
            int randomIndex = generator.nextInt(caseReturnablesArray.length);

            tempStringArray[i] = caseReturnablesArray[randomIndex];

            tempNameString = tempNameString + caseReturnablesArray[randomIndex] + " ";
        }

        System.out.println(tempNameString);
        System.out.println(case_nltcs(Arrays.asList(tempStringArray)));
        //System.out.println(case_nltcs_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_ltcs(Arrays.asList(tempStringArray)));
        //System.out.println(case_ltcs_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_dcs(Arrays.asList(tempStringArray)));
        //System.out.println(case_dcs_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_hunda(Arrays.asList(tempStringArray)));
        //System.out.println(case_hunda_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_hundb(Arrays.asList(tempStringArray)));
        //System.out.println(case_hundb_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_stcs(Arrays.asList(tempStringArray)));
        //System.out.println(case_stcs_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_lpcs(Arrays.asList(tempStringArray)));
        //System.out.println(case_lpcs_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_asymcs(Arrays.asList(tempStringArray)));
        //System.out.println(case_asymcs_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_asymos(Arrays.asList(tempStringArray)));
        //System.out.println(case_asymos_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_sphcs(Arrays.asList(tempStringArray)));
        //System.out.println(case_sphcs_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_sphos(Arrays.asList(tempStringArray)));
        //System.out.println(case_sphos_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_ltos(Arrays.asList(tempStringArray)));
        //System.out.println(case_ltos_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_lpos(Arrays.asList(tempStringArray)));
        //System.out.println(case_lpos_return(Arrays.asList(tempStringArray)));
        System.out.println(tempNameString);
        System.out.println(case_nltos(Arrays.asList(tempStringArray)));
        //System.out.println(case_nltos_return(Arrays.asList(tempStringArray)));

        return "";
    }

    private String case_nltcs(List<String> returnablesNameList) {

        String query = "";
        String prefix = "nltcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();
            System.out.println("tempValue " + tempValue);
            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v3")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Ka")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Kc")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else {
                System.out.println("Not Found " + tempValue);
            }
        }
        System.out.println("query " + query);
        
        return query;
    }

    // 
    private String case_nltcs_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        
        String prefix = "nltcs";


        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("v1")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("v2")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("v3")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("Ka")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("Kc")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("F1")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("F2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_ltcs(List<String> returnablesNameList) {
        String query = "";
        String prefix = "ltcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("l2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v3")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("kronigParity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }

        return query;
    }

    private String case_ltcs_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "ltcs";


        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v1")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("l2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v3")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F1")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("kronigParity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_dcs(List<String> returnablesNameList) {
        String query = "";

        String prefix = "dcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }

        return query;
    }

    private String case_dcs_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);

        String prefix = "dcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F1")) {
                query = query + "$" + tempValue + "_" + prefix ;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_hunda(List<String> returnablesNameList) {
        String query = "";
        String prefix = "hunda";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecRefl")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Lambda")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Sigma")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Omega")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("S")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("kronigParity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }

        return query;
    }

    private String case_hunda_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "hunda";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("elecInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecRefl")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Lambda")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Sigma")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Omega")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("S")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F1")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("kronigParity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_hundb(List<String> returnablesNameList) {
        String query = "";
        String prefix = "hundb";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecRefl")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Lambda")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("S")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("N")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("SpinComponentLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("kronigParity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }

        return query;
    }

    private String case_hundb_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "hundb";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecRefl")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Lambda")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("S")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("N")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("SpinComponentLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F1")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("kronigParity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_stcs(List<String> returnablesNameList) {
        String query = "";
        String prefix = "stcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vi")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("li")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("l")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("K")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rotSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rovibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("I")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Fj")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }
        return query;
    }

    private String case_stcs_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "stcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vi")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("li")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("l")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("K")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("rotSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("rovibSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("I")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Fj")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_lpcs(List<String> returnablesNameList) {
        String query = "";
        String prefix = "lpcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vi")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("li")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("l")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibRefl")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("I")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Fj")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("kronigParity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }

        return query;
    }

    private String case_lpcs_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "lpcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vi")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("li")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("l")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibRefl")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("I")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Fj")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("kronigParity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_asymcs(List<String> returnablesNameList) {
        String query = "";
        String prefix = "asymcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vi")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Ka")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Kc")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rotSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rovibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("I")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Fj")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }
        return query;
    }

    private String case_asymcs_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "asymcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vi")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Ka")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Kc")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("rotSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("rovibSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("I")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Fj")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_asymos(List<String> returnablesNameList) {
        String query = "";
        String prefix = "asymos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("S")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vi")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("N")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Ka")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Kc")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rotSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rovibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("I")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Fj")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }
        return query;
    }

    private String case_asymos_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "asymos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("elecInv")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("elecSym")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("S")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("vi")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("vibInv")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("vibSym")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("N")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("Ka")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("Kc")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("rotSym")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("rovibSym")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("I")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("Fj")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix + ", \"\t\", ";
            } else {
                query = query + " \"----\", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_sphcs(List<String> returnablesNameList) {
        String query = "";
        String prefix = "sphcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vi")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("li")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rotSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rovibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("I")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Fj")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }
        return query;
    }

    private String case_sphcs_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "sphcs";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vi")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("li")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("rotSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("rovibSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("I")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Fj")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_sphos(List<String> returnablesNameList) {
        String query = "";
        String prefix = "sphos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("S")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vi")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("li")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("N")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rotSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("rovibSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("I")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Fj")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }
        return query;
    }

    private String case_sphos_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "sphos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("S")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vi")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("li")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("N")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("rotSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("rovibSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("I")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Fj")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_ltos(List<String> returnablesNameList) {
        String query = "";
        String prefix = "ltos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecRefl")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("S")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("l2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v3")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("N")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("kronigParity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }
        return query;
    }

    private String case_ltos_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "ltos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecRefl")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("S")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v1")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("l2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v3")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("N")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F1")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("kronigParity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_lpos(List<String> returnablesNameList) {
        String query = "";
        String prefix = "lpos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecRefl")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("S")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vi")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("li")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("l")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("vibRefl")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("N")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("I")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Fj")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("kronigParity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }
        return query;
    }

    private String case_lpos_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "lpos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecRefl")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("S")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vi")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("li")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("l")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("vibRefl")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("N")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("I")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Fj")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("kronigParity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }

    private String case_nltos(List<String> returnablesNameList) {
        String query = "";
        String prefix = "nltos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("elecInv")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("S")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("v3")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("J")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("N")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Ka")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("Kc")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F1")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F2")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("F")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("r")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("parity")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            } else if (tempValue.equals("asSym")) {
                query = query + " let $" + tempValue + "_" + prefix + " := $cases/" + prefix + ":QNs/" + prefix + ":" + tempValue + " \n";
            }
        }
        return query;
    }

    private String case_nltos_return(List<String> returnVar, List<String> returnablesNameList) {
        String query = "concat( " + returnVarsReturn(returnVar);
        String prefix = "nltos";

        Iterator<String> iterator = returnablesNameList.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();

            if (tempValue.equals("ElecStateLabel")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("elecInv")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("S")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v1")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("v3")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("J")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("N")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Ka")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("Kc")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F1")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F2")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("F")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("r")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("parity")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else if (tempValue.equals("asSym")) {
                query = query + "$" + tempValue + "_" + prefix;
            } else {
                query = query + " \"----\" ";
            }
            if (iterator.hasNext()){
                 query = query + ", \"\t\", ";
            }
        }
        query = query + ") \n";
        return query;
    }
    
    private String returnVarsReturn(List<String> returnVar){
        String returnStatement = "";
        Iterator<String> iterator = returnVar.iterator();
        while (iterator.hasNext()) {
            String tempValue = iterator.next().trim();
            
                returnStatement = returnStatement + tempValue + ", \"\t\",  ";
                
                // here call each case for return with if else condtion
            
        }
        
        return returnStatement;
    }

    private String case_ltcs_return1() {
        String query = "";
        query = query + "concat($stateID,  \"\t\",$index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $caseJ,  \"\t\", $casev1,";
        query = query + "\"\t\", $casev2,  \"\t\", $casev3,  \"\t\", $casel2,  \"\t\", $caser,  \"\t\", $casekronigParity,  \"\t\", $caseElecStateLabel) \n";
        return query;
    }

    private String case_nltcs_return1() {
        String query = "";
        query = query + "concat($stateID,  \"\t\",$index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $speciesID)";
        query = query + "\n";
        return query;
    }

    private String case_dcs_return1() {
        String query = "";
        query = query + "concat($stateID,  \"\t\",$index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $caseElecStateLabel_dcs, ";
        query = query + "\"\t\", $caseJ_dcs,  \"\t\", $casev_dcs) \n";
        return query;
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

    private String case_hunda_return() {
        String query = "";

        return query;
    }

    private String case_hundb_return() {
        String query = "";

        return query;
    }

    private String case_stcs_return() {
        String query = "";

        return query;
    }

    private String case_lpcs_return() {
        String query = "";

        return query;
    }

    private String case_asymcs_return() {
        String query = "";

        return query;
    }

    private String case_asymos_return() {
        String query = "";

        return query;
    }

    private String case_sphcs_return() {
        String query = "";

        return query;
    }

    private String case_sphos_return() {
        String query = "";

        return query;
    }

    private String case_ltos_return() {
        String query = "";

        return query;
    }

    private String case_lpos_return() {
        String query = "";

        return query;
    }

    private String case_nltos_return() {
        String query = "";

        return query;
    }
}
/*
+ " return if($caseID=\"ltcs\") "
+ "     then concat($index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $caseJ,  \"\t\", $casev1,  " 
+ "         \"\t\", $casev2,  \"\t\", $casev3,  \"\t\", $casel2,  \"\t\", $caser,  \"\t\", $casekronigParity,  \"\t\", $caseElecStateLabel)"
+ "     else if($caseID=\"dcs\") then concat($index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $caseElecStateLabel_dcs, " 
+ "          \"\t\", $caseJ_dcs,  \"\t\", $casev_dcs)" 
+ "     else concat($index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $speciesID)";
}
 */

/*  switch /case is not working for me ....!
 *          + " switch ($caseID) \n"
            + "     case \"ltcs\" return concat($index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $caseJ,  \"\t\", $casev1,  " 
            + "         \"\t\", $casev2,  \"\t\", $casev3,  \"\t\", $casel2,  \"\t\", $caser,  \"\t\", $casekronigParity,  \"\t\", $caseElecStateLabel) \n" 
            
            + "     case \"dcs\" return concat($index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $caseElecStateLabel_dcs, " 
            + "          \"\t\", $caseJ_dcs,  \"\t\", $casev_dcs) \n"
            
            + "     case \"nltcs\" return concat($index,  \"\t\",$caseID,  \"\t\", $speciesID,  \"\t\", $speciesID) \n";
 */