import java.io.*;
import java.util.regex.Pattern;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.*;

public class Prova {

    public static void main(String Args[]) {

        // The name of file or path
        String fileName = "AAMedP-1 EDA V1 E.txt";
        //String fileName = "Prova.txt";
        String line = null; //line read
        String CompleteString = null;

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
                line = line.trim(); //da aggiugnere
                if (line.length()!=0) { //check if the line is empty
                    sb.append(line).append("\n"); //append the line
                }
                line = br.readLine(); //read new line prof file
            }
            //System.out.println(sb);
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
        //System.out.println(CompleteString);

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
        //System.out.println(CompleteString);
        CompleteString = matchString(CompleteString);
        System.out.println(CompleteString);
       
        //volendo si può fare l'eliminazione degli spazi con ^ e $ però la regex deve essere multi-line
        //CompleteString = CompleteString.replaceAll("(\\w)(\\n)(\\w)", "$1 $3");//delte \n betwenn two line with no dot
        //CompleteString = CompleteString.replaceAll("\\.{2,100}", "\n"); //delete the many dot in the index of text (DA TOGLIERE!)
        //System.out.println(CompleteString);

    }

    public static String TextCleaning (String x ) {

        x = x.replaceAll("[ \t]+"," ");//delete tabulations, \t
        x = x.replaceAll(" \n","\n");//delete space before \n
        x = x.replaceAll("(\\$)START_PAGE_(\\d+)","");//delete START_PAGE_n patter
        x = x.replaceAll("(\\$)END_PAGE_(\\d+)","");//delete END_PAGE_n pattern
        x = x.replaceAll("(?m)^\\s", ""); //delete empty line (?m == probabile attivazione multi-line)
        x = x.replaceAll("^[ ]","");//delete last empty line

        //System.out.println("Sono qua");
        //System.out.println(x);
        return x;
    }

    /*public static String UnionOfString (String x) {

        //create String vector to split string of text
        String lines[] = x.split("\\r?\\n"); //split line and save the single string without '\n'
        int size = lines.length;
        //System.out.println("N° string: " + size);
        //array of boolean to define if a line can be pulled up
        Boolean[] check = new Boolean[size];
        for (int i=0; i<size; i++) { //set a false all position
            check[i] = false;
            //System.out.println(check[i]);
        }
        int count = 0;
        int count2 = 0;
        //analyze all string
        for (int i = 0; i<size-1; i++) {
            String a = lines[i] + "\n" + lines[i+1]; //create a string to find the pattern
            //cases with a lower case word or ')' or ',' at the end of line, \n and after a lower case word
            Pattern p = Pattern.compile("(([[:lower:])?,?])[\r\n]+([[:lower:]1-9][^.)]))"); 
            Matcher m = p.matcher(a);

            Pattern q = Pattern.compile("((^[\\w \t[:punct:]]{50,}\b[[:lower:]]+)[\r\n]+([A-Z][[:lower:]]+\b))"); 
            Matcher n = q.matcher(a);
            
            Pattern r = Pattern.compile("((\b\\w+),[\r\n]+(\\w+\b))"); 
            Matcher o = r.matcher(a);

            count2 += 1 ;

            if ( m.find() || n.find() || o.find() ) {
                //System.out.println("Entro");
                count+=1;
                check[i+1] = true;
            }
        }//fine for

        //System.out.println("count2 "+count2); //953

        System.out.println("Entro " + count + " volte");

        String ret = lines[0]; //String output

        for (int i = 0; i<size; i++) {
            if(check[i]==true) {
                ret = ret + " " +  lines[i];
            }
            else {
                ret = ret + "\n" + lines[i];
            }
        }
        
        System.out.println(ret);
        return ret;
    }*/

    public static String matchString (String x) {
        //regex for a good end line
        String R1 = "([a-z]+)([\\t]+)?$";//con cambia nulla senza lo \\t
        String R2 = "^([a-z]+)";
        int count = 0;

        String lines[] = x.split("\\n"); //split line and save the single string without '\n'
        int size = lines.length;

        Boolean[] check = new Boolean[size];
        for (int i=0; i<size; i++) { //set a false all position
            check[i] = false;
            System.out.println(lines[i]);
        }

        for (int i = 0; i<size-1; i++) {
            //String a = lines[i]; //+ "\n" + lines[i+1]; //create a string to find the pattern
            //String b = lines[i+1];
            
            Pattern p = Pattern.compile(R1);
            Matcher n = p.matcher(lines[i]); 
            
            Pattern q = Pattern.compile(R2);
            Matcher o = q.matcher(lines[i+1]);
            
            if ( n.find() || o.find() ) {
                count+=1;
                System.out.println(i+1);
                check[i+1] = true;
            }
        }    
        //System.out.println(count); //anche senza \\t stampa sempre quel valore

        String ret = lines[0];

        for (int i = 1; i<size; i++) {
            if(check[i]==true) {
                ret = ret + " " +  lines[i];
            }
            else {
                ret = ret + "\n" + lines[i];
            }
        }
        return ret;
    }
}
