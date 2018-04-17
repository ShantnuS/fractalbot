import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


/*
 * Made by: Shantnu Singh
 * Student ID = 2776 3153
 * Made For: Programming 2 - Assignment 1
 */

//MAIN CLASS FOR GUI!
public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	//COMPONENTS ON THE MAIN GUI
	protected MandelPanel imagePanel = new MandelPanel(); //this is the main panel on the left side where the fractal is drawn
	protected MandelPanel juliaPanel = new MandelPanel(); //this is the panel on the julia set tab where the julia fractal is drawn
	protected JSplitPane splitPane = new JSplitPane(); //Adds a split pane to rezie left and right side...
	protected JPanel juliaSettingsPanel = new JPanel(); //This is the panel where the julia image is shows
	protected JPanel juliaOptionsPanel = new JPanel(); //This is where setting for julia image are...
	protected JPanel settingsPanel = new JPanel(); //this represents the entire right side of the screen
	protected JPanel optionsPanel = new JPanel(); //this is the panel on the options tab where all the buttons are
	protected JTabbedPane tabs = new JTabbedPane(); //this allows me to create tabs
	protected JLabel userSelectedPointLabel = new JLabel("Please Select a Point"); 
	protected JLabel userSelectedPointLabel2 = new JLabel("Please Select a Point");
	protected JLabel timeTakenLabel = new JLabel("0.00s");
	protected JTextField iterationsField = new JTextField(10);
	protected DefaultListModel<Complex> list = new DefaultListModel<Complex>();
	protected JList<Complex> favouritesList = new JList<Complex>(list);
	protected JScrollPane scroller = new JScrollPane(favouritesList);
	protected JTextField xOffsetMin = new JTextField(10);
	protected JTextField xOffsetMax = new JTextField(10);
	protected JTextField yOffsetMin = new JTextField(10);
	protected JTextField yOffsetMax = new JTextField(10);
	protected JCheckBox zoomCheck = new JCheckBox("Zoom Julia?");
	protected JComboBox<String> colourBox; //Combobox where you can choose colours
	protected JComboBox<String> fractalTypeBox; //Combobox where you can choose the fractal type
	
	//Other useful variables!
	double xMin = -2.0;
	double xMax = 2.0;
	double yMin = -1.6;
	double yMax = 1.6;
	boolean isIterationsRunning;
	
	//These variables are needed if you don't want to zoom in on the julia panel
	double xMinInitial = -2.0;
	double xMaxInitial = 2.0;
	double yMinInitial = -1.6;
	double yMaxInitial = 1.6;
	
	public GUI(){
		isIterationsRunning = false;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200,600);
		setTitle("MandelBrot Fractal!");
		setVisible(true);
		setLayout(new BorderLayout());
	}
		 
	public void initialise(){
		
		//Split pane
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT );
		add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.9);
		
		
		//Image Panel
		splitPane.setLeftComponent(imagePanel);
		
		//Settings Panel
		settingsPanel.setBorder(BorderFactory.createEtchedBorder());
		settingsPanel.setLayout(new BorderLayout());
		settingsPanel.add(tabs, BorderLayout.CENTER);
		splitPane.setRightComponent(settingsPanel);
		
		//The combo box filled with colour options
		String[] colours = {"Default", "Crazy", "Crazy 2", "Candy", "Purple", "Cool Edges", "TwoColour", "TriColour", "Pink Blue"};
		colourBox = new JComboBox<String>(colours);
		
		//The combo box filled with fractal types
		String[] fractalTypes = {"MandelBrot", "Burning Ship", "MandelBrot Squared"};
		fractalTypeBox = new JComboBox<String>(fractalTypes);
		fractalTypeBox.setBackground(Color.white);
		
		//Other panels for each group
		JPanel iterationsPanel = new JPanel();
		iterationsPanel.setLayout(new FlowLayout());
		iterationsPanel.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel xOffsetPanel = new JPanel();
		xOffsetPanel.setLayout(new FlowLayout());
		xOffsetPanel.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel yOffsetPanel = new JPanel();
		yOffsetPanel.setLayout(new FlowLayout());
		yOffsetPanel.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel selectedPointPanel = new JPanel();
		selectedPointPanel.setLayout(new GridLayout(3,2));
		selectedPointPanel.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout());
		buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
		
		//Adding things to the options tab
		optionsPanel.setLayout(new GridLayout(5,1));
		JButton startButton = new JButton("Start");
		JButton iterationsButton = new JButton("View Each Iteration");
		JButton saveButton = new JButton("Save Image");
		JButton saveJuliaButton = new JButton("Save Image");
		JButton bookMarkButton = new JButton("Bookmark");
		JButton removeBookMarkButton = new JButton("Remove From Bookmark");
		iterationsField.setText("100");
		
		selectedPointPanel.add(new JLabel("	   Fractal Type:"));
		selectedPointPanel.add(fractalTypeBox);
		selectedPointPanel.add(new JLabel("	   User Selected Point:"));
		selectedPointPanel.add(userSelectedPointLabel);
		selectedPointPanel.add(new JLabel("	   Time Taken:"));
		selectedPointPanel.add(timeTakenLabel);
		optionsPanel.add(selectedPointPanel);
		
		xOffsetMin.setText(String.valueOf(xMin));
		xOffsetMax.setText(String.valueOf(xMax));
		xOffsetPanel.add(new JLabel("X-Offset: "));
		xOffsetPanel.add(xOffsetMin);
		xOffsetPanel.add(new JLabel(" to "));
		xOffsetPanel.add(xOffsetMax);
		optionsPanel.add(xOffsetPanel);
		
		yOffsetMin.setText(String.valueOf(yMin));
		yOffsetMax.setText(String.valueOf(yMax));
		yOffsetPanel.add(new JLabel("Y-Offset: "));
		yOffsetPanel.add(yOffsetMin);
		yOffsetPanel.add(new JLabel(" to "));
		yOffsetPanel.add(yOffsetMax);
		optionsPanel.add(yOffsetPanel);
		
		iterationsPanel.add(new JLabel("Iterations:"));
		iterationsPanel.add(iterationsField);
		iterationsPanel.add(new JLabel("     Colour:"));
		iterationsPanel.add(colourBox);
		optionsPanel.add(iterationsPanel);
		
		buttonsPanel.add(startButton);
		buttonsPanel.add(saveButton);
		buttonsPanel.add(iterationsButton);
		optionsPanel.add(buttonsPanel);
		
		//START BUTTON!
		startButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				xMin = Double.parseDouble(xOffsetMin.getText());
				xMax = Double.parseDouble(xOffsetMax.getText());
				yMin = Double.parseDouble(yOffsetMin.getText());
				yMax = Double.parseDouble(yOffsetMax.getText());
				
				xMinInitial = xMin;
				xMaxInitial = xMax;
				yMinInitial = yMin;
				yMaxInitial = yMax;
				
				imagePanel.setColourOption((String) colourBox.getSelectedItem());
				juliaPanel.setColourOption((String) colourBox.getSelectedItem());
				
				imagePanel.setFractalType((String) fractalTypeBox.getSelectedItem());
				juliaPanel.setFractalType((String) fractalTypeBox.getSelectedItem());
				
				try{
					imagePanel.setIterations(Integer.parseInt(iterationsField.getText()));
				}
				catch(Exception myException){
					//If the text is not a number then the iterations is set to 100 and error is printed
					System.err.println("Not a number!");
					imagePanel.setIterations(100);
				}
				
				if (isIterationsRunning == false){
				//Stops the entire program from freezing when the button is pressed
				Thread myThread = new Thread() {
				      public void run() {
				    	  long timeBefore = System.nanoTime();
				    	  timeTakenLabel.setText("Loading...");
				    	  imagePanel.calculateSet();
				    	  imagePanel.repaint();
				    	  double timeTaken = (System.nanoTime()-timeBefore)/1000000000.0;
				    	  String timeString = (Double.toString(timeTaken));
				    	  timeTakenLabel.setText(timeString.substring(0, Math.min(timeString.length(), 6)) + "s");
				      }
				    };
				    myThread.start();
				}
			}
		});
		
		//Save the image on the main image panel
		saveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Stops the entire program from freezing when the button is pressed
				Thread myThread = new Thread() {
				      public void run() {
				    	  if(imagePanel.isStarted()){
					    	  try {
								imagePanel.saveAsImage();
							} catch (IOException e) {
								System.err.println(e.getMessage());
							}
				    	  }
				      }
				    };
			myThread.start();
			}
		});
		
		//Save the julia fractal
		saveJuliaButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Stops the entire program from freezing when the button is pressed
				Thread myThread = new Thread() {
				      public void run() {
				    	  if(imagePanel.isStarted()){
					    	  try {
								juliaPanel.saveAsImage();
							} catch (IOException e) {
								System.err.println(e.getMessage());
							}
				    	  }	
				      }
				    };
			myThread.start();
			}
		});
		
		//This button shows the fractal for each number of iterations until the set amount (or 100 whichever is closer)
		iterationsButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				imagePanel.setColourOption((String) colourBox.getSelectedItem());
				juliaPanel.setColourOption((String) colourBox.getSelectedItem());
				
				imagePanel.setFractalType((String) fractalTypeBox.getSelectedItem());
				juliaPanel.setFractalType((String) fractalTypeBox.getSelectedItem());
				
				if (isIterationsRunning == false){
					//Stops the entire program from freezing when the button is pressed
					Thread myThread = new Thread() {
					public void run() {
						isIterationsRunning = true;
						long timeBefore = System.nanoTime();
						timeTakenLabel.setText("Loading...");
						int iterationsToDo = 1;		
						
						try{
							iterationsToDo = Integer.parseInt(iterationsField.getText());
						}
						catch(Exception myException){
							System.err.println("Not a number!");
						}
						
						for (int i = 1; i <= iterationsToDo; i++){
							imagePanel.setIterations(i);
							imagePanel.calculateSet();
							imagePanel.repaint();
							
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									System.err.println(e.getMessage());
								}
						} 
						isIterationsRunning = false;
						double timeTaken = (System.nanoTime()-timeBefore)/1000000000.0;
						String timeString = (Double.toString(timeTaken));
				    	timeTakenLabel.setText(timeString.substring(0, Math.min(timeString.length(), 6)) + "s");
					}
					};
					myThread.start();
			}	
			}
		});
		
		//Add item to bookmark
		bookMarkButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Complex tempComplex = imagePanel.getTmpComplex();
				//addToFavourites(imagePanel.getTmpComplex());
				//Stops the entire program from freezing when the button is pressed
				Thread myThread = new Thread() {
				      public void run() {	
				    	  list.addElement(tempComplex);
				      }
				    };
			myThread.start();
			}
		});
		
		//Remove from bookmark
		removeBookMarkButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Stops the entire program from freezing when the button is pressed
				Thread myThread = new Thread() {
				      public void run() {
				    	  //If there is an item selected and the number of items is more than 0 then remove selected item
				    	  try{
				    		  if(list.size()>0){
				    			  list.remove(favouritesList.getSelectedIndex());
				    		  }  
				    	  }
				    	  catch(Exception myException){
				    		  //if there was no item selected than the bottom item can be removed...
				    		  list.remove(list.getSize()-1);
				    	  }
				    	  
				      }
				    };
			myThread.start();
			}
		});
		
		//Calculate from favourites
		favouritesList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
				if(favouritesList.getValueIsAdjusting() == true){
					Complex myComplex = favouritesList.getSelectedValue();
					updateJulia(myComplex);
				}
			}
	
		});
		
		//Julia fractal tab settings
		juliaOptionsPanel.setBorder(BorderFactory.createEtchedBorder());
		juliaSettingsPanel.setLayout(new GridLayout(2,1));
		juliaSettingsPanel.add(juliaPanel);
		juliaSettingsPanel.add(juliaOptionsPanel);
		
		JPanel juliaLabelPanel = new JPanel();
		juliaLabelPanel.setLayout(new GridLayout(2,1));
		
		JPanel juliaButtonPanel = new JPanel();
		juliaButtonPanel.setLayout(new FlowLayout());
		
		JPanel juliaLabelPanel2 = new JPanel();
		juliaLabelPanel2.setLayout(new FlowLayout());
		
		JPanel juliaLabelPanel3 = new JPanel();
		juliaLabelPanel3.setLayout(new FlowLayout());
		
		//Favourites List
		scroller.setBorder(BorderFactory.createEtchedBorder());
		
		//Adding stuff to julia's options panel
		juliaOptionsPanel.setLayout(new BorderLayout());
		juliaLabelPanel3.add(zoomCheck);
		juliaLabelPanel2.add(new JLabel("User Selected Point:"));
		juliaLabelPanel2.add(userSelectedPointLabel2);
		juliaLabelPanel.add(juliaLabelPanel3);
		juliaLabelPanel.add(juliaLabelPanel2);
		juliaButtonPanel.add(bookMarkButton);
		juliaButtonPanel.add(removeBookMarkButton);
		juliaButtonPanel.add(saveJuliaButton);
		juliaOptionsPanel.add(scroller, BorderLayout.CENTER);
		juliaOptionsPanel.add(juliaButtonPanel, BorderLayout.SOUTH);
		juliaOptionsPanel.add(juliaLabelPanel, BorderLayout.NORTH);
		
		//Tabs for Julia set and options
		tabs.add("Options",optionsPanel);
		tabs.add("Julia Set",juliaSettingsPanel);
		
	}
	
	public void updateJulia(Complex myComplex){
		juliaPanel.calculateJulia(myComplex.getReal(), myComplex.getImaginary(), imagePanel.getIterations());
		juliaPanel.repaint();
	}
	
	//MAIN METHOD
	public static void main(String[] args){
	SwingUtilities.invokeLater ( 
		new Runnable () {
			public void run() {  
			 	GUI myGUI = new GUI();
			    myGUI.initialise();   }
		});
	}
	
