package org.fleen.maximilian.composition;

import org.fleen.geom_Kisrhombille.KGrid;

/*
 * NODE GRID ROOT
 * Grid node on Tree where the grid is defined by local params
 * A simple wrapper for a KGrid
 */
public class MGridBasic extends MGrid{
  
  private static final long serialVersionUID=-6976402647903611804L;
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public MGridBasic(double[] origin,double foreward,boolean twist,double fish){
    localkgrid=new KGrid(origin,foreward,twist,fish);}
  
  //default form
  private static final double[] ORIGIN_DEFAULT={0,0};
  private static final double FOREWARD_DEFAULT=0;
  private static final boolean TWIST_DEFAULT=true;
  private static final double FISH_DEFAULT=1.0;
  
  public MGridBasic(){
    this(ORIGIN_DEFAULT,FOREWARD_DEFAULT,TWIST_DEFAULT,FISH_DEFAULT);}
  
  /*
   * ################################
   * KGRID
   * ################################
   */
  
  //geometry cache (the local kgrid) gets created at construction and is immutable
  //so this never gets used
  protected KGrid initLocalKGrid(){return null;}
  
}
