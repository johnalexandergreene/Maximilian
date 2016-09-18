package org.fleen.maximilian.jig;

import java.util.Arrays;
import java.util.List;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.geom_2D.DPolygon;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;

/*
 * boil, split inner polygon, shrink children
 */
public class MJig_Crusher extends MJig_Abstract{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  //param is forsythia jig
  //TODO call that "Forsythia Operator" or something
  public MJig_Crusher(Jig fjig,double outergap,double innergap){
    this.forsythiaoperator=fjig;
    this.outergap=outergap;
    this.innergap=innergap;}
  
  /*
   * ################################
   * FORSYTHIA OPERATOR
   * ################################
   */
  
  Jig forsythiaoperator;
  
  /*
   * ################################
   * GAPS
   * ################################
   */
  
  double outergap,innergap;  
  
  /*
   * ################################
   * CREATE SHAPES
   * ################################
   */
  
  protected CreatedShapes createShapes(MShape target){
    MPolygon targetpolygon=(MPolygon)target;
    //get an inner polygon, squeezed in like boiling
    DPolygon dp0=(DPolygon)targetpolygon.dpolygon.clone();
    Util.shrink(dp0,outergap);
    MPolygon inner0=new MPolygon(dp0,targetpolygon.mmetagon);
    //split up the inner
    List<MPolygon> shards=Util.split(inner0,forsythiaoperator);
    //shrink the shards
    for(MPolygon shard:shards){
      Util.shrink(shard.dpolygon,innergap);
      shard.addTags(Arrays.asList(new String[]{"egg"}));
      shard.setParent(target);}
    //do the yard
    MYard yard=new MYard((MPolygon)target,shards,0,Arrays.asList(new String[]{"foam"}));
    //
    return new CreatedShapes(shards,yard);}
  
}
