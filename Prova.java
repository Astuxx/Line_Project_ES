import java.io.*;
import java.util.regex.Pattern;
import java.util.*;
import java.io.File;
import java.io.IOException;
//import java.util.regex;

public class Prova {

    public static void main(String Args[]) {
        //System.out.println("Ciao");
        //String regexBlankLine = "^[ \\t]$+";
        //String regexMultiSpace = "^[ \\t]+";

        // The name of file or path
        //String fileName = "nato_documents/txts/AAMedP-1 EDA V1 E.txt";
        String fileName = "ANEP-MNEP-86 EDA V1.txt";
        String line = null; //line read
        String CompleteString = null;

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
                //line = line.replaceFirst("^  *", "");
                line = line.trim(); //da aggiugnere
                if (line.length()!=0) { //check if the line is empty
                    sb.append(line).append("\n"); //append the line
                }
                line = br.readLine(); //read new line prof file
            }
            System.out.println(sb);
            CompleteString = new String(sb);

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

        System.out.println("PROVA STAMPA");
        System.out.println(CompleteString);

        /*Pattern p = Pattern.compile(regexBlankLine, Pattern.MULTILINE);
        System.out.println("FIRST REMOVE \n");
        System.out.println(p.matcher(CompleteString).replaceAll("")); //ok works

        p = Pattern.compile(regexMultiSpace);
        System.out.println("SECOND REMOVE \n");
        System.out.println(p.matcher(CompleteString).replaceAll("")); //still eat \n
        */

        System.out.println("");
        System.out.println("STAMPA DOPO LE MODIFICHE");
        System.out.println("");

        
        //stringacomplete = stringacomplete.replaceAll("(\\w)(\\n)(\\w)", "$1 $3");

        CompleteString = TextCleaning(CompleteString);
       
        //volendo si può fare l'eliminazione degli spazi con ^ e $ però la regex deve essere multi-line
        CompleteString = CompleteString.replaceAll("(\\w)(\\n)(\\w)", "$1 $3");//delte \n betwenn two line with no dot
        CompleteString = CompleteString.replaceAll("\\.{2,100}", "\n"); //delete the many dot in the index of text (DA TOGLIERE!)
        System.out.println(CompleteString);

    }

    public static String TextCleaning (String x ) {
        x = x.replaceAll("[ \t]+"," ");//delete tabulations, \t
        x = x.replaceAll(" \n","\n");//delete space before \n
        x = x.replaceAll("(\\$)START_PAGE_(\\d+) ","");//delete START_PAGE_n patter
        x = x.replaceAll("(\\$)END_PAGE_(\\d+)","");//delete END_PAGE_n pattern
        x = x.replaceAll("(?m)^\\s", ""); //delete empty line (?m == probabile attivazione multi-
        //line)
        x = x.replaceAll("^[ ]","").trim();//delete last empty line

        return x;
    }
}
