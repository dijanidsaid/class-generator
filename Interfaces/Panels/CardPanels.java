package Panels;

import java.awt.CardLayout;

import javax.swing.JPanel;


public class CardPanels extends JPanel {
    //variables declaration
    private Attributes attributesPage;
    private Methodes methodesPage;
    private nombreDesClass nombreDeClassPage;
    private LesNomsDesClasses nomsDesClassesPage;
    private ParametresDesMethodes parametresDesMethodesPage;
    private End endPage;
    private Apropos aproposPage;
    private Help helpPage;

    
    //end of variables declaration


    public CardPanels() {
        initComponents();
    }
    private void initComponents() {

        attributesPage = new Attributes();
        methodesPage = new Methodes();
        nombreDeClassPage = new nombreDesClass();
        nomsDesClassesPage = new LesNomsDesClasses();
        parametresDesMethodesPage = new ParametresDesMethodes();
        endPage = new End();
        aproposPage = new Apropos();
        helpPage = new Help();

        setLayout(new CardLayout());

        


        add(nombreDeClassPage, "1");
        add(nomsDesClassesPage,"2");
        add(attributesPage, "3");
        add(methodesPage , "4");
        add(parametresDesMethodesPage, "5");
        add(endPage, "end");
        add(aproposPage, "apropos");
        add(helpPage, "help");

        ((CardLayout) getLayout()).show(this, "1");

    }
}
