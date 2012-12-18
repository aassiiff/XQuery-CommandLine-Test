/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc;

/**
 *
 * @author aakram
 */
public class Querries {

    private static String databaseName = "axE9dup+GZZ0riQkt0c+TBT2.dbxml"; //"EqS8Gbpb8IzK2QwFS7uHjf2+.dbxml"; //
    public static String queryFlowr1 = ""
            + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n "
            + " for $radTrans in collection('" + databaseName + "')/XSAMSData \n"
            + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n"
            + " for $stateRef in $radTrans/Processes/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n"
            + " for $wavelengthValue in $stateRef/*/Wavelength/Value \n"
            + " let $tranPobA := $stateRef/*/TransitionProbabilityA/Value \n"
            + " let $lowerEnergy := $xsams/Isotope/Ion/AtomicState[@stateID=$stateID]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $angMom := $xsams/Isotope/Ion/AtomicState[@stateID=$stateID]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            + " where $xsams/Isotope/Ion/AtomicState/@stateID=$stateID \n"
            + " return concat($wavelengthValue,  \"\t\", $tranPobA,  \"\t\", $angMom, \"\t\", $lowerEnergy)";
    public static String queryFlowr1A = ""
            + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n "
            + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes \n"
            + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n"
            + " for $stateRefU in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n"
            + " for $wavelengthValueU in $stateRefU/EnergyWavelength/Wavelength/Value \n"
            + " let $Frequency :=  $stateRefU/EnergyWavelength/Wavenumber/Value  \n"
            + " let $tranPobAU := $stateRefU/Probability/TransitionProbabilityA/Value \n"
            + " let $upperEnergy := $xsams/Isotope/Ion/AtomicState[@stateID=$stateID]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $angMom := $xsams/Isotope/Ion/AtomicState[@stateID=$stateID]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            + " where $xsams/Isotope/Ion/AtomicState/@stateID=$stateID \n"
            + " return concat($wavelengthValueU,  \"\t\", $tranPobAU,  \"\t\", $angMom, \"\t\", $upperEnergy, \"\t\", $Frequency)";
    public static String queryFlowr1AA = ""
            + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n "
            + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes \n"
            + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n"
            + " for $upperStateRef in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n"
            + " for $wavelengthValueU in  $upperStateRef/*/Wavelength/Value  \n"
            //+ " for $Frequency in  $upperStateRef/*/Frequency/Value  \n" 

            + " let $quantumDefect := $xsams/Isotope/Ion/AtomicState[@stateID=$stateID]/AtomicNumericalData/QuantumDefect/Value \n"
            + " let $hyperfineConstantA := $xsams/Isotope/Ion/AtomicState[@stateID=$stateID]/AtomicNumericalData/HyperfineConstantA/Value \n"
            + " let $angMom := $xsams/Isotope/Ion/AtomicState[@stateID=$stateID]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            + " where $xsams/Isotope/Ion/AtomicState/@stateID=$stateID \n"
            + " return concat($hyperfineConstantA,  \"\t\", $wavelengthValueU,  \"\t\", $quantumDefect, \"\t\", $wavelengthValueU, \"\t\", $angMom)";
    public static String queryFlowr1B = ""
            + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n "
            + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes \n"
            + " for $radTransID in $radTrans/Radiative/RadiativeTransition/@id \n"
            + " for $wavelength in $radTrans/Radiative/RadiativeTransition/EnergyWavelength/Wavelength \n"
            + " let $upperStateRef := $radTrans/UpperStateRef \n"
            //+ " for $stateRefU in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n" 
            + " for $wavelengthValueU in $upperStateRef/EnergyWavelength/Wavelength/Value \n"
            + " let $tranPobAU := $upperStateRef/Probability/TransitionProbabilityA/Value \n"
            + " let $upperEnergy := $xsams/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $angMom := $xsams/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            + " where $radTrans/Radiative/RadiativeTransition/@id=$radTransID \n"
            + " return concat($wavelengthValueU,  \"\t\", $tranPobAU,  \"\t\", $angMom, \"\t\", $upperEnergy)";
    public static String queryFlowr2 = ""
            + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species \n "
            + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes/Radiative/RadiativeTransition \n"
            + " for $radTransID in $radTrans/@id \n"
            + " for $wavelength in $radTrans/EnergyWavelength/Wavelength \n"
            + " let $lowerStateRef := $radTrans/LowerStateRef \n"
            + " let $upperStateRef := $radTrans/UpperStateRef \n"
            + " let $speciesRef := $radTrans/SpeciesRef \n"
            + " let $wavelengthValue := $wavelength/Value \n"
            //+ " let $tranPobA := $radTrans/Probability/TransitionProbabilityA/Value \n"

