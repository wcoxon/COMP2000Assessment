import javax.swing.*;
import java.awt.*;

public abstract class Page {
    public JPanel pagePanel;
    private Page previousPage;
    //private JFrame app;
    public void newPage(KioskApp app){};
    //public JComponent[] elements;

    /*public Page(){
        pagePanel = new JPanel();
    };
    public Page(Page prev){
        pagePanel = new JPanel();
        previousPage = prev;
    }*/
    /*public void loadElements(){
        for(int x=0;x<elements.length;x++){

            pagePanel.add(elements[x]);
        }
    }*/
    /*
    public Page getPreviousPage(){

        return previousPage;
    }*/


}
