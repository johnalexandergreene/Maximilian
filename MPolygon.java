package org.fleen.maximilian;

import java.util.List;

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
    this.dpolygon=dpolygon;
    this.mmetagon=mmetagon;}
  
  /*
   * ################################
   * GEOM
   * ################################
   */
  
  public DPolygon dpolygon;
  public MMetagon mmetagon;
  
}
