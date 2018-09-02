import java.io.*;
import java.util.Scanner;
/*Daniel Cosgrove
 * R00153272
 * 
 *The purpose of this program is to allow the user to display and add class sessions to a gym, as well as print timetables 
 *for instructors and display the payments due for each instructor.
*/

public class ProjectGoToGym {

	static Scanner kb = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {


		final int SIZE = 12; //The size of all parallel arrays, class types and class codes.
		String [] classTypes = new String[SIZE];
		String []  classCodes = new String[SIZE];
		final int []  times = {7, 8, 9}; //The 3 possible times.
		final String []  days = {"Mon", "Tues", "Wed"}; //The 3 possible days.
		String [] instructors = new String[4]; //4 instructors.
		
		//Methods to load arrays.
		int numInstructors = loadInstructorsNames(instructors);//Loads the instructors names into an array.
		int numClassTypes = loadClassTypes(classTypes);//Loads the class types into an array.
		loadClassCodes(classTypes ,classCodes, numClassTypes); //Creates all the class codes for the class types.
		 
		//Parallel arrays for class sessions.
		String [] sessionClasses = new String[SIZE]; //Session class types.
		String [] sessionDays = new String[SIZE]; // Session days.
		int [] sessionTimes = new int[SIZE]; //Session times.
		String [] sessionInstructors = new String[SIZE]; //Session instructors.
		
		int numberOfClasses = loadSavedSessions(sessionClasses, sessionDays, sessionTimes, sessionInstructors );//Loads previous saved sessions from text file into parallel arrays.
		
		printTitle("Classes");//Print title banner.
		
		boolean exit = false;

		do //User is continuously prompted to choose a option until they choose exit.
		{
			System.out.println("1. Add a New Class Session"); 
			System.out.println("2. Show Times of All Current Classes"); 
			System.out.println("3. Show Instructor Payments Due"); 
			System.out.println("4. Print TimeTable for Instructor"); 	
			System.out.println("5. Show Ordered TimeTable with Codes"); 
			System.out.println("6. Exit"); 
			
			
			
			int choice = readIntInRange(">> ", 1, 6);//Reads in a integer between 1 and 6.
			
			switch (choice)//One method is carried out based on what integer the user chose. 
			{
				case 1:
					if(numberOfClasses < 12){
					numberOfClasses = addNewClassSession(classTypes, numClassTypes, days, times , classCodes , instructors, sessionClasses, sessionDays, sessionTimes, sessionInstructors, numberOfClasses);
					}
					else if(numberOfClasses >= 12){
						System.out.println("\nNo new classes can be added - max limit of sessions met. ");
					}
					break;
				case 2:
					showClassTimes(classTypes, classCodes, numClassTypes, sessionClasses, sessionDays, sessionTimes, sessionInstructors, numberOfClasses);
					break;
				case 3:
					showInstructorPay(instructors, numInstructors, sessionTimes, sessionInstructors, numberOfClasses);
					break;
				case 4:
					printIntructorsTimetable(instructors, classTypes, numClassTypes, sessionClasses, sessionDays, sessionTimes, sessionInstructors, numberOfClasses);
					break;
				case 5:
					showOrderedTimetable(days, times, sessionClasses, sessionDays, sessionTimes, numberOfClasses);
					break;
				case 6:
					exit = true;//Exit becomes true and do while loop does not repeat.
					break;
			}
			
			System.out.println("\nPress Return to continue");//Prompts the user to press return to do the loop over again.
			kb.nextLine();
			
		} while(!exit);//When the user chooses to exit, the do while loop ends and they are no longer prompted to choose an option. 

		System.out.println("Program terminated.");//User is informed the program has ended.
		kb.close();//Keyboard closes.
	}

	/**Allows the user to add a new class session and chose the class type, day, time and instructor. All parallel arrays
	 * are added to when a new class session is created. The class session is also written to the class details text file.
	 * A new class is not added if there is already a class on for the instructor on that time and day, or if the max number
	 * of 12 class sessions is already met.
	 * 
	 * 
	 * @param classTypes - An array of all the class types that exist.
	 * @param numClassTypes - The number of class types that exist.
	 * @param days - An array if all the days that exist.
	 * @param times - An array of all the times that exist.
	 * @param classCodes - An array of all the class type codes.
	 * @param instructors -  An array of all instructors that exist.
	 * @param sessionClasses - The class types for the class sessions.
	 * @param sessionDays - The class days for the class sessions.
	 * @param sessionTimes - The class times for the class sessions.
	 * @param sessionInstructors - The class instructors for the class sessions.
	 * @param numberOfClasses - The number of class sessions that exist.
	 * @return An updated number of class sessions that exist.
	 */
	
