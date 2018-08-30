package games;

public class MyNode {
private boolean a ;
public boolean isA() {
	return a;
}
public  synchronized  void setA(boolean a) {
	this.a = a;
}
//private long x;
private String x;
@Override
	public String toString() {
		// TODO Auto-generated method stub
		return x.toString();
	}
@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return x.equals(((MyNode)obj).x);//&&(a==((MyNode)obj).a);
	}
@Override
	public int hashCode() {
		return x.hashCode();//(int) (x ^ (x >>> 32));
	//Longs.hashCode(x);;
	}
public MyNode(String strMass) {
	//if(Math.random()>0.5)a=true;
	x=strMass;//Long.valueOf(strMass);
}
}
