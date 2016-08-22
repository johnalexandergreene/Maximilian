package org.fleen.maximilian.app.test;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.JTextField;

import org.fleen.forsythia.grammar.ForsythiaGrammar;
import org.fleen.maximilian.MComposition;
import org.fleen.maximilian.grammar.MGrammar;

/*
 * test for maximilian
 */
public class Test{
  
  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public Test(){
    initUI();
    initGrammar();}
  
  /*
   * ################################
   * UI
   * ################################
   */
  
  private static final String TITLE="Test";
  
  private static final int ERRORMESSAGEFONTSIZE=24;
  private static final Font ERRORMESSAGEFONT=new Font("Sans",Font.PLAIN,ERRORMESSAGEFONTSIZE);
  
  public UI ui;
  
  private void initUI(){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ui=new UI(Test.this);
          ui.setDefaultWindowBounds();
          ui.setVisible(true);
          ui.setTitle(TITLE);
          ui.txtinterval.setText(String.valueOf(CREATION_INTERVAL_DEFAULT));
          ui.txtexportsize.setText(String.valueOf(EXPORTWIDTH_DEFAULT)+"x"+String.valueOf(EXPORTHEIGHT_DEFAULT));
         }catch(Exception e){
           e.printStackTrace();}}});}
  
  /*
   * ################################
   * GRAMMAR
   * The Maximilian grammar uses a Forsythia grammar
   *   it elaborates it. from the metagons and operators it derives splitters, boilers and crushers
   *   
   * So we import an FGrammar and then we use it as a param to construct an MGrammar
   * ################################
   */
  
  private static final String FGRAMMAR_FILE_PATH=
      "/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/grammars/2016_06_05/g000_hexroot_kindasimple";
  