	public static int addNewClassSession(String classTypes[], int numClassTypes, String[] days, int [] times, String  classCodes[], String [] instructors, String [] sessionClasses, String [] sessionDays, int [] sessionTimes, String [] sessionInstructors, int numberOfClasses) throws IOException{
		
		printTitle("Add A Class");//Prints title banner.
		
		
		System.out.println("Enter the class type:");//Asks the user to pick a class type.
			for(int k =0; k < numClassTypes; k++){//Loops though all class types.
				System.out.println((k + 1) + ". " + classTypes[k]);//Print the class type.
			}
		int classNumberChosen =  readIntInRange(">> ", 1, numClassTypes);//Class type index is chosen.
		
		
		System.out.println("\nEnter the day:");//Ask the user to pick a day.
			for(int k = 0; k < days.length; k++){//Loops through all the days.
				System.out.println((k + 1) + ". " + days[k]);//Prints the day.
			}
		int dayNumberChosen = readIntInRange(">> " ,1 , days.length);//Class day index is chosen.
		
		
		
		System.out.println("\nChoose a time:");//Ask the user to pick a time.
			for(int k =0; k < times.length; k++){//Loops through all the times.
				System.out.println((k + 1) + ". " + times[k] + " o' clock");//Prints the time.
			}
		int timeNumberChosen =  readIntInRange(">> ", 1, times.length);//Class time index is chosen.
		
				
		
		System.out.println("\nChoose a Instructor:");//Ask the user to pick an instructor.
			for(int k =0; k < instructors.length; k++){//Loops through all the instructors.
				System.out.println((k + 1) + ". " + instructors[k]);//Prints the instructor.
			}
		int instructorNumberChosen =  readIntInRange(">> ", 1, instructors.length);//Class instructor index is chosen.
		
		
		//Opens class details file and allows appending.
		File outputFile = new File("ClassDetails.txt");
		FileWriter fileWriter = new FileWriter(outputFile, true);
		BufferedWriter buffer = new BufferedWriter(fileWriter);
		PrintWriter printWriter = new PrintWriter(buffer);
		
		//Ask the user if the want to add the proposed class session.
		System.out.println("\nAdd " + classCodes[classNumberChosen -1] + " on " + days[dayNumberChosen -1] + " at " + times[timeNumberChosen -1] + " with " + instructors[instructorNumberChosen -1] +"?");
		System.out.println("1. Yes\n2. No");
		int addTheNewClass = readIntInRange(">> ", 1, 2);
		
	
		//Checks if the instructor already has a session on at the proposed time and day.
		boolean classExists = checkIfClassExists(days[dayNumberChosen-1], times[timeNumberChosen -1], instructors[instructorNumberChosen -1], numberOfClasses, sessionClasses, sessionDays, sessionTimes, sessionInstructors);
		
		//If the user chooses to add the class.
		if(addTheNewClass == 1){
			
			////If the instructor already has a session on at the proposed time and day.
			if(classExists == true){
				System.out.println("Instructor already has a class at this time. - Class not added.");
			}
			
			//If the instructor does not have a session on at the proposed time and day.
			if(classExists == false){
				
				//Class session details are written to class details text file.
				printWriter.println(classCodes[classNumberChosen -1]);
				printWriter.println(days[dayNumberChosen -1]);
				printWriter.println(times[timeNumberChosen -1]);
				printWriter.println(instructors[instructorNumberChosen -1]);
				
				//Informs the user the class has been added.
				System.out.println("\nAdded " + classCodes[classNumberChosen -1] + " on " + days[dayNumberChosen -1] + " at " + times[timeNumberChosen -1] + " with " + instructors[instructorNumberChosen -1] );
				
				//Class session details are added to the parallel arrays.
				sessionClasses[numberOfClasses] = classCodes[classNumberChosen -1];
				sessionDays[numberOfClasses] = days[dayNumberChosen -1];
				sessionTimes[numberOfClasses] = times[timeNumberChosen -1];
				sessionInstructors[numberOfClasses] = instructors[instructorNumberChosen -1];
				
				numberOfClasses++; //Number of classes is added by one.
			}
		}
		
		//If the user chooses to not add the class.
		else if(addTheNewClass == 2){
			System.out.println("\nNew class not added.");
		}
	    
		printWriter.close();//Closes file
		return numberOfClasses; //Returns the number of class sessions.
	}
	
