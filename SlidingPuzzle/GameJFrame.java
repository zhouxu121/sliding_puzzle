import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main graphical user interface for the sliding puzzle game.
 * 
 * Responsibilities of this class:
 * - create and display the Swing window and menus,
 * - render the current state provided by {@link GameModel},
 * - react to mouse clicks and menu actions,
 * - count the player's valid moves,
 * - switch between number mode and image mode,
 * - load all visible texts from ResourceBundle files for internationalization.
 * 
 * The actual puzzle rules are intentionally kept in GameModel.
 */
public class GameJFrame extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 1L;

    // Number of valid tile moves made in the current game.
    private int step;
    
    // Current board dimension. Supported values are 3, 4 and 5.
    private int size;

    // Model object that stores the board state and implements the game rules.
    private GameModel model;

    // Label that displays the current number of moves.
    private JLabel stepCountLabel;
    
    // Panel that contains all puzzle tiles in a GridLayout.
    private JPanel boardPanel;
    
    // References to the existing tile labels so they can be refreshed efficiently.
    private JLabel[][] tiles;

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

    // Language currently used by the interface.
    private Locale currentLocale;
    
    // Provides translated UI texts from Messages_*.properties files.
    private ResourceBundle messages;

    // Menu components are stored as fields because their texts can change at runtime.
    private JMenuBar menuBar;

    private JMenu functionMenu;
    private JMenu levelMenu;
    private JMenu languageMenu;

    private JMenuItem replayItem;
    private JMenuItem closeItem;
    private JMenuItem easyItem;
    private JMenuItem mediumItem;
    private JMenuItem hardItem;
    private JMenuItem imageItem;
    private JMenuItem numberItem;

    private JMenuItem chineseItem;
    private JMenuItem englishItem;
    private JMenuItem germanItem;

    /**
     * Creates the game window, loads the default language,
     * builds the menu bar and starts a new 3 * 3 game.
     */
    public GameJFrame(){
        step = 0;
        size = 3;

        currentLocale = Locale.ENGLISH;
        loadLanguage();

        initJFrame();
        initJMenuBar();

        startNewGame(size);

        this.setVisible(true);
    }

    /**
     * Configures the basic JFrame properties such as size, layout,
     * close operation and initial screen position.
     */
    private void initJFrame(){
        this.setSize(680,720);

        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());
    }

    /**
     * Creates all menus and menu items, adds item to the menu bar,
     * registers this frame as their ActionListener and applies translated texts.
     */
    private void initJMenuBar(){
        menuBar = new JMenuBar();

        functionMenu = new JMenu();
        levelMenu = new JMenu();
        languageMenu = new JMenu();

        replayItem = new JMenuItem();
        closeItem = new JMenuItem();
        easyItem = new JMenuItem();
        mediumItem = new JMenuItem();
        hardItem = new JMenuItem();
        imageItem = new JMenuItem();
        numberItem = new JMenuItem();

        chineseItem = new JMenuItem("中文");
        englishItem = new JMenuItem("English");
        germanItem = new JMenuItem("Deutsch");

        levelMenu.add(easyItem);
        levelMenu.add(mediumItem);
        levelMenu.add(hardItem);

        languageMenu.add(chineseItem);
        languageMenu.add(englishItem);
        languageMenu.add(germanItem);

        functionMenu.add(replayItem);
        functionMenu.add(imageItem);
        functionMenu.add(numberItem);
        functionMenu.addSeparator();
        functionMenu.add(closeItem);

        easyItem.addActionListener(this);
        mediumItem.addActionListener(this);
        hardItem.addActionListener(this);
        replayItem.addActionListener(this);
        closeItem.addActionListener(this);
        imageItem.addActionListener(this);
        numberItem.addActionListener(this);

        chineseItem.addActionListener(this);
        englishItem.addActionListener(this);
        germanItem.addActionListener(this);

        menuBar.add(functionMenu);
        menuBar.add(levelMenu);
        menuBar.add(languageMenu);
        setJMenuBar(menuBar);

        updateTexts();
    }

    /**
     * Creates a fresh shuffled puzzle with the selected board size.
     * 
     * The step counter is reset. If an image is currently active, the same
     * image is divided again so that its pieces match the new board size.
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
     * Rebuilds the visible game area from the current model state.
     * 
     * This method is used after structural changes such as starting a new game,
     * loading an image or switching back to number mode. Normal tile moves use
     * refreshTiles() instead, which is cheaper because it reuses existing labels.
     */
    private void drawGame() {
        getContentPane().removeAll();

        JPanel informationPanel = new JPanel();

        stepCountLabel = new JLabel(messages.getString("label.steps") + ": " + step);
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
        tiles = new JLabel[size][size];

        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                JLabel tile = createTile(row, col);
                tiles[row][col] = tile;
                boardPanel.add(tile);
            }
        }
    }

    /**
     * Creates one clickable JLabel for a specific board position.
     * The label receives a mouse listener that forwards clicks to handleTileClick().
     * 
     * @param row row of this tile in the board
     * @param col column of this tile in the board
     * @return configured JLabel representing the tile
     */
    private JLabel createTile(final int row, final int col) {
        JLabel tile = new JLabel("", SwingConstants.CENTER);

        tile.setOpaque(true);
        tile.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));

        updateTile(tile, row, col);

        /*
         * Each label remembers its row and column through
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
     * Updates one existing tile label so that it matches the model state.
     * 
     * Empty positions are shown as blank with fields. Non-empty positions show
     * either a number or the corresponding image piece depending on imageMode.
     * 
     * @param tile label that should be updated
     * @param row row represented by the label
     * @param col column represented by the label
     */
    private void updateTile(JLabel tile, int row, int col) {

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
                tile.setText(String.valueOf(model.getValueAt(row, col)));
                tile.setIcon(null);
                tile.setBackground(new Color(225, 225, 225));

                /*
                 * Keep borders in number mode.
                 */
                tile.setBorder(new LineBorder(Color.DARK_GRAY));
            }
        }

    }

    /**
     * Refreshes the existing labels after a valid move.
     * Reusing the labels avoids rebuilding the complete Swing hierarchy.
     */
    private void refreshTiles() {
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                updateTile(tiles[row][col], row, col);
            }
        }

        // The information panel is not rebuilt after a normal move.
        stepCountLabel.setText(messages.getString("label.steps") + ": " + step);
        boardPanel.repaint();
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

        refreshTiles();

        if(model.isSolved()) {
            showWinDialog();
        }
    }

    /**
     * Shows the localized win dialog and optionally starts another game.
     */
    private void showWinDialog() {
        String message = messages.getString("dialog.win.message")
                + "\n"
                + messages.getString("dialog.win.steps")
                + " " + step
                + "\n"
                + messages.getString("dialog.win.restart");

        int result = JOptionPane.showConfirmDialog(
                this,
                message,
                messages.getString("dialog.win.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if(result == JOptionPane.YES_OPTION) {
            startNewGame(size);
        }
    }

    /**
     * Opens a file chooser and allows the user to select an image.
     *
     * Loading an image does not create a new game.
     * The current board arrangement and step counter remain unchanged.
     */
    private void chooseImage(){
        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle(
            messages.getString("dialog.image.title")
            );

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                messages.getString("dialog.image.filter"),
                "jpg", "jpeg", "png"
                );

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
                JOptionPane.showMessageDialog(
                        this,
                        messages.getString("dialog.image.invalid.message"),
                        messages.getString("dialog.image.error.title"),
                        JOptionPane.ERROR_MESSAGE
                        );
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
            JOptionPane.showMessageDialog(
                    this,
                    messages.getString("dialog.image.error.message") + ": " + exception.getMessage(),
                    messages.getString("dialog.image.error.title"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Scales the selected image and divides it into puzzle pieces.
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
         * The empty field does not need its own image piece,
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
     * Switches from image display back to number display without changing the puzzle state.
     * 
     *
     * The current game state remains unchanged.
     */
    private void showNumbers() {
        imageMode = false;
        drawGame();
    }

    /**
     * Handles all menu actions from the Game, Difficulty and Language menus.
     * 
     * @param e Swing event describing which menu item was selected
     */
    @Override
    public void actionPerformed(ActionEvent e){
        Object obj = e.getSource();

        if(obj == easyItem){
            startNewGame(3);
        }else if(obj == mediumItem){
            startNewGame(4);

        }else if(obj == hardItem){
            startNewGame(5);

        }else if(obj == replayItem){
            startNewGame(size);
        }else if(obj == closeItem){
            System.exit(0);
        }else if (obj == imageItem) {
            chooseImage();
        }else if(obj == numberItem) {
            showNumbers();
        }else if (obj == chineseItem) {
            changeLanguage(Locale.SIMPLIFIED_CHINESE);
        }else if (obj == englishItem) {
            changeLanguage(Locale.ENGLISH);
        }else if (obj == germanItem) {
            changeLanguage(Locale.GERMAN);
        }
    }

    /**
     * Changes the active UI language and immediately updates visible texts.
     * 
     * @param locale locale whose resource bundle should be loaded
     */
    private void changeLanguage(Locale locale) {
        currentLocale = locale;
        loadLanguage();
        updateTexts();
    }

    /**
     * Loads the ResourceBundle that matches currentLocale.
     * Java automatically chooses files such as Messages_en.properties,
     * Messages_de.properties or Message_zh.properties.
     */
    private void loadLanguage() {
        messages = ResourceBundle.getBundle("Messages", currentLocale);
    }

    /**
     * Applies the currently loaded translations to the window title, menus,
     * menu items and step counter. The current game state is not modified.
     */
    private void updateTexts() {
        setTitle(messages.getString("game.title"));

        functionMenu.setText(messages.getString("menu.game"));
        levelMenu.setText(messages.getString("menu.difficulty"));
        languageMenu.setText(messages.getString("menu.language"));

        replayItem.setText(messages.getString("menu.restart"));
        closeItem.setText(messages.getString("menu.close"));
        imageItem.setText(messages.getString("menu.image"));
        numberItem.setText(messages.getString("menu.number"));

        easyItem.setText(messages.getString("difficulty.easy"));
        mediumItem.setText(messages.getString("difficulty.medium"));
        hardItem.setText(messages.getString("difficulty.hard"));

        if(stepCountLabel != null) {
            stepCountLabel.setText(messages.getString("label.steps") + ": " + step);
        }
    }
}
