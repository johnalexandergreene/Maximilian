package org.fleen.maximilian.composition;

import java.util.ArrayList;

/*
 * It's a list of integers
 * Describes a maximilian composition geometry element in a contexty way.
 * We consider the geometry's chorus index, the chorus index of 
 * the geometry's first ancestral geometry component and so on, all the way to the root.
 */
public class MShapeSignature extends ArrayList<Integer>{
  
  private static final long serialVersionUID=2037581696842008653L;

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public MShapeSignature(MShape p){
    MShape node=p;
    while(node!=null){
      add(node.getChorusIndex());
      node=node.getFirstAncestorGeometry();}}
  
  //empty sig
  public MShapeSignature(){}
  
  /*
   * ################################
   * OBJECT
   * ################################
   */
  
  //hash code is summation of chorus indices
  public Integer hashcode=null;
  
  public int hashCode(){
    if(hashcode==null)initHashCode();
    return hashcode;}
  
  //sum of component chorus indices
  private void initHashCode(){
    hashcode=new Integer(0);
    for(int a:this)
      hashcode+=a;}
  
  public boolean equals(Object a){
    MShapeSignature s0=(MShapeSignature)a;
    if(s0.hashCode()!=hashCode())return false;
    int 
      c0=s0.size(),
      c1=size();
    if(c0!=c1)return false;
    int g0,g1;
    for(int i=0;i<c0;i++){
      g0=s0.get(i);
      g1=get(i);
      if(g0!=g1)return false;}
    return true;}
  
  private String objectstring=null;
  
  public String toString(){
    if(objectstring==null)initObjectString();
    return objectstring;}
  
  private void initObjectString(){
    objectstring="Signature[";
    int s=size()-1;
    for(int i=0;i<s;i++)
      objectstring+=get(i)+",";
    objectstring+=get(s)+"]";}
  
}
