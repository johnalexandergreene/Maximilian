package org.fleen.maximilian.app.test;

import java.util.List;
import java.util.Random;

import org.fleen.forsythia.grammar.Jig;
import org.fleen.maximilian.MComposition;
import org.fleen.maximilian.MJig;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;
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
    
    
    
//    //skew root for deformity test
//    MPolygon root=composition.getRoot();
//    DPoint a=root.dpolygon.get(0);
//    a.x+=0.3;
//    a.y+=0.3;
////    a=root.dpolygon.get(1);
////    a.x+=0.5;
////    a.y+=2;
    
    try{
      for(int i=0;i<5;i++)
        cultivate(composition);
    }catch(Exception x){}
    
    return composition;}
  

  
  private void cultivate(MComposition composition){
    System.out.println("===cultivating 1 cycle===");
    System.out.println("pre shape count : "+composition.getShapes().size());
    List<MShape> 
      shapes=composition.getLeafShapes(),
      newshapes;
    MJig jig;
    for(MShape shape:shapes){
      jig=getJig(composition,shape);
      System.out.println("got jig : "+jig);
      if(jig!=null){
        newshapes=jig.createShapes(shape);
        System.out.println("created shapes : "+newshapes.size());
        }}
    System.out.println("post shape count : "+composition.getShapes().size());
    System.out.println("+++++++++++++++++++++");}
  
  private MJig getJig(MComposition composition,MShape shape){
    if(shape instanceof MYard)return null;
    MJig jig=null;
    int r=rnd.nextInt(3);
    if(r==0)
      jig=new MJig_Boiler();
    else if(r==1)
      jig=getSplitter(composition,(MPolygon)shape);
    else
      jig=getCrusher(composition,(MPolygon)shape);
    return jig;}
  
  Random rnd=new Random();
  
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
