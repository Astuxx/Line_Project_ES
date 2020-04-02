import java.awt.*;
import java.io.*;
import java.lang.*;

import java.util.*;
import java.io.IOException;

import java.io.FileReader;
import java.io.File;

public class Line extends Component {

    public Line(File file) {
        this.fileName = file;
    }

    public File fileName;

    public static void main(File fileName) throws IOException {

        System.out.println("processing file "+fileName.getName());
        String line = null; //line red
        String CompleteString = null;
        try { //read input file
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();

            line = br.readLine();

            while (line == null) {
                line = br.readLine();
            }

            while (line != null) { //delete multiple empty space in a single line
                line = line.replaceFirst("^  *", "");
                line = line.trim();
                if (line.length()!=0) { //check if the line is empty
                    sb.append(line).append("\n"); //append the line
                }
                line = br.readLine(); //read new line prof file
            }
            CompleteString = new String(sb);
            br.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +/*fileName +*/ "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '" + /*fileName +*/ "'");
        }

        CompleteString = LineFunction.TextCleaning(CompleteString);
        CompleteString = LineFunction.matchString(CompleteString);

        LineJson.CallJSon(CompleteString,fileName);
    }
}
