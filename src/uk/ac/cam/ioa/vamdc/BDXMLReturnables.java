package uk.ac.cam.ioa.vamdc;

/**
 *
 * @author aakram
 */
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.db.Transaction;
import java.io.*;
import com.sleepycat.dbxml.*;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * mainQuery = "/XSAMSData/Species/Atoms/Atom/Isotope/Ion/AtomicState/@stateID";
secondQuery = "/XSAMSData/Processes/Radiative/RadiativeTransition[LowerStateRef=$valueID]/Probability/TransitionProbabilityA/Value";
thirdQuery = "/XSAMSData/Processes/Radiative/RadiativeTransition[LowerStateRef=$valueID]/Probability/WeightedOscillatorStrength/Value";
 */
public class BDXMLReturnables {

    private static String tempContainer = "+nf4FSLtRnCMfwYGPxMlUtBT";
    private static String theContainer = tempContainer + ".dbxml";//"TestProject171011_TestXMLDB.dbxml";
    //private static String theContainer = "MyTestContainer.dbxml";
    private static String collection = "collection('" + theContainer + "')";
    private static String queryFlowr = "for $doc in collection('" + theContainer + "')/XSAMSData/Species/Atoms/Atom/Isotope/Ion \n"
            + " for $stateID in $doc/AtomicState/@stateID \n"
            + " for $speciesRef in collection('" + theContainer + "')/XSAMSData/Processes/Radiative/RadiativeTransition/LowerStateRef \n"
            + // " where $doc/ChemicalElement[ElementSymbol=\"Fe\"] \n" + 
            //  " where $doc/Isotope/Ion[@speciesID=\"Xchianti-1026\" ]\n" + 
            " where $speciesRef=$stateID \n"
            + //" return $doc/Isotope/Ion/AtomicState/@stateID";
            // $speciesRef/../EnergyWavelength/Wavelength/@methodRef
            "return concat($stateID, \"\t\",  $speciesRef/../Probability/WeightedOscillatorStrength/Value, \"\t\", $speciesRef/../Probability/TransitionProbabilityA/Value )";
    private static String queryFlowr1 = "for $radTrans in collection('" + theContainer + "')/XSAMSData/Processes/Radiative/RadiativeTransition \n"
            + " for $doc in collection('" + theContainer + "')/XSAMSData \n"
            + " for $radTransID in $radTrans/@id \n"
            + " for $wavelength in $radTrans/EnergyWavelength/Wavelength \n"
            + " let $lowerStateRef := $radTrans/LowerStateRef \n"
            + " let $upperStateRef := $radTrans/UpperStateRef \n"
            + " let $speciesRef := $radTrans/SpeciesRef \n"
            + " let $wavelengthValue := $wavelength/Value \n"
            + " let $wavelengthMethodRef := $wavelength/@methodRef \n"
            + " let $lowerEnergy := $doc//Species/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $upperEnergy := $doc//Species/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$upperStateRef]/AtomicNumericalData/StateEnergy/Value \n"
            + "let $lowerStateRefSpeciesID := $lowerEnergy/../../../../@speciesID \n"
            + "let $upperStateRefSpeciesID := $upperEnergy/../../../../@speciesID \n"
            + " where $radTrans/@id=$radTransID \n"
            + "return concat($radTransID, \"\t\",  $lowerStateRef, \"\t\", $lowerEnergy, \"\t\", $lowerStateRefSpeciesID, "
            //+ "\"\t\", $doc//Species/Atoms/Atom/Isotope/Ion/AtomicState[@stateID=$lowerStateRef]/parent::/@speciesID"
            + "\"\t\", $upperStateRef, \"\t\", $upperEnergy, \"\t\", $upperStateRefSpeciesID, \"\t\", $wavelengthValue, \"\t\", $wavelengthMethodRef)";
    
