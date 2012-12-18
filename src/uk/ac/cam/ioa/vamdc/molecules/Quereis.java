/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc.molecules;

/**
 *
 * @author aakram
 */
public class Quereis {
    
    private String flworQuery;
    
    private String databaseName = "hitran-container.dbxml";
    
    public Quereis(String databaseNameValue) {
        this.databaseName = databaseNameValue;
    }
    
    /*
     * for $molecule in collection('hitran-container.dbxml')/XSAMSData/Species/Molecules/Molecule 
 for $moleculeState at $index in $molecule/MolecularState 
 let $cases := $moleculeState/Case 
 let $caseID := $cases/@caseID 
     * 
+ " let $TotalStatisticalWeight := $moleculeState/MolecularStateCharacterisation/TotalStatisticalWeight  \n"
+ " let $y3 := $molecule/MolecularChemicalSpecies/NormalModes/NormalMode/DisplacementVectors/Vector/@y3  \n"
+ " let $Description := $moleculeState/Description  \n"
+ " let $electronicStateRef := $molecule/MolecularChemicalSpecies/NormalModes/NormalMode/@electronicStateRef  \n"
 + " let $v_dcs := $cases/dcs:QNs/dcs:v  \n"
 + " let $v_hunda := $cases/hunda:QNs/hunda:v  \n"
 + " let $elecInv_hunda := $cases/hunda:QNs/hunda:elecInv  \n"
 + " let $v_hundb := $cases/hundb:QNs/hundb:v  \n"
 + " let $elecInv_hundb := $cases/hundb:QNs/hundb:elecInv  \n"
 + " let $elecInv_asymos := $cases/asymos:QNs/asymos:elecInv \n" 
 + " let $elecInv_sphos := $cases/sphos:QNs/sphos:elecInv  \n"
 + " let $elecInv_ltos := $cases/ltos:QNs/ltos:elecInv  \n"
 + " let $elecInv_lpos := $cases/lpos:QNs/lpos:elecInv  \n"
 + " let $elecInv_nltos := $cases/nltos:QNs/nltos:elecInv  \n"
return  if($caseID="nltcs") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	",  ---- ) 
else if($caseID="ltcs") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	",  ---- ) 
else if($caseID="dcs") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",  $v_dcs, "	",  ---- ) 
else if($caseID="hunda") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",  $v_hunda, "	", $elecInv_hunda) 
else if($caseID="hundb") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",  $v_hundb, "	", $elecInv_hundb) 
else if($caseID="stcs") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	",  ---- ) 
else if($caseID="lpcs") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	",  ---- ) 
else if($caseID="asymcs") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	",  ---- ) 
else if($caseID="sphcs") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	",  ---- ) 
else if($caseID="sphos") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	", $elecInv_sphos) 
else if($caseID="ltos") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	", $elecInv_ltos) 
else if($caseID="lpos") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	", $elecInv_lpos) 
else if($caseID="nltos") 
 then concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	", $elecInv_nltos) 
 else concat( $TotalStatisticalWeight, "	",  $y3, "	",  $Description, "	",  $electronicStateRef, "	",   ---- , "	"  ---- , "	" )
     */
    
    
    public String queryFlowr1 = ""
             + "for $molecule in collection('hitran-container.dbxml')/XSAMSData/Species/Molecules/Molecule \n"
            + " for $moleculeState at $index in $molecule/MolecularState \n"
            + " let $cases := $moleculeState/Case  \n"
            + " let $caseID := $cases/@caseID  \n"
            
            + " let $NuclearStatisticalWeight := $moleculeState/MolecularStateCharacterisation/NuclearStatisticalWeight \n"
            + " let $IonCharge := $molecule/MolecularChemicalSpecies/IonCharge \n"
            
            + " let $MolecularWeight := $molecule/MolecularChemicalSpecies/StableMolecularProperties/MolecularWeight/Value  \n"
            + " let $LifeTime := $moleculeState/MolecularStateCharacterisation/LifeTime/Value  \n"
            + " let $pointGroupSymmetry := $molecule/MolecularChemicalSpecies/NormalModes/NormalMode/@pointGroupSymmetry \n"
            
            + " let $HarmonicFrequency := $molecule/MolecularChemicalSpecies/NormalModes/NormalMode/HarmonicFrequency/Value \n"
            + " let $electronicStateRef := $molecule/MolecularChemicalSpecies/NormalModes/NormalMode/@electronicStateRef \n"
            
            + " let $v_dcs := $cases/dcs:QNs/dcs:v  \n"
 + " let $v_hunda := $cases/hunda:QNs/hunda:v  \n"
 + " let $elecInv_hunda := $cases/hunda:QNs/hunda:elecInv  \n"
 + " let $v_hundb := $cases/hundb:QNs/hundb:v  \n"
 + " let $elecInv_hundb := $cases/hundb:QNs/hundb:elecInv  \n"
 + " let $elecInv_asymos := $cases/asymos:QNs/asymos:elecInv \n" 
 + " let $elecInv_sphos := $cases/sphos:QNs/sphos:elecInv  \n"
 + " let $elecInv_ltos := $cases/ltos:QNs/ltos:elecInv  \n"
 + " let $elecInv_lpos := $cases/lpos:QNs/lpos:elecInv  \n"
 + " let $elecInv_nltos := $cases/nltos:QNs/nltos:elecInv  \n"
            
            + " return if($caseID=\"nltcs\") "
            + " then concat( $NuclearStatisticalWeight,  \"\t\",  $LifeTime,  \"\t\",  $MolecularWeight,  \"\t\",  $electronicStateRef,  \"\t\",   \"----\" )  \n"
            + " else concat($NuclearStatisticalWeight,  \"\t\", $IonCharge,  \"\t\", $HarmonicFrequency,  \"\t\", $electronicStateRef" 
            + " ,  \"\t\", $MolecularWeight,  \"\t\", $LifeTime,  \"\t\", $pointGroupSymmetry) \n";
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
}
