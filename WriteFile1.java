package practice;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class WriteFile1 {

	private String path;
	private boolean append = false;

	public WriteFile1(String file_path, boolean append_val) {
		path = file_path;
		append = append_val;
	}

	// This method writes to the text file.
	public void writeToFile(String textLine) throws IOException {

		FileWriter write = new FileWriter(path, append);
		PrintWriter print_line = new PrintWriter(write);

		printFileContent(textLine, print_line);

		print_line.close();

	}

	// This method prints the specified line to the text file.
	private void printFileContent(String textLine, PrintWriter print_line) {
		print_line.printf("%s" + "%n", textLine);
	}
}


