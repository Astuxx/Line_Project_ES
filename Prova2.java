import java.awt.*;
import java.io.*;
import java.lang.*;
import java.lang.Math;
import java.util.regex.Pattern;

import java.util.*;
import java.io.IOException;
import java.util.regex.*;

import java.net.*;

//for json
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.File;
import javax.swing.JFileChooser;



public class Prova2 extends Component {

    public Prova2(File file) {
        this.fileName = file;
    }

    public static float ShortVerbString = 0;
    public static float LongVerbString = 0;
    public static float ShortNVerbString = 0;
    public static float LongNVerbString = 0;


    public File fileName;

    public static void main(File fileName) throws IOException {

        System.out.println("processing file "+fileName.getName());
        String line = null; //line red
        String CompleteString = null;
        //String fileName = "8196027822.txt";
        try {
            // read file as String in Java SE 6 and lower version
            //BufferedReader br = new BufferedReader(new FileReader(file));"8196027822.txt"));
            /*
            JFileChooser fileChooser = new JFileChooser();
            int n = fileChooser.showOpenDialog(Prova2.this);
            //File files[] = fileChooser.getSelectedFiles();
            File file = fileChooser.getSelectedFile();*/

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
                    "Unable to open file '" +/*fileName +*/ "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '" + /*fileName +*/ "'");
        }

        //System.out.println("");
        //System.out.println("STAMPA DOPO LE MODIFICHE");
        //System.out.println("");

        //stringacomplete = stringacomplete.replaceAll("(\\w)(\\n)(\\w)", "$1 $3");

        CompleteString = TextCleaning(CompleteString);
        //System.out.println(CompleteString);
        CompleteString = matchString(CompleteString);

        CallJSon(CompleteString,fileName);
        //System.out.println(CompleteString);

