/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.ioa.vamdc;

/**
 *
 * @author aassif
 */
import com.sleepycat.db.Environment;
import com.sleepycat.db.ErrorHandler;

public class MyErrorHandler implements ErrorHandler
{
    public void error(Environment env,
               String errpfx,
               String msg)
    {
        System.err.println(errpfx + " : " + msg);
    }
}   
