import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerEntryPage extends Page{

    //private JComponent[] elements;
    private JTable ItemCart;
    private JButton AdminButton;
    private JButton CheckoutButton;
    private JTextField ProductCodeField;
    private JButton ScanButton;
    private JTextArea TotalPrice;
    private JButton DeleteButton;

    @Override
    public void newPage(KioskApp app) {
        pagePanel = new JPanel();

        AdminButton = new JButton();
        ItemCart = new JTable();
        CheckoutButton = new JButton();
        ProductCodeField = new JTextField();
        ScanButton = new JButton();
        TotalPrice = new JTextArea();
        DeleteButton = new JButton();

        pagePanel.add(ItemCart);
        pagePanel.add(AdminButton);
        pagePanel.add(CheckoutButton);
        pagePanel.add(ProductCodeField);
        pagePanel.add(ScanButton);
        pagePanel.add(TotalPrice);
        pagePanel.add(DeleteButton);

        pagePanel.setLayout(null);

        ItemCart.setLocation(10,10);
        //ItemCart.setLocation(pagePanel.getWidth()/5,10);
        ItemCart.setSize(230,300);

        AdminButton.setLocation(390,10);
        AdminButton.setSize(100,20);
        AdminButton.setText("Admin");

        CheckoutButton.setLocation(300,400);
        CheckoutButton.setSize(100,20);
        CheckoutButton.setText("Checkout");

        ProductCodeField.setLocation(300,300);
        ProductCodeField.setSize(100,20);

        ScanButton.setLocation(300,340);
        ScanButton.setSize(80,20);
        ScanButton.setText("Scan");

        TotalPrice.setLocation(140,320);
        TotalPrice.setSize(100,100);

        DeleteButton.setLocation(250,10);
        DeleteButton.setSize(150,20);
        DeleteButton.setText("Remove Item");



        ItemCart.setModel(new DefaultTableModel(new String[] {"name","price"},0));
        ItemCart.setModel(app.getScannedProducts());

        ScanButton.addActionListener(actionEvent ->{
            app.scanProduct(ProductCodeField.getText());
            ItemCart.setModel(app.getScannedProducts());
            TotalPrice.setText(String.valueOf(Math.round(app.getTotalPrice()*100f)/100f));

        });

        AdminButton.addActionListener(actionEvent ->{
            app.setPage(new AdminLoginPage());
        });

        CheckoutButton.addActionListener(actionEvent ->{
            app.setPage(new CheckoutPage());
        });

        DeleteButton.addActionListener(actionEvent->{
            app.removeFromCart(ItemCart.getSelectedRow());
            ItemCart.setModel(app.getScannedProducts());
        });


    }
}