            + " let $lowerEnergy := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            //+ " let $angMomL := $xsams/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"

            + " let $upperEnergy := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            //+ " let $angMomU := $xsams/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"

            + " let $lowerStateRefSpeciesID := $lowerEnergy/../../../../@speciesID \n"
            + " let $upperStateRefSpeciesID := $upperEnergy/../../../../@speciesID \n"
            + " let $nuclearCharge := $lowerEnergy/../../../../../../ChemicalElement/NuclearCharge \n"
            //+ " let $nuclearCharge := $lowerEnergy/../../../../../../ChemicalElement/NuclearCharge \n"
            //+ " let $elementSymbol := $lowerEnergy/../../../../@speciesID \n"

            + " where $radTrans/@id=$radTransID \n"
            + "return concat($radTransID, \"\t\",  $lowerStateRef, \"\t\", $lowerEnergy, \"\t\", $lowerStateRefSpeciesID, "
            + "\"\t\", $upperStateRef, \"\t\", $upperEnergy, \"\t\", $upperStateRefSpeciesID, \"\t\", $wavelengthValue, \"\t\", $nuclearCharge)";
    public static String queryFlowr2A1 = ""
            + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species \n "
            + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes/Radiative/RadiativeTransition \n"
            + " for $atoms in $xsams/Atoms/Atom \n"
            + " for $radTransID in $radTrans/@id \n"
            + " for $wavelength in $radTrans/EnergyWavelength/Wavelength \n"
            + " let $lowerStateRef := $radTrans/LowerStateRef \n"
            + " let $upperStateRef := $radTrans/UpperStateRef \n"
            + " let $lowerAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef] \n"
            + " let $upperAtomicState := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef] \n"
            + " let $speciesRef := $radTrans/SpeciesRef \n"
            + " let $wavelengthValue := $wavelength/Value \n"
            //+ " let $tranPobA := $radTrans/Probability/TransitionProbabilityA/Value \n"

            + " let $lowerEnergy := $lowerAtomicState/AtomicNumericalData/StateEnergy/Value \n"
            //+ " let $angMomL := $xsams/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"

            + " let $upperEnergy := $upperAtomicState/AtomicNumericalData/StateEnergy/Value \n"
            //+ " let $angMomU := $xsams/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"

            + " let $lowerStateRefSpeciesID := $lowerEnergy/../../../../@speciesID \n"
            + " let $upperStateRefSpeciesID := $upperEnergy/../../../../@speciesID \n"
            + " let $LandeFactorLower := $xsams/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicNumericalData/LandeFactor/Value  \n"
            + " let $ElementSymbol := $atoms/*/ElementSymbol  \n"
            + " let $nuclearCharge := $lowerEnergy/../../../../../../ChemicalElement/NuclearCharge \n"
            + "let $OscillatorStrength :=  $radTrans/*/OscillatorStrength/Value \n"
            + "let $LineStrength :=  $radTrans/*/LineStrength/Value \n "
            //+ " let $nuclearCharge := $lowerEnergy/../../../../../../ChemicalElement/NuclearCharge \n"
            //+ " let $elementSymbol := $lowerEnergy/../../../../@speciesID \n"

            + " where $radTrans/@id=$radTransID and"
            + "  $atoms/Isotope/Ion/AtomicState/@stateID=$lowerStateRef \n"
            + "return concat($ElementSymbol, \"\t\",  $LandeFactorLower, \"\t\", $lowerEnergy, \"\t\", $lowerStateRefSpeciesID, "
            + "\"\t\", $upperStateRef, \"\t\", $upperEnergy, \"\t\", $upperStateRefSpeciesID, \"\t\", $wavelengthValue, \"\t\", $nuclearCharge)";
    public static String queryFlowr2A = "for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes/Radiative/RadiativeTransition \n"
            + " for $doc in collection('" + databaseName + "')/XSAMSData \n"
            + " for $radTransID in $radTrans/@id \n"
            + " for $wavelength in $radTrans/EnergyWavelength/Wavelength \n"
            + " let $lowerStateRef := $radTrans/LowerStateRef \n"
            + " let $upperStateRef := $radTrans/UpperStateRef \n"
            + " let $speciesRef := $radTrans/SpeciesRef \n"
            + " let $wavelengthValue := $wavelength/Value \n"
            //+ " let $wavelengthMethodRef := $wavelength/@methodRef \n"
            + " let $tranPobA := $radTrans/Probability/TransitionProbabilityA/Value \n"
            + " let $lowerEnergy := $doc/Species/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $angMomL := $doc/Species/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            + " let $upperEnergy := $doc/Species/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $angMomU := $doc/Species/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            //+ "let $lowerStateRefSpeciesID := $lowerEnergy/../../../../@speciesID \n"
            //+ "let $upperStateRefSpeciesID := $upperEnergy/../../../../@speciesID \n"