	/**Checks to see if an instructor already has a class at the entered time and day by comparing
	 * them to all the session days and times.
	 * 
	 * 
	 * @param day - The proposed day the user wants to add a class on.
	 * @param time - The proposed time the user wants to add a class at.
	 * @param instructor - The proposed instructor the user wants to add a class with.
	 * @param numberOfClasses - The number of class sessions that exist.
	 * @param sessionClasses - The class types for the class sessions.
	 * @param sessionDays - The class days for the class sessions.
	 * @param sessionTimes - The class times for the class sessions.
	 * @param sessionInstructors - The class Instructors for the class sessions.
	 * @return a boolean, true if there already is a class on at that time and day, or false if there is not.
	 * */
	
	public static boolean checkIfClassExists(String day, int time, String instructor, int numberOfClasses, String[] sessionClasses, String [] sessionDays, int [] sessionTimes, String [] sessionInstructors) throws IOException{
		
		boolean classExists = false;
	
		//Loops through each class session
		for(int k =0; k < numberOfClasses; k++){
			
			//Checks if the instructor already has a session running at that time and day.
			 if(day.compareTo(sessionDays[k]) == 0 && time == sessionTimes[k] && instructor.compareTo(sessionInstructors[k]) == 0){
				 
				 //If so classExist is true.
				 classExists = true;
			 }
		}
		
		return classExists; //Return the boolean of if there is already a session running at that time or not.
	}
	
	/**Prints the times, day and instructor for each session that is running, ordered by class type.
	 * 
	 * @param classTypes - A string array with the full name of each class type that exists.
	 * @param classCodes - A string array with the two letter code of each class type that exists.
	 * @param numClassTypes - The number of class types that exist.
	 * @param sessionClasses - The class types for the class sessions.
	 * @param sessionDays - The class days for the class sessions.
	 * @param sessionTimes - The class times for the class sessions.
	 * @param sessionInstructors - The class instructors for the class sessions.
	 * @param numberOfClasses - The number of class sessions that exist.
	 */
	
	public static void showClassTimes(String [] classTypes, String []classCodes, int numClassTypes,  String [] sessionClasses,  String [] sessionDays, int[] sessionTimes, String [] sessionInstructors, int numberOfClasses) throws IOException{
		
		printTitle("Classes");//Print title banner.
		
		//Loops through class types.
		for(int k = 0; k < numClassTypes; k++){
		
		System.out.println(classTypes[k] + "\n");
			
			//Loops though class sessions.
			for(int g =0; g < numberOfClasses; g++){
			
				 String theClass = sessionClasses[g];
				 String theDay= sessionDays[g];
				 int theTime = sessionTimes[g];
				 String theInstructor = sessionInstructors[g];
				 	 
				 //If the class session code is the sequential class, the session details are printed.
				 	if(theClass.compareTo(classCodes[k]) == 0){
				 	
				 		//Prints the session details.
				 		System.out.println("\t" + theDay + " at " + theTime + "" + " with " + theInstructor);
				 	}
			}
		}
	}
	
	/**Calculates the pay for each instructor, as well as the total pay for all 
	 * instructors this week and prints them in order of instructor.
	 * 
	 * 
	 * @param instructors - An array of all instructors that exist.
	 * @param numInstructors - The number of instructors that exist.
	 * @param sessionTimes - The class times for the class sessions.
	 * @param sessionInstructors - The class instructors for the class sessions.
	 * @param numberOfClasses - The number of class sessions that exist.
	 */
	
	public static void showInstructorPay(String [] instructors, int numInstructors, int[] sessionTimes, String[] sessionInstructors, int numberOfClasses) throws IOException {
		
		printTitle("Payments Due");//Prints title banner.
		
		System.out.println("Name\tClasses\tPay(Euros)");
		System.out.println("----\t-------\t----------");
		
		int totalPay = 0;
		
		//Loops through the instructors.
		for(int k =0; k < numInstructors; k++){
		
		    int pay = 0;
		    int numberOfClassesTaught =0;
		    String instructor;
		    int time = 0;
		    
		    //Loops through each class.
			for(int g =0; g < numberOfClasses; g++){
			
				time = sessionTimes[g];
			    instructor = sessionInstructors[g];
			   
			    if( instructor.compareTo(instructors[k]) == 0){
			    	
			    	//If the class is at 9 then they earn 80 euro.
			    	if(time == 9){
			    		pay += 80;
			    	}
			    	
			    	//If the class is at 7 or 8 then they earn 60 euro.
			    	else if(time == 8 || time == 7){
			    		pay += 60;
			    	}
			    	numberOfClassesTaught++;
			    }
			}	
			
			//Instructor earns extra 10 euro for ever class if they have taught more than one class.
			if(numberOfClassesTaught > 1){
		    	
		    	pay += (10 * numberOfClassesTaught);
		    }
			
			System.out.println(instructors[k] + "\t" + numberOfClassesTaught + "\t" + pay);
			totalPay += pay; //Pay is added to the total weeks pay.
		}
		  System.out.println("\nTotal wages for this week: " + totalPay + " Euros"); //Prints entire pay for the week.
	}
	
