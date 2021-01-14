

public abstract class Transaction {

    public Product[] products;
    public float subtotal;
    //private Connection conn;

    public Transaction(){
        products = new Product[]{};
        subtotal = 0.0f;
    }
    public void scan(int code){
        //conn = DriverManager.getConnection("");
    }
    public void checkout(){

    }
    public abstract void paymentInfo();
    public abstract void printReceipt();
    public void removeItem(){};

}