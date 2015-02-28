
/* @author: Sharon 
*/
package practice;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;



public class TextBuddy {
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy! %1$s is ready for use.";
	
	private static final String MESSAGE_EMPTY_FILE = "nothing to %1$s %2$s is empty!";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format: %1$s";
	private static final String MESSAGE_SEARCH_UNSUCCESSFUL = "not found";
	private static final String MESSAGE_UNRECOGNIZED_COMMANDTYPE = "Command type not recognized";
	private static final String MESSAGE_NULL_CONTENT = "Content is null! Try again.";
	
	private static final String MESSAGE_ADD = "Content added to %1$s: %2$s ";
	private static final String MESSAGE_DELETE = "Content deleted from %1$s: %2$s" ;
	private static final String MESSAGE_CLEAR = "All content removed from %1$s";
	private static final String MESSAGE_SORT = "%1$s has been sorted.";
	private static final String MESSAGE_SEARCH_NOT_FOUND = "%1$s has not been found in %2$s.";

	

	private static Scanner sc = new Scanner(System.in);

	enum CommandType {
		ADD, DELETE, DISPLAY, EXIT, INVALID, CLEAR, SORT, SEARCH
	};

	public static void main(String[] args) throws IOException {
		String fileName = getFileName(args);
		printWelcomeMessage(fileName);
		processInput(fileName);
	}

	// This method gets the file name specified by the user when he runs
	// TextBuddy.
	public static String getFileName(String[] args) {
		String fileName = args[0];
		return fileName;
	}

	// This method processes the user commands till the user exits the program.
	public static void processInput(String fileName) throws IOException {
		while (true) {
			System.out.print("Enter command:");
			String userCommand = sc.nextLine();
			checkCommandValidity(userCommand, fileName);
		}
	}
	public static String printWelcomeMessage(String fileName) {
		return (String.format(MESSAGE_WELCOME, fileName));
	}

