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
        String fileName = "Prova.txt";
        String line = null; //line read
        String stringacomplete = null;

        //create array list to save all string readed
        ArrayList<String> arrlist = new ArrayList<String>();

        try {
            // read file as String in Java SE 6 and lower version
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();

            line = br.readLine();
            while (line != null) {
            if (stringacomplete.charAt(0) == ' ') {
                stringacomplete = stringacomplete.replaceFirst("^  *", "");
            }

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
        
        line = stringacomplete.replaceAll("[ \t]+"," ");
        line = stringacomplete.replaceAll("^[ ]","").trim();
        System.out.println(stringacomplete);

    }
}