            + " where $radTrans/@id=$radTransID \n"
            + "return concat($radTransID, \"\t\",  $lowerEnergy, \"\t\",  $upperEnergy, \"\t\", $angMomL, \"\t\", $angMomU, \"\t\", $wavelengthValue, \"\t\", $tranPobA)";
    public static String queryFlowr2C = "for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes/Radiative/RadiativeTransition \n"
            + " for $doc in collection('" + databaseName + "')/XSAMSData/Species \n"
            + " for $radTransID in $radTrans/@id \n"
            + " for $wavelength in $radTrans/EnergyWavelength/Wavelength \n"
            + " let $lowerStateRef := $radTrans/LowerStateRef \n"
            + " let $upperStateRef := $radTrans/UpperStateRef \n"
            + " let $speciesRef := $radTrans/RadiativeTransition/SpeciesRef \n"
            + " let $wavelengthValue := $wavelength/Value \n"
            //+ " let $wavelengthMethodRef := $wavelength/@methodRef \n"
            + " let $tranPobA := $radTrans/Probability/TransitionProbabilityA/Value \n"
            + " let $lowerEnergy := $doc/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $angMomL := $doc/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            + " let $upperEnergy := $doc/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $angMomU := $doc/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            //+ "let $lowerStateRefSpeciesID := $lowerEnergy/../../../../@speciesID \n"
            //+ "let $upperStateRefSpeciesID := $upperEnergy/../../../../@speciesID \n"

            + " where $radTrans/@id=$radTransID \n"
            + "return concat($radTransID, \"\t\",  $lowerEnergy, \"\t\",  $upperEnergy, \"\t\", $angMomL, \"\t\", $angMomU, \"\t\", $wavelengthValue, \"\t\", $tranPobA)";
    public static String queryFlowr2CC = ""
            + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n "
            + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes \n"
            + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n"
            + " for $upperStateRef in $radTrans/Radiative/RadiativeTransition[UpperStateRef=$stateID] \n"
            + " let $IonCharge := $xsams/Isotope/Ion/IonCharge   \n"
            + " let $MassNumber := $xsams/Isotope/IsotopeParameters/MassNumber   \n"
            + " let $Wavenumber :=  $upperStateRef/*/Wavenumber/Value \n"
            + " let $speciesID := $xsams/Isotope/Ion/@speciesID \n"
            + " where $xsams/Isotope/Ion/AtomicState/@stateID=$stateID \n"
            + " return concat($IonCharge,  \"\t\", $MassNumber,  \"\t\", $Wavenumber, \"\t\", $speciesID)";
    public static String queryFlowrSample = ""
            + " for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n "
            + " for $radTrans in collection('" + databaseName + "')/XSAMSData/Processes \n"
            + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n"
            + " for $lowerStateRef in $radTrans/Radiative/RadiativeTransition[LowerStateRef=$stateID] \n"
            + " let $NuclearCharge := $xsams/ChemicalElement/NuclearCharge   \n"
            + " let $UpperStateRef :=  $lowerStateRef/*/UpperStateRef    \n"
            + " let $Energy :=  $lowerStateRef/*/Energy/Value \n"
            + " let $speciesID := $xsams/Isotope/Ion/@speciesID \n"
            + " where $xsams/Isotope/Ion/AtomicState/@stateID=$stateID \n"
            + " return concat($NuclearCharge,  \"\t\", $UpperStateRef,  \"\t\", $Energy, \"\t\", $speciesID)";
    
    public static String queryFromPortal = ""
            + "for $xsams in collection('" + databaseName + "')/XSAMSData/Species/Atoms/Atom \n"
            + " for $stateID in $xsams/Isotope/Ion/AtomicState/@stateID \n"
            + " let $InChI := $xsams/Isotope/Ion/InChI \n"
            + " let $InChIKey := $xsams/Isotope/Ion/InChIKey \n"
            + " let $ElementSymbol := $xsams/ChemicalElement/ElementSymbol \n"
            + " return concat($InChI, \",\", $InChIKey, \",\", $ElementSymbol)";
    
    //.dbxml
}
