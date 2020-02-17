/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;*/

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
//import com.github.cliftonlabs.json_simple
import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class LineJson {

    //public variable used to obtain statistical data on the text
    public static float ShortVerbString = 0;
    public static float LongVerbString = 0;
    public static float ShortNVerbString = 0;
    public static float LongNVerbString = 0;

    public static void CallJSon(String x, File Nome) throws IOException {
        JsonObject obj = new JsonObject(); //Create a Json object

        //create json call for ESSEX
        obj.put("jsonrpc", "2.0");
        obj.put("method", "analyzetext");
        obj.put("id", "1");

        JsonObject params = new JsonObject();//Create a new Json object to add at the first object
        params.put("package", "base_en");
        params.put("data", x); //add the string of text

        obj.put("params", params);

        //create a file with a request
        File file = new File("request.json");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(obj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Connection to essex
        URL url = new URL("http://157.27.193.120:6090/essex/json-rpc");
        //URL url = new URL("http://192.168.1.226:6090/essex/json-rpc");
        HttpURLConnection con = (HttpURLConnection) url.openConnection(); //Open connection

        try {
            JsonParser parser = new JsonParser();
            JsonObject dati = (JsonObject) parser.parse(new FileReader(file.getAbsolutePath()));//path to the JSON file.

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            OutputStream outStream = con.getOutputStream();
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
            outStreamWriter.write(obj.toString());
            outStreamWriter.flush();
            outStreamWriter.close();
            outStream.close();
            String response = "";

            DataInputStream input = new DataInputStream( con.getInputStream() );
            for( int c = input.read(); c != -1; c = input.read() )
                response=response+(char)c;
            input.close();

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("response.json"));
                writer.write(response.toString());
                writer.close();
            }
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

        //read response of jason
        try (Reader reader = new FileReader("response.json")) {

            JsonParser p = new JSONParser();
            JsonObject response = (JsonObject) p.parse(reader);

            JsonObject result = (JsonObject) response.get("result");
            String result_inner = (String) result.get("result");

            if(result_inner.equals("success")){
                JsonObject data = (JsonObject) result.get("data");
                JsonArray sentences = (JsonArray) data.get("sentences");

                boolean checkVerb = false;
                boolean checkLength = false;

                Iterator<JsonObject> iterator = sentences.iterator();
                while(iterator.hasNext()) {
                    JsonObject current = iterator.next();

                    checkVerb = false;
                    checkLength = false;

                    if((current.get("text").toString().length())>= 50){
                        checkLength = true;
                        if(current.get("b") != null) {
                            JsonArray b = (JsonArray) current.get("b");
                            Iterator<JsonObject> iterator2 = b.iterator();
                            while(iterator2.hasNext()){
                                JsonObject current2 = iterator2.next();
                                if (current2.get("gt").equals("VER")){
                                    checkVerb = true; //++
                                }
                            } LongVerbString++;//end while
                        }
                    }
                    if(checkLength && (checkVerb == false)) LongNVerbString++;

                    if((current.get("text").toString().length())< 50){
                        if(current.get("b") != null) {
                            JsonArray b = (JsonArray) current.get("b");
                            Iterator<JsonObject> iterator2 = b.iterator();
                            while(iterator2.hasNext()){
                                JsonObject current2 = iterator2.next();
                                if (current2.get("gt").equals("VER")){
                                    checkVerb = true;
                                }
                            }
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

        con.disconnect(); //End connection

        //Prendiamo dei semplici riferiemnto dalla stringa di ritorno,
        //se è lunga con o senza verba o se è corta con o senza verbo
        float total = LongVerbString+LongNVerbString+ShortVerbString+ShortNVerbString;
        float fraz = 100/total;

        //add the statistical data found to the file
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
}