	/**Parallel arrays are filled from the class details text file. All saved class details are
	 * stored in the text file. It is read from to initialize the four parallel arrays.
	 * 
	 * 
	 * @param sessionClasses - Empty string array to be filled with classes from the saved class sessions. 
	 * @param sessionDays - Empty string array to be filled with days from the saved class sessions.
	 * @param sessionTimes - Empty int array to be filled with times from the saved class sessions.
	 * @param sessionInstructors - Empty string array to be filled with instructors from the saved class sessions.
	 * @return The number of class sessions that has been saved in the class details text file. 
	 */
	
	public static int loadSavedSessions(String[] sessionClasses, String []sessionDays, int [] sessionTimes, String[] sessionInstructors ) throws IOException {
		
		int numOfClasses = 0;
		int index = 0;
		
		File file = new File("ClassDetails.txt"); //Opens class details file.
		Scanner inputFile = new Scanner(file);
			
		//Assigns all saved class session details to parallel arrays.
		while(inputFile.hasNext()){
			
			sessionClasses[index] = inputFile.nextLine();
			sessionDays[index] = inputFile.nextLine();
			sessionTimes[index] = inputFile.nextInt();
			inputFile.nextLine();
			sessionInstructors[index] =inputFile.nextLine();
			
			numOfClasses += 1;
			index++;
		}
		
		 inputFile.close(); //Closes text file.
		 
		return numOfClasses; //Returns the number of class sessions that are saved.
	}
	
	/**Prints a timetable of all class sessions running for an instructor and creates a text file
	 * for that instructor and also prints the timetable to that text file. User enters an instructors
	 * name to do so, if the entered name does not exist, no times are printed and no text file is created.
	 * 
	 * @param instructorsNames - An array of all the instructors that exist.
	 * @param classTypes - An array of all the class types that exist. 
	 * @param numClassTypes - The number of class types that exist.
	 * @param sessionClasses - The class types for the class sessions.
	 * @param sessionDays - The class days for the class sessions.
	 * @param sessionTimes - The class times for the class sessions.
	 * @param sessionInstructors - The class instructors for the class sessions.
	 * @param numberOfClasses - The number of class sessions that exist.
	 */
	
	public static void printIntructorsTimetable (String [] instructorsNames, String[] classTypes, int numClassTypes, String [] sessionClasses, String [] sessionDays, int[] sessionTimes, String []sessionInstructors, int numberOfClasses) throws IOException{
		
		printTitle("Timetables");//Prints title banner.
		String nameEntered  = readString("Please enter a name: "); //Prompt the user to enter a name.
		
		int index =0;
		int match = -1;
		
		String enteredInstructor = ("");
		
		//Runs through the instructors array to check if the entered name matches any.
		do{
			
			match = nameEntered.compareToIgnoreCase(instructorsNames[index]);
			
			if(match == 0){
				enteredInstructor = instructorsNames[index];
			}
			index++;
			
			if(index > (instructorsNames.length -1)){
				index = -1;
			}
			
		}
		while(match != 0 && index > -1);
		
		//If the entered name is not one of the instructors.
		if(match != 0){
			
			System.out.println("\nName not found.");
			
		}
		
		//If the entered name is one of the instructors.
		else if(match == 0){
			
			System.out.println("\n" + enteredInstructor + "sTimetable.txt created.");
			System.out.println("Writing to file...\n");
			
			//Creates a text file for the instructor and writes to it.
			PrintWriter outputFile = new PrintWriter(enteredInstructor + "sTimetable.txt");
			
			 	String instructor;
			    int time = 0;
			    String theClass;
			    String day;
			    int numberOfClassesForInstructor = 0;
			    
			    //Runs through each class session.
				for(int k =0; k < numberOfClasses; k++){
				
					theClass = sessionClasses[k];
					day = sessionDays[k];
					time = sessionTimes[k];
				    instructor = sessionInstructors[k];
				    
				    //If the instructor has a class session running at the time and day.
				    if(enteredInstructor.compareTo(instructor) == 0){
				    	
				    	String convertedClass = convertClassCode(theClass, classTypes, numClassTypes);//Converts class type code to full word.
				    	
				    	//Print the class to the console and text file.
				    	outputFile.println(convertedClass + "\t    " + day + " at " + time);
						System.out.println(convertedClass + "\t    " + day + " at " + time);
				    	
						numberOfClassesForInstructor++;
				    }
				    
				    
				}
				
				//If the instructor has no class sessions. 
				if(numberOfClassesForInstructor == 0){	
					System.out.println("No classes assigned this week.");
			    	outputFile.println("No classes assigned this week.");
			    }
			
			
			outputFile.close();//Closes file.
		}
	}
	
