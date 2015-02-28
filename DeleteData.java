package practice;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class DeleteData {

	private String file;
	private String lineToRemove;

	public DeleteData(String fileToRead, String line) {
		file = fileToRead;
		lineToRemove = line;
	}

	public DeleteData(String fileToRead) {
		file = fileToRead;
	}

	// This method removes a line from the text file.
	public void removeLineFromFile() throws IOException, FileNotFoundException {

		try {

			File inFile = processFile();
			File tempFile = createTempFile(inFile);

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			copyToTemp(br, pw);

			pw.close();
			br.close();

			deleteFile(inFile);
			checkRenameFile(inFile, tempFile);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	// This method copies the content of the text file which is to be kept after
	// deletion, to a temporary file.
	private void copyToTemp(BufferedReader br, PrintWriter pw)
			throws IOException {
		String line = null;
		boolean found = false;
		int lineNum = getLineNum();

		while ((line = br.readLine()) != null) {

			if (isLineBeforeLineToDelete(line, found)) {
				printLinesFromOriginal(pw, line);
			}
			if (isLineToDelete(line)) {
				found = setLineFound();
			}
			if (isLineAfterLineToDelete(line, found)) {
				printLinesAfterLineToDelete(pw, line, lineNum);
				lineNum++;
			}
		}
	}

	// This method checks if a line is after the line to be deleted.
	private boolean isLineAfterLineToDelete(String line, boolean found) {
		return (found) && (!isLineToDelete(line));
	}

	// This method checks if a line is the line to be deleted.
	private boolean isLineToDelete(String line) {
		return line.trim().equals(lineToRemove);
	}

	// This method checks if a line is before the line to be deleted.
	private boolean isLineBeforeLineToDelete(String line, boolean found) {
		return (!found) && (!isLineToDelete(line));
	}

	// This method checks that the text file to delete from exists and returns
	// the text file.
	private File processFile() {
		File inFile = new File(file);
		checkExistingFile(inFile);
		return inFile;
	}

	// This method writes the lines after the line to delete, to the text file.
	private void printLinesAfterLineToDelete(PrintWriter pw, String line,
			int lineNum) {
		int index = line.indexOf(" ");
		String lineWithNum = updateLineNumber(line, lineNum, index);

		pw.println(lineWithNum);
	}

	// This method updates the line number of the lines.
	private String updateLineNumber(String line, int lineNum, int index) {
		String lineWithoutNum = getLineWithoutNum(line, index);
		String lineWithNum = getLineWithNum(lineNum, lineWithoutNum);
		return lineWithNum;
	}

	// This method returns the line with the line number.
	private String getLineWithNum(int lineNum, String lineWithoutNum) {
		return String.valueOf(lineNum).concat(". ").concat(lineWithoutNum);
	}

	// This method returns the content of the line, without the line number.
	private String getLineWithoutNum(String line, int index) {
		String lineWithoutNum = line.substring(index + 1);
		return lineWithoutNum;
	}

	// This method marks that the line to be deleted has been found.
	private boolean setLineFound() {
		boolean found = true;
		return found;
	}

	// This method writes lines from the text file to the temporary file.
	private void printLinesFromOriginal(PrintWriter pw, String line) {
		pw.println(line);
		pw.flush();
	}

	// This method checks if the temporary file can be renamed and prints
	// feedback if the file cannot be renamed.
	private void checkRenameFile(File inFile, File tempFile) {
		if (!tempFile.renameTo(inFile)) {
			System.out.println("Unable to rename file");
		}
	}

	// This method checks if the file can be deleted and prints feedback if the
	// file cannot be deleted.
	public void deleteFile(File inFile) {
		if (!inFile.delete()) {
			System.out.println("Unable to delete file");
			return;
		}
	}

	// This method creates and returns a temporary file.
	public File createTempFile(File inFile) {
		File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
		return tempFile;
	}

	// This method gets the line number.
	public int getLineNum() {
		int dotIndex = lineToRemove.indexOf(".");
		String num = lineToRemove.substring(0, dotIndex);
		int lineNum = Integer.parseInt(num);

		return lineNum;
	}

	// This method deletes all the content in the text file.
	public void clearText() throws IOException, FileNotFoundException {
		try {
			File inFile = processFile();

			File tempFile = createTempFile(inFile);

			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			pw.print("");
			pw.close();

			deleteFile(inFile);

			checkRenameFile(inFile, tempFile);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// This method checks if given file is an existing file. If not, it prints
	// feedback that the file is not an existing file.
	public void checkExistingFile(File inFile) {
		if (!inFile.isFile()) {
			System.out.println("This is not an existing file");
			return;
		}
	}
}


