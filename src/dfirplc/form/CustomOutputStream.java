package dfirplc.form;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

/**
 * This class extends from OutputStream to redirect output to a JTextArrea
 *
 * @author www.codejava.net
 *
 */
public class CustomOutputStream extends OutputStream {

    @SuppressWarnings("FieldMayBeFinal")
    private JTextArea textArea;
    private int pointer = 0;
    private int MAXLINE = 5;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {

        if (b != -61) {
            if (b < -64 && b > -320) {
                b = 320 + b;
            }
            textArea.insert(String.valueOf((char) b), pointer);
            if (b != 10) {
                pointer++;
            } else {
                pointer = 0;
            }
// scrolls the text area to the end of data
            //textArea.setCaretPosition(textArea.getDocument().getLength());
           
        }
    }
}
