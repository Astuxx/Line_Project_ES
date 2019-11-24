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
            // FileReader to read file with the default endline ("\n")
            FileReader fileReader = new FileReader(fileName);

            // Wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //int i = 0;
            while((line = bufferedReader.readLine()) != null) {
                stringacomplete=stringacomplete+line;
                arrlist.add(line);
            }

            // Close file
            bufferedReader.close();
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
        
        Pattern p = Pattern.compile(regexBlankLine, Pattern.MULTILINE);
        System.out.println("FIRST REMOVE \n");
        System.out.println(p.matcher(stringacomplete).replaceAll("")); //ok works
        
        p = Pattern.compile(regexMultiSpace);
        System.out.println("SECOND REMOVE \n");
        System.out.println(p.matcher(stringacomplete).replaceAll("")); //still eat \n

    }
}
