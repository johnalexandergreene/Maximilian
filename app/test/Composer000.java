package org.fleen.maximilian.app.test;

import java.util.List;
import java.util.Random;

import org.fleen.forsythia.grammar.ForsythiaGrammar;
import org.fleen.maximilian.MComposition;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;
import org.fleen.maximilian.jig.MJig;
import org.fleen.maximilian.jig.MJigServer;
import org.fleen.maximilian.jig.MJigServer_Basic;

public class Composer000 implements Composer{
  
  Test test;
  
  Composer000(Test test){
    this.test=test;
  }
  
  MComposition composition;
  ForsythiaGrammar forsythiagrammar;
  
  public MComposition compose(){
    forsythiagrammar=test.getForsythiaGrammar();
    composition=new MComposition();
    composition.setForsythiaGrammar(forsythiagrammar);
    composition.initTree();
    initJigServer();
    //
    try{
      for(int i=0;i<6;i++)
        cultivate(composition);
    }catch(Exception x){
      x.printStackTrace();}
    return composition;}
  
  private void cultivate(MComposition composition){
    List<MShape> oldshapes=composition.getLeafShapes();
    MJig jig;
    for(MShape oldshape:oldshapes){
      jig=getJig(composition,oldshape);
      if(jig!=null){
        try{
          jig.createAndLinkShapes(oldshape);
        }catch(Exception x){
          System.out.println("JIG FAILED");
          x.printStackTrace();
          }}}}
  
  Random rnd=new Random();
  
  private MJig getJig(MComposition composition,MShape shape){
    MJig jig;
    if(shape instanceof MYard){
      return null;}
    //split eggs; boil or crush shards
    
    System.out.println("shape = "+shape);
    
    if(shape.hasTags("egg")){
      System.out.println("getting splitter");
      jig=jigserver.getJig(shape,new String[]{"splitter"});
    }else{//shard
      int r=rnd.nextInt(4);
      if(r==0){
        jig=jigserver.getJig(shape,new String[]{"boiler"});
      }else{
        jig=jigserver.getJig(shape,new String[]{"crusher"});}}
    return jig;}
  
  /*
   * ################################
   * JIG SERVER
   * ################################
   */

  private static final double 
    UNITGAP=0.03,
    DETAILLIMIT=0.1,
    DISTORTIONLIMIT=100;
  
  MJigServer jigserver;

  private void initJigServer(){
    jigserver=new MJigServer_Basic(composition,forsythiagrammar,UNITGAP,DETAILLIMIT,DISTORTIONLIMIT);}
  
}