        //volendo si può fare l'eliminazione degli spazi con ^ e $ però la regex deve essere multi-line
        //CompleteString = CompleteString.replaceAll("(\\w)(\\n)(\\w)", "$1 $3");//delte \n betwenn two line with no dot
        //CompleteString = CompleteString.replaceAll("\\.{2,100}", "\n"); //delete the many dot in the index of text (DA TOGLIERE!)
        //System.out.println(CompleteString);
    }

    public static void CallJSon(String x, File Nome) throws IOException {
        //Create the request body
        JSONObject obj = new JSONObject(); //Create a Json object

        obj.put("jsonrpc", "2.0");
        obj.put("method", "analyzetext");
        obj.put("id", "1");

        JSONObject params = new JSONObject();//Create a new Json object to add at the first object
        params.put("package", "base_en");
        params.put("data", x); //add the string of text

        obj.put("params", params);

        File file = new File("request.json");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.print(obj); stampa corretta

        URL url = new URL("http://157.27.193.120:6090/essex/json-rpc");
        //URL url = new URL("http://192.168.1.226:6090/essex/json-rpc");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        try {
            JSONParser parser = new JSONParser();
            //Use JSONObject for simple JSON and JSONArray for array of JSON.

            JSONObject dati = (JSONObject) parser.parse(new FileReader(file.getAbsolutePath()));//path to the JSON file.

            //System.out.println(data.toJSONString());

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            OutputStream outStream = con.getOutputStream();
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
            outStreamWriter.write(obj.toJSONString());
            outStreamWriter.flush();
            outStreamWriter.close();
            outStream.close();
            String response = "";

            //System.out.println(con.getResponseCode());
            //System.out.println(con.getResponseMessage());

            //DataInputStream input = new DataInputStream(con.getInputStream());
            /*BufferedReader d = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((response = d.readLine())!= null) {
                System.out.println(response);
                d.close();
            }

            InputStream stream = Prova2.class.getResourceAsStream(fileName);
            String result = CharStreams.toString(new InputStreamReader(stream));*/
            DataInputStream input = new DataInputStream( con.getInputStream() );
            for( int c = input.read(); c != -1; c = input.read() )
                response=response+(char)c;
            //System.out.print( (char)c );
            input.close();

            //System.out.println("Resp Message:"+ con .getResponseMessage());




        /*try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while (true) {
                try {
                    if (!((responseLine = br.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response.append(responseLine.trim());
            }

            //System.out.println(response.toString());
        */
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("response.json"));
                writer.write(response.toString());
                writer.close();
            }
            //catch eventually error generate
            catch (FileNotFoundException ex) {
                System.out.println(
                        "Unable to open file '" + "response.json" + "'");
            } catch (IOException ex) {
                System.out.println(
                        "Error reading file '" + "response.json" + "'");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try (Reader reader = new FileReader("response.json")) {

            JSONParser p = new JSONParser();
            JSONObject response = (JSONObject) p.parse(reader);
            //System.out.println(jsonObject);

            JSONObject result = (JSONObject) response.get("result");
            String result_inner = (String) result.get("result");

            //System.out.println(result_inner);
            if(result_inner.equals("success")){
                JSONObject data = (JSONObject) result.get("data");
                JSONArray sentences = (JSONArray) data.get("sentences");

                boolean checkVerb = false;
                boolean checkLength = false;

                Iterator<JSONObject> iterator = sentences.iterator();
                while(iterator.hasNext()) {
                    JSONObject current = iterator.next();

                    checkVerb = false;
                    checkLength = false;

                    if((current.get("text").toString().length())>= 50){
                        checkLength = true;
                        if(current.get("b") != null) {
                            JSONArray b = (JSONArray) current.get("b");
                            Iterator<JSONObject> iterator2 = b.iterator();
                            while(iterator2.hasNext()){
                                JSONObject current2 = iterator2.next();
                                if (current2.get("gt").equals("VER")){
                                    checkVerb = true; //++
                                }
                            } LongVerbString++;//end while
                        }
                    }
                    //if (checkVerb && checkLength) LongVerbString++;
                    if(checkLength && (checkVerb == false)) LongNVerbString++;

                    if((current.get("text").toString().length())< 50){
                        if(current.get("b") != null) {
                            JSONArray b = (JSONArray) current.get("b");
                            Iterator<JSONObject> iterator2 = b.iterator();
                            while(iterator2.hasNext()){
                                JSONObject current2 = iterator2.next();
                                if (current2.get("gt").equals("VER")){
                                    checkVerb = true;
                                    if(!checkLength) System.out.println(current.get("text").toString());
                                }
                            } //end while
                        }
                    }
                    if (checkVerb && checkLength==false)
                        ShortVerbString++;
                    else if (checkLength==false && checkVerb==false) ShortNVerbString++;


                } // fine iteratore sentences
            } // fine success

        } catch (ParseException ex) {
            ex.printStackTrace();
        }


        // loop array
            /*JSONArray msg = (JSONArray) jsonObject.get("messages");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }*/

        /*} catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        //con.setConnectTimeout(10000);
        con.disconnect();

        float total = LongVerbString+LongNVerbString+ShortVerbString+ShortNVerbString;
        float fraz = 100/total;
        //System.out.println(total+", "+fraz);

        //System.out.println("connessione chiusa");
        //System.out.printf("Numero frasi lunghe con verbo %.2f%%\n",LongVerbString*fraz);
        //System.out.printf("Numero frasi lunghe senza verbo %.2f%%\n",LongNVerbString*fraz);
        //System.out.printf("Numero frasi corte con verbo %.2f%%\n",ShortVerbString*fraz);
        //System.out.printf("Numero frasi corte senza verbo %.2f%%\n",ShortNVerbString*fraz);

        BufferedWriter out = new BufferedWriter(new FileWriter("Output.txt", true));
        String str;
        str = Nome.getAbsolutePath();
        String[] path = str.split("/");

        float number = 888.7893f;
        String numberAsString = String.format ("%.2f", number);

        LongVerbString = LongVerbString*fraz;
        String LongVerbasString = String.format("%.2f",(LongVerbString));

        LongNVerbString = LongNVerbString*fraz;
        String LongNVerbasString = String.format("%.2f",(LongNVerbString));

        ShortVerbString= ShortVerbString*fraz;
        String ShortVerbasString = String.format("%.2f",(ShortVerbString));

        ShortNVerbString = ShortNVerbString*fraz;
        String ShortNVerbasString = String.format("%.2f",(ShortNVerbString));


        String results = path[path.length -1] + "     " + LongVerbasString + "%"+"     "+ LongNVerbasString +"%"+"      "+ ShortVerbasString +"%"+"      "+ShortNVerbasString+"%";
        out.write(results);
        out.write("\n");
        out.close();
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
        String R1 = "([a-z]+)$";//R1 AND R2
        String R2 = "^([a-z]+)";
        String R3 = "\\[0-9]+$"; //R3 AND R4
        String R4 = "^[A-Z,a-z]{2,100}";
        String R5 = "[a-z]$"; // R5 and R6
        String R6 = "^\\b+";

        String R7 = "[\\,\\]]$"; //single
        String R8 = "[\\-?]$";

        String R9 = "^[a-z]\\."; //points listed
        String R10 = "^[0-9]+\\. [a-z,A-Z]"; //points listed
        String R11 = "^[a-z]+\\)";  //points listed
        String R15 = "^\\([a-z]+\\)"; //points listed
        String R18 = "\\:$";
        String R21 = "^[0-9][\\.,0-9]+ [a-z,A-Z]";//points listed (ex. number.number.number)

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
        RegexSingleNegative.add(Pattern.compile(R21));

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
            Matcher w = RegexSingleNegative.get(5).matcher(lines[i]);
            //Matcher a = RegexSingleNegative.get(5).matcher(lines[i]);
            if (r.find() || s.find() || t.find() || u.find() || z.find() || w.find() ) {
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

        }//end for


        //find regex in text
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

        //single regex
        for (int i = 0; i<size-1; i++) {

            //System.out.println("ciao");
            Matcher p = Pattern.compile("^[A-Z,a-z]+ [A-Z,a-z]+$").matcher(lines[i+1]); //find regex(part 1) in line i
            Matcher q = Pattern.compile("^[A-Z,a-z]+$").matcher(lines[i+1]); //find regex(part 1) in line i
            Matcher r = Pattern.compile("^[A-Z,a-z]+ [A-Z,a-z]+$").matcher(lines[i]);
            Matcher s = Pattern.compile("^[A-Z,a-z]+$").matcher(lines[i]);
            if ( (r.find() && ( p.find() || q.find() ))  || (s.find() && (p.find() || q.find())) ) {
                //System.out.println("ciao");
                //System.out.println(lines[i+1]);
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
        }//end for single regex

        //Prima di tirar su le righe, controllo se altre possono essere tirate su!

        for (int i = 0; i<size; i++) {
            if (check[i] == 0 ) { //if this line should not be pulled up
                if(i>=5 && i<=size-5) {
                    if (lines[i].length()>=90) {
                        int val = 0;
                        for (int j = i-4; j<=i+4; j++) {
                            //System.out.println(lines[i]);
                            //System.out.println(lines[j]);
                            //if ((lines[i].length() >= lines[j].length()-1) && (lines[i].length() <= lines[j].length()+1)) {
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
        /*
        for (int i = 0; i<size; i++) { //controllo stringhe marcate con 0 e -1
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
            writer.close();
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