package practice;



	import java.io.BufferedReader;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintWriter;
	import java.util.ArrayList;

	public class EditFile {
	    /**
	     * This class helps TextBuddyMain.java read and write lines from files. It
	     * can read and write text to a textfile line by line.
	     */

	    private String fileName;
	    private boolean appendToFile = true;

	    public EditFile(String fileName) {
	        this.fileName = fileName;
	    }

	    public EditFile(String fileName, boolean append_value) {
	        this.fileName = fileName;
	        this.appendToFile = append_value;
	    }

	    public String getFileName() {
	        return this.fileName;
	    }

	    public void writeToFile(String textLine) throws IOException {
	        FileWriter write = new FileWriter(fileName, this.appendToFile);
	        PrintWriter printALine = new PrintWriter(write);
	        printALine.printf("%s" + "%n", textLine);
	        printALine.close();
	    }

	    /**
	     * This method checks to see if a file exists or if the file is a valid file
	     * and not a folder
	     */
	    public boolean isFound() throws IOException {
	        FileReader fileToRead = null;
	        boolean isFileFound = true;
	        try {
	            fileToRead = new FileReader(this.fileName);

	        } catch (IOException ex) {
	            System.out.println(ex.getMessage());
	        } finally {

	            if (fileToRead == null) {
	                return false;
	            }

	            else
	                fileToRead.close();
	        }
	        return isFileFound;
	    }

	    /**
	     * This method opens a file and stores all the file content line by line
	     * into textData
	     * 
	     * @return textData
	     * @throws IOException
	     */
	    public ArrayList<String> getFileContent() throws IOException {

	        FileReader fileToRead = null;
	        BufferedReader textReader = null;
	        ArrayList<String> textData = new ArrayList<String>();
	        try {
	            fileToRead = new FileReader(fileName);
	            textReader = new BufferedReader(fileToRead);

	            int numberOfLines = readLines();

	            for (int i = 0; i < numberOfLines; i++) {
	                textData.add(i, textReader.readLine());
	            }
	        } catch (IOException e) {
	            e.getMessage();
	        } finally {
	            textReader.close();
	            fileToRead.close();
	        }
	        return textData;

	    }

	    /**
	     * This method counts the number of lines in the file
	     */
	    public int readLines() throws IOException {

	        FileReader fileToRead = null;
	        BufferedReader textReader = null;
	        int numberOfLines = 0;
	        try {
	            fileToRead = new FileReader(fileName);
	            textReader = new BufferedReader(fileToRead);
	            @SuppressWarnings("unused")
	            String aLine;
	            while ((aLine = textReader.readLine()) != null) {
	                numberOfLines++;
	            }
	        } catch (IOException e) {
	            e.getMessage();
	        } finally {

	            textReader.close();
	            fileToRead.close();
	        }
	        return numberOfLines;

	    }

	    /**
	     * This method checks to see if the file has anything written to it or is
	     * empty
	     */
	    public boolean isEmpty() throws IOException {
	        BufferedReader textReader = null;
	        FileReader fileToRead = null;
	        boolean isEmpty = false;
	        try {
	            fileToRead = new FileReader(this.fileName);
	            textReader = new BufferedReader(fileToRead);

	            if (textReader.readLine() == null) {
	                isEmpty = true;
	            }
	        } catch (IOException e) {
	            e.getMessage();
	        } finally {
	            fileToRead.close();
	            textReader.close();
	        }
	        return isEmpty;
	    }
	}