	/**Method compares a class code to each class type and returns that class type if the class code 
	 * is the first two letters of a class type.
	 * 
	 * @param enteredClassCode - A class code entered.
	 * @param classTypes - The class types for the class sessions.
	 * @param numClassTypes - Number used as the number of times a for loop cycles.
	 * @return classConverted - Returns a String that is the correct corresponding class to the entered code.
	 */
	
	public static String convertClassCode(String enteredClassCode, String[] classTypes, int numClassTypes) throws IOException{
		
		String classConverted = ("");
		
		//Loops through each classTypes.
		for(int k =0; k < numClassTypes; k++){
			
			
			//Checks if the the class code corresponds to any class type.
			if(enteredClassCode.compareToIgnoreCase(classTypes[k].charAt(0) + "" + classTypes[k].charAt(1)) == 0){
			   classConverted = classTypes[k];		
			}
		}
		
		return classConverted;//Returns the full class type word.
	}
	
	/**Prints a timetable of all class sessions running, ordered by time and day.
	 * 
	 * 
	 * @param days - The three possible days are printed and used to compare against session days.
	 * @param times - The three possible times are printed and used to compare against session times.
	 * @param sessionClasses - The class types for the class sessions.
	 * @param sessionDays - The class days for the class sessions.
	 * @param sessionTimes - The class times for the class sessions.
	 * @param numberOfClasses - The number of class session that exist.
	 */
	
	public static void showOrderedTimetable(String[] days, int[] times, String[] sessionClasses, String[] sessionDays, int[] sessionTimes, int  numberOfClasses) throws IOException{
		
		printTitle("Timetable Ordered By Time"); // Prints title banner.
		
	    int time = 0;
	    String theClass;
	    String day;
	    String dayPrinted; 
	    int timePrinted;
	    
	    //Loops through each day.
	    for(int k =0; k < days.length; k++){
		    
	    	dayPrinted = days[k];
	    	
	    	System.out.print("\n" + days[k] + ":");
	    		
	    		//Loops though each time.
		    	for(int j =0; j < times.length; j++){
		    		
		    	timePrinted = times[j];
		    	System.out.print("\n\t"+ timePrinted + "    ");
		    	 	
		    		//Loops through each session class.
					for(int p = 0; p < numberOfClasses; p++){
					
						theClass = sessionClasses[p];
						day = sessionDays[p];
						time = sessionTimes[p];
					    
						
						//If class time and class day correspond to the time and day printed, the class code is printed.
					    if(time == timePrinted && day.compareTo(dayPrinted) == 0){
					    	
					    	System.out.print(theClass + " ");
					    	
					    }
					}			
		    	}	
	    }
	    System.out.println("");
   }
	
	/**
	 * Fills empty instructor names array with all the instructor names within the instructor text file.
	 * 
	 * @param instructorsNames - Empty string array is filled from the instructor text file.
	 * @return The number of instructors there are is returned.
	 */
	
	public static int loadInstructorsNames(String [] instructorsNames) throws IOException{
		
		File file = new File("Instructors.txt"); //Opens instructors text file.
		Scanner inputFile = new Scanner(file);
		
		int index =0;
		int numInstructors = 0;
		 while (inputFile.hasNext()) 
	      {
	         
	         instructorsNames[index] = inputFile.nextLine();//Assigns each instructor to an array
	              
	         index++;
	         numInstructors++;//Number of instructors is kept track of.
	         
	      }
		 inputFile.close();//Closes text file.
		
		return numInstructors; //Returns the final number of instructors that exist.
	}
	
