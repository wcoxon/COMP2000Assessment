import javax.swing.*;

public class CheckoutPage extends Page{

    private JButton CashButton;
    private JButton CardButton;



    @Override
    public void newPage(KioskApp app){
        pagePanel = new JPanel();

        CashButton = new JButton();
        CardButton = new JButton();

        pagePanel.add(CashButton);
        pagePanel.add(CardButton);


        CashButton.setLocation(50,50);
        CashButton.setSize(50,50);
        CashButton.setText("pay with cash");

        CardButton.setLocation(200, 50);
        CardButton.setSize(50,50);
        CardButton.setText("pay with card");

        CashButton.addActionListener(actionEvent->{
            app.PurchaseScannedItems();
            app.buildReceipt();
        });

        CardButton.addActionListener(actionEvent->{
            app.PurchaseScannedItems();
            app.buildReceipt();
        });




    }
}