	// This method checks if file is empty, otherwise it displays all the
	// content in the text file.
	public static void displayText(String userCommand, String fileName)
			throws IOException {

		String commandType = getCommandType(userCommand);
		try {
			String[] lines = readFile1(fileName);

			if (checkFileEmpty(lines)) {
				printFeedbackEmptyFile(fileName, commandType);
			} else {
				printFileContent(lines);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	// This method checks if the text file is empty.
	public static boolean checkFileEmpty(String[] lines) {
		return lines.length == 0;
	}

	// This methods prints all the content in the text file.
	public static void printFileContent(String[] lines) {
		for (int i = 0; i < lines.length; i++) {
			System.out.println(lines[i]);
		}
	}
	// This method prints feedback that the file is empty.
		public static void printFeedbackEmptyFile(String fileName,
				String commandType) {
			System.out.println(String.format(MESSAGE_EMPTY_FILE, commandType,
					fileName));
		}

		// This method checks if the user has entered anything at all. If the user
		// has entered something it processes the command.
		public static void checkCommandValidity(String userCommand, String fileName)
				throws IOException {
			try {
				if (checkEmptyCommand(userCommand)) {

					printInvalidCommand(userCommand);
				} else {
					processNonEmptyCommands(userCommand, fileName);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		// This method checks if the command is null or not.
		public static boolean checkEmptyCommand(String userCommand) {
			return userCommand.trim().equals("");
		}

		// This method gets the command type and processes commands which are not
		// empty.
		public static void processNonEmptyCommands(String userCommand,
				String fileName) throws IOException {
			String commandTypeString = getCommandType(userCommand);
			CommandType commandType = matchCommandType(commandTypeString);
			String content = removeCommand(userCommand);
			String[] lines = readFile1(fileName);
			processCommand(commandType, userCommand, fileName, lines, content);
		}

		public static void printInvalidCommand(String userCommand) {
			System.out.println(String.format(MESSAGE_INVALID_FORMAT, userCommand));
		}

		// This method returns the command type for each commandTypeString input.
		public static CommandType matchCommandType(String commandTypeString) {
			if (commandTypeString.equalsIgnoreCase("add")) {
				return CommandType.ADD;
			} else if (commandTypeString.equalsIgnoreCase("display")) {
				return CommandType.DISPLAY;
			} else if (commandTypeString.equalsIgnoreCase("delete")) {
				return CommandType.DELETE;
			} else if (commandTypeString.equalsIgnoreCase("clear")) {
				return CommandType.CLEAR;
			} else if (commandTypeString.equalsIgnoreCase("sort")) {
				return CommandType.SORT;
			} else if (commandTypeString.equalsIgnoreCase("search")) {
				return CommandType.SEARCH;
			} else if (commandTypeString.equalsIgnoreCase("exit")) {
				return CommandType.EXIT;
			} else {
				return CommandType.INVALID;
			}
		}

		// This method gets the type of command entered by the user, either add,
		// delete, display, clear, search or sort.
		public static String getCommandType(String userCommand) {
			return userCommand.trim().split("\\s+")[0];
		}
		public static void processCommand(CommandType commandType,
				String userCommand, String fileName, String[] lines, String content)
				throws IOException {

			switch (commandType) {
			case ADD:
				addText(userCommand, fileName, lines, content);
			case DELETE:
				deleteText(userCommand, fileName, lines, content);
			case DISPLAY:
				displayText(userCommand, fileName);
			case INVALID:
				printInvalidCommand(userCommand);
			case CLEAR:
				clearText(userCommand, fileName, lines);
			case SORT:
				sortLines(fileName, userCommand, lines);
			case SEARCH:
				searchWord(userCommand, fileName, lines);
			case EXIT:
				System.exit(0);
			default:
				throw new Error(MESSAGE_UNRECOGNIZED_COMMANDTYPE);
			}

		}

		// This method checks if the user entered a word to search or if the file is
		// empty. Otherwise, it proceeds with the search.
		public static void searchWord(String userCommand, String fileName,
				String[] lines) {

			String word = removeCommand(userCommand);
			String commandType = getCommandType(userCommand);

				if (checkFileEmpty(lines)) {
					printFeedbackEmptyFile(fileName, commandType);
				} else {
					String feedback = getLineWithWord(lines, word);
					printFeedbackSearch(fileName, feedback, word);
				}
			}
		public static String getLineWithWord(String[] lines, String word) {
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].matches(".*\\b" + word + "\\b.*")) {
					return lines[i];
				}
			}

			return MESSAGE_SEARCH_UNSUCCESSFUL;
		}

		public static void printFeedbackSearch(String fileName, String feedback,
				String word) {

			if (feedback.equals(MESSAGE_SEARCH_UNSUCCESSFUL)) {
				System.out.println(String.format(MESSAGE_SEARCH_NOT_FOUND,
						word, fileName));
			} else {
				System.out.println(feedback);
			}
		}

		// This method sorts lines in the text file alphabetically.
		public static void sortLines(String fileName, String userCommand,
				String[] lines) throws IOException {
			String commandType = getCommandType(userCommand);
			try {
				if (checkFileEmpty(lines)) {
					printFeedbackEmptyFile(fileName, commandType);
				} else {
					processSort(fileName, lines);
					printFeedbackSorted(fileName);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		// This method copies all the content in the text file and sorts it. It then
		// clears all the content in the text file and copies back the sorted content to the text file.
		public static void processSort(String fileName, String[] lines)
				throws IOException, FileNotFoundException {
			List<String> temp = new ArrayList<String>();
			copyFileContent(lines, temp);
			Collections.sort(temp);
			clearFileContent(fileName);
			copyBackToFile(fileName, temp);
		}
		
		//This method deletes all the lines in the text file.
		public static void clearFileContent(String fileName) throws IOException,
				FileNotFoundException {
			DeleteData deleteLine = new DeleteData(fileName);
			deleteLine.clearText();
		}

		// This method copies the sorted content to the text file.
		public static void copyBackToFile(String fileName, List<String> temp)
				throws IOException {
			for (int i = 1; i <= temp.size(); i++) {
				String textLine = i + ". " + temp.get(i - 1);
				writeToFile(textLine, fileName);
			}
		}

		// This method copies all the content of text file to an ArrayList object.
		public static void copyFileContent(String[] lines, List<String> temp) {
			for (int i = 0; i < lines.length; i++) {
				int start = lines[i].indexOf(" ");
				String lineNoNum = lines[i].substring(start).trim();
				temp.add(lineNoNum);
			}
		}

		public static void printFeedbackSorted(String fileName) {
			System.out.println(String.format(MESSAGE_SORT, fileName));
		}

		// This method clears all the content in the text file.
		public static void clearText(String userCommand, String fileName,
				String[] lines) throws IOException {
			try {
				String commandType = getCommandType(userCommand);

				if (checkFileEmpty(lines)) {
					printFeedbackEmptyFile(fileName, commandType);
				} else {
					clearFileContent(fileName);
					printFeedbackClear(fileName);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		// This method reads all the content in the text file and returns the lines.
		public static String[] readFileContent(String fileName) throws IOException {
			ReadFile1 file = new ReadFile1(fileName);
			String[] lines = file.OpenFile();
			return lines;
		}

		// This method prints feedback that all the content in the text file has
		// been deleted.
		public static void printFeedbackClear(String fileName) {
			System.out.println(String.format(MESSAGE_CLEAR, fileName));
		}

		// This method checks if the user has specified which line to delete and
		// checks if text file is empty. If not, it proceeds to delete a specific
		// line
		// in the text file.
		public static void deleteText(String userCommand, String fileName,
				String[] lines, String content) throws IOException {
			try {
				String commandType = getCommandType(userCommand);

				if (checkFileEmpty(lines)) {
					printFeedbackEmptyFile(fileName, commandType);
				} else {
					int numOfLineDelete = getNumLineToDelete(content);
					searchLineToDelete(fileName, lines, numOfLineDelete);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		// This method searches for the line to delete in the text file.
		public static void searchLineToDelete(String fileName, String[] lines,
				int numOfLineDelete) throws IOException, FileNotFoundException {
			for (int i = 0; i < lines.length; i++) {
				if (i + 1 == numOfLineDelete) {
					String line = deleteLineFromFile(fileName, lines, i);
					printFeedbackDelete(fileName, line);
					break;
				}
			}
		}

		// This method prints feedback that the line has been deleted and prints the
		// line without the line number.
		public static void printFeedbackDelete(String fileName, String line) {
			int index = line.indexOf(" ");
			System.out.println(String.format(MESSAGE_DELETE, fileName,
					line.substring(index).trim()));
		}

		// This method deletes the specified line from the text file.
		public static String deleteLineFromFile(String fileName, String[] lines,
				int i) throws IOException, FileNotFoundException {
			String line = lines[i];
			DeleteData deleteLine = new DeleteData(fileName, line);
			deleteLine.removeLineFromFile();
			return line;
		}

		// This method gets the line number of line to be deleted.
		public static int getNumLineToDelete(String line) {
			return Integer.parseInt(line);
		}

		// This method checks if user specifies what to add. If it is specified, it
		// proceeds to add a line to the text file.
		public static void addText(String userCommand, String fileName,
				String[] lines, String content) throws IOException {

			try {
				String commandType = getCommandType(userCommand);

				if (checkEmptyCommand(content)) {
					printFeedbackNullContent(commandType);
				} else {
					processAddCommand(fileName, lines, content);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		public static String printFeedbackNullContent(String commandType) {
			return (String.format(MESSAGE_NULL_CONTENT, commandType));
		}


		// This method writes a line to the text file.
		public static void processAddCommand(String fileName, String[] lines,
				String content) throws IOException {
			try {
				String textLine = attachLineNum(content, lines);
				writeToFile(textLine, fileName);
				printFeedbackAdd(content, fileName);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		// This method writes a single line to the text file.
		public static void writeToFile(String textLine, String fileName)
				throws IOException {
			WriteFile1 data = new WriteFile1(fileName, true);
			data.writeToFile(textLine);
		}

		public static void printFeedbackAdd(String line, String fileName) {
			System.out.println(String.format(MESSAGE_ADD, fileName, line));
		}

		// This method reads the content of the text files and returns the content.
		public static String[] readFile1(String fileName) throws IOException {
			String[] lines = readFileContent(fileName);
			return lines;
		}

		// This method attaches the correct line number to the line in the text
		// file.
		public static String attachLineNum(String line, String[] lines) {
			String numToInsert = String.valueOf(lines.length + 1).concat(". ");
			String textLine = numToInsert.concat(line);
			return textLine;
		}

		// This method removes the command type entered by the user.
		public static String removeCommand(String userCommand) {
			return userCommand.replace(getCommandType(userCommand), "").trim();
		}

	}




	
	
	
	
	
	
	
	
	
	
	
	
	
	