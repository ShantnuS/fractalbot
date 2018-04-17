
public class Complex {

	protected double real;
	protected double imaginary;
	
	public Complex(double tempReal, double tempImaginary){
		setReal(tempReal);
		setImaginary(tempImaginary);
	}
	
	public Complex(){ //in case no parameters were passed, real and imaginary are 0
		this(0,0);
	}
	
	public Complex square(){
		double tempReal = real*real - imaginary*imaginary;
        double tempImaginary = real*imaginary + imaginary*real;
        return new Complex(tempReal, tempImaginary);
	}
	
	public Complex add(Complex d){
		double tempReal = d.getReal();
		double tempImaginary = d.getImaginary();
		
		real = real + tempReal;
		imaginary = imaginary + tempImaginary;
		
		return new Complex(real, imaginary);
	}
	
	public double modulusSquared(){
		double tempReal = real*real;
		double tempImaginary = imaginary*imaginary;
		
		
		return tempReal+tempImaginary;
	}
	
	public String asString(){
		String realPart = ("" + real);
		String imaginaryPart = ("" + imaginary);
		
		realPart = realPart.substring(0, Math.min(realPart.length(), 6));
		imaginaryPart = imaginaryPart.substring(0, Math.min(imaginaryPart.length(), 6));
		
		String correctFormat = ("(" + realPart + ")" + "+" + "(" + imaginaryPart  +")i");
		
		return correctFormat;
	}
	
	public String toString(){
		return asString();
	}
	
	public double getReal(){
		return real;
	}
	
	public double getImaginary(){
		return imaginary;
	}
	
	public void setReal(double tempDouble){
		real = tempDouble;
	}
	
	public void setImaginary(double tempDouble){
		imaginary = tempDouble;
	}
	
}
