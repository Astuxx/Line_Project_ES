import java.awt.*;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Launch extends Component {

    private JFrame frame;
    private JFrame frame2;
    public String testo;
    public String risultato;
    public int iterazioni;



    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Launch window = new Launch();
                    //window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Launch() throws IOException {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() throws IOException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true); // to select multiple files
        //chooser.showOpenDialog(frame);
        int n = fileChooser.showOpenDialog(Launch.this);
        File[] file = fileChooser.getSelectedFiles(); //array of Files

            BufferedWriter out = new BufferedWriter(new FileWriter("Output.txt"));
            out.write("                   LS.V.      LS.NV.     SS.V.      SS.NV."+ System.getProperty( "line.separator" ));
            out.close();
            //System.out.println("check");
            for(int i = 0; i < file.length; i++) {
                //Prova2 prova2 = new Prova2();
                //prova2.main(file);
                Prova2 p = new Prova2(file[i]);
                p.main(file[i]);
                System.out.println("    terminated file "+file[i].getName());
            }
        /*} catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }*/
    }
}