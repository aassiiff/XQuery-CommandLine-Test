/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc.molecules;

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

/**
 *
 * @author aakram
 */
public class MoleculesMain {

    private static String tempContainer = "hitran-container";
    private static String fileName = "CDMS-2012-06-25T00_28_11.916574.xsams";
                                        // "CDMS-2012-06-25T00_26_24.465914.xsams";
                                        //"HSHD-2012-06-25T00_27_41.103372.xsams";
    
    //private static String tempContainer = "basecol-container";
    //private static String fileName = "****";
    private static String theContainer = tempContainer + ".dbxml";
    private static String collection = "collection('" + theContainer + "')";

    public static void main(String argv[]) {

        MoleculeQueries molQueries = new MoleculeQueries(theContainer);
        
        Quereis queries = new Quereis(theContainer);
        
        File path2DbEnv = new File("/opt/jboss/VAMDCData/tempXSAMS/" + tempContainer + "/");

        Environment env = null;
        XmlContainer openedContainer = null;
        //XmlTransaction txn = null;
        XmlManager theMgr = null;

        try {
            env = createEnv(path2DbEnv);

            theMgr = new XmlManager(env, new XmlManagerConfig());

            XmlContainerConfig config = new XmlContainerConfig();

            System.out.println("Opening DB Container");

            //Open a non-transactional container
            if (theMgr.existsContainer(theContainer) == 0) {
                openedContainer = theMgr.createContainer(theContainer, config);
            }

            openedContainer = theMgr.openContainer(theContainer, config);

            /*
            XmlInputStream inputSteam = theMgr.createLocalFileInputStream("/opt/jboss/VAMDCData/xsams/" + fileName);
            
            System.out.println("Adding file in the container: " + theContainer);
            String result = openedContainer.putDocument(fileName, inputSteam, XmlDocumentConfig.DEFAULT);
            
            System.out.println("Added " + " file " + " to container"
            + theContainer + " result: " + result);
             
             */
            
            ReturnableXQueryMapping tempMapping = new ReturnableXQueryMapping();
            HashMap<String, String>  XQueryMappingArray = tempMapping.radTransXQueryMapping(7);
            
            String tempFlworQuery = molQueries.buildQuery(XQueryMappingArray);
            System.out.println(tempFlworQuery);
            
            String mainQuery = //"/XSAMSData/Species/Molecules/Molecule/MolecularChemicalSpecies/NormalModes/NormalMode/HarmonicFrequency/Value";
                    //"/XSAMSData/Species/Molecules/Molecule[@speciesID=\"XHIT-XLYOFNOQVPJJNP-UHFFFAOYSA-N\"]/MolecularChemicalSpecies/InChIKey";
                    //"/XSAMSData/Species/Molecules/Molecule/MolecularChemicalSpecies/OrdinaryStructuralFormula";
                    "/XSAMSData/Species/Molecules/Molecule/MolecularState/Case";
                    //"/XSAMSData/Species/Molecules/Molecule/MolecularState/Case/ltcs:QNs/ltcs:v1";
                    //"/XSAMSData/Species/Molecules/Molecule/@speciesID";
            
            mainQuery = collection + mainQuery; 
            //String secondQuery = "/XSAMSData/Processes/Radiative/RadiativeTransition[SpeciesRef=$valueID]/EnergyWavelength/Wavelength/Value";
            
           
            //mainQuery = molQueries.queryFlowr1;
            //mainQuery = queries.queryFlowr1;
            mainQuery = tempFlworQuery;
            
            doQuery(theMgr, mainQuery);
            openedContainer.close();

            //molQueries.createReturnablesList(10);

        } catch (Exception exp) {
            Logger.getLogger(MoleculesMain.class.getName()).log(Level.SEVERE, null, exp);
        } catch (Throwable ex) {
            Logger.getLogger(MoleculesMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cleanup(env, openedContainer, theMgr);
        }

    }

    private static void doQuery(XmlManager mgr, String mainQuery, String secondQuery) throws Throwable {
        //Perform a single query against the referenced container.
        // No context is used for this query.
        String fullQuery = collection + mainQuery;
        System.out.println("Exercising query: '" + fullQuery + "'.");


        //Perform the query

        XmlQueryContext context = mgr.createQueryContext();

        //context.setNamespace("", "http://vamdc.org/xml/xsams/0.2");
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

    private static void doQuery(XmlManager mgr, String mainQuery) throws Throwable {
        //Perform a single query against the referenced container.
        // No context is used for this query.
        //String fullQuery = collection + mainQuery;
        System.out.println("Exercising query: '" + mainQuery + "'.");


        String ElecStateLabel = "./ltcs:QNs/ltcs:ElecStateLabel";
        
        String v1 = "./ltcs:QNs/ltcs:v1";
        String v2 = "./ltcs:QNs/ltcs:v2";
        String v3 = "./ltcs:QNs/ltcs:v3";
        
        String F = "./ltcs:QNs/ltcs:F";
        String F1 = "./ltcs:QNs/ltcs:F1";
        String F2 = "./ltcs:QNs/ltcs:F2";
        
        String Ka = "./ltcs:QNs/ltcs:Ka";
        String Kc = "./ltcs:QNs/ltcs:Kc";
        
        String l2 = "./ltcs:QNs/ltcs:l2";
        
        String J = "./ltcs:QNs/ltcs:J";
        
        String r = "./ltcs:QNs/ltcs:r";
        
        String kronigParity = "./ltcs:QNs/ltcs:kronigParity";
        
        String parity = "./ltcs:QNs/ltcs:parity";
        
        String asSym = "./ltcs:QNs/ltcs:asSym";
        //Perform the query

        XmlQueryContext context = mgr.createQueryContext();

        //context.setNamespace("", "http://vamdc.org/xml/xsams/0.2");
        
        context.setNamespace("", "http://vamdc.org/xml/xsams/0.3");
        
        context.setNamespace("ltcs", "http://vamdc.org/xml/xsams/0.3/cases/ltcs");
        
        context.setNamespace("nltcs", "http://vamdc.org/xml/xsams/0.3/cases/nltcs");
        
        context.setNamespace("dcs", "http://vamdc.org/xml/xsams/0.3/cases/dcs");
        
        context.setNamespace("hunda", "http://vamdc.org/xml/xsams/0.3/cases/hunda");
        
        context.setNamespace("hundb", "http://vamdc.org/xml/xsams/0.3/cases/hundb");
        
        context.setNamespace("stcs", "http://vamdc.org/xml/xsams/0.3/cases/stcs");
        
        context.setNamespace("lpcs", "http://vamdc.org/xml/xsams/0.3/cases/lpcs");
        
        context.setNamespace("asymcs", "http://vamdc.org/xml/xsams/0.3/cases/asymcs");
        
        context.setNamespace("asymos", "http://vamdc.org/xml/xsams/0.3/cases/asymos");
        
        context.setNamespace("sphcs", "http://vamdc.org/xml/xsams/0.3/cases/sphcs");
        
        context.setNamespace("sphos", "http://vamdc.org/xml/xsams/0.3/cases/sphos");
        
        context.setNamespace("ltos", "http://vamdc.org/xml/xsams/0.3/cases/ltos");
        
        context.setNamespace("lpos", "http://vamdc.org/xml/xsams/0.3/cases/lpos");
        
        context.setNamespace("nltos", "http://vamdc.org/xml/xsams/0.3/cases/nltos");
        
        XmlQueryExpression ElecStateLabelExpr = mgr.prepare(ElecStateLabel, context);

        XmlQueryExpression v1Expr = mgr.prepare(v1, context);
        XmlQueryExpression v2Expr = mgr.prepare(v2, context);
        XmlQueryExpression v3Expr = mgr.prepare(v3, context);
        
        XmlQueryExpression FExpr  = mgr.prepare(F, context);
        XmlQueryExpression F1Expr = mgr.prepare(F1, context);
        XmlQueryExpression F2Expr = mgr.prepare(F2, context);
        
        XmlQueryExpression KaExpr = mgr.prepare(Ka, context);
        XmlQueryExpression KcExpr = mgr.prepare(Kc, context);

        XmlQueryExpression l2Expr = mgr.prepare(l2, context);

        XmlQueryExpression JExpr = mgr.prepare(J, context);

        XmlQueryExpression rExpr = mgr.prepare(r, context);

        XmlQueryExpression kronigParityExpr = mgr.prepare(kronigParity, context);
        
        XmlQueryExpression parityExpr = mgr.prepare(parity, context);
        
        XmlQueryExpression asSymExpr = mgr.prepare(asSym, context);

        //XmlQueryExpression fnExpr = mgr.prepare(v1, context);
        //XmlQueryExpression fnExpr = mgr.prepare(v1, context);

        XmlResults results = mgr.query(mainQuery, context, null);
        System.out.println(results.size() + " results returned for query '"
                + mainQuery + "'.");

        //Iterate over the results of the query using an XmlValue object
        XmlValue value;
        while ((value = results.next()) != null) {
            //String nameSpacePrefix = value.getNodeValue();
            System.out.println(value.asString());

            /*
            XmlResults v1Results = v1Expr.execute(value, context);
            XmlResults v2Results = v2Expr.execute(value, context);
            XmlResults v3Results = v3Expr.execute(value, context);

            XmlResults l2Results = l2Expr.execute(value, context);

            XmlResults JResults = JExpr.execute(value, context);

            XmlResults rResults = rExpr.execute(value, context);

            XmlResults kronigParityResults = kronigParityExpr.execute(value, context);

            
            String v1String = "";
            if (v1Results.size() > 0) {
                XmlValue fnValue = v1Results.next();
                v1String = fnValue.asString();

                System.out.println(v1String);
            }

            String v2String = "";
            if (v2Results.size() > 0) {
                XmlValue fnValue = v2Results.next();
                v2String = fnValue.asString();

                System.out.println(v2String);
            }

            String v3String = "";
            if (v3Results.size() > 0) {
                XmlValue fnValue = v3Results.next();
                v3String = fnValue.asString();

                System.out.println(v3String);
            }

            String l2String = "";
            if (l2Results.size() > 0) {
                XmlValue fnValue = l2Results.next();
                l2String = fnValue.asString();

                System.out.println(l2String);
            }

            String JString = "";
            if (JResults.size() > 0) {
                XmlValue fnValue = JResults.next();
                JString = fnValue.asString();

                System.out.println(JString);
            }

            String rString = "";
            if (rResults.size() > 0) {
                XmlValue fnValue = rResults.next();
                rString = fnValue.asString();

                System.out.println(rString);
            }

            String kronigParityString = "";
            if (kronigParityResults.size() > 0) {
                XmlValue fnValue = kronigParityResults.next();
                kronigParityString = fnValue.asString();

                System.out.println(kronigParityString);
            }

           */
             
            System.out.println();

        }
        /*
        System.out.println(results.size() + " results returned for query '"
        + fullQuery + "'.");*/
        //results.delete();
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


        //config.setErrorHandler(new MyErrorHandler());
        config.setErrorStream(System.err);

        config.setLogAutoRemove(true);

        config.setLogBufferSize(10000);

        config.setMaxLockers(10000);
        config.setMaxLockObjects(10000);
        config.setMaxLocks(10000);

        System.out.println(config.getMaxLocks());
        return new Environment(home, config);
    }

    //Utility function to clean up objects, exceptions or not,
    // containers and environments must be closed.
    private static void cleanup(Environment env, XmlContainer openedContainer, XmlManager theMgr) {
        try {
            if (openedContainer != null) {
                openedContainer.delete();
            }
            if (env != null) {
                //env.close();
            }
            if (theMgr != null) {
                theMgr.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
