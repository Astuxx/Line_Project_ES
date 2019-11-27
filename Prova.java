import java.io.*;
import java.util.regex.Pattern;
import java.util.*;
import java.io.File;
import java.io.IOException;
//import java.util.regex;

public class Prova {

    public static void main(String Args[]) {
        //System.out.println("Ciao");
        String regexBlankLine = "^[ \\t]$+";
        String regexMultiSpace = "^[ \\t]+";

        // The name of file or path
        //String fileName = "nato_documents/txts/AAMedP-1 EDA V1 E.txt";
        String fileName = "AAMedP-1 EDA V1 E.txt";
        String line = null; //line read
        String stringacomplete = null;

        //create array list to save all string readed
        ArrayList<String> arrlist = new ArrayList<String>();

        try {
            // read file as String in Java SE 6 and lower version
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();

            line = br.readLine();
            //System.out.println(line);
            while (line == null) {
                line = br.readLine();
            }
            while (line != null) {
                line = line.replaceFirst("^  *", "");
                sb.append(line).append("\n");
                line = br.readLine();
            }
            System.out.println(sb);
            stringacomplete = new String(sb);

            // Close file
            br.close();
        }
        //catch eventually error generate
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" +fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" + fileName + "'");
        }

        System.out.println("Prova stampa");
        System.out.println(stringacomplete);

        /*Pattern p = Pattern.compile(regexBlankLine, Pattern.MULTILINE);
        System.out.println("FIRST REMOVE \n");
        System.out.println(p.matcher(stringacomplete).replaceAll("")); //ok works

        p = Pattern.compile(regexMultiSpace);
        System.out.println("SECOND REMOVE \n");
        System.out.println(p.matcher(stringacomplete).replaceAll("")); //still eat \n
        */

        stringacomplete = stringacomplete.replaceAll("[ \t]+"," ");//delete tabulations, \t
        //stringacomplete = stringacomplete.replaceAll("(\\w)(\\n)(\\w)", "$1 $3");
        stringacomplete = stringacomplete.replaceAll(" \n","\n");//delete space before \n
        stringacomplete = stringacomplete.replaceAll("(\\w)(\\n)(\\w)", "$1 $3");//delte \n betwenn two line with no dot
        stringacomplete = stringacomplete.replaceAll("(\\$)START_PAGE_(\\d+) ","");//delete START_PAGE_n patter
        stringacomplete = stringacomplete.replaceAll("(\\$)END_PAGE_(\\d+)","");//delete END_PAGE_n pattern
        stringacomplete = stringacomplete.replaceAll("(?m)^\\s", ""); //delete empty line
        stringacomplete = stringacomplete.replaceAll("\\.{2,100}", "\n"); //delete the many dot in the index of text
        stringacomplete = stringacomplete.replaceAll("^[ ]","").trim();//delete last empty line
        System.out.println(stringacomplete);

    }
}
