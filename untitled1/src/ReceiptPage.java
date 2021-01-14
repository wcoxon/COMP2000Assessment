import javax.swing.*;

public class ReceiptPage extends Page{

    private JTextPane text;

    @Override
    public void newPage(KioskApp app) {
        pagePanel = new JPanel();
        text = new JTextPane();

        pagePanel.setLayout(null);
        pagePanel.add(text);

        text.setLocation(0,0);
        text.setSize(500,500);

        app.printReceipt(text);


    }

}
