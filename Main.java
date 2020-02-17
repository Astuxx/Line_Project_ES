import java.awt.*;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Main extends Component {

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
                    Main window = new Main();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Main() throws IOException {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() throws IOException {

        JFileChooser fileChooser = new JFileChooser(); //selector for files from UI
        fileChooser.setMultiSelectionEnabled(true); // to select multiple files
        int n = fileChooser.showOpenDialog(Main.this); //get the number of files selected
        File[] file = fileChooser.getSelectedFiles(); //array of Files

            //write a n output file with a benchmark of regex used
            BufferedWriter out = new BufferedWriter(new FileWriter("Output.txt"));
            out.write("                   LS.V.      LS.NV.     SS.V.      SS.NV."+ System.getProperty( "line.separator" ));
            out.close();
            for(int i = 0; i < file.length; i++) {
                //Line p = new Line(file[i]);
                Line.main(file[i]); //call function
                System.out.println("    terminated file "+file[i].getName());
            }
    }
}