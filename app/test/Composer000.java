package org.fleen.maximilian.app.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.maximilian.MComposition;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MShapeSignature;
import org.fleen.maximilian.MYard;
import org.fleen.maximilian.jig.MJig;
import org.fleen.maximilian.jig.MJig_Boiler;
import org.fleen.maximilian.jig.MJig_Crusher;
import org.fleen.maximilian.jig.MJig_Splitter;

public class Composer000 implements Composer{
  
  Test test;
  
  Composer000(Test test){
    this.test=test;
  }
  
  MComposition composition;
  
  public MComposition compose(){
    
    composition=new MComposition();
    composition.setForsythiaGrammar(test.getForsythiaGrammar());
    composition.initTree();
    try{
      for(int i=0;i<4;i++)
        cultivate(composition);
    }catch(Exception x){
      x.printStackTrace();
      
    }
    
    return composition;}
  

  
  private void cultivate(MComposition composition){
    List<MShape> oldshapes=composition.getLeafShapes();
    MJig jig;
    jigbysig=new HashMap<MShapeSignature,MJig>();
    for(MShape oldshape:oldshapes){
      jig=getJig(composition,oldshape);
      if(jig!=null){
        try{
          jig.createShapes(oldshape);
        }catch(Exception x){
          System.out.println("JIG FAILED");
          x.printStackTrace();
          }}}}
  
  Map<MShapeSignature,MJig> jigbysig;
  
  Random rnd=new Random();
  
  private MJig getJig(MComposition composition,MShape shape){
    if(shape instanceof MYard){
      return null;}
    //try sig
    MShapeSignature sig=shape.getSignature();
    MJig jig=jigbysig.get(sig);
    if(jig!=null)return jig;
    //
    int r;
    //split eggs; boil or crush shards
    if(shape.hasTag("egg")){
      jig=getSplitter(composition,(MPolygon)shape);
    }else{//shard
      r=rnd.nextInt(4);
      if(r==0){
        jig=new MJig_Boiler();
      }else{
        jig=getCrusher(composition,(MPolygon)shape);}}
    //
    jigbysig.put(sig,jig);
    return jig;}
  
  private MJig getSplitter(MComposition composition,MPolygon polygon){
    List<Jig> a=composition.getForsythiaGrammar().getJigs(polygon.mmetagon);
    if(a.isEmpty())return null;
    Jig fjig=a.get(rnd.nextInt(a.size()));
    return new MJig_Splitter(fjig);//TODO we will create these all at once at jig server init
    }
  
  private MJig getCrusher(MComposition composition,MPolygon polygon){
    List<Jig> a=composition.getForsythiaGrammar().getJigs(polygon.mmetagon);
    if(a.isEmpty())return null;
    Jig fjig=a.get(rnd.nextInt(a.size()));
    return new MJig_Crusher(fjig);//TODO we will create these all at once at jig server init
  }

}
