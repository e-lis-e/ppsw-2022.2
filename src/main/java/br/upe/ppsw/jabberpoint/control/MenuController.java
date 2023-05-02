package br.upe.ppsw.jabberpoint.control;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import br.upe.ppsw.jabberpoint.model.Presentation;
import br.upe.ppsw.jabberpoint.model.accessor.Accessor;
import br.upe.ppsw.jabberpoint.model.accessor.JSONAccessor;
import br.upe.ppsw.jabberpoint.model.accessor.XMLAccessor;
import br.upe.ppsw.jabberpoint.viewer.AboutBox;

public class MenuController extends MenuBar {

  private static final long serialVersionUID = 227L;

  private Frame parent;
  private Presentation presentation;

  protected static final String ABOUT = "Sobre";
  protected static final String FILE = "Arquivo";
  protected static final String EXIT = "Sair";
  protected static final String GOTO = "Pular para";
  protected static final String HELP = "Ajuda";
  protected static final String NEW = "Novo";
  protected static final String NEXT = "Próximo";
  protected static final String OPEN = "Abrir";
  protected static final String PAGENR = "Npumero do Slide?";
  protected static final String PREV = "Anteior";
  protected static final String SAVE = "Salvar";
  protected static final String VIEW = "Visualizar";

  protected static final String TESTFILE = "classpath:test.xml";
  protected static final String SAVEFILE = "classpath:dump.xml";

  protected static final String IOEX = "IO Exception: ";
  protected static final String LOADERR = "Erro ao carregar";
  protected static final String SAVEERR = "Erro ao salvar";

  public MenuController(Frame frame, Presentation pres) {
    parent = frame;
    presentation = pres;

    MenuItem menuItem;

    Menu fileMenu = new Menu(FILE);
    fileMenu.add(menuItem = mkMenuItem(OPEN));
    menuItem.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent actionEvent) {
    	    presentation.clear();
    	    Accessor accessor = null;
    	    JFileChooser fileChooser = new JFileChooser();
    	    int option = fileChooser.showOpenDialog(parent);
    	    if (option == JFileChooser.APPROVE_OPTION) {
    	      File selectedFile = fileChooser.getSelectedFile();
    	      String filename = selectedFile.getAbsolutePath();
    	      String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    	      if (extension.equals("xml")) {
    	        accessor = new XMLAccessor();
    	      } else if (extension.equals("json")) {
    	        accessor = new JSONAccessor();
            } else {
    	        JOptionPane.showMessageDialog(parent, "Unknown file type");
    	      }
    	      try {
    	        accessor.loadFile(presentation, filename);
    	        presentation.setSlideNumber(0);
    	      } catch (IOException exc) {
    	        JOptionPane.showMessageDialog(parent, IOEX + exc, LOADERR, JOptionPane.ERROR_MESSAGE);
    	      }
    	    }
    	    parent.repaint();
    	  }
    	});
    
    fileMenu.add(menuItem = mkMenuItem(NEW));

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        presentation.clear();
        parent.repaint();
      }
    });

    fileMenu.add(menuItem = mkMenuItem(SAVE));

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar apresentação");
        int option = fileChooser.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();
          String filename = selectedFile.getAbsolutePath();
          String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
          Accessor accessor = null;
          if (extension.equals("xml")) {
            accessor = new XMLAccessor();
          } else if (extension.equals("json")) {
            accessor = new JSONAccessor();
          } else {
            JOptionPane.showMessageDialog(parent, "Unknown file type");
          }
          if (accessor != null) {
            try {
              accessor.saveFile(presentation, filename);
            } catch (IOException exc) {
              JOptionPane.showMessageDialog(parent, IOEX + exc, SAVEERR, JOptionPane.ERROR_MESSAGE);
            }
          }
        }
      }
    });

    fileMenu.addSeparator();

    fileMenu.add(menuItem = mkMenuItem(EXIT));

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        presentation.exit(0);
      }
    });

    add(fileMenu);

    Menu viewMenu = new Menu(VIEW);
    viewMenu.add(menuItem = mkMenuItem(NEXT));

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        presentation.nextSlide();
      }
    });

    viewMenu.add(menuItem = mkMenuItem(PREV));

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        presentation.prevSlide();
      }
    });

    viewMenu.add(menuItem = mkMenuItem(GOTO));

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        String pageNumberStr = JOptionPane.showInputDialog((Object) PAGENR);
        int pageNumber = Integer.parseInt(pageNumberStr);
        presentation.setSlideNumber(pageNumber - 1);
      }
    });

    add(viewMenu);

    Menu helpMenu = new Menu(HELP);
    helpMenu.add(menuItem = mkMenuItem(ABOUT));

    menuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        AboutBox.show(parent);
      }
    });
    

    setHelpMenu(helpMenu);
  }

  public MenuItem mkMenuItem(String name) {
    return new MenuItem(name, new MenuShortcut(name.charAt(0)));
  }
}
