import java.*;
import java.util.*;

class Point{
    public double x;
    public double y;
    public double z;
    public double r;
    public boolean isAnchor;
   
    Point(double x, double y,double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
}


public class TrilThreeDimensions {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc=new Scanner(System.in);
        Random r=new Random();
       
        System.out.println("Vnesete broj na jazli vo mrezata: ");
        int N=sc.nextInt();
       
        System.out.println();
       
        System.out.println("Vnesete dimenzija na oblasta: ");
        int L=sc.nextInt();
       
        System.out.println();
        
        System.out.println();
        
        System.out.println("Vnesete range na koj ke se slusaat jazlite: ");
        int R=sc.nextInt();
       
        System.out.println();
       
        System.out.println("Vnesete procent na anchor jazli: ");
        int AncPercent=sc.nextInt();
       
        System.out.println();
       
        int anchor=(N*AncPercent)/100;
       
        System.out.println("Vnesete procent na shum: ");
        int Noise=sc.nextInt();
       
        //Samo anchori
        ArrayList<Point> anchors=new ArrayList<>();
        //Site ke se tuka sose anchorite
        ArrayList<Point> nodes=new ArrayList<>();
        //Tie so gi barame
        ArrayList<Point> nodesTemp=new ArrayList<>();
        //Tuka samo najdenite
        ArrayList<Point> found=new ArrayList<>();
        ArrayList<Integer> distances=new ArrayList<>();
        ArrayList<Integer> neighbors=new ArrayList<>();
        
        int num=N-anchor;
        
        int neigborsSum=0;
        int neighborsCount=0;
        
        for(int i=0;i<anchor;i++){
        	
            int x=r.nextInt(L);
            int y=r.nextInt(L);
            int z=r.nextInt(L);
            Point p=new Point(x,y,z);
            p.isAnchor=true;
            anchors.add(p);
            nodes.add(p);
        }
        
        for(int i=0;i<num;i++){
        	
            double x=r.nextInt(L);
            double y=r.nextInt(L);
            double z=r.nextInt(L);
            Point p=new Point(x,y,z);
            p.isAnchor=false;
            nodes.add(p);
            nodesTemp.add(p);
        }
        
        double d1;
        double d2;
        double d3;
        
        double []err=new double[nodes.size()];
        boolean f=false;
        
        while (nodes.size()!=anchors.size()){
            
            for(int i=0;i<nodes.size();i++){
                distances.clear();
                neighborsCount=0;
               
                if(nodes.get(i).isAnchor==false){
                   
                    for(int j=0;j<anchors.size();j++){
                        double d=Distance(anchors.get(j).x,anchors.get(j).y,anchors.get(j).z,nodes.get(i).x,nodes.get(i).y,nodes.get(i).z);
                        if(d<R) neighborsCount++;
                        double shum=d*Noise/100;
                        d+=shum;
                        distances.add((int)d); 
                        //anchors.get(j).r=d;
                    }
                   
                    int []niza=new int[6];
                    niza=Min(distances);
                    d1=niza[0];
                    int poz1=niza[1];
                   
                    double x1=anchors.get(poz1).x;
                    double y1=anchors.get(poz1).y;
                    double z1=anchors.get(poz1).z;
                   
                    d2=niza[2];
                    int poz2=niza[3];
                   
                    double x2=anchors.get(poz2).x;
                    double y2=anchors.get(poz2).y;
                    double z2=anchors.get(poz2).z;
                   
                    d3=niza[4];
                    int poz3=niza[5];
                   
                    double x3=anchors.get(poz3).x;
                    double y3=anchors.get(poz3).y;
                    double z3=anchors.get(poz3).z;
                   
                    
                    if(d1<=R && d2<=R && d3<=R){
                    	
                        //double x=GetX(x1,y1,x2,y2,d1,d2,d);
                        //double y=GetY(x1,y1,x2,y2,d1,d2,d);
                    	Point p1=new Point(x1,y1,z1);
                        p1.r=d1;
                        Point p2=new Point(x2,y2,z2);
                        p2.r=d2;
                        Point p3=new Point(x3,y3,z3);
                        p3.r=d3;        
                        
                        Point p=Trilaterate(p1,p2,p3);
                        
                        err[i]=Distance(p.x,p.y,p.z,nodes.get(i).x,nodes.get(i).y,nodes.get(i).z);
                        anchors.add(p);
                        found.add(p);
                        nodes.get(i).isAnchor=true;
                        neighborsCount++;
                    }                   
                    
                }
               
                neighbors.add(neighborsCount);
            }
           
            if(AllNodesFound(nodes)==true){
                break;
            }
        }
        
        System.out.println("Original: "+ "\t"+"Found: ");
        
        for(int i=0;i<nodesTemp.size();i++){
        	
            System.out.println("X: "+String.format("%.1f", nodesTemp.get(i).x)+"\t"+"Y: "+String.format("%.1f", nodesTemp.get(i).y)+"\t"+"Z: "+String.format("%.1f", nodesTemp.get(i).z)+"\t"+"X: "+String.format("%.1f", found.get(i).x)+"\t"+"Y: "+String.format("%.1f", found.get(i).y)+"\t"+"Z: "+String.format("%.1f", found.get(i).z));
        
        }
        
System.out.println();
        
        System.out.println("Size na site originalni nodes: "+nodes.size());
        System.out.println("Size na site najdeni jazli:" + anchors.size());
       
        double sum=0;
        double avg=0;
        for(int i=0;i<err.length;i++){
            sum+=err[i];
        }
        
