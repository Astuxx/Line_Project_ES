import java.io.*;
import java.lang.*;
import java.lang.Math;
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
        //System.out.println(CompleteString);
       
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
        x = x.replaceAll("[\\‘\\‘]", "\'");
        x = x.replaceAll("[\\“\\”]", "\"");
        return x;
    }

    public static String matchString (String x) {
        //regex for a good end line
        String R1 = "([a-z])$";//R1 AND R2
        String R2 = "^([a-z])";
        String R3 = "\\[0-9]+$"; //R3 AND R4 
        String R4 = "^[A-Z,a-z]{2,100}";
        String R5 = "[a-z]$"; // R5 and R6
        String R6 = "^\\b+";

        String R7 = "[\\,\\]]$"; //single
        String R8 = "[\\-?]$";

        String R9 = "^[a-z]+\\."; //points listed
        String R10 = "^[0-9]+\\. [a-z,A-Z]"; //points listed
        String R11 = "^[a-z]+\\)";  //points listed
        String R15 = "^\\([a-z]+\\)"; //points listed
        String R18 = "\\:$";

        String R12 ="([a-z,A-Z])(\\-)([ ,\n])([a-z,A-Z])"; //word- word --> word-word 
        String R13 = "^(Page)( )([0-9]+)";
        String R14 = "^\\([a-z]+";
        String R16 = "\\b[A-Z](\\w+)$"; //last word of a line ends with a Uppercase word
        String R17 = "^\\b[A-Z](\\w+)";  
        //String R18 = "^\\([a-z]+"; 
        String R19 = "^[a-z]+"; //riga inizia con parola minuscola
        String R20 = "^\\([A-Z,a-z]+ ";

        //String R21 = "[0-9]+$";
        //String R22 = "^\\b[A-Z][a-z]+";

        /*String R23 = "\\([\\w ]+$";
        String R24 = "^[\\w ]+\\)"; 

        String R25 = "\\b[A-Z]{2,100}$";
        String R26 = "^\\b[A-Z]{2,100}";*/

        ArrayList<Pattern> RegexDouble = new ArrayList<Pattern>();
        RegexDouble.add(Pattern.compile(R1));
        RegexDouble.add(Pattern.compile(R2));
        RegexDouble.add(Pattern.compile(R3));
        RegexDouble.add(Pattern.compile(R4));
        RegexDouble.add(Pattern.compile(R5));
        RegexDouble.add(Pattern.compile(R6));
        //RegexDouble.add(Pattern.compile(R21));
        //RegexDouble.add(Pattern.compile(R22));
        /*RegexDouble.add(Pattern.compile(R23));
        RegexDouble.add(Pattern.compile(R24));
        RegexDouble.add(Pattern.compile(R25));
        RegexDouble.add(Pattern.compile(R26));*/

        ArrayList<Pattern> RegexSingle = new ArrayList<Pattern>();
        RegexSingle.add(Pattern.compile(R7));
        RegexSingle.add(Pattern.compile(R8));
        RegexSingle.add(Pattern.compile(R14));
        RegexSingle.add(Pattern.compile(R17));
        RegexSingle.add(Pattern.compile(R19));
        RegexSingle.add(Pattern.compile(R20));
  
        //regex for lines that should not be pulled up
        ArrayList<Pattern> RegexSingleNegative = new ArrayList<Pattern>();
        RegexSingleNegative.add(Pattern.compile(R9));
        RegexSingleNegative.add(Pattern.compile(R10));
        RegexSingleNegative.add(Pattern.compile(R11));
        RegexSingleNegative.add(Pattern.compile(R13));
        RegexSingleNegative.add(Pattern.compile(R15));
        //RegexSingleNegative.add(Pattern.compile(R18));

        //int count = 0;
        String lines[] = x.split("\\n"); //split line and save the single string without '\n'
        int size = lines.length;

        Integer[] check = new Integer[size]; 
        /*
        -1 = non tocco la riga
        0 = non faccio nulla 
        1 = tiro sula riga con uno spazio
        2 = tiro su la riga con nessuno spazio
        */

        for (int i=0; i<size; i++) { //set 0 all position
            check[i] = 0;
        }

        for (int i = 0; i<size-1; i++) { //so I don't look at the bulleted lists
            Matcher r = RegexSingleNegative.get(0).matcher(lines[i]);
            Matcher s = RegexSingleNegative.get(1).matcher(lines[i]);
            Matcher t = RegexSingleNegative.get(2).matcher(lines[i]);
            Matcher u = RegexSingleNegative.get(3).matcher(lines[i]);
            Matcher z = RegexSingleNegative.get(4).matcher(lines[i]);
            //Matcher a = RegexSingleNegative.get(5).matcher(lines[i]);
            if (r.find() || s.find() || t.find() || u.find() || z.find() ) {
            check[i] = -1; //
                }

           if (lines[i].charAt(lines[i].length() -1 ) != '.' && lines[i].length()<=50 ) {
                Matcher e = (Pattern.compile(R16)).matcher(lines[i]);
                Matcher y = (Pattern.compile(R17)).matcher(lines[i+1]);
    
                if (e.find() && y.find()) {
                    check[i+1] = -1;
                }
            }

            Matcher l = (Pattern.compile(R18)).matcher(lines[i]);
            if (l.find()) {
                check[i+1] = -1;
            }
        }

        //find regex in text
        for (int j = 0; j<RegexDouble.size(); j+=2) { //Pattern double
            for (int i = 0; i<size-1; i++) {
                if (check[i+1]==0) {
                    Matcher t = RegexDouble.get(j).matcher(lines[i]); //find regex(part 1) in line i
                    Matcher y = RegexDouble.get(j+1).matcher(lines[i+1]);//find regex(part 2) in the next line
                        if ( t.find() && y.find()) {
                            check[i+1] = 1;
                        }

                    //Pattern single
                    Matcher z = RegexSingle.get(0).matcher(lines[i]); //Regex R7
                    if (z.find()) {
                        check[i+1] = 1;
                        }

                    Matcher w = RegexSingle.get(1).matcher(lines[i]); //Regex R8
                    if (w.find()) {
                        check[i+1] = 2; //delete a space and pulled up the line i+1
                        }
                        
                    if (check[i]!=-1) {
                        Matcher d = RegexSingle.get(2).matcher(lines[i]); //Regex 14
                        if (d.find()) {
                            check[i] = 1; //delete a space and pulled up the line i+1
                            }
                    }

                    /*Matcher g = (Pattern.compile("[A-Z]+$")).matcher(lines[i-1]);
                    Matcher f = RegexSingle.get(4).matcher(lines[i]); //Regex R8
                    if (f.find() && !(g.find()) && check[i]!=-1) {
                        check[i] = 1; //delete a space and pull up the line i+1
                        }*/

                    //replace a particular case
                    lines[i] = lines[i].replaceAll(R12, "$1$2$4");

                    /*Matcher r = RegexSingle.get(3).matcher(lines[i]);
                    if(r.find() && lines[i].length()<50 && lines[i].charAt(length[size-1])!="."){
                        check[i] = -2;
                    }*/
                
                }// end if check line == 0

                Matcher f = RegexSingle.get(4).matcher(lines[i]); //Regex R19
                    if (f.find() && check[i]!=-1) {
                        check[i] = 1;
                    }

                Matcher m = RegexSingle.get(5).matcher(lines[i]); //Regex R20
                    if (m.find()) {
                        check[i] = 1; //delete a space and pulled up the line i+1
                        }

            if(j == 8) {
                Matcher t = RegexDouble.get(j).matcher(lines[i]); //find regex(part 1) in line i
                Matcher y = RegexDouble.get(j+1).matcher(lines[i+1]);//find regex(part 2) in the next line
                    if ( t.find() && y.find()) {
                        check[i+1] = 1;
                    }
            }
            }//end for line 
        }//end regex   
        
        //Prima di tirar su le righe, controllo se altre possono essere tirate su!
        
        for (int i = 0; i<size; i++) {
            if (check[i] == 0 ) { //if this line should not be pulled up
                if(i>=5 && i<=size-5) {
                    if (lines[i].length()>=90) {
                        int val = 0;
                        for (int j = i-4; j<=i+5; j++) {
                            //if ((lines[i].length() >= lines[j].length()-1) && (lines[i].length() <= lines[j].length()+1)) {
                              if ((Math.abs(lines[i].length()-lines[j].length()))<=5){ //se la differenza di caratteri tra una riga e le successive è max 10
                                val+=1;
                            }
                        }//end for

                        if (val>=8) {
                            check[i] = 1; //pulled up line 
                        }   
                    }
                }
            }
        } 
        
        /*for (int i = 0; i<size; i++) { //controllo stringhe marcate con 0 e -1
            if(check[i]==0){
                System.out.println("LINEA MARCATA CON 0, riga numero       "+i+" "+lines[i]+"\n");
            }
        }
        for (int i = 0; i<size; i++) {
            if(check[i]==-1){
                System.out.println("LINEA MARCATA CON -1, riga numero      "+i+" "+lines[i]+"\n");
            }
        }*/

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