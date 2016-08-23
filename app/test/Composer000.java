package org.fleen.maximilian.app.test;

import org.fleen.maximilian.MComposition;

public class Composer000 implements Composer{
  
  Test test;
  
  Composer000(Test test){
    this.test=test;
  }
  
  public MComposition compose(){
    
    MComposition composition=new MComposition();
    composition.setForsythiaGrammar(test.getForsythiaGrammar());
    composition.initTree();
    return composition;
  }

}
