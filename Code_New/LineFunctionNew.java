import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineFunction {
    public static String TextCleaning (String x ) { //to roughly clean the text

        x = x.replaceAll("[ \t]+"," ");//delete tabulations, \t
        x = x.replaceAll(" \n","\n");//delete space before \n
        x = x.replaceAll("(\\$)START_PAGE_(\\d+)","");//delete START_PAGE_n patter
        x = x.replaceAll("(\\$)END_PAGE_(\\d+)","");//delete END_PAGE_n pattern
        x = x.replaceAll("(?m)^\\s", ""); //delete empty line (?m == probabile attivazione multi-line)
        x = x.replaceAll("^[ ]","");//delete last empty line
        x = x.replaceAll("[\\‘\\‘]", "\'");
        x = x.replaceAll("[\\“\\”]", "\"");
        return x; //return a string
    }

    public static String matchString (String x) throws IOException { //use of the regex to go up
        // the target and high frequency lines in the texts

        //ReGeXSingle
        String R1 = "[\\,\\]]$"; //single
        String R2 = "[\\-?]$"; 
        String R3 = "^\\([a-z]+";
        String R4 = "^\\b[A-Z](\\w+)";
        String R5 = "^[a-z]+"; //riga inizia con parola minuscola
        String R6 = "^\\([A-Z,a-z]+ ";

        //ReGeXSingleNegative
        String R7 = "^[a-z]\\."; //points listed
        String R8 = "^[0-9]+\\. [a-z,A-Z]"; //points listed
        String R9 = "^[a-z]+\\)";  //points listed
        String R10 = "^(Page)( )([0-9]+)";
        String R11 = "^\\([a-z]+\\)"; //points listed
        String R12 = "^[0-9][\\.,0-9]+ [a-z,A-Z]";//points listed (ex. number.number.number)

        //RegexDouble
        String R13 = "([a-z]+)$";//R13 AND R14
        String R14 = "^([a-z]+)";
        String R15 = "\\[0-9]+$"; //R15 AND R16
        String R16 = "^[A-Z,a-z]{2,100}";
        String R17 = "[a-z]$"; // R17 and R18
        String R18 = "^\\d+";

        //create ad array with single regex
        ArrayList<Pattern> RegexSingle = new ArrayList<Pattern>();
        RegexSingle.add(Pattern.compile(R1));
        RegexSingle.add(Pattern.compile(R2));
        RegexSingle.add(Pattern.compile(R3));
        RegexSingle.add(Pattern.compile(R4));
        RegexSingle.add(Pattern.compile(R5));
        RegexSingle.add(Pattern.compile(R6));

        //regex for lines that should not be pulled up
        ArrayList<Pattern> RegexSingleNegative = new ArrayList<Pattern>();
        RegexSingleNegative.add(Pattern.compile(R7));
        RegexSingleNegative.add(Pattern.compile(R8));
        RegexSingleNegative.add(Pattern.compile(R9));
        RegexSingleNegative.add(Pattern.compile(R10));
        RegexSingleNegative.add(Pattern.compile(R11));
        RegexSingleNegative.add(Pattern.compile(R12));

        //create ad array with double regex
        ArrayList<Pattern> RegexDouble = new ArrayList<Pattern>();
        RegexDouble.add(Pattern.compile(R13));
        RegexDouble.add(Pattern.compile(R14));
        RegexDouble.add(Pattern.compile(R15));
        RegexDouble.add(Pattern.compile(R16));
        RegexDouble.add(Pattern.compile(R17));
        RegexDouble.add(Pattern.compile(R18));

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

        //Single Negative ReGeX
        for (int i = 0; i<size-1; i++) { //so I don't look at the bulleted lists
            Matcher r = RegexSingleNegative.get(0).matcher(lines[i]);
            Matcher s = RegexSingleNegative.get(1).matcher(lines[i]);
            Matcher t = RegexSingleNegative.get(2).matcher(lines[i]);
            Matcher u = RegexSingleNegative.get(3).matcher(lines[i]);
            Matcher z = RegexSingleNegative.get(4).matcher(lines[i]);
            Matcher w = RegexSingleNegative.get(5).matcher(lines[i]);
            if (r.find() || s.find() || t.find() || u.find() || z.find() || w.find() ) {
                check[i] = -1;
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

            if (lines[i].charAt(lines[i].length() -1) == '.') {
                check[i+1] = -1;
            }

        }//end for

        //find ReGeX in text
        for (int j = 0; j<RegexDouble.size(); j+=2) { //Pattern double
            for (int i = 0; i<size-1; i++) {
                if (check[i+1]==0) {
                    Matcher t = RegexDouble.get(j).matcher(lines[i]); //find regex(part 1) in line i
                    Matcher y = RegexDouble.get(j+1).matcher(lines[i+1]);//find regex(part 2) in the next line
                    if ( t.find() && y.find()) {
                        check[i+1] = 1;
                    }
                }
            }
        }

        //Single ReGeX
        for (int i = 0; i<size-1; i++) {
            Matcher p = Pattern.compile("^[A-Z,a-z]+ [A-Z,a-z]+$").matcher(lines[i+1]); //find regex(part 1) in line i
            Matcher q = Pattern.compile("^[A-Z,a-z]+$").matcher(lines[i+1]); //find regex(part 1) in line i+1
            Matcher r = Pattern.compile("^[A-Z,a-z]+ [A-Z,a-z]+$").matcher(lines[i]);
            Matcher s = Pattern.compile("^[A-Z,a-z]+$").matcher(lines[i]);
            if ( (r.find() && ( p.find() || q.find() ))  || (s.find() && (p.find() || q.find())) ) {
                check[i+1] = 1;
            }

            if (check[i] != -1) {
                //Pattern single
                Matcher z = RegexSingle.get(0).matcher(lines[i]); //Regex R7
                if (z.find()) {
                    check[i+1] = 1;
                }

                Matcher w = RegexSingle.get(1).matcher(lines[i]); //Regex R8
                if (w.find()) {
                    check[i+1] = 2; //delete a space and pulled up the line i+1
                }

                Matcher d = RegexSingle.get(2).matcher(lines[i]); //Regex 14
                if (d.find()) {
                    check[i] = 1; //delete a space and pulled up the line i+1
                }
            }
            //replace a particular case
            lines[i] = lines[i].replaceAll(R12, "$1$2$4");

            Matcher f = RegexSingle.get(4).matcher(lines[i]); //Regex R19
            if (f.find() && check[i]!=-1) {
                check[i] = 1;
            }

            Matcher m = RegexSingle.get(5).matcher(lines[i]); //Regex R20
            if (m.find()) {
                check[i] = 1; //delete a space and pulled up the line i+1
            }

            if (check[i]!=-1) {
                Matcher t = RegexDouble.get(0).matcher(lines[i]); //find regex(part 1) in line i
                Matcher y = RegexDouble.get(1).matcher(lines[i+1]);//find regex(part 2) in the next line
                if(t.find() && y.find()) {
                    check[i+1] = 1;
                }
            }
            if (check[i+1] != -1) {
                if (lines[i+1].charAt(lines[i+1].length() -1 ) == '.' && lines[i].charAt(lines[i].length() -1 ) != '.') {
                    check[i+1] = 1;
                }
            }
            if (lines[i].charAt(0)== '"') {
                check[i] = 1;
            }
        }//end for Single ReGeX

        for (int i = 0; i<size; i++) {
            if (check[i] == 0 ) { //if this line should not be pulled up
                if(i>=5 && i<=size-5) {
                    if (lines[i].length()>=90) {
                        int val = 0;
                        for (int j = i-4; j<=i+4; j++) {
                            if ((Math.abs(lines[i].length()-lines[j].length()))<=5){ //se la differenza di caratteri tra una riga e le successive è max 10
                                val+=1;
                            }
                        }//end for

                        if (val>=7) {
                            check[i] = 1; //pulled up line
                        }
                    }
                }
            }
        }

        String ret = lines[0]; //String to return

        //create return string
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

        try { // write a file with return string
            BufferedWriter writer = new BufferedWriter(new FileWriter("Line_out.txt"));
            writer.write(ret);
            writer.close();
        }
        //catch eventually error generate
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" + "Line_out.txt" + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '" + "Line_out.txt" + "'");
        }

        return ret; //string after pre-processing
    }
}