//MANDELBROT PANEL CLASS
class MandelPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
    protected BufferedImage mandelBrot;
    protected Complex complexOne;
    protected Complex complexTwo;
    protected Complex tmpComplex = new Complex();
	protected int iterations = 1000;
	protected boolean started = false;
	protected double xAxis;
	protected double yAxis;
	protected boolean mousePressed =false;
	protected String colourOption;
	protected String fractalType = "MandelBrot";
	
	//variables for location of drag box
	int dragBoxX = 0;
	int dragBoxY = 0;
	int dragBoxWidth = 0;
	int dragBoxHeight = 0;


	//CLICKING ON ANYPOINT CALCULATES JULIA SET
	public MandelPanel(){
		
		addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	//You can do other things while julia set is being calculated...
		    	Thread myThread = new Thread() {
		    	      public void run() {
		    	    	  if( e.getSource() == imagePanel && started == true){
		    	    		  
		  			    	double dReal = (e.getX()/(getWidth()/(xMax - xMin))) + xMin;
		  			        double dImaginary = (e.getY()/(getHeight()/(yMax - yMin))) + yMin;
		  			        
		  			    	juliaPanel.calculateJulia(dReal, dImaginary, iterations);
		  			    	tmpComplex = new Complex (dReal, dImaginary);
		  			    	getTmpComplex();
		  			    	
		  			       	juliaPanel.repaint();
		  		    	}
		    	      }
		    	    };
		    myThread.start();
		    }
		});
		
		MouseAdapter myAdapter = new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				if( e.getSource() == imagePanel && started == true){
					mousePressed = true;
		    		dragBoxX = e.getX();
		    		dragBoxY = e.getY();
		    		repaint();
				}
		    }

			public void mouseReleased(MouseEvent e) {
			mousePressed = false;
				if( e.getSource() == imagePanel && started == true){
					
					if(dragBoxWidth > 5 && dragBoxHeight > 5){ //prevent accidental dragging by adding minimum sizes to the drag box
						zoom(dragBoxX, dragBoxY, dragBoxWidth, dragBoxHeight);
						calculateSet();
					}
					
					dragBoxX = 0;
					dragBoxY = 0;
		    		dragBoxWidth = 0;
		    		dragBoxHeight = 0;
		    		
					repaint();
				}
		    }
			
			public void mouseDragged(MouseEvent e) {
				if( e.getSource() == imagePanel && started == true){
					dragBoxWidth = e.getX()-dragBoxX;
		    		dragBoxHeight = e.getY()-dragBoxY;
		    		repaint();
				}
		    }
		};
		
		addMouseListener(myAdapter);
		addMouseMotionListener(myAdapter);
		
	}
	
    public void calculateSet(){
      mandelBrot = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
      complexTwo = new Complex();
      
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
            	
                complexOne = new Complex(0,0);
                
                xAxis = (x/(getWidth()/(xMax - xMin))) + xMin;
                yAxis = (y/(getHeight()/(yMax - yMin))) + yMin;
                
                complexTwo.setReal(xAxis);complexTwo.setImaginary(yAxis);
                
                int iter = iterations;
                
                while (complexOne.modulusSquared() < 4 && iter > 0) {
                	
                	switch(fractalType){
        
                	case "MandelBrot":		complexOne = calculationMandelBrot(complexOne, complexTwo);
                							break;
                		
                	case "Burning Ship":	complexOne = calculationBurningShip(complexOne, complexTwo);
                							break;
                							
                	case "MandelBrot Squared":	complexOne = calculationMandelBrot(complexOne, complexTwo).square();
                							break;
                	}
                	
                    iter = iter -1;
                }
                
                //Choose location and colour of pixel depending on the colour selected :)
                switch(colourOption){
                
                case "Default":		mandelBrot.setRGB(x, y, iter*10000); 
                					break;
                case "Crazy":		mandelBrot.setRGB(x, y, iter*x*y*100); 
                					break;
                case "Candy":		mandelBrot.setRGB(x, y, 16777215-(iter*iter*iter + 4*iter -6));		
                					break;
                case "Purple": 		mandelBrot.setRGB(x, y, iter | (iter << 2000)); 
                					break;
                case "Cool Edges":	mandelBrot.setRGB(x, y, iter | (iter << 10)); 
                					break;
                case "TwoColour":	mandelBrot.setRGB(x, y, iter | (iter << 15)); 
                					break;
                case "Pink Blue":	mandelBrot.setRGB(x, y, iter | (iter << 343)); 
                					break;
                case "TriColour":	mandelBrot.setRGB(x, y, iter | (iter << iter*10)); 
                					break;
                case "Crazy 2": 	mandelBrot.setRGB(x, y, iter*x*y);
                					break;
                }
                
                
            }
        }
        started = true;
    }
    
    //CALCULATE THE JULIA SET FOR GIVEN COORDINATE
    public void calculateJulia(double dReal, double dImaginary, int iterations){
    	mandelBrot = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    	
    	
    	for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
        
            	if(zoomCheck.isSelected()){
            		xAxis = (x/(juliaPanel.getWidth()/(xMax - xMin))) + xMin;
            		yAxis = (y/(juliaPanel.getHeight()/(yMax - yMin))) + yMin;
            	}
            	else{
            		xAxis = (x/(juliaPanel.getWidth()/(xMaxInitial - xMinInitial))) + xMinInitial;
            		yAxis = (y/(juliaPanel.getHeight()/(yMaxInitial - yMinInitial))) + yMinInitial;
            	}
            	
                complexOne = new Complex(xAxis,yAxis);

                int iter = iterations;
                
                while (complexOne.modulusSquared() < 4 && iter > 0) {
                	Complex myComplex = new Complex(dReal, dImaginary);
                	
                	switch(fractalType){
                	
                	case "MandelBrot":		complexOne = calculationMandelBrot(complexOne, myComplex);
                							break;
                							
                	case "Burning Ship":	complexOne = calculationBurningShip(complexOne, myComplex);
											break;
											
                	case "MandelBrot Squared":	complexOne = calculationMandelBrot(complexOne, myComplex).square();
											break;
                							
                	}
                	
                    iter = iter -1;
                }
              
                switch(colourOption){
                
                case "Default":		mandelBrot.setRGB(x, y, iter*10000); 
                					break;
                case "Crazy":		mandelBrot.setRGB(x, y, iter*x*y*100); 
                					break;
                case "Candy":		mandelBrot.setRGB(x, y, 16777215-(iter*iter*iter + 4*iter -6));		
                					break;
                case "Purple": 		mandelBrot.setRGB(x, y, iter | (iter << 2000)); 
                					break;
                case "Cool Edges":	mandelBrot.setRGB(x, y, iter | (iter << 10)); 
                					break;
                case "TwoColour":	mandelBrot.setRGB(x, y, iter | (iter << 15)); 
                					break;
                case "Pink Blue":	mandelBrot.setRGB(x, y, iter | (iter << 343)); 
                					break;
                case "TriColour":	mandelBrot.setRGB(x, y, iter | (iter << iter*10)); 
                					break;
                case "Crazy 2": 	mandelBrot.setRGB(x, y, iter*x*y);
                					break;
                }
                
               // 16777215-(iter*iter*iter + 4*iter -6)
            }
        }
    	started = true;
    }
    
    //FRACTAL CALCULATIONS
    //Calculation for the mandelbrot set fractal
    public Complex calculationMandelBrot(Complex firstComplex, Complex secondComplex){
    	firstComplex = firstComplex.square().add(secondComplex);
    	return firstComplex;
    }
    
    //Calculation for the burning ship fractal
    public Complex calculationBurningShip(Complex firstComplex, Complex secondComplex){
    	firstComplex.setReal(Math.abs(firstComplex.getReal()));
    	firstComplex.setImaginary(Math.abs(firstComplex.getImaginary()));
    	firstComplex = firstComplex.square().add(secondComplex);
    	return firstComplex;
    }
    
    public void setFractalType(String tempString){
    	fractalType = tempString;
    }
    
    public void setIterations(int tempInt){
    	iterations = tempInt;
    }
    
    public int getIterations(){
    	return iterations;
    }
    
    public void saveAsImage() throws IOException{
    	/*Allows the user to save the fractal as a png
    	 * The default save location is the desktop, but this can be changed...
    	 * The png extension is added automatically
    	 */
    	
    	String directory = System.getProperty("user.home");
    	JFileChooser myFileChooser = new JFileChooser(directory + "\\Desktop");
    	
    	int retVal = myFileChooser.showSaveDialog(null);
    	if(retVal==JFileChooser.APPROVE_OPTION){
    		File myImage = new File(myFileChooser.getSelectedFile()+".png");
    	    ImageIO.write(mandelBrot, "png", myImage);
    	 }
    }
    
    //Paint the Mandelbrot panel. Update this so button is used to update instead!
    public void paint(Graphics g) {
    	
    	//Draw the background
    	g.setColor(Color.darkGray);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//Draw the fractal
    	g.drawImage(mandelBrot, 0, 0, this);
    		
    	//Draw the drag box
    	if(mousePressed = true){
    		if(dragBoxWidth > 0 && dragBoxHeight > 0){ //Prevent negative drag boxes being drawn
    			g.setColor(getRandomColour());
    			g.drawRect(dragBoxX, dragBoxY, dragBoxWidth, dragBoxHeight);
    		}
    	}
    }
    
    public void zoom(double xValue, double yValue, double horizontal, double vertical){
    	
    	double horizontalZoom= horizontal/getWidth() * (xMax-xMin);
	    double verticalZoom = vertical/getHeight() * (yMax-yMin);
	    double horizontalX=  xValue/getWidth() * (xMax-xMin)+xMin;
	    double verticalY = yValue/getHeight() * (yMax-yMin)+yMin;
	    
    	xMin = horizontalX;
    	xMax = xMin + horizontalZoom;
    	yMin = verticalY;
    	yMax = yMin + verticalZoom;
    }
    
    public Color getRandomColour(){
    	//Create a random colour
    	Random random = new Random();
    	float red = random.nextFloat();
    	float green = random.nextFloat();
    	float blue = random.nextFloat();
    	Color randomColour = new Color(red, green, blue);
    					
    	return randomColour;
    }
    
    public Complex getComplexTwo(){
    	return complexTwo;
    }
    
    public Complex getComplexOne(){
    	return complexOne;
    }
    
    public boolean isStarted(){
    	return started;
    }
    
    public Complex getTmpComplex(){
    	userSelectedPointLabel.setText(tmpComplex.asString()); 
    	userSelectedPointLabel2.setText(tmpComplex.asString()); 
    	return tmpComplex;
    }
    
    public void setColourOption(String tempColour){
    	colourOption = tempColour;
    }

}

}
