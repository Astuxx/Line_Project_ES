import java.io.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
//import java.util.regex;

public class Prova {

    public static void main(String Args[]) {
        //System.out.println("Ciao");
        String regexStr = " ";

        // The name of file or path
        //String fileName = "nato_documents/txts/AAMedP-1 EDA V1 E.txt";
        String fileName = "Prova.txt";
        String line = null; //line read

        //create array list to save all string readed
        ArrayList<String> arrlist = new ArrayList<String>();

        try {
            // FileReader to read file with the default endline ("\n")
            FileReader fileReader = new FileReader(fileName);

            // Wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //int i = 0;
            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                arrlist.add(line);
                /*i++;
                if (i==10) {
                    break;
                }*/
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
        //end

        String pattern = "\\s+";
        int i = 0;
        for (String str : arrlist) {
            arrlist.set(arrlist.indexOf(str), str.replaceAll("\\s+"," "));

            //System.out.println(str);
            if(str.isEmpty() ){
                //arrlist.set(arrlist.indexOf(str), str.replaceAll("([\\n\\r]+\\s*)*$",""));
                arrlist.remove(str);
                //System.out.println(i);
            }
            //System.out.println(str);
            i = i+1;
        }


        int pos = 0;
        int lunghezza = arrlist.size();
        System.out.println(lunghezza);
        for ( i =0; i<lunghezza; i++) {
            System.out.println(arrlist.get(i).substring(arrlist.get(i).length()-1));

            if(!arrlist.get(i).substring(arrlist.get(i).length()-1).matches("\\.") && arrlist.get(i+1).substring(0,1).matches("[a-z_0-9]") && i<(lunghezza) ){
                arrlist.set(pos, arrlist.get(i)+" "+arrlist.get(i+1));
                arrlist.remove(arrlist.indexOf(pos+1));
            }
        }

        System.out.println("***********");
        //System.out.println("\n");

        for (String str : arrlist) {
            System.out.println(str);
        }
    }
}
