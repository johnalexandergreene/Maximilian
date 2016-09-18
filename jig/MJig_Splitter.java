package org.fleen.maximilian.jig;

import java.util.Arrays;
import java.util.List;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;

/*
 * A splitter splits a polygon into nore polygons, puzzlewise
 * 
 * the pieces are all tagged with "shard"
 */
public class MJig_Splitter extends MJig_Abstract{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public MJig_Splitter(Jig forsythiaoperator){
    this.forsythiaoperator=forsythiaoperator;}
  
  /*
   * ################################
   * FORSYTHIA OPERATOR
   * ################################
   */
  
  private Jig forsythiaoperator;
  
  /*
   * ################################
   * CREATE SHAPES
   * ################################
   */
  
  public CreatedShapes createShapes(MShape target){
    List<MPolygon> shards=Util.split((MPolygon)target,forsythiaoperator);
    for(MShape shape:shards)
      shape.addTags(Arrays.asList(new String[]{"shard"}));
    return new CreatedShapes(shards);}

}
