package org.fleen.maximilian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_2D.DPolygon;

/*
 * A 2d polygon : dpolygon
 * based roughly upon an ideal form : mmetagon
 *   gleaned from a grammar
 */
public class MPolygon extends MShape{
  
  private static final long serialVersionUID=7403675520824450721L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public MPolygon(DPolygon dpolygon,MMetagon mmetagon,int chorusindex,List<String> tags){
    super(chorusindex,tags);
    this.dpolygon=new DPolygon(dpolygon);
    this.mmetagon=mmetagon;}
  
  //for mpolygon geometry manipulation stuff in jig or whatever
  public MPolygon(DPolygon dpolygon,MMetagon mmetagon){
    super(0,new ArrayList<String>(0));
    this.dpolygon=new DPolygon(dpolygon);
    this.mmetagon=mmetagon;}
  
  public MPolygon(MMetagon mmetagon){
    super(0,Arrays.asList(mmetagon.getTags()));
    this.mmetagon=mmetagon;
    dpolygon=mmetagon.getPolygon().getDefaultPolygon2D();}
  
  //TODO for jigs we will need anchor params or something.
  
  /*
   * ################################
   * GEOM
   * ################################
   */
  
  public DPolygon dpolygon;
  public MMetagon mmetagon;
  
  public DPolygon getDPolygon(){
    return dpolygon;}
  
  public boolean hasBadGeometry(){
    for(DPoint p:dpolygon)
      if(p==null)
        return true;
    return false;}
  
}
