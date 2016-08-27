package org.fleen.maximilian.app.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import org.fleen.geom_2D.DPoint;
import org.fleen.geom_Kisrhombille.KVertex;
import org.fleen.maximilian.MPolygon;
import org.fleen.maximilian.MShape;
import org.fleen.maximilian.MYard;
import org.fleen.maximilian.boundedDeformableKGrid.BoundedDeformableKGrid;
import org.fleen.maximilian.boundedDeformableKGrid.KVertexPointDefinition;

public class Renderer000 implements Renderer{
  
  Test test;
  
  Renderer000(Test test){
    this.test=test;
  }
  
  /*
   * ################################
   * STROKE
   * ################################
   */
  
  private float strokewidth;
  
  private Stroke createStroke(){
    Stroke stroke=new BasicStroke(strokewidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,0,null,0);
    return stroke;}
  
  /*
   * ################################
   * RENDER
   * ################################
   */
  
  Random rnd=new Random();
  
  private static final double DOT0_SIZE=5.0;
  
  public BufferedImage render(){
    int 
      w=test.ui.panimage.getWidth(),
      h=test.ui.panimage.getHeight();
    BufferedImage image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    AffineTransform t=getTransform(w,h,test.composition.getRoot());
    Graphics2D graphics=image.createGraphics();
    graphics.setPaint(Color.white);
    graphics.fillRect(0,0,w,h);
    graphics.setTransform(t);
    
    //
    Path2D path;
    graphics.setPaint(Color.black);
    graphics.setStroke(createStroke());
    for(MShape shape:test.composition.getShapes()){
      path=getShapePath(shape);
      graphics.draw(path);}
    
    //TEST render BoundedDeformableKGrid
    DPoint p;
    Ellipse2D e=new Ellipse2D.Double();
    double dot0size=DOT0_SIZE/t.getScaleX();
    for(MShape shape:test.composition.getShapes()){
      BoundedDeformableKGrid g=new BoundedDeformableKGrid(shape,3);
      KVertexPointDefinition def;
      for(KVertex v:g.edgevertices){
        if(g.isCorner(v))
          graphics.setPaint(Color.red);
        else
          graphics.setPaint(Color.green);
        def=g.edgevertexpointdefinitions.get(v);
        if(def==null)System.out.println("NULL DEF");
        p=def.getPoint();
        e.setFrameFromCenter(p.x,p.y,p.x-dot0size,p.y-dot0size);
        graphics.fill(e);}}
    
    return image;}
  
  private Path2D getShapePath(MShape shape){
    Path2D path;
    if(shape instanceof MYard)
      path=getYardPath((MYard)shape);
    else
      path=getPolygonPath((MPolygon)shape);
    return path;}
  
  private Path2D getPolygonPath(MPolygon polygon){
    Path2D.Double path=new Path2D.Double();
    DPoint p=polygon.dpolygon.get(0);
    path.moveTo(p.x,p.y);
    for(int i=1;i<polygon.dpolygon.size();i++){
      p=polygon.dpolygon.get(i);
      path.lineTo(p.x,p.y);}
    path.closePath();
    return path;}
  
  private Path2D getYardPath(MYard yard){
    Path2D.Double path=new Path2D.Double();
    for(MPolygon p:yard.mpolygons)
      path.append(getPolygonPath(p),false);
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
