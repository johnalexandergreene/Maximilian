package org.fleen.maximilian.app.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.fleen.geom_2D.DPoint;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MShapeSignature;
import org.fleen.maximilian.MYard;

public class Renderer002_fill implements Renderer{
  
  Test test;
  
  Renderer002_fill(Test test){
    this.test=test;
  }
  
  /*
   * ################################
   * STROKE
   * ################################
   */
  
  private Stroke createStroke(double strokewidth){
    Stroke stroke=new BasicStroke((float)strokewidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return stroke;}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
//  static final Color[] COLORS={new Color(255,0,0),new Color(0,0,0),new Color(255,255,255),new Color(222,222,222)};
  static final Color[] COLORS={new Color(255,0,0),new Color(0,0,0),new Color(255,255,255),new Color(222,222,222),new Color(255,255,0)};
  
  Random rnd=new Random();
  Map<MShapeSignature,Color> colorbysig=new HashMap<MShapeSignature,Color>();
  
  private Color getRandomColor(MShape shape){
    MShapeSignature sig=shape.getSignature();
    Color c=colorbysig.get(sig);
    if(c==null){
      c=COLORS[rnd.nextInt(COLORS.length)];
      colorbysig.put(sig,c);}
    return c;}
  
  public BufferedImage render(){
    colorbysig.clear();
    int 
      w=test.ui.panimage.getWidth(),
      h=test.ui.panimage.getHeight();
    BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    AffineTransform t=getTransform(w,h,test.composition.getRoot());
    Graphics2D graphics=image.createGraphics();
    graphics.setPaint(Color.white);
    graphics.fillRect(0,0,w,h);
    graphics.setTransform(t);
    //fill
    Path2D path;
    List<MShape> shapes=test.composition.getLeafShapes();
    for(MShape ms:shapes){
      graphics.setPaint(getRandomColor(ms));
      path=getShapePath(ms);
      if(path!=null)graphics.fill(path);}
    //stroke
    
//    //separate good from bad
//    shapes=test.composition.getShapes();
//    List<MPolygon> 
//      goodpolygons=new ArrayList<MPolygon>(),
//      badpolygons=new ArrayList<MPolygon>();
//    for(MShape shape:shapes){
//      if(shape instanceof MPolygon)
//        if(shape.hasBadGeometry())
//          badpolygons.add((MPolygon)shape);
//        else
//          goodpolygons.add((MPolygon)shape);}
//    
//    //stroke good
//    graphics.setPaint(Color.black);
//    graphics.setStroke(createStroke(0.011));
//    for(MPolygon polygon:goodpolygons){
//      path=getShapePath(polygon);
//      graphics.draw(path);}
//    
//    //stroke bad
//    graphics.setPaint(Color.red);
//    graphics.setStroke(createStroke(0.022));
//    for(MPolygon polygon:badpolygons){
//      path=getBadGeometryPolygonPath(polygon);
//      if(path!=null)
//        graphics.draw(path);}
    
    //
    return image;}
  
  private Path2D getShapePath(MShape shape){
    Path2D path;
    if(shape instanceof MYard)
      path=getYardPath((MYard)shape);
    else
      path=getPolygonPath((MPolygon)shape);
    return path;}
  
  private Path2D getBadGeometryShapePath(MShape shape){
    Path2D path;
    if(shape instanceof MYard)
      path=getBadGeometryYardPath((MYard)shape);
    else
      path=getBadGeometryPolygonPath((MPolygon)shape);
    return path;}
  
  private Path2D getPolygonPath(MPolygon polygon){
    Path2D.Double path=new Path2D.Double();
    try{
      DPoint p=polygon.dpolygon.get(0);
      if(p!=null)path.moveTo(p.x,p.y);
      for(int i=1;i<polygon.dpolygon.size();i++){
        p=polygon.dpolygon.get(i);
        if(p!=null)path.lineTo(p.x,p.y);}
      path.closePath();
    }catch(Exception x){
      x.printStackTrace();
      return null;}
    return path;}
  
  private Path2D getBadGeometryPolygonPath(MPolygon polygon){
    List<DPoint> goodpoints=new ArrayList<DPoint>();
    System.out.println("-------------------------");
    System.out.println("POLYGON BAD GEOMETRY");
    for(DPoint p:polygon.dpolygon){
      System.out.println(p);
      if(p!=null)goodpoints.add(p);}
    System.out.println("-------------------------");
    if(goodpoints.isEmpty())return null;
    //
    Path2D.Double path=new Path2D.Double();
    DPoint p=goodpoints.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<goodpoints.size();i++){
      p=goodpoints.get(i);
      if(p!=null)path.lineTo(p.x,p.y);}
      path.closePath();
    return path;}
  
  private Path2D getYardPath(MYard yard){
    Path2D.Double path=new Path2D.Double(),polygonpath;
    for(MPolygon p:yard.mpolygons){
      path.append(getPolygonPath(p),false);}
    return path;}
  
  private Path2D getBadGeometryYardPath(MYard yard){
    Path2D path=new Path2D.Double(),polygonpath;
    for(MPolygon p:yard.mpolygons){
      polygonpath=getBadGeometryPolygonPath(p);
      if(polygonpath!=null)
        path.append(polygonpath,false);}
    return path;}
  
  private static final double MARGIN=30;
  
  private AffineTransform getTransform(int imagewidth,int imageheight,MPolygon rootpolygon){
    //get all the relevant metrics
    Rectangle2D.Double bounds=getPolygonBoundingRect(rootpolygon);
    double
      dmargin=MARGIN,
      cbwidth=bounds.getWidth(),
      cbheight=bounds.getHeight(),
      cbxmin=bounds.getMinX(),
      cbymin=bounds.getMinY();
    AffineTransform transform=new AffineTransform();
    //scale
    double 
      sw=(imagewidth-dmargin*2)/cbwidth,
      sh=(imageheight-dmargin*2)/cbheight;
    double scale=Math.min(sw,sh);
    transform.scale(scale,-scale);//flip y for proper cartesian orientation
    //offset
    double
      xoff=((imagewidth/scale-cbwidth)/2.0)-cbxmin,
      yoff=-(((imageheight/scale+cbheight)/2.0)+cbymin);
    transform.translate(xoff,yoff);
    //
    return transform;}
  
  //returns the bounding rectangle2d of the specified NPolygon
  protected Rectangle2D.Double getPolygonBoundingRect(MPolygon mpolygon){
    List<DPoint> points=mpolygon.getDPolygon();
    double maxx=Double.MIN_VALUE,maxy=maxx,minx=Double.MAX_VALUE,miny=minx;
    for(DPoint p:points){
      if(minx>p.x)minx=p.x;
      if(miny>p.y)miny=p.y;
      if(maxx<p.x)maxx=p.x;
      if(maxy<p.y)maxy=p.y;}
    return new Rectangle2D.Double(minx,miny,maxx-minx,maxy-miny);}
  
  

}
