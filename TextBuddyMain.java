package practice;

//TextBuddy program takes in the respective commands (add,delete,clear,sort,search,exit). 
//This program terminates at the exit command.
//It is assumed that sort is performed using case sensitive to ensure credibility.
//Made major changes as compared to CE1 due to refactoring and improving quality of code.
//Created an open file and close file program that will assist with the TextBuddy program.
//Had performed 'commit' several times but it was incorrect.
//Made use of ArrayList which was a major modification from CE1 so that it made sorting easier.
//*
// @author: Sharon Mathew
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TextBuddyMain {
	
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_CLEAR = "clear";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_SORT = "sort";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_EXIT = "exit";

	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use.";
	private static final String MESSAGE_COMMAND = "command: ";
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\".";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\".";
	private static final String MESSAGE_CLEARED = "all content deleted from %1$s.";
	private static final String MESSAGE_SORTED = "%1$s has been sorted.";
	private static final String MESSAGE_SEARCH = "%1$s result(s) have been found.";

	// Error messages to be printed when commands are unable to be carried out
	private static final String GET_FILE_NAME = "Enter new filename: ";
	private static final String MESSAGE_EMPTY_FILE = "%1$s is empty.";
	private static final String MESSAGE_ALREADY_EMPTY_FILE = "%1$s is already empty.";
	private static final String MESSAGE_END_OF_FILE = "End of \"%1$s\" file reached.";
	private static final String MESSAGE_INVALID_COMMAND = "\"%1$s\" is an invalid command. Please try again.";
	private static final String MESSAGE_INVALID_LINE_NUMBER = "Line \"%1$s\" does not exist. Total number of Lines: %2$s. Please try again.";
	private static final String MESSAGE_EMPTY_VALUE_ADD = "Nothing to add. Please try again.";
	private static final String MESSAGE_EMPTY_VALUE_SEARCH = "Nothing to search with. Please try again.";
	private static final String MESSAGE_EMPTY_VALUE_DELETE = "You forgot to say what to delete. Please try again.";
	private static final String MESSAGE_INCORRECT_COMMAND = "Command is not recognised. Please try again.";
	private static final String MESSAGE_EMPTY_STRING = "Empty line. Try again.";
	private static final String MESSAGE_COMMAND_ERROR = "Error. Unable to complete command.";

	private static final String MESSAGE_LINE_NUMBER = "Line Number: ";

	private static final int ERROR = -568;

	
	enum COMMAND_TYPE {
		ADD, DELETE, CLEAR, SORT, DISPLAY, SEARCH, EXIT, INVALID
	};

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);

		String fileName = getFileName(sc, args);
		EditFile file = getFileEditor(fileName, sc);

		showUserMessage(String.format(MESSAGE_WELCOME, fileName));

		while (true) {
			executeProgram(sc, file);
		}
	}

	public static void showUserMessage(String message) {
		if (message != null) {
			System.out.println(message);
		}
	}

	/**
	 * Returns fileName entered by at command line
	 * 
	 * @param args
	 *            contains the String the user enters at command line
	 */
	private static String getFileName(Scanner sc, String[] args) {

		String fileName = null;
		try {
			fileName = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			// asks the user for file name if user did not include file name at
			// command line
			fileName = getFileNameFromUser(sc);
		}
		return fileName;
	}

	
	private static EditFile getFileEditor(String fileName, Scanner sc)

			throws IOException {

		if (isEmptyInput(fileName)) {
			System.out.println(String.format(MESSAGE_EMPTY_STRING));
			fileName = getFileNameFromUser(sc);
		}

		EditFile file = new EditFile(fileName, true);
		while (!file.isFound()) {
			fileName = getFileNameFromUser(sc);
			file = new EditFile(fileName);
		}
		return file;
	}

	/**
	 * Asks the user for another fileName if program is unable to open the file
	 */
	private static String getFileNameFromUser(Scanner sc) {
		System.out.print(GET_FILE_NAME);
		String fileName = sc.nextLine();
		return fileName;
	}

	private static void executeProgram(Scanner sc, EditFile file)
			throws IOException {
		System.out.print(MESSAGE_COMMAND);
		String userInput = sc.nextLine();
		String feedback = executeCommand(userInput, file);
		showUserMessage(feedback);
	}

	private static String executeCommand(String userInput, EditFile file)
			throws IOException {

		if (isEmptyInput(userInput)) {
			return String.format(MESSAGE_EMPTY_STRING);
		}

		String command = getFirstWord(userInput);
		String textToEdit = removeFirstWord(userInput).trim();

		COMMAND_TYPE commandType = determineCommandType(command);

		switch (commandType) {
		case ADD:
			return add(textToEdit, file);
		case DELETE:
			return delete(userInput, file);
		case CLEAR:
			return clear(file);
		case DISPLAY:
			return display(file);
		case SORT:
			return sort(file);
		case SEARCH:
			return search(textToEdit, file);
		case INVALID:
			return String.format(MESSAGE_INVALID_COMMAND, userInput);
		case EXIT:
			System.exit(0);
		default:
			throw new Error(MESSAGE_INCORRECT_COMMAND);
		}
	}

	private static COMMAND_TYPE determineCommandType(String command) {

		if (command.equalsIgnoreCase(COMMAND_ADD)) {
			return COMMAND_TYPE.ADD;
		} else if (command.equalsIgnoreCase(COMMAND_DELETE)) {
			return COMMAND_TYPE.DELETE;
		} else if (command.equalsIgnoreCase(COMMAND_CLEAR)) {
			return COMMAND_TYPE.CLEAR;
		} else if (command.equalsIgnoreCase(COMMAND_DISPLAY)) {
			return COMMAND_TYPE.DISPLAY;
		} else if (command.equalsIgnoreCase(COMMAND_EXIT)) {
			return COMMAND_TYPE.EXIT;
		} else if (command.equalsIgnoreCase(COMMAND_SORT)) {
			return COMMAND_TYPE.SORT;
		} else if (command.equalsIgnoreCase(COMMAND_SEARCH)) {
			return COMMAND_TYPE.SEARCH;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	/**
	 * This method adds the lines of text that the user entered to the file. It
	 * can only add one line at a time
	 */
	public static String add(String text, EditFile file) throws IOException {
		String fileName = file.getFileName();
		if (isEmptyInput(text)) {
			return String.format(MESSAGE_EMPTY_VALUE_ADD);
		}
		try {
			int lineNumberToAdd = file.readLines() + 1;
			String textToAdd = String.valueOf(lineNumberToAdd) + ". " + text;
			file.writeToFile(textToAdd);
			return String.format(MESSAGE_ADDED, fileName, text);
		} catch (EOFException ef) {
			return String.format(MESSAGE_END_OF_FILE, file);
		} catch (IOException e) {
			return MESSAGE_COMMAND_ERROR;
		}
	}

	/**
	 * This method deletes the line whose line number the user has entered. It
	 * can only delete one line at a time
	 */
	public static String delete(String text, EditFile file) throws IOException {

		String notValidInput = checkValidInput(text, file);

		if (notValidInput != null) {
			return notValidInput;
		}

		String fileName = file.getFileName();
		int lineNumber = getLineNumberToDelete(text);
		String textToRemove = null;

		try {
			ArrayList<String> lines = file.getFileContent();
			int totalNumOfLines = lines.size();
			String lineToDelete = lines.get(lineNumber - 1);
			textToRemove = removeNumberFromLine(lineToDelete);

			clearAll(fileName);
			rewriteDataToFile(file, lineNumber, lines, totalNumOfLines);

		} catch (IOException e) {
			return MESSAGE_COMMAND_ERROR;
		}
		return String.format(MESSAGE_DELETED, fileName, textToRemove);
	}

	public static String display(EditFile file) throws IOException {
		String fileName = file.getFileName();
		if (file.isEmpty()) {
			return String.format(MESSAGE_EMPTY_FILE, fileName);
		}

		try {
			return printFromFile(file);
		} catch (IOException e) {
			return MESSAGE_COMMAND_ERROR;
		}
	}

	private static String printFromFile(EditFile file) throws IOException {
		ArrayList<String> lines = file.getFileContent();
		int numOfLines = file.readLines();
		try {
			for (int i = 0; i < numOfLines; i++) {
				System.out.println(lines.get(i));
			}
		} catch (IndexOutOfBoundsException e) {
			e.getMessage();
			return MESSAGE_COMMAND_ERROR;
		}
		return null;
	}

	public static String clear(EditFile file) throws IOException {
		String fileName = file.getFileName();
		if (file.isEmpty()) {
			return String.format(MESSAGE_ALREADY_EMPTY_FILE, fileName);
		}
		return clearAll(fileName);
	}

	/**
	 * This method deletes all the lines in the file
	 */
	private static String clearAll(String fileName) throws IOException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName);
			writer.print("");
		} catch (IOException e) {
			return MESSAGE_COMMAND_ERROR;
		} finally {
			writer.close();
		}
		return String.format(MESSAGE_CLEARED, fileName);
	}

	public static String sort(EditFile file) throws IOException {
		String fileName = file.getFileName();

		if (file.isEmpty()) {
			return String.format(MESSAGE_EMPTY_FILE, fileName);
		}

		ArrayList<String> lines = file.getFileContent();
		ArrayList<String> linesToSort = removeNumberFromLinesList(lines);
		Collections.sort(linesToSort, String.CASE_INSENSITIVE_ORDER);
		printToFile(linesToSort, file);

		return String.format(MESSAGE_SORTED, fileName);
	}

	public static String search(String textToSearch, EditFile file)
			throws IOException {
		if (isEmptyInput(textToSearch)) {
			return String.format(MESSAGE_EMPTY_VALUE_SEARCH);
		}

		if (file.isEmpty()) {
			String fileName = file.getFileName();
			return String.format(MESSAGE_EMPTY_FILE, fileName);
		}
		ArrayList<String> searchResults = getSearchResults(textToSearch, file);
		printSearchResults(searchResults);
		return null;
	}

	private static String checkValidInput(String text, EditFile file)
			throws IOException {
		String notValid = checkFileAndText(text, file);

		if (notValid != null) {
			return notValid;
		} else {

			notValid = checkLineNumber(text, file);

			if (notValid != null) {
				return notValid;
			}
		}
		return notValid;
	}

	/**
	 * Checks to see if the either the text and file are empty
	 * 
	 * @return null if its not valid, otherwise returns an error message
	 */
	private static String checkFileAndText(String text, EditFile file)
			throws IOException {
		String fileName = file.getFileName();

		// checks if file and input
		if (file.isEmpty()) {
			return String.format(MESSAGE_ALREADY_EMPTY_FILE, fileName);
		} else if (isEmptyInput(text)) {
			return String.format(MESSAGE_EMPTY_VALUE_DELETE);
		}
		return null;
	}

	/**
	 * Checks to see if line number given to delete exists
	 * 
	 * @return null if it exists else returns Error message
	 */
	private static String checkLineNumber(String text, EditFile file)
			throws IOException {
		ArrayList<String> lines = file.getFileContent();
		int totalNumOfLines = lines.size();
		int lineNumber = getLineNumberToDelete(text);

		if (lineNumber <= -1) {
			return String.format(MESSAGE_INVALID_COMMAND, text);
		}
		// checks to see if line number entered is valid

		if (lineNumber > totalNumOfLines) {
			return String.format(MESSAGE_INVALID_LINE_NUMBER, lineNumber,
					totalNumOfLines);
		}

		return null;
	}

	/**
	 * gets the line number that the user wants to delete from the user input
	 * 
	 * @param text
	 *            is the string representation of the lineNumber the user wants
	 *            to delete
	 */
	private static int getLineNumberToDelete(String text) {

		int lineNumber;
		try {
			lineNumber = Integer.parseInt(getSecondWord(text));
		} catch (NumberFormatException er) {
			return ERROR;
		}
		return lineNumber;
	}

	/**
	 * Rewrites all data to file without the line to be deleted
	 */
	private static void rewriteDataToFile(EditFile file, int lineNumber,
			ArrayList<String> lines, int totalNumOfLines) throws IOException {
		for (int i = 0; i < totalNumOfLines; i++) {

			if (lineNumber - 1 < i) {
				reNumberLinesAfterDeletion(file, lines, i);
			} else if (lineNumber - 1 == i) {
				continue;
			} else {
				file.writeToFile(lines.get(i));
			}
		}
	}

	/**
	 * corrects the numbering of lines written to file after removing a single
	 * line.
	 */
	private static void reNumberLinesAfterDeletion(EditFile file,
			ArrayList<String> lines, int index) throws IOException {
		String linesWithNewNumbers = changeNumbering(index, lines.get(index));
		file.writeToFile(linesWithNewNumbers);
	}

	/**
	 * changes the line number to be printed along with each line of text in
	 * file
	 * 
	 * @param newLineNumber
	 *            : line number to be added to file
	 * @param line
	 *            : line to be edited
	 * @return the string containing the correct line numbers and text
	 */
	private static String changeNumbering(int newLineNumber, String line) {
		String oldLineNumberString = getFirstWord(line).trim();
		String newLineNumberString = String.valueOf(newLineNumber) + ".";
		String lineWithNewNumber = line.replaceFirst(oldLineNumberString,
				newLineNumberString);
		return lineWithNewNumber;
	}

	private static void printToFile(ArrayList<String> linesToPrint,
			EditFile file) throws IOException {
		String fileName = file.getFileName();
		clearAll(fileName);
		int length = linesToPrint.size();
		for (int i = 0; i < length; i++) {
			String textLine = linesToPrint.get(i);
			String line = i + 1 + ". " + textLine;
			file.writeToFile(line);
		}
	}

	private static ArrayList<String> removeNumberFromLinesList(
			ArrayList<String> listOfLines) {
		int length = listOfLines.size();
		ArrayList<String> linesWithoutNumber = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			String line = listOfLines.get(i);
			String lineToSort = removeNumberFromLine(line);
			linesWithoutNumber.add(i, lineToSort);

		}
		return linesWithoutNumber;
	}

	private static void printSearchResults(ArrayList<String> searchResults) {
		int length = searchResults.size();
		for (int i = 0; i < length; i++) {
			String line = searchResults.get(i);
			showUserMessage(line);
		}
	}

	public static ArrayList<String> getSearchResults(String textToSearch,
			EditFile file) throws IOException {
		ArrayList<String> linesFound = findMatchingLines(textToSearch, file);
		ArrayList<String> searchResults = formatLines(linesFound);
		return searchResults;
	}

	/**
	 * Searches the entire file, finds and stores lines that match textToSearch
	 * into linesFound
	 */
	private static ArrayList<String> findMatchingLines(String textToSearch,
			EditFile file) throws IOException {
		int numberOfLines = file.readLines();
		ArrayList<String> linesFound = new ArrayList<String>();
		for (int i = 0; i < numberOfLines; i++) {
			ArrayList<String> fileContent = file.getFileContent();
			String line = fileContent.get(i);
			if (line.contains(textToSearch)) {
				linesFound.add(line);
			}
		}
		return linesFound;
	}

	/**
	 * Formats the matching lines found into a more user-friendly view.
	 */
	private static ArrayList<String> formatLines(ArrayList<String> linesFound) {

		int length = linesFound.size();
		ArrayList<String> searchResults = new ArrayList<String>();
		String numberOfResultsFound = String.format(MESSAGE_SEARCH, length);
		searchResults.add(numberOfResultsFound);
		for (int i = 0; i < length; i++) {
			String line = linesFound.get(i);
			String firstWord = getFirstWord(line);
			String lineNumber = firstWord.replace(".", "");
			String text = removeFirstWord(line);
			searchResults.add(MESSAGE_LINE_NUMBER + lineNumber);
			searchResults.add(text);
		}
		return searchResults;
	}

	private static boolean isEmptyInput(String userInput) {
		if (userInput.trim().equals("") || userInput.trim().equals("\n")) {
			return true;
		}
		return false;
	}

	private static String removeNumberFromLine(String line) {
		return removeFirstWord(line);
	}

	private static String getFirstWord(String line) {
		String firstWord = line.trim().split("\\s+")[0];
		return firstWord;
	}

	private static String getSecondWord(String line) {
		String secondWord = line.trim().split("\\s+")[1];
		return secondWord;
	}

	private static String removeFirstWord(String line) {
		String firstWord = getFirstWord(line);
		String leftoverString = line.replaceFirst(firstWord + " ", "");
		return leftoverString;
	}

}

