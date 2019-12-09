import java.io.*;
import java.util.regex.Pattern;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.*;

public class Prova {

    public static void main(String Args[]) {

        // The name of file or path
        String fileName = Args[0];
         //"AAMedP-1.1 EdA v1 E.txt";
        //String fileName = "Prova.txt";
        String line = null; //line read
        String CompleteString = null;
        int j = 0;

        try {
            // read file as String in Java SE 6 and lower version
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();

            line = br.readLine();
             
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
        return x;
    }

    public static String matchString (String x) {
        //regex for a good end line
        String R1 = "([a-z])$";//R1 AND R2
        String R2 = "^([a-z])";
        String R3 = "\\b$";
        String R4 = "^[a-z]{2,100}";
        String R5 = "[a-z]$";
        String R6 = "^\\b+";
        String R7 = "[\\,?\\)?\\;?]$"; //single
        String R8 = "[\\-?]$";
        String R9 = "^[a-z]\\."; //points listed
        String R10 = "^[0-9]+\\."; //points listed
        
        ArrayList<Pattern> RegexDouble = new ArrayList<Pattern>();
        RegexDouble.add(Pattern.compile(R1));
        RegexDouble.add(Pattern.compile(R2));
        RegexDouble.add(Pattern.compile(R3));
        RegexDouble.add(Pattern.compile(R4));
        RegexDouble.add(Pattern.compile(R5));
        RegexDouble.add(Pattern.compile(R6));

        ArrayList<Pattern> RegexSingle = new ArrayList<Pattern>();
        RegexSingle.add(Pattern.compile(R7));
        RegexSingle.add(Pattern.compile(R9));
  
        //regex for lines that should not be pulled up
        ArrayList<Pattern> RegexSingleNegative = new ArrayList<Pattern>();
        RegexSingleNegative.add(Pattern.compile(R9));
        RegexSingleNegative.add(Pattern.compile(R10));

        //int count = 0;
        String lines[] = x.split("\\n"); //split line and save the single string without '\n'
        int size = lines.length;

        Integer[] check = new Integer[size]; 
        /*
        0 = non faccio nulla
        1 = tiro sula riga con uno spazio
        2 = tiro su la riga con nessuno spazio
        */
        for (int i=0; i<size; i++) { //set 0 all position
            check[i] = 0;
        }

        for (int i = 0; i<size; i++) { //so I don't look at the bulleted lists
            Matcher r = RegexSingleNegative.get(0).matcher(lines[i]);
            Matcher s = RegexSingleNegative.get(1).matcher(lines[i]);
            if (r.find() || s.find()) {
            check[i] = -1; //
                }
        }

        //find regex in text
        for (int j = 0; j<RegexDouble.size(); j+=2) { //Patter double
            for (int i = 0; i<size-1; i++) {
                if (check[i+1]==0) {
                    Matcher t = RegexDouble.get(j).matcher(lines[i]); //find regex(part 1) in line i
                    Matcher y = RegexDouble.get(j+1).matcher(lines[i+1]);//find regex(part 2) in the next line
                        if ( t.find() && y.find()) {
                            check[i+1] = 1;
                        }

                //Patter single
                Matcher z = RegexSingle.get(0).matcher(lines[i]);
                if (z.find()) {
                check[i+1] = 1;
                    }

                Matcher w = RegexSingle.get(1).matcher(lines[i]);
                if (w.find()) {
                check[i+1] = 2; //delete a space and pulled up the line i+1
                    }
                
                }// end if check line == 0

            }//end for line 
        }    

        //Prima di tirar su le righe, controllo se altre possono essere tirate su!
        for (int i = 0; i<size; i++) {
            if (check[i] == 0) { //if this line should not be pulled up
                if(i>=1 && i<=size-5) {
                    if (lines[i].length()>=90) {
                        int val = 0;
                        for (int j = i+1; j<=i+5; j++) {
                            if ((lines[i].length() >= lines[j].length()-1) && (lines[i].length() <= lines[j].length()+1)) {
                                val+=1;
                            }
                        }//end for

                        for (int j = i-1; j>=i-5; j--) {
                            if ((lines[i].length() >= lines[j].length()-1) && (lines[i].length() <= lines[j].length()+1)) {
                                val+=1;
                            }
                        }//end for

                        if (val>=2) {
                            check[i] = 1;
                        }   
                    }
                }
            }
        }

        String ret = lines[0]; //String to return

        for (int i = 1; i<size; i++) {
            if(check[i]==1) {
                ret = ret + " " +  lines[i];
            }
            else if(check[i]==2) {
                ret = ret + lines[i];
            }
            else {
                ret = ret + "\n" + lines[i];
            }
        }
        //print in a file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Prova_out.txt"));
            writer.write(ret);
        }
         //catch eventually error generate
         catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + "Prova_out.txt" + "'");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" + "Prova_out.txt" + "'");
        }

        return ret;
    }
}