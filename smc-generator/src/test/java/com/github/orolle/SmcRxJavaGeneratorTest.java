/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.orolle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import junit.framework.TestCase;
import net.sf.smc.generator.SmcJavaGenerator;
import net.sf.smc.generator.SmcOptions;
import net.sf.smc.generator.SmcRxJavaGenerator;
import net.sf.smc.model.SmcMap;
import net.sf.smc.parser.SmcParser;

/**
 *
 * @author Oliver Rolle <oliver.rolle@the-urban-institute.de>
 */
public class SmcRxJavaGeneratorTest extends TestCase {

  public SmcRxJavaGeneratorTest(String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  
  public void testEasy() throws Exception {
    String srcSm = ""
      + "%class Turnstile\n"
      + "%package turnstile\n"
      + "%start MainMap::Locked\n"
      + "%map MainMap\n"
      + "%%\n"
      + "Locked\n"
      + "  Entry{activateAlarm();}"
      + "  Exit{deactivateAlarm();}"
      + "{\n"
      + "coin(value : Double) [isEnoughValue(value)] Unlocked\n"
      + " { unlock(); }\n"
      + "coin(value : Double) [isEnoughValue(value).map(b -> !b)] Locked\n"
      + " {}\n"
      + "pass nil\n"
      + " { alarm(); }\n"
      + "}\n"
      + "Unlocked\n"
      + "{\n"
      + "pass Locked {\n"
      + " lock(); }\n"
      + "coin(value : Double) nil\n"
      + " {\n"
      + " thankyou(); }\n"
      + "}\n"
      + "%%";
    diff_match_patch dmp = new diff_match_patch();

    String javaCode = smToJava("Turnstile", srcSm);
    String rxjavaCode = smToRxJava("Turnstile", srcSm);

    /*
    LinkedList<diff_match_patch.Diff> data = dmp.diff_main(javaCode, rxjavaCode);
    data.stream().
      filter(d -> !d.operation.equals(diff_match_patch.Operation.EQUAL)).
      forEach(System.out::println);
    */
    
    //System.out.println(javaCode);
    //System.out.println(rxjavaCode);
    
    /*
    Turnstile ts = new Turnstile();
    TurnstileContext sm = new TurnstileContext(ts);

    sm.coin(1.0);
    sm.pass();
    sm.pass();

    sm.coin(0.0);
    sm.pass();
    sm.pass();
    */
  }

  public static String smToJava(String name, String srcSm) throws Exception {
    InputStream isSm = new ByteArrayInputStream(srcSm.getBytes());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream outCode = new PrintStream(bos);

    SmcMap._stateId = 0;
    SmcParser parser = new SmcParser(name, isSm, SmcParser.TargetLanguage.JAVA, false);
    SmcJavaGenerator java = new SmcJavaGenerator(new SmcOptions("", "", "", "", "", "", 0, true, -1, true, true, true, true, false, true, true, "public"));

    java.setSource(outCode);
    java.visit(parser.parse());

    return new String(bos.toByteArray());
  }

  public static String smToRxJava(String name, String srcSm) throws Exception {
    InputStream isSm = new ByteArrayInputStream(srcSm.getBytes());
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PrintStream outCode = new PrintStream(bos);

    SmcMap._stateId = 0;
    SmcParser parser = new SmcParser(name, isSm, SmcParser.TargetLanguage.JAVA, false);
    SmcRxJavaGenerator rxjava = new SmcRxJavaGenerator(new SmcOptions("", "", "", "", "", "", 0, true, -1, true, true, true, true, false, true, true, "public"));

    rxjava.setSource(outCode);
    rxjava.visit(parser.parse());

    return new String(bos.toByteArray());
  }
}
