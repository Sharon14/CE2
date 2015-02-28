package practice;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;



public class ReadFile1 {

	private String path;

	public ReadFile1(String file_path) {
		path = file_path;
	}

	// This method returns all the lines in the text file.
	public String[] OpenFile() throws IOException {

		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);

		int numLines = readLines();
		String[] textData = new String[numLines];

		readFileContent(textReader, numLines, textData);

		textReader.close();
		return textData;
	}

	// This method reads the content of the text file.
	private void readFileContent(BufferedReader textReader, int numLines,
			String[] textData) throws IOException {
		for (int i = 0; i < numLines; i++) {
			textData[i] = textReader.readLine();
		}
	}

	// This method returns the number of lines in the text file.
	int readLines() throws IOException {

		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);

		int numLines = 0;
		numLines = calculateNumLines(bf, numLines);

		bf.close();

		return numLines;
	}

	private int calculateNumLines(BufferedReader bf, int numLines)
			throws IOException {
		while ((bf.readLine()) != null) {
			numLines++;
		}
		return numLines;
	}
}


