package org.fleen.maximilian;

import java.util.List;

import org.fleen.forsythia.grammar.ForsythiaGrammar;

/*
 * Based on a ForsythiaGrammar
 * A list of MMetagons (derived from FMetagons). The operands in our grammar. Shapes in the abstract.
 * A list of basic jigs. Splitters. Derived from ForsythiaGrammar.
 * Methods for getting boilers and crushers, each derived from a splitter and some geometry params
 */
public class MJigServer{
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  public MJigServer(ForsythiaGrammar fg){
    
  }
  
  /*
   * ################################
   * MMETAGONS
   * ################################
   */
  
  public List<MMetagon> getMMetagons(){
    return null;
  }
  
  /*
   * ################################
   * JIGS APPLICABLE TO MPOLYGONS
   * ################################
   */
  
  /*
   * simple. based on FGrammar operators
   */
  public List<MJig> getSplitters(MPolygon mpolygon){
    return null;
  }
  
  public List<MJig> getBoilers(MPolygon mpolygon,double span){
    return null;
  }
  
  public List<MJig> getCrushers(MPolygon mpolygon,double span0,double span1){
    return null;
  }
  
  /*
   * ################################
   * JIGS APPLICABLE TO MYARDS
   * ################################
   */
  
  

}
