package org.fleen.maximilian.jig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.grammar.ForsythiaGrammar;
import org.fleen.forsythia.grammar.Jig;
import org.fleen.maximilian.MComposition;
import org.fleen.maximilian.MMetagon;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MShapeSignature;

/*
 * This is our basic jig server
 * it serves Splitters, Boilers, Crushers and Grinders 
 */
public class MJigServer_Basic implements MJigServer{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public MJigServer_Basic(MComposition composition,ForsythiaGrammar fg,double unitgap,double detaillimit,double distortionlimit){
    this.detaillimit=detaillimit;
    this.distortionlimit=distortionlimit;
    initGaps(unitgap);
    initJigs(composition,fg);}
  
  /*
   * ################################
   * DETAIL LIMIT
   * ################################
   */
  
  double detaillimit;
  
  /*
   * ################################
   * DISTORTION LIMIT
   * ################################
   */
  
  double distortionlimit;
  
  /*
   * ################################
   * JIG ACCESS
   * ################################
   */
  
  Map<MShapeSignature,MJig> jigbysig=new HashMap<MShapeSignature,MJig>();
  Random rnd=new Random();
  
  public MJig getJig(MShape target,String[] tags){
    //check to see if we already have a jig for shapes with that sig, for chorussing. Symmetry that is
    MShapeSignature sig=target.getSignature();
    MJig jig=jigbysig.get(sig);
    if(jig!=null)return jig;
    //get all of the jigs for the shape
    List<MJig> jigs=getJigs(target);
    
    System.out.println("getjigs unfiltered size = "+jigs.size());
    
    //filter by tags
    if(tags!=null&&tags.length!=0)
      jigs=filterByTags(jigs,tags);
    
    System.out.println("getjigs filtered by tags size = "+jigs.size());
    
    if(jigs.isEmpty())return null;
    //filter by detail size
    jigs=filterByDetailSize(jigs,target);
    if(jigs.isEmpty())return null;
    //filter by distortion level
    jigs=filterByDistortionLevel(jigs,target);
    if(jigs.isEmpty())return null;
    //get random jig from list of prospects
    if(jigs.size()==1)
      jig=jigs.get(0);
    else
      jig=jigs.get(rnd.nextInt(jigs.size()));
    //store it for future use, keyed by shape sig for chorussing
    jigbysig.put(sig,jig);
    //
    return jig;}
  
  private List<MJig> filterByTags(List<MJig> jigs,String[] tags){
    List<MJig> filteredjigs=new ArrayList<MJig>();
    for(MJig jig:jigs)
      if(jig.hasTags(tags))
        filteredjigs.add(jig);
    return filteredjigs;}
  
  private List<MJig> filterByDetailSize(List<MJig> jigs,MShape target){
    List<MJig> filteredjigs=new ArrayList<MJig>();
    for(MJig jig:jigs)
      if(jig.getDetailSizePreview(target)>detaillimit)
        filteredjigs.add(jig);
    return filteredjigs;}
  
  private List<MJig> filterByDistortionLevel(List<MJig> jigs,MShape target){
    List<MJig> filteredjigs=new ArrayList<MJig>();
    for(MJig jig:jigs)
      if(jig.getDistortionLevelPreview(target)<distortionlimit)
        filteredjigs.add(jig);
    return filteredjigs;}
  
  /*
   * return all jigs for the specified metagon
   */
  public List<MJig> getJigs(MShape shape){
    List<MJig> jigs=new ArrayList<MJig>();
    jigs.addAll(jigsbymetagon.get(((MPolygon)shape).mmetagon));//THIS JUST WORKS FOR POLYGONS PRESENTLY TODO
    jigs.addAll(boilers);
    return jigs;}

  /*
   * ################################
   * GAPS
   * for boiler, crusher and grinder
   * we have a unit and we do a couple integer multiples for each usage
   * grinder uses crusher gaps
   * ################################
   */
  
  private static final int 
    BOILERGAPSMALL=2,
    BOILERGAPLARGE=4,
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
  
  List<MJig> boilers=new ArrayList<MJig>();
  Map<MMetagon,List<MJig>> jigsbymetagon=new HashMap<MMetagon,List<MJig>>();
  
  /*
   * ++++++++++++++++++++++++++++++++
   * INIT
   * ++++++++++++++++++++++++++++++++
   */
  
  private void initJigs(MComposition composition,ForsythiaGrammar fg){
    //init universal jigs
    initBoilers();
    //init metagon-specific jigs
    List<MMetagon> metagons=composition.getMMetagons();
    List<Jig> fjigs;
    List<MJig> mjigs;
    for(MMetagon mmetagon:metagons){
      fjigs=fg.getJigs(mmetagon);
      mjigs=new ArrayList<MJig>();
      jigsbymetagon.put(mmetagon,mjigs);
      initSplitters(fjigs,mjigs);
      initCrushers(fjigs,mjigs);
      initGrinders(fjigs,mjigs);}}

  /*
   * boilers are universal
   * for each boiler gap value we create one boiler
   */
  private void initBoilers(){
    MJig_Boiler boiler;
    for(double gap:boilergaps){
      boiler=new MJig_Boiler(gap);
      boilers.add(boiler);}}
  
  private void initSplitters(List<Jig> fjigs,List<MJig> mjigs){
    MJig_Splitter splitter;
    for(Jig fjig:fjigs){
      splitter=new MJig_Splitter(fjig);
      mjigs.add(splitter);}}
  
  private void initCrushers(List<Jig> fjigs,List<MJig> mjigs){
    MJig_Crusher crusher;
    for(Jig fjig:fjigs){
      for(double gap0:crushergap0s){
        for(double gap1:crushergap1s){
          crusher=new MJig_Crusher(fjig,gap0,gap1);
          mjigs.add(crusher);}}}}
  
  private void initGrinders(List<Jig> fjigs,List<MJig> mjigs){
    
  }


}