    private static String queryFlowr2 = "for $doc in collection('" + theContainer + "')/XSAMSData/Species/Atoms/Atom \n"
            + " for $atomState in $doc/Isotope/Ion/AtomicState \n"
            + " for $stateID in $atomState/@stateID \n"
            + " let $elementSymbol := $doc/ChemicalElement/ElementSymbol \n"
            + " let $nuclearCharge := $doc/ChemicalElement/NuclearCharge \n"
            + " let $energyValue := $atomState[@stateID=$stateID]/AtomicNumericalData/StateEnergy/Value \n"
            + " let $units := $atomState[@stateID=$stateID]/AtomicNumericalData/StateEnergy/Value/@units \n"
            + " let $accuracy := $atomState[@stateID=$stateID]/AtomicNumericalData/StateEnergy/Accuracy \n"
            + " let $confidenceInterval := $atomState[@stateID=$stateID]/AtomicNumericalData/StateEnergy/Accuracy/@confidenceInterval \n"
            + " let $parity := $atomState[@stateID=$stateID]/AtomicQuantumNumbers/Parity \n"
            + " let $totAngMom := $atomState[@stateID=$stateID]/AtomicQuantumNumbers/TotalAngularMomentum \n"
            + " let $kappa := $atomState[@stateID=$stateID]/AtomicQuantumNumbers/Kappa \n"
            ///XSAMSData/Species/Atoms/Atom/Isotope/Ion/AtomicState/@stateID

            //+ " let $lowerEnergy := $doc/Atom/Isotope/Ion/AtomicState/AtomicNumericalData/StateEnergy/Value \n" 

            //+ " let $lowerStateRefSpeciesID := $lowerEnergy/../../../../@speciesID \n"        

            //+ " where $doc/Atom/Isotope/Ion/AtomicState/@stateID=$stateID \n"

            + "return concat($elementSymbol, \"\t\",   $nuclearCharge, \"\t\", $stateID, \"\t\", $energyValue, \"\t\", $units, \"\t\", $accuracy, \"\t\", $confidenceInterval, \"\t\", $parity, \"\t\", $totAngMom, \"\t\", $kappa)";

    private static void doQuery(XmlManager mgr) throws Throwable {

        XmlQueryContext context = mgr.createQueryContext();

        context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");

        XmlResults results = mgr.query(queryFlowr2, context, null);

        System.out.println("Query Results Size: " + results.size());
        XmlValue value;
        while ((value = results.next()) != null) {
            System.out.println(value.asString());
        }
    }

    private static void doQuery(XmlManager mgr, String mainQuery, String secondQuery) throws Throwable {
        //Perform a single query against the referenced container.
        // No context is used for this query.
        String fullQuery = collection + mainQuery;
        System.out.println("Exercising query: '" + fullQuery + "'.");


        //Perform the query

        XmlQueryContext context = mgr.createQueryContext();

        context.setNamespace("", "http://vamdc.org/xml/xsams/0.2");
        context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");
        XmlResults results = mgr.query(fullQuery, context, null);
        System.out.println(results.size() + " results returned for query '"
                + fullQuery + "'.");
        //Iterate over the results of the query using an XmlValue object
        XmlValue value;
        while ((value = results.next()) != null) {
            System.out.println(value.asString());
            context.setVariableValue("valueID", value);
            //query = "/XSAMSData/Processes/Radiative/RadiativeTransition[SpeciesRef=$speciesID]/EnergyWavelength/Wavelength/Value";
            fullQuery = collection + secondQuery;

            XmlResults results2 = mgr.query(fullQuery, context, null);
            System.out.println("\t" + results2.size() + " results returned for query '"
                    + fullQuery + "'.");
            /**/

            XmlValue value2;
            while ((value2 = results2.next()) != null) {
                System.out.println("\t \t" + value2.getFirstChild().asString());

            }

        }
        /*
        System.out.println(results.size() + " results returned for query '"
        + fullQuery + "'.");*/
        results.delete();
    }

