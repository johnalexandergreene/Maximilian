package org.fleen.maximilian.jig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fleen.forsythia.grammar.ForsythiaGrammar;
import org.fleen.maximilian.MComposition;
import org.fleen.maximilian.MMetagon;
import org.fleen.maximilian.MShape;

/*
 * This is our basic jig server
 * it serves Splitters, Boilers, Crushers and Grinders
 * takes a forsythia grammar and a gap value
 * creates all of our various jigs
 * a list for each metagon 
 */
public class MJigServer000 implements MJigServer{
  
  public MJigServer000(MComposition composition,ForsythiaGrammar fg,double unitgap){
    initGaps(unitgap);
    initJigs(composition,fg);}

  
  /*
   * ################################
   * GAPS
   * for boiler, crusher and grinder
   * we have a unit and we do a couple integer multiples for each usage
   * grinder uses crusher gaps
   * ################################
   */
  
  private static final int 
    BOILERGAPSMALL=1,
    BOILERGAPLARGE=2,
    CRUSHERGAP0SMALL=1,
    CRUSHERGAP0LARGE=2,
    CRUSHERGAP1SMALL=1,
    CRUSHERGAP1LARGE=2;
  
  private double[] boilergaps,crushergap0s,crushergap1s;
  
  private void initGaps(double unitgap){
    boilergaps=new double[]{
      unitgap*BOILERGAPSMALL,
      unitgap*BOILERGAPLARGE};
    crushergap0s=new double[]{
      unitgap*CRUSHERGAP0SMALL,
      unitgap*CRUSHERGAP0LARGE};
    crushergap1s=new double[]{
      unitgap*CRUSHERGAP1SMALL,
      unitgap*CRUSHERGAP1LARGE};}
  
  /*
   * ################################
   * JIGS
   * ################################
   */
  
  Map<MMetagon,List<MJig>> jigsbymetagon=new HashMap<MMetagon,List<MJig>>();
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INIT
   * ++++++++++++++++++++++++++++++++
   */
  
  private void initJigs(MComposition composition,ForsythiaGrammar fg){
    
  }
  
  /*
   * ++++++++++++++++++++++++++++++++
   * ACCESS
   * ++++++++++++++++++++++++++++++++
   */
  
  public List<MJig> getJigs(MShape shape){
    // TODO Auto-generated method stub
    return null;
  }

  public List<MJig> getJigs(MShape shape,String...tag){
    // TODO Auto-generated method stub
    return null;
  }
  
 



}
