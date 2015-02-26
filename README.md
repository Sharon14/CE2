<<<<<<< HEAD
# CE2
CE2 Code. Testing if cloning works.






















=======
//Original CE1 code without edits
package practice;
	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileNotFoundException ;
	import java.io.IOException;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.BufferedReader;
	import java.io.PrintWriter ;
	import java.util.Scanner;

	   public class TextBuddy{
		   private static String fileName = "" ;
		   private static File file;
		   private static final String WELCOME_MESSAGE = "Welcome to TextBuddy %s is ready for use." ;
		   private static final String DEFAULT_FILE_NAME = "mytextbuddy.txt";
		   private static final String EXIT_MESSAGE = "Bye" ;
		   
		   
		   private static void welcomeMessage(String message, String fileNameFromUser){
			   if(fileNameFromUser == null || fileNameFromUser.isEmpty()){
				   file = new File(DEFAULT_FILE_NAME) ;
				   fileName = DEFAULT_FILE_NAME;
				   System.out.println(String.format(WELCOME_MESSAGE, DEFAULT_FILE_NAME)) ;
				   return;
			   }
			   fileName = fileNameFromUser;
			   System.out.println(String.format(WELCOME_MESSAGE, fileName)) ;
		   }
		   private static void readLines() throws IOException{
				FileReader fileReader = new FileReader(fileName) ;
				BufferedReader text = new BufferedReader(fileReader) ;
				
				String invalid = fileName + " is invalid or empty!";
				
				int numLines = 0 ;
				if(text.readLine() == null){
					System.out.println(invalid);
				}
				else{
					
					String startLine;
					while((startLine = text.readLine()) != null){
						numLines++ ;
						System.out.println(numLines+". " + startLine) ;
					}
				}
				text.close();
				
			}
		
		   public static void main(String args[]){
			   welcomeMessage(WELCOME_MESSAGE, null) ;
			   try {
				executeCommandtoExit() ;
			} catch (IOException e) {
				e.printStackTrace();
				
				System.out.println(EXIT_MESSAGE);
			}
			   
		   }
		   public static String executeCommandtoExit()throws IOException {
			   while(true){
				   System.out.print("command: ") ;
				   Scanner sc = new Scanner(System.in) ;
				   String commandType = sc.next().toLowerCase() ;
				   
				   if(commandType.equals("add")){
					   String inputLine ="" ;
					   if(sc.hasNextLine()){	
						   inputLine = sc.nextLine().trim() ;
						   addLine(inputLine);
					   }
				   }
				   else if(commandType.equals("delete")){
						   int numLine = 0;
						   if(sc.hasNextLine()){
							   numLine = sc.nextInt();
							   deleteInst(numLine) ;
						   }
					   }
					   else if(commandType.equals("display")){
						   readLines() ;
					   }
					   else if(commandType.equals("clear")){
						   clearFile();
					   }
					   else if((commandType.equals("exit"))){
						   System.out.print(EXIT_MESSAGE);
						   sc.close();
						   System.exit(0) ;
						   
						   
					   }
					   else if ((commandType.equals(""))){
						   continue;
					   }
					   
				   
			   }	   
					
		   }
		  private static void addLine(String inputLine) throws IOException{
			   FileWriter input = new FileWriter(fileName, true) ;
			   PrintWriter printOut = new PrintWriter(input) ;
			   
			   printOut.println(inputLine) ;
			   printOut.close() ;
			   String addedMsg = "added to " + fileName + ":\""+inputLine + "\"";
			   
			System.out.println(addedMsg) ;
			   	
		   }
		  
		  private static void deleteInst(int deleteLine)throws IOException{
			  File temp = new File("temp"+ fileName);
			  BufferedReader text = new BufferedReader(new FileReader(fileName)) ;
			  BufferedWriter writeOut = new BufferedWriter(new FileWriter(temp)) ;
			  int lineNumber = 1;
			  String original = "" ;
			  String currentLine = " " ;

			  while((currentLine = text.readLine()) != null){
				  if(lineNumber != deleteLine){
					  writeOut.write(currentLine) ;
					  writeOut.newLine();
					  lineNumber++;
				  }
				  else{
					  original = currentLine;
					  lineNumber++;
					  continue;
				  }
			  }
			  text.close();
			  writeOut.close();

			  file.delete();
			  temp.renameTo(file) ;

			  String deleted = "deleted " + fileName + ":\"" + original + "\"" ;
			  System.out.println(deleted) ;

		  }
		  
		   private static void clearFile() throws FileNotFoundException {
				PrintWriter print = new PrintWriter(fileName);
				print.close();
				String deleteMsg = "all content deleted from " + fileName;
				System.out.println(deleteMsg) ;
			}

		}
		   
>>>>>>> 42cc080a8cdfb4f851eb914c03e653e777aca98c