    private static void doQuery(XmlManager mgr, String mainQuery, String header1, String secondQuery, String header2) throws Throwable {
        //Perform a single query against the referenced container.
        // No context is used for this query.
        String fullQuery = collection + mainQuery;
        System.out.println("Exercising query: '" + fullQuery + "'.");


        //Perform the query

        XmlQueryContext context = mgr.createQueryContext();

        context.setNamespace("", "http://vamdc.org/xml/xsams/0.2");
        context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");
        XmlResults results = mgr.query(fullQuery, context, null);
        /*
        System.out.println(results.size() + " results returned for query '"
        + fullQuery + "'.");
         * 
         */
        //Iterate over the results of the query using an XmlValue object

        System.out.println(header1 + "\t" + header2);
        XmlValue value;
        while ((value = results.next()) != null) {
            //System.out.println(value.asString());
            context.setVariableValue("valueID", value);
            //query = "/XSAMSData/Processes/Radiative/RadiativeTransition[SpeciesRef=$speciesID]/EnergyWavelength/Wavelength/Value";
            fullQuery = collection + secondQuery;

            XmlResults results2 = mgr.query(fullQuery, context, null);
            /*System.out.println("\t" + results2.size() + " results returned for query '"
            + fullQuery + "'.");
             */

            XmlValue value2;
            while ((value2 = results2.next()) != null) {
                System.out.println(value.getNodeValue() + "\t" + value2.getFirstChild().asString());

            }

        }
        /*
        System.out.println(results.size() + " results returned for query '"
        + fullQuery + "'.");*/
        results.delete();
    }

    private static void doQuery(XmlManager mgr, String mainQuery, String header1, String secondQuery, String header2,
            String thirdQuery, String header3) throws Throwable {

        String fullQuery = collection + mainQuery;
        System.out.println("Exercising query: '" + fullQuery + "'.");

        XmlQueryContext context = mgr.createQueryContext();

        context.setNamespace("", "http://vamdc.org/xml/xsams/0.2");
        context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");
        XmlResults results = mgr.query(fullQuery, context, null);

        System.out.println(header1 + "\t" + header2 + "\t" + header3);
        XmlValue value;
        while ((value = results.next()) != null) {

            context.setVariableValue("valueID", value);

            fullQuery = collection + secondQuery;

            XmlResults results2 = mgr.query(fullQuery, context, null);
            //System.out.print(results2.size() + "  ");
            XmlValue value2;
            while ((value2 = results2.next()) != null) {
                context.setVariableValue("value2ID", value2);

                fullQuery = collection + thirdQuery;

                XmlResults results3 = mgr.query(fullQuery, context, null);
                //System.out.println(results2.size() + "  " + results3.size());
                XmlValue value3;
                while ((value3 = results3.next()) != null) {
                    System.out.println(value.getNodeValue() + "\t" + value2.getFirstChild().asString() + "\t" + value3.getFirstChild().asString());
                }

                for (int i = results2.size() - results3.size(); i < results2.size(); i++) {
                    //System.out.println(value.getNodeValue() + "\t" + value2.getFirstChild().asString() + "\t");
                }

                for (int i = results3.size() - results2.size(); i < results3.size(); i++) {
                    // System.out.println(value.getNodeValue() + "\t" + "\t" + "\t" + value3.getFirstChild().asString());
                }
            }
            /*
            while ((value2 = results2.next()) != null) {
            System.out.println(value.getNodeValue() + "\t" + value2.getFirstChild().asString());
            }*/

        }
        /*
        System.out.println(results.size() + " results returned for query '"
        + fullQuery + "'.");*/
        results.delete();
    }

