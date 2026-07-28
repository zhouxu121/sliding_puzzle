import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

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

    /*
     * The original image selected by the user.
     * It is stored so that the same image can be divided again
     * when the board size changes.
     */
    private BufferedImage originalImage;

    /*
     * Contains the individual image pieces.
     *
     * The index corresponds to the number stored in GameModel.
     */
    private BufferedImage[] imagePieces;

    /*
     * Determines whether number or image pieces are displayed.
     */
    private boolean imageMode;

    /*
     * 600 can be divided exactly by 3, 4 and 5.
     */
    private static final int IMAGE_SIZE = 600;

    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem einfachItem = new JMenuItem("简易 3*3");
    JMenuItem mittelItem = new JMenuItem("中等 4*4");
    JMenuItem schwerItem = new JMenuItem("困难 5*5");
    JMenuItem imageItem = new JMenuItem("上传图片");
    JMenuItem numberItem = new JMenuItem("显示数字");

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
        functionJMenu.add(numberItem);

        level.add(einfachItem);
        level.add(mittelItem);
        level.add(schwerItem);

        einfachItem.addActionListener(this);
        mittelItem.addActionListener(this);
        schwerItem.addActionListener(this);
        replayItem.addActionListener(this);
        closeItem.addActionListener(this);
        imageItem.addActionListener(this);
        numberItem.addActionListener(this);

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

        if(imageMode && originalImage != null) {
            createImagePieces();
        }

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

        /*
         * No horizontal or vertical gaps between tiles.
         */
        boardPanel = new JPanel(new GridLayout(size, size, 0, 0));
        
        /*
         * The complete scaled image is 600 * 600,
         * so the board must also be exactly 600 * 600.
         */
        boardPanel.setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));

        createNumberTiles();
        
        /*
         * The wrapper prevents BorderLayout from stretching
         * the board beyond 600 * 600.
         */
        JPanel boardWrapper = new JPanel(new GridBagLayout());
        
        boardWrapper.add(boardPanel);
        
        add(informationPanel, BorderLayout.NORTH);
        add(boardWrapper, BorderLayout.CENTER);

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

        /*
         * No border between the image pieces.
         */
        tile.setBorder(null);

        if(model.isEmpty(row, col)) {
            /*
             * The empty field displays neither text or image.
             */
            tile.setText("");
            tile.setIcon(null);
            tile.setBackground(Color.WHITE);
        }else {
            int value = model.getValueAt(row, col);

            if(imageMode && imagePieces != null) {
                /*
                 * The Model value is used as the index
                 * of the corresponding image piece.
                 */
                tile.setText("");
                tile.setIcon(new ImageIcon(imagePieces[value]));
                tile.setBackground(Color.WHITE);
            }else {
                tile.setIcon(null);
                tile.setText(String.valueOf(model.getValueAt(row, col)));
                tile.setBackground(new Color(225, 225, 225));

                /*
                 * Keep borders in numer mode.
                 */
                tile.setBorder(new LineBorder(Color.DARK_GRAY));
            }
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

    /**
     * Open a file chooser and allows the user to select an image.
     *
     * Loading an image dose not create a new game.
     * The current board arrangement and step counter remain unchanged.
     */
    private void chooseImage(){
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image files(*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png");

        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(this);
        if(result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = chooser.getSelectedFile();

        try {
            originalImage = ImageIO.read(selectedFile);

            /*
             * ImageIO.read() can return null when the selected
             * file is not a supported image.
             */
            if(originalImage == null) {
                JOptionPane.showMessageDialog(this, "无法读取所选择的图片.", "图片错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            /*
             * First scale the complete image and then call
             * getSubimage() on the scaled image.
             */
            createImagePieces();

            imageMode = true;

            /*
             * Only redraw the interface.
             *
             * startNewGame() is deliberately not called,
             * so the game is not interrupted.
             */
            drawGame();
        }catch(IOException exception) {
            JOptionPane.showMessageDialog(this, "读取图片时发生错误:\n" + exception.getMessage(), "图片错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Scales the selected image and divided it into puzzle pieces.
     *
     * getSubimage() is called only after a new scaled BufferedImage has been created
     * and drawn using Graphics.drawImage().
     */
    private void createImagePieces() {
        if(originalImage == null) {
            imagePieces = null;
            return;
        }

        /*
         * Create a new BufferedImage with the required size.
         */
        BufferedImage scaledImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);

        /*
         * Draw the original image into the new scaled image.
         */
        Graphics2D graphics = scaledImage.createGraphics();

        try {
            graphics.drawImage(originalImage, 0, 0, IMAGE_SIZE, IMAGE_SIZE, null);
        }finally {
            graphics.dispose();
        }

        int pieceSize = IMAGE_SIZE / size;
        /*
         * Thr empty field does not need its own image piece,
         * so only size * size - 1 pieces are created.
         */
        imagePieces = new BufferedImage[size * size - 1];

        for(int number = 0; number < imagePieces.length; number++) {
            int sourceRow = number / size;
            int sourceCol = number % size;

            int sourceX = sourceCol * pieceSize;
            int sourceY = sourceRow * pieceSize;

            imagePieces[number] = scaledImage.getSubimage(sourceX, sourceY, pieceSize, pieceSize);
        }
    }

    /**
     * Switches from image display back to number display.
     *
     * The current game state remains unchanged.
     */
    private void showNumbers() {
        imageMode = false;
        drawGame();
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
        }else if(obj == numberItem) {
            showNumbers();
        }
    }
}
