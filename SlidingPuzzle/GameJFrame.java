import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Klasse GameFrame.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */

public class GameJFrame extends JFrame implements ActionListener
{
    int step = 0;
    int size = 3;

    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem closeItem = new JMenuItem("关闭游戏");
    JMenuItem einfachItem = new JMenuItem("简易 3*3");
    JMenuItem mittelItem = new JMenuItem("中等 4*4");
    JMenuItem schwerItem = new JMenuItem("难 5*5");
    JMenuItem imageItem = new JMenuItem("上传图片");
        
    public GameJFrame(){
        initJFrame();
        
        initJMenuBar();
        
        this.setVisible(true);
    }
    
    private void initJFrame(){
        this.setSize(680,680);
        
        this.setTitle("Schiebepuzzle");
        
        this.setLocationRelativeTo(null);
        
        this.setDefaultCloseOperation(3);
        
        this.setLayout(null);
    }
    
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
    
    private void initDate(){
        //打乱数据
    }
    
    private void initImage(){
        this.getContentPane().removeAll();
        
        //图片对象？       
        //背景，边框等等美化
        
        this.getContentPane().repaint();
        
        JLabel stepCount = new JLabel("步数：" + step);
        stepCount.setBounds(50,30,100,20);
        this.getContentPane().add(stepCount);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object obj = e.getSource();
        
        if(obj == einfachItem){
            step = 0;
            size = 3;
            
            
        }else if(obj == mittelItem){
            step = 0;
            size = 4;
            
        }else if(obj == schwerItem){
            step = 0;
            size = 5;
            
        }else if(obj == replayItem){
            step = 0;
            
            initDate();
            
            initImage();
        }else if(obj == closeItem){
            System.exit(0);
        }else if (obj == imageItem) {
            JFileChooser chooser = new JFileChooser();
            
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "png");
            chooser.setFileFilter(filter);
            
            int res = chooser.showOpenDialog(this);
            
            if(res == JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                
                //图片覆盖
            }
        }
    }
}