    //Utility function to clean up objects, exceptions or not,
    // containers and environments must be closed.
    private static void cleanup(Environment env, XmlContainer openedContainer, XmlManager theMgr) {
        try {
            if (openedContainer != null) {
                openedContainer.delete();
            }
            if (env != null) {
                env.close();
            }
            if (theMgr != null) {
                theMgr.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Environment createEnv(File home)
            throws DatabaseException, FileNotFoundException {

        EnvironmentConfig config = new EnvironmentConfig();
        config.setCacheSize(50 * 1024 * 1024);
        //config.setCacheSize(1024 * 1024 * 50);
        //config.setCacheMax(1024 * 1024 * 100);
        config.setAllowCreate(true);
        config.setInitializeCache(true);
        config.setTransactional(false);
        config.setInitializeLocking(true);
        config.setInitializeLogging(false);


        config.setErrorHandler(new MyErrorHandler());
        config.setErrorStream(System.err);

        config.setLogAutoRemove(true);

        config.setLogBufferSize(10000);

        config.setMaxLockers(10000);
        config.setMaxLockObjects(10000);
        config.setMaxLocks(10000);

        System.out.println(config.getMaxLocks());
        return new Environment(home, config);
    }

    public static void main(String argv[]) {
        //createFile();

        ReturnableXQueryMapping tempMapping = new ReturnableXQueryMapping();

        //HashMap<String, String> XQueryMappingArray = tempMapping.randomXQueryMapping(10);
         HashMap<String, String>  XQueryMappingArray = tempMapping.radTransXQueryMapping(10);
        /*
        Object[] keysArray =  XQueryMappingArray.keySet().toArray();
        Object[] valuesArray = XQueryMappingArray.values().toArray();
        for(int i = 0; i < XQueryMappingArray.size(); i++ ){
        
        System.out.println(keysArray[i].toString() + " " + valuesArray[i].toString());
        
        }
         */

        String query = "";
        query = new QueryBuilder().buildQuery(XQueryMappingArray, tempContainer);
        //System.out.println(query); 
        //query = "";

        //System.out.println("\n");

        queryFlowr2 = query;



        //queryFlowr2 = queryFlowr1;

        //queryFlowr2 = Querries.queryFromPortal;
        //queryFlowr2 = readFileAsString();

        System.out.println(queryFlowr2);
        //doQuery(theMgr);

        File path2DbEnv = new File("/opt/jboss/VAMDCData/tempXSAMS/" + tempContainer + "/");

        Environment env = null;
        XmlContainer openedContainer = null;
        //XmlTransaction txn = null;
        XmlManager theMgr = null;

        try {

            env = createEnv(path2DbEnv);

            theMgr = new XmlManager(env, new XmlManagerConfig());

            XmlContainerConfig config = new XmlContainerConfig();
            //config.setTransactional(true);

            //theMgr.setLogLevel(LEVEL_ALL, true);
            //theMgr.setLogCategory(CATEGORY_ALL, true);


            System.out.println("Opening DB Container");
            //Open a non-transactional container
            if (theMgr.existsContainer(theContainer) == 0) {
                openedContainer = theMgr.createContainer(theContainer, config);
            }

            openedContainer = theMgr.openContainer(theContainer, config);

            //theMgr.renameContainer(theContainer, theContainer.toLowerCase());
            // openedContainer.

            //Transaction dbtxn = env.beginTransaction(null, null);
            //txn = theMgr.createTransaction(dbtxn);

            //f_ampp2_3160611.xml
            XmlInputStream inputSteam = theMgr.createLocalFileInputStream("/opt/jboss/VAMDCData/xsams/vald-2012-02-21T02_24_31.795979.xsams");

            //inputSteam.
            /*
            String theFile = file.toString();
            
            //Load the contents of the XML file into a String
            String theLine = null;
            String xmlString = new String();
            FileInputStream fis = new FileInputStream(theFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            while((theLine=br.readLine()) != null) {
            xmlString += theLine;
            xmlString += "\n";
            }
            br.close();
             */

            System.out.println("ContainerType " + theMgr.getDefaultContainerType());
            //Declare an xml document
            //XmlDocument xmlDoc = theMgr.createDocument();

            //XmlDocumentConfig xmlDocConfig = new XmlDocumentConfig();

            //xmlDocConfig.s

            //xmlDocConfig.
            //Set the xml document's content to the xmlString we just obtained.
            //xmlDoc.setContent(xmlString);

            //Set the document name
            //xmlDoc.setName(file.getName());
/*
             * 
            Date theDate = new Date();
            xmlDoc.setMetaData(mdConst.uri, mdConst.name, new XmlValue(theDate.toString()));
             */
            //Place that document into the container */

            /*      
            System.out.println("Adding file in the container: " + theContainer);
            String result = openedContainer.putDocument("vald-2012-02-21T02_24_31.795979.xsams", inputSteam, XmlDocumentConfig.DEFAULT);
            
            System.out.println("Added " + " file " + " to container"
            + theContainer + " result: " + result);
             */
            //txn.commit();
            //txn.delete();
            //doQuery(theMgr, "//*/Species/Atoms/Atom/ChemicalElement/ElementSymbol");
            System.out.println("DB Container Opened");
            if (queryFlowr2 != null && queryFlowr2.trim().length() > 0) {
                doQuery(theMgr);
            }

            String mainQuery = "/XSAMSData/Species/Atoms/Atom/ChemicalElement/ElementSymbol";
            String secondQuery = "/XSAMSData/Species/Atoms/Atom/Isotope/Ion/@speciesID";
            String thirdQuery = "/XSAMSData/Species/Atoms/Atom/Isotope/Ion/AtomicState/AtomicNumericalData/StateEnergy/Value";
            //doQuery(theMgr, mainQuery, "Element Symbol", secondQuery, "Species ID");

            //System.out.println("Return to continue: ");
            //System.in.read();

            mainQuery = "/XSAMSData/Species/Atoms/Atom/Isotope/Ion/@speciesID";
            secondQuery = "/XSAMSData/Processes/Radiative/RadiativeTransition[SpeciesRef=$valueID]/EnergyWavelength/Wavelength/Value";
            //doQuery(theMgr, mainQuery, "Species ID", secondQuery, "Wavelength");

            //System.out.println("Return to continue: ");
            //System.in.read();

            mainQuery = "/XSAMSData/Species/Atoms/Atom/Isotope/Ion/AtomicState/@stateID";
            secondQuery = "/XSAMSData/Processes/Radiative/RadiativeTransition[LowerStateRef=$valueID]/Probability/TransitionProbabilityA/Value";
            //doQuery(theMgr, mainQuery, "State ID", secondQuery, "TransitionProbabilityA ");

            mainQuery = "/XSAMSData/Species/Atoms/Atom/Isotope/Ion/AtomicState/@stateID";
            secondQuery = "/XSAMSData/Processes/Radiative/RadiativeTransition[LowerStateRef=$valueID]/Probability/TransitionProbabilityA/Value";
            thirdQuery = "/XSAMSData/Processes/Radiative/RadiativeTransition[LowerStateRef=$valueID]/Probability/TransitionProbabilityA[Value=$value2ID]/../WeightedOscillatorStrength/Value";
            //doQuery(theMgr, mainQuery, "State ID", secondQuery, "TransitionProbabilityA", thirdQuery, "WeightedOscillatorStrength");

        } catch (Exception exp) {
            Logger.getLogger(BDXMLReturnables.class.getName()).log(Level.SEVERE, null, exp);
        } catch (Throwable ex) {
            Logger.getLogger(BDXMLReturnables.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cleanup(env, openedContainer, theMgr);
        }
    }

    private void createQuery(String[] xqueryMapping) {
    }

    private static void createFile() {
        FileOutputStream fop = null;
        File file;
        String content = "This is the text content";

        try {

            file = new File("xquery.txt");
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String readFileAsString() {
        StringBuffer fileData = new StringBuffer(1000);
        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader("/opt/IDE/NetBeans/NetBeansProjects/BDXMLReturnables/xquery.txt"));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData.toString();
    }
}
