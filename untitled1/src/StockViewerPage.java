import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StockViewerPage extends Page{

    private JTable stockTable;
    private JButton logoutButton;
    private JButton editButton;
    private JTextField usernameRegister;
    private JPasswordField passwordRegister;
    private JButton registerButton;


    @Override
    public void newPage(KioskApp app){
        pagePanel = new JPanel();
        stockTable = new JTable();
        logoutButton = new JButton();
        editButton = new JButton();
        usernameRegister = new JTextField();
        passwordRegister = new JPasswordField();
        registerButton = new JButton();

        pagePanel.setLayout(null);

        pagePanel.add(stockTable);
        pagePanel.add(logoutButton);
        pagePanel.add(editButton);
        pagePanel.add(usernameRegister);
        pagePanel.add(passwordRegister);
        pagePanel.add(registerButton);

        logoutButton.setLocation(380,10);
        logoutButton.setSize(100,100);
        logoutButton.setText("Logout");

        editButton.setLocation(10,10);
        editButton.setSize(150,100);
        editButton.setText("Commit Changes");

        stockTable.setLocation(10,150);
        stockTable.setSize(300,200);
        stockTable.setModel(app.getStockModel());

        usernameRegister.setLocation(350,250);
        usernameRegister.setSize(150,20);


        passwordRegister.setLocation(350,270);
        passwordRegister.setSize(150,20);

        registerButton.setLocation(370,300);
        registerButton.setSize(50,50);
        registerButton.setText("Register Admin");

        registerButton.addActionListener(actionEvent ->{
            app.createAdmin(usernameRegister.getText(),passwordRegister.getPassword());
        });


        editButton.addActionListener(actionEvent->{

            app.uploadStockModel((DefaultTableModel)stockTable.getModel());
        });

        logoutButton.addActionListener(actionEvent->{
            app.setPage(new CustomerEntryPage());
        });


    }

}
