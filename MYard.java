package org.fleen.maximilian;

import java.util.ArrayList;
import java.util.List;

import org.fleen.geom_2D.DYard;

/*
 * A polygonal shape pierced by 1..n polygonal shapes (holes)
 * It can come in several varieties. Specifics are denoted by tag.
 * ie : 
 *   "bagel" : a polygon pierced by 1 similar polygon creating a bagel-like shape with a uniform wraparound shape
 *   "sponge" " a polygon pierced in such a way, by 1 or more polygons, as to create a pretty irregular space
 * 
 */
public class MYard extends MShape{

  private static final long serialVersionUID=-726090849488448340L;
  
  /*
   * ################################
   * CONSTRUCTORS
   * ################################
   */
  
  /*
   * the first polygon is the outer edge, the rest are holes
   * There is only ever at most just 1 yard in a jig-generated geometry 
   *   system, so we assign the chorus index automatically.
   *   We use our reserved chorus index for yards 
   */
  public MYard(List<MPolygon> mpolygons,int chorusindex,List<String> tags){
    super(MYARDCHORUSINDEX,tags);
    //create a new list to decouple param, for safety
    this.mpolygons=new ArrayList<MPolygon>(mpolygons);}
  
  public MYard(MPolygon outer,List<MPolygon> inner,int chorusindex,List<String> tags){
    super(MYARDCHORUSINDEX,tags);
    this.mpolygons=new ArrayList<MPolygon>(inner.size()+1);
    this.mpolygons.add(outer);
    this.mpolygons.addAll(inner);}
  
  /*
   * ################################
   * GEOMETRY
   * ################################
   */
  
  public List<MPolygon> mpolygons;
  
  public DYard getDYard(){
    DYard y=new DYard(mpolygons.size());
    for(MPolygon p:mpolygons)
      y.add(p.dpolygon);
    return y;}
  
  /*
   * ################################
   * CHORUS INDEX
   * ################################
   */
  
  public static final int MYARDCHORUSINDEX=Integer.MAX_VALUE;

}