	/** Fills empty class types array with all the class types within the class type text file.
	 * 
	 * @param classTypes - Empty string array is filled from the class types text file.
	 * @return The number of class types there are is returned.
	 */
	
	public static int loadClassTypes (String[] classTypes) throws IOException{
		
		File file = new File("ClassTypes.txt"); //Opens class type file.
		Scanner inputFile = new Scanner(file);
		
		int index =0;
		int numClassTypes =0;
		while (inputFile.hasNext()) //Runs through each class type in file.
	      {
	         //Reads in each class time to an array.
	         classTypes[index] = inputFile.nextLine(); 
	        
	         index++;
	         numClassTypes++; //Number of classes types is kept track of.
	         
	      }
		
		inputFile.close(); // Closes file.
		return numClassTypes; //Returns final number of class types.
	}
	
	/**
	 * Reads the class types file and creates a 2 letter code for each class type.
	 * 
	 * @param classTypes - the class types strings are used to make the two letter codes.
	 * @param classCodes - empty class code array is filled with the class codes.
	 * @param numberOfClassTypes - used as the amount of times the for loop will cycle.
	 */
	
	public static void loadClassCodes (String classTypes[], String classCodes[], int numberOfClassTypes) throws IOException{
		
		for(int k=0; k < numberOfClassTypes; k++){ //Loops for the amount of class codes that exist.
	    
	         String enteredClassType = classTypes[k]; // Takes a string that is the class type.
	         String code = enteredClassType.charAt(0) + "" + enteredClassType.charAt(1); // creates a string that is first 2 letters of the class type.
	         classCodes[k] = code.toUpperCase(); // Assigns code to array and makes it upper case.
	             
	      }
	}
	
	/**
	 * Prints custom Goto Gym banner
	 * 
	 * @param customStr The custom string added to the end of generic banner
	 */
	
	public static void printTitle(String customStr) 
	{
		System.out.println("\nThe Goto Gym " + customStr);//Custom string is added to the banner.
		System.out.println("-------------------------\n");
	}

	
	/**
	 * Displays a custom prompt to enter a String, and reads
	 * in that String.  If the String is empty, the user is
	 * re-prompted.
	 * 
	 * @param prompt Custom prompt to display
	 * @return a non-empty string
	 */
	
	
	public static String readString(String prompt) {
		String response;
		do {
			System.out.print(prompt);
			response = kb.nextLine();
		} while (response.isEmpty());
		return response;
	}
	
	/**
	 * Displays a custom prompt to enter an integer in a specific range, and reads
	 * in that integer.  If the user does not enter an integer, or if the entered
	 * integer is not within the specified range, an error message is displayed and
	 * the user is re-prompted.
	 * 
	 * @param question Custom prompt that is displayed
	 * @param min Minimum valid integer
	 * @param max Maximum valid integer
	 * @return Returns an integer in a specified range
	 */
	
	public static int readIntInRange(String question, int min, int max) {
		int choice = -1;
		System.out.print(question);
		boolean valid = false;
		while (!valid) 
		{
			while (!kb.hasNextInt()) 
			{
				kb.nextLine(); //clear incorrect entry
				System.out.println("ERROR - must be integer");//Informs the user that their choice must be an integer.
				System.out.print(question);
			}
			choice = kb.nextInt();
			kb.nextLine(); //clear the buffer
			
			//Choice must be between the min and max int.
			if (choice >= min && choice <= max) {
				valid = true;//While loop ends when user enders a valid int.
			}
			else
			{
				System.out.println("ERROR - integer must be between " + min + " and " + max); 
				System.out.print(question);
			}
		}
		return choice;   //Returns the users chosen integer.
	}

	/**
	 * Reads an integer value from the console window
	 * 
	 * @param question Question to be asked of the user
	 * @return An integer value is returned.
	 */
	public static int readInt(String question) {
		int choice;
		System.out.print(question);
		while (!kb.hasNextInt()) {
			kb.nextLine();
			System.out.println("ERROR - must be integer");
			System.out.println(question);
		}
		choice = kb.nextInt();
		kb.nextLine();
		return choice;
	}

	/**
	 * Reads a double value from the console window
	 * 
	 * @param question Question to be asked of the user
	 * @return A double value is returned.
	 */
	
	public static double readDouble(String question) {
		double choice;
		System.out.print(question);
		while (!kb.hasNextDouble()) {
			kb.nextLine();
			System.out.println("ERROR - must be number");
			System.out.println(question);
		}
		choice = kb.nextDouble();
		kb.nextLine();
		return choice;
	}

}