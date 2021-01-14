
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;

public class KioskApp extends JFrame {

    private JPanel mainPanel;
    private MessageDigest md;
    private Page currentPage;

    public KioskApp(Page startPage){


        setPage(startPage);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500,500));

        pack();
        setLocationRelativeTo(this);
        setVisible(true);
    }


    public void setPage(Page destinationPage){
        currentPage = destinationPage;
        newPage();
        displayPage();
    }
    public void newPage(){
        currentPage.newPage(this);
    }
    public void displayPage(){
        setContentPane(currentPage.pagePanel);
        revalidate();
    }

    class ConnectionThread extends Thread {
        private String path;
        private File connection;
        public Scanner dataReader;
        public FileWriter dataWriter;

        public ConnectionThread(String _path) {
            path = _path;
        }

        public void readData() {
            try {
                connection = new File(path);
                dataReader = new Scanner(connection);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }

        public DefaultTableModel getTableModel(DefaultTableModel model) {
            String[] row;
            readData();
            while (dataReader.hasNextLine()) {

                row = new String[model.getColumnCount()];

                for (int x = 0; x < model.getColumnCount(); x++) {
                    row[x] = dataReader.nextLine();

                }
                model.addRow(row);
            }

            return model;

        }


        public void uploadTableModel(DefaultTableModel model) {
            String payload = "";
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int column = 0; column < model.getColumnCount(); column++) {

                    payload += model.getValueAt(row, column);
                    payload += "\n";

                }
            }
            WriteData(payload);
        }


        public void WriteData(String data) {
            try {

                dataWriter = new FileWriter(path);
                dataWriter.write(data);
                dataWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        public void addRow(Object[] row){
            try {
                dataWriter = new FileWriter(path,true);
                for(int x = 0;x<row.length;x++){
                    dataWriter.append("\n" + row[x]);
                }
                dataWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        public void deleteRow(DefaultTableModel model, int index){
            getTableModel(model);
            model.removeRow(index);
            uploadTableModel(model);


        }

        public String fileContents(){
            readData();
            String buffer = "";
            while(dataReader.hasNextLine()){
                buffer += dataReader.nextLine() + "\n";
            }

            return buffer;

        }
    }

    public void buildReceipt(){
        ConnectionThread ReceiptConnection = new ConnectionThread("src/Receipt.txt");
        DefaultTableModel model = getScannedProducts();

        ReceiptConnection.WriteData("");
        for(int x = 0; x<model.getRowCount();x++){
            ReceiptConnection.addRow(new Object[]{model.getValueAt(x,1) + "  :  " + model.getValueAt(x,2)});
        }
        ReceiptConnection.addRow(new Object[]{"total cost: " + getTotalPrice()});
        KioskApp receiptWindow = new KioskApp(new ReceiptPage());

    }
    public void printReceipt(JTextPane text){
        ConnectionThread ReceiptConnection = new ConnectionThread("src/Receipt.txt");
        text.setText(ReceiptConnection.fileContents());

    }

    public String hashSHA256(char[] plain,byte[] salt){
        try {
            md = MessageDigest.getInstance("SHA-256");


            return new String(md.digest((new String(plain).concat(new String(salt))).getBytes()));

        }catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }
        return null;
    }

    public byte[] generateSalt(){
        byte[] salt = new byte[32];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public byte[] getSalt(String username){

        ConnectionThread adminThread = new ConnectionThread("src/Admins.txt");
        DefaultTableModel adminTable = adminThread.getTableModel(new DefaultTableModel(new String[]{"user","hashPass","salt"},0));
        for(int x = 0; x<adminTable.getRowCount();x++){
            if(adminTable.getValueAt(x,0).equals(username)){

                    return adminTable.getValueAt(x, 2).toString().getBytes();

            }
        }
        return new byte[32];
    }
    public void createAdmin(String username, char[] plain){

        byte[] salt = generateSalt();
        ConnectionThread adminThread = new ConnectionThread("src/Admins.txt");

        adminThread.addRow(new Object[]{username,hashSHA256(plain,salt), new String(salt)});
    }

    public void authenticate(String username, String hashedPass){

        ConnectionThread adminThread = new ConnectionThread("src/Admins.txt");

        DefaultTableModel model = adminThread.getTableModel(new DefaultTableModel(new String[] {"user","hashPass","salt"},0));
        try {
            adminThread.join();

            for (int row = 0; row < model.getRowCount(); row++) {

                if (model.getValueAt(row, 0).equals(username)) {

                    if (model.getValueAt(row, 1).equals(hashedPass)) {

                        setPage(new StockViewerPage());
                        return;
                    }
                }

            }
        }catch(InterruptedException ex){ ex.printStackTrace();}

    }


    public DefaultTableModel getScannedProducts(){
        DefaultTableModel dtm = new DefaultTableModel(new String[] {"code","name","price","stock"},0);
        try{
            ConnectionThread ScannerThread = new ConnectionThread("src/ScannedProductCodes.txt");
            DefaultTableModel Codes = ScannerThread.getTableModel(new DefaultTableModel(new String[]{"code"},0));

            ScannerThread.join();

            ConnectionThread ProductsThread = new ConnectionThread("src/Stocks.txt");
            DefaultTableModel Products = ProductsThread.getTableModel(new DefaultTableModel(new String[]{"code","name","price","stock"},0));
            ProductsThread.join();

            for(int codeIndex = 0;codeIndex < Codes.getRowCount();codeIndex++){

                for(int productIndex = 0; productIndex<Products.getRowCount();productIndex++){

                    if(Codes.getValueAt(codeIndex,0).equals(Products.getValueAt(productIndex,0))){
                        dtm.addRow(new Object[]{Products.getValueAt(productIndex,0),
                                Products.getValueAt(productIndex,1),
                                Products.getValueAt(productIndex,2),
                                Products.getValueAt(productIndex,3)});

                        break;
                    }

                }
            }

        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
        return dtm;
    }

    public void scanProduct(String code){
        ConnectionThread kioskScanner = new ConnectionThread("src/ScannedProductCodes.txt");
        kioskScanner.addRow(new Object[]{code});
    }

    public float getTotalPrice(){
        float sum = 0f;
        DefaultTableModel products = getScannedProducts();
        for(int x = 0; x<products.getRowCount();x++) {
            sum += Float.parseFloat((String)products.getValueAt(x, 2));
        }
        return sum;
    }

    public DefaultTableModel getStockModel(){
        DefaultTableModel dtm = new DefaultTableModel(new String[] {"code","name","price","stock"},0);
        ConnectionThread stocksLoader = new ConnectionThread("src/Stocks.txt");
        stocksLoader.getTableModel(dtm);


        return dtm;
    }

    public void uploadStockModel(DefaultTableModel model){
        ConnectionThread uploaderThread = new ConnectionThread("src/Stocks.txt");
        uploaderThread.uploadTableModel(model);


    }

    public void PurchaseScannedItems(){
        DefaultTableModel Cart = getScannedProducts();
        ConnectionThread stocksConnector = new ConnectionThread("src/Stocks.txt");
        DefaultTableModel model = stocksConnector.getTableModel(new DefaultTableModel(new String[]{"code","name","price","stock"},0));
        try {
            stocksConnector.join();
            for (int cartIndex = 0; cartIndex < Cart.getRowCount(); cartIndex++) {
                for(int productIndex = 0; productIndex<model.getRowCount();productIndex++) {
                    if(Cart.getValueAt(cartIndex,0).equals(model.getValueAt(productIndex,0))) {

                        model.setValueAt(Integer.parseInt(model.getValueAt(productIndex, 3).toString()) - 1, productIndex, 3);
                        break;
                    }
                }
            }
        }catch(InterruptedException ex){ex.printStackTrace();}
        stocksConnector.uploadTableModel(model);
    }

    public void removeFromCart(int index){
        if(index<0){
            return;
        }
        ConnectionThread cartConnector = new ConnectionThread("src/ScannedProductCodes.txt");
        cartConnector.deleteRow(new DefaultTableModel(new String[]{"code"},0),index);
    }

    public static void main(String[] args) {
        KioskApp page = new KioskApp(new CustomerEntryPage());
    }
}
