import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import javax.swing.border.*;

/**
 * Klasse GameFrame.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */

public class GameJFrame extends JFrame implements ActionListener
{
    private int step;
    private int size;
    
    private GameModel model;
    
    private JLabel stepCountLabel;
    private JPanel boardPanel;

    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem einfachItem = new JMenuItem("简易 3*3");
    JMenuItem mittelItem = new JMenuItem("中等 4*4");
    JMenuItem schwerItem = new JMenuItem("难 5*5");
    JMenuItem imageItem = new JMenuItem("上传图片");
        
    /**
     * Creates the game window and starts a new 3 * 3 game.
     */
    public GameJFrame(){
        step = 0;
        size = 3;
        
        initJFrame();
        initJMenuBar();
        
        startNewGame(size);
        
        this.setVisible(true);
    }
    
    /**
     * Initializes the main window.
     */
    private void initJFrame(){
        this.setSize(680,720);
        
        this.setTitle("Schiebepuzzle");
        
        this.setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.setLayout(new BorderLayout());
    }
    
    /**
     * Creates the menu bar and registers the action listeners.
     */
    private void initJMenuBar(){
        JMenuBar jMenuBar = new JMenuBar();
        
        JMenu functionJMenu = new JMenu("功能");
        JMenu level = new JMenu("难度");
        
        functionJMenu.add(level);
        functionJMenu.add(replayItem);
        functionJMenu.add(imageItem);
        functionJMenu.add(closeItem);
        
        
        level.add(einfachItem);
        level.add(mittelItem);
        level.add(schwerItem);
        
        einfachItem.addActionListener(this);
        mittelItem.addActionListener(this);
        schwerItem.addActionListener(this);
        replayItem.addActionListener(this);
        closeItem.addActionListener(this);
        imageItem.addActionListener(this);
        
        jMenuBar.add(functionJMenu);
        
        this.setJMenuBar(jMenuBar);
    }
    
    /**
     * Creates a new game with the selected board size.
     */
    private void startNewGame(int newSize) {
        size = newSize;
        step = 0;
        
        model = new GameModel(size);
        
        drawGame();
    }
    
    /**
     * Redraws the complete game interface.
     */
    private void drawGame() {
        getContentPane().removeAll();
        
        JPanel informationPanel = new JPanel();
        
        stepCountLabel = new JLabel("步数: " + step);
        stepCountLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        
        informationPanel.add(stepCountLabel);
        
        boardPanel = new JPanel(new GridLayout(size, size, 2, 2));
        
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        createNumberTiles();
        
        add(informationPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        
        /*
         * revalidate() recalculates the Swing layout.
         * repaint() redraws the visible components.
         */
        revalidate();
        repaint();
    }
    
    /**
     * Creates all number fields according to the current model state.
     */
    private void createNumberTiles(){
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                JLabel tile = createTile(row, col);
                boardPanel.add(tile);
            }
        }
    }
    
    /**
     * Creates one clickable puzzle field.
     */
    private JLabel createTile(final int row, final int col) {
        JLabel tile = new JLabel("", SwingConstants.CENTER);
        
        tile.setOpaque(true);
        tile.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        
        tile.setBorder(new LineBorder(Color.DARK_GRAY));
        
        if(model.isEmpty(row, col)) {
            tile.setText("");
            tile.setBackground(Color.WHITE);
        }else {
            tile.setText(String.valueOf(model.getValueAt(row, col)));
            
            tile.setBackground(new Color(225, 225, 225));
        }
        
        /*
         * Each lable remembers its row and column through
         * the local variables row and col.
         */
        tile.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event){
                handleTileClick(row, col);
            }
        });
        
        return tile;
    }
    
    /**
     * Handles a click on one puzzle field
     * 
     * @param row clicked row
     * @param col clicked col
     */
    private void handleTileClick(int row, int col) {
        boolean moved = model.move(row, col);
        
        /*
         * Invalid clicks do not increase the step counter.
         */
        if(!moved) {
            return;
        }
        
        step++;
        
        drawGame();
        
        if(model.isSolved()) {
            showWinDialog();
        }
    }
    
    /**
     * Displays a meesage after the puzzle has been solved.
     */
    private void showWinDialog() {
        int result = JOptionPane.showConfirmDialog(this, 
            "恭喜,你完成了拼图\n总步数: " + step + "\n是否开始新游戏?", "游戏完成", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            
        if(result == JOptionPane.YES_OPTION) {
            startNewGame(size);
        }
    }
    
    private void chooseImage(){
        JFileChooser chooser = new JFileChooser();
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png");

        chooser.setFileFilter(filter);
        
        int result = chooser.showOpenDialog(this);
        
        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            
            /*
             * The selected image will later be loaded and divided
             * into individual puzzle pieces.
             */
            JOptionPane.showMessageDialog(this, "已选择图片: \n" + selectedFile.getAbsolutePath());
        }
    }
    
    /**
     * Handles all menu actions.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        Object obj = e.getSource();
        
        if(obj == einfachItem){
            startNewGame(3);
        }else if(obj == mittelItem){
            startNewGame(4);
            
        }else if(obj == schwerItem){
            startNewGame(5);
            
        }else if(obj == replayItem){
            startNewGame(size);
        }else if(obj == closeItem){
            System.exit(0);
        }else if (obj == imageItem) {
            chooseImage();
        }
    }
}