//  private static final String FGRAMMAR_FILE_PATH=
//      "/home/john/projects/code/Forsythia/src/org/fleen/forsythia/samples/grammars/2016_06_16/g001_simpletriangles_hexroot";
  
  MGrammar mgrammar;
  
  private void initGrammar(){
    ForsythiaGrammar fgrammar=getFGrammar();
    mgrammar=new MGrammar(fgrammar);}
    
  private ForsythiaGrammar getFGrammar(){
    ForsythiaGrammar grammar=null;
    try{
      File f=new File(FGRAMMAR_FILE_PATH);
      grammar=importGrammarFromFile(f);
    }catch(Exception x){}
    if(grammar==null)
      printGrammarImportFailed();
    return grammar;}
    
  private ForsythiaGrammar importGrammarFromFile(File file){
    FileInputStream fis;
    ObjectInputStream ois;
    ForsythiaGrammar g=null;
    try{
      fis=new FileInputStream(file);
      ois=new ObjectInputStream(fis);
      g=(ForsythiaGrammar)ois.readObject();
      ois.close();
    }catch(Exception x){}
    return g;}
  
  private void printGrammarImportFailed(){
    Graphics2D g=initImageForError();
    g.setPaint(Color.white);
    g.setFont(ERRORMESSAGEFONT);
    g.drawString("GRAMMAR IMPORT FAILED!",20,60);
    ui.panimage.repaint();}
    
  /*
   * ################################
   * COMPOSER
   * ################################
   */
  
  Composer composer=new Composer000(this);
  
  /*
   * ################################
   * RENDERER
   * ################################
   */
  
  Renderer renderer=new Renderer000(this);
  BufferedImage image=null;
  
  private Graphics2D initImageForError(){
    int 
      w=ui.panimage.getWidth(),
      h=ui.panimage.getHeight();
    image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics=image.createGraphics();
    graphics.setPaint(Color.red);
    graphics.fillRect(0,0,w,h);
    return graphics;}
  
  /*
   * ################################
   * MCOMPOSITION GENERATION CONTROL
   * ################################
   */
  
  MComposition composition;
  private boolean stopcontinuouscreation;

  /*
   * generate a composition, render it then stop
   */
  private void doIntermittantCreation(){
    composition=composer.compose();
    image=renderer.render();
    ui.panimage.repaint();
    //maybe export
    if(isExportModeAuto())
      export();}
  
  /*
   * generate a composition, render it, pause, repeat... until we tell it to stop
   */
  private void doContinuousCreation(){
    stopcontinuouscreation=false;
    new Thread(){
      public void run(){
        long 
          starttime,
          elapsedtime,
          pausetime;
        while(!stopcontinuouscreation){
          starttime=System.currentTimeMillis();
          //compose and render
          composition=composer.compose();
          image=renderer.render();
          //pause if necessary
          elapsedtime=System.currentTimeMillis()-starttime;
          pausetime=creationinterval-elapsedtime;
          try{
            if(pausetime>0)Thread.sleep(pausetime,0);
          }catch(Exception x){x.printStackTrace();}
          //paint
          ui.panimage.repaint();
          //maybe export
          if(isExportModeAuto())
            export();}}
    }.start();}
  
  private void stopContinuousCreation(){
    stopcontinuouscreation=true;}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * CREATION MODE
   * continuous or intermittant
   * ++++++++++++++++++++++++++++++++
   */
  
  static final boolean MODE_CONTINUOUS=false,MODE_INTERMITTANT=true; 
  boolean creationmode=MODE_INTERMITTANT;
  
  void toggleCreationMode(){
    if(creating)startStopCreation();
    creationmode=!creationmode;
    if(creationmode==MODE_CONTINUOUS){
      ui.lblmode.setText("CON");
    }else{
      ui.lblmode.setText("INT");}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * START-STOP CREATION
   * ++++++++++++++++++++++++++++++++
   */
  
  private boolean creating=false;
  
  void startStopCreation(){
    if(creationmode==MODE_INTERMITTANT){
      if(!creating){
        ui.lblstartstop.setText("||");
        doIntermittantCreation();
        ui.lblstartstop.setText(">>");}
    }else{
      if(creating){
        stopContinuousCreation();
        ui.lblstartstop.setText(">>");
        creating=false;
      }else{
        doContinuousCreation();
        ui.lblstartstop.setText("||");
        creating=true;}}}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * CREATION INTERVAL
   * minimum interval between images in continuous creation mode
   * ++++++++++++++++++++++++++++++++
   */
  
  private static final long CREATION_INTERVAL_DEFAULT=1000;
  
  private long creationinterval=CREATION_INTERVAL_DEFAULT;
  
  void setCreationInterval(JTextField t){
    try{
      long a=Long.valueOf(t.getText());
      creationinterval=a;
    }catch(Exception x){
      if(t.getText().equals(""))creationinterval=0;
      t.setText(String.valueOf(creationinterval));}}
  
  /*
   * ################################
   * EXPORT
   * ################################
   */
  
  private static final String EXPORTDIR="/home/john/Desktop/testexport";
  
  private static final int 
    EXPORTMODE_MANUAL=0,
    EXPORTMODE_AUTO=1;
  
  private static final int 
    EXPORTWIDTH_DEFAULT=1000,
    EXPORTHEIGHT_DEFAULT=1000;
  
  private int getExportMode(){
    if(ui.chkautoexport.isSelected())
      return EXPORTMODE_AUTO;
    else
      return EXPORTMODE_MANUAL;}
  
  boolean isExportModeManual(){
    return getExportMode()==EXPORTMODE_MANUAL;}
  
  boolean isExportModeAuto(){
    return getExportMode()==EXPORTMODE_AUTO;}
  
  void export(){
    File exportdir=getExportDir();
    if(exportdir==null||!exportdir.isDirectory()){
      printGetExportDirFailed();
      return;}
    //
    int w=EXPORTWIDTH_DEFAULT,h=EXPORTHEIGHT_DEFAULT;
    try{
      String a=ui.txtexportsize.getText();
      String[] b=a.split("x");
      w=Integer.valueOf(b[0]);
      h=Integer.valueOf(b[1]);
    }catch(Exception x){
      printGetExportImageSizeFailed();}
    //
    export(exportdir,w,h);}
  
  private void printGetExportDirFailed(){
    Graphics2D g=initImageForError();
    g.setPaint(Color.white);
    g.setFont(ERRORMESSAGEFONT);
    g.drawString("GET EXPORT DIR FAILED!",20,60);
    ui.panimage.repaint();}
  
  private void printGetExportImageSizeFailed(){
    Graphics2D g=initImageForError();
    g.setPaint(Color.white);
    g.setFont(ERRORMESSAGEFONT);
    g.drawString("%^&$ EXPORT IMAGE SIZE GARBLED!",20,ERRORMESSAGEFONTSIZE*2);
    g.drawString("Proper format is 123x456",20,ERRORMESSAGEFONTSIZE*4);
    g.drawString("Using default size : "+EXPORTWIDTH_DEFAULT+"x"+EXPORTHEIGHT_DEFAULT,20,ERRORMESSAGEFONTSIZE*6);
    ui.panimage.repaint();}
  
  private File getExportDir(){
    File exportdir=null;
    try{
      exportdir=new File(EXPORTDIR);
    }catch(Exception x){}
    return exportdir;}
  
  RasterExporter rasterexporter=new RasterExporter();
  
  private void export(File exportdir,int w,int h){
    System.out.println("export");
    BufferedImage exportimage=renderer.render();
    rasterexporter.setExportDir(exportdir);
    rasterexporter.export(exportimage);}
  
  /*
   * ################################
   * MAIN
   * ################################
   */
  
  public static final void main(String[] a){
    new Test();}
  
}