        avg=sum/(err.length);
        System.out.println("Prosechna greska sose anchor jazlite: " + String.format("%.1f", avg));
        double percentage=avg*100/R;
        System.out.println("Greskata iznesuva "+ String.format("%.1f",percentage)+ " od range-ot");
       
        double []error=new double[nodesTemp.size()];
        sum=0;
        
        for(int i=0;i<nodesTemp.size();i++){
           double d=Distance(nodesTemp.get(i).x,nodesTemp.get(i).y,nodesTemp.get(i).z,found.get(i).x,found.get(i).y,found.get(i).z);
           error[i]=d;
           sum+=error[i];
        }
        
        avg=sum/(error.length);
        System.out.println("Prosechna greska bez anchor jazlite: "+String.format("%.1f", avg));
        percentage=avg*100/R;
        System.out.println("Greskata iznesuva "+ String.format("%.1f",percentage)+ " od range-ot");
        
        int suma=0;
        for(int i=0;i<neighbors.size();i++){
        	suma+=neighbors.get(i);
        }
        
        double averageN=suma/neighbors.size();
        System.out.println("Prosecen broj sosedi: "+averageN);
   

	}
	
	 public static double Distance(double x1, double y1, double z1, double x2, double y2, double z2){
	        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)+(z1-z2)*(z1-z2));
	    }
	 
	  public static boolean AllNodesFound(ArrayList<Point> list){
	        boolean flag=true;
	        
	        for(int i=0;i<list.size();i++){
	            if(list.get(i).isAnchor==false){
	                flag=false;
	                break;
	            }
	        }
	        return flag;
	    }
	  
	  public static int[] Min(ArrayList<Integer> list){
	        int current=Integer.MAX_VALUE;
	       
	        int poz=0;
	        int []array=new int[6];
	       
	        for(int i=0;i<list.size();i++){
	            if(list.get(i)<current){
	                current=list.get(i);
	                poz=i;
	            }
	        }
	        array[0]=current;
	        array[1]=poz;
	        list.set(poz, 1000000);
	        current=Integer.MAX_VALUE;
	       
	        for(int i=0;i<list.size();i++){
	            if(list.get(i)<current){
	                current=list.get(i);
	                poz=i;
	            }
	        }
	       
	        array[2]=current;
	        array[3]=poz;
	        list.set(poz, 100000);
	        current=Integer.MAX_VALUE;
	       
	        for(int i=0;i<list.size();i++){
	            if(list.get(i)<current){
	                current=list.get(i);
	                poz=i;
	            }
	        }
	       
	        array[4]=current;
	        array[5]=poz;
	       
	        return array;      
	       
	    }
	  
	  public static double sqr(double a) {
	        return a * a;
	    }
	  
	  public static double norm(Point a) {
	        return Math.sqrt(sqr(a.x) + sqr(a.y) + sqr(a.z));
	    }
	  
	  public static double dot(Point a, Point b) {
	        return a.x * b.x + a.y * b.y + a.z * b.z;
	    }
	  
	  public static Point vector_subtract(Point a, Point b) {
          
          double x=a.x - b.x;
          double y=a.y - b.y;
          double z=a.z - b.z;
          Point p=new Point(x,y,z);
          return p;
     
	  	}
	  
	  public static Point vector_add(Point a,Point b) {
          
          double x= a.x + b.x;
          double y= a.y + b.y;
          double z= a.z + b.z;
          Point p=new Point(x,y,z);
          return p;
   
	  	}
	  
	  public static Point vector_cross(Point a, Point b) {
	         
          double x=a.y * b.z - a.z * b.y;
          double y=a.z * b.x - a.x * b.z;
          double z=a.x * b.y - a.y * b.x;
          Point p=new Point(x,y,z);
          return p;
      
	  	}
	  
	  public static Point vector_divide(Point a, double b) {
          
          double x= a.x / b;
          double y= a.y / b;
          double z= a.z / b;
          Point p=new Point (x,y,z);
          return p;
      
	  	}
	  
	  public static Point vector_multiply(Point a, double b) {
          
          double x= a.x * b;
          double y= a.y * b;
          double z= a.z * b;
          Point p=new Point(x,y,z);
          return p;
     
	  	}
	  
	  public static Point Trilaterate(Point p1, Point p2, Point p3){
	       
	        //var ex, ey, ez, i, j, d, a, x, y, z, p4;
	        Point ex = vector_divide(vector_subtract(p2, p1), norm(vector_subtract(p2, p1)));
	        double i = dot(ex, vector_subtract(p3, p1));
	        Point a = vector_subtract(vector_subtract(p3, p1), vector_multiply(ex, i));
	        Point ey = vector_divide(a, norm(a));
	        Point ez = vector_cross(ex, ey);
	        double d = norm(vector_subtract(p2, p1));
	        double j = dot(ey, vector_subtract(p3, p1));
	       
	        double x = (sqr(p1.r) - sqr(p2.r) + sqr(d)) / (2 * d);
	        double y = (sqr(p1.r) - sqr(p3.r) + sqr(i) + sqr(j)) / (2 * j) - (i / j) * x;
	        double z = Math.sqrt(sqr(p1.r) - sqr(x) - sqr(y));
	       
	        Point aa = vector_add(p1, vector_add(vector_multiply(ex, x), vector_multiply(ey, y)));
	        Point p4a = vector_add(a, vector_multiply(ez, z));
	        Point p4b = vector_subtract(a, vector_multiply(ez, z));
	       
	        //double xx=(p4a.x+p4b.x)/2;
	        //double yy=(p4a.y+p4b.y)/2;
	        //double zz=(p4a.z+p4b.z)/2;
	       
	        return aa;
	       
	    }
	  
}
