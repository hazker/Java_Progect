package com.server.gradleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TheGame extends JFrame {
    private JLabel[][] minimap =new JLabel[11][11];

    private JPanel jpanel1;
    private JPanel jpanel_minimap = new JPanel();
    private JLabel room=new JLabel();
    private JTextArea HeroView=new JTextArea();
    private JButton left=new JButton("На Запад");
    private JButton right=new JButton("На Восток");
    private JButton down=new JButton("На Север");
    private JButton up=new JButton("На Юг");
    private JLabel hero=new JLabel();
    private JLabel enemy=new JLabel();
    private JTextArea textmap=new JTextArea();
    private JFrame frame;
    private Icon icon;
    private BufferedImage img;
    private FileReader reader;

    private ClientUI cl;

    private PrintWriter out;
    private Scanner in;

    private int c;
    private String status="";
    private int[] currentpos;
    private int[][] currentallpos;
    private String msg;

    private final int mapsize=11;
    private final int roomcount=11;

    private int[][] map=new int[mapsize][mapsize];

    public TheGame(Integer Hero, Socket fromserver,ClientUI tcl) {
        frame = new JFrame("Game");
        Dimension size = new Dimension(1200, 700);
        jpanel1.setLayout(new GridBagLayout());
        jpanel_minimap.setLayout(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        GridBagConstraints ff = new GridBagConstraints();
        frame.setPreferredSize(size);
        frame.setContentPane(jpanel1);

        cl=tcl;

        currentallpos = cl.getallpospos();
        currentpos=cl.getpos();
        /////////////////////Противник
        f.fill = GridBagConstraints.LAST_LINE_START;
        f.gridx=0;
        f.gridy=1;
        f.gridwidth = 4;

        //f.anchor=GridBagConstraints.NORTH;
        //  f.insets=new Insets(0,0,500,0);
        jpanel1.add(enemy,f);
        f.gridwidth = 1;
/////////////////////Противник

/////////////////////////////Комната
        f.anchor=GridBagConstraints.FIRST_LINE_START;
     //   f.fill = GridBagConstraints.BOTH;
        f.gridx = 0;
        f.gridy = 0;
        f.gridheight=2;
        jpanel1.add(room, f);
        f.gridheight=1;
////////////////////////////Комната

/////////////////////Текстовое поле
        f.gridx = 0;
        f.gridy = 2;
        f.fill = GridBagConstraints.BOTH;
        HeroView.setEditable(false);
        HeroView.setLineWrap(true);
        jpanel1.add(HeroView, f);
/////////////////////Текстовое поле
///////////////////////////Кароточки на карте

        f.gridwidth = 2;
        //f.fill = GridBagConstraints.VERTICAL;
        f.gridx=1;
        f.gridy=0;
        f.gridwidth = 3;
        jpanel1.add(jpanel_minimap,f);
        ff.fill = GridBagConstraints.FIRST_LINE_START;
        for(int i=0; i<minimap.length;i++)
            for(int j=0;j<minimap.length;j++){
                ff.gridx = i;
                ff.gridy = j;
                minimap[i][j] = new JLabel();
                jpanel_minimap.add(minimap[i][j],ff);
            }

//////////////////////////Карточки на карте


/////////////////Герой
        f.fill = GridBagConstraints.FIRST_LINE_START;
        f.gridx=1;
        f.gridy=1;
        f.gridwidth = 4;
        jpanel1.add(hero,f);
        f.gridwidth = 1;
/////////////////////Герой

/////////////////////Кнопки
        f.fill = GridBagConstraints.BOTH;
        f.gridwidth = 1;
        f.gridx=1;
        f.gridy=2;
        jpanel1.add(down,f);

        f.gridx=2;
        f.gridy=2;
        jpanel1.add(left,f);

        f.gridx=3;
        f.gridy=2;
        jpanel1.add(right,f);

        f.gridx=4;
        f.gridy=2;
        jpanel1.add(up,f);

/////////////////////Кнопки

        try {
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Rooms\\1.png"));
            //img = img.getScaledInstance(800, 600,  java.awt.Image.SCALE_SMOOTH);
            img = ChangeImage(img,0,800,600,0.8,0.8);
            icon = new ImageIcon(img);
            room.setIcon(icon);
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Hero\\"+ Hero +".png"));
            //img = img.getScaledInstance(390, 250,  java.awt.Image.SCALE_SMOOTH);
            img = ChangeImage(img,0,390,250,0.3,0.26);
            icon = new ImageIcon(img);
            hero.setIcon(icon);


            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\fon.png"));
            img = ChangeImage(img,0,25,25,0.15,0.15);
            //img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            for(int i=0; i<minimap.length;i++)
                for(int j=0;j<minimap.length;j++) {
                    minimap[i][j].setIcon(icon);
                }
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\12.png"));
            //img = img.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH);
            img = ChangeImage(img,0,25,25,0.17,0.18);
            icon = new ImageIcon(img);
            minimap[5][5].setIcon(icon);
            map[5][5]=12;
            //jpanel1.add(hero, f);
            cl.getdMsg();//Получаю координаты стартовые
            //msg+='@';
            //}
            //System.out.println(msg);
            //cl.sendMsg(msg);
            //paintMap();
            reader = new FileReader("src\\main\\resources\\Database\\bd.csv");
            while ((c = reader.read()) != '\n') {
                if (c != '1')
                    status += Character.toString(c);
                //System.out.print((char) c);
            }
            HeroView.setText(status);
            status = "";
        } catch (IOException e) {
            System.out.println(e);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentallpos[0][0]-1>=0){
                    currentallpos[0][0]-=1;
                    paint(0);
                }
                else{
                    HeroView.setText("Нельзя двигаться на юг");
                }
            }
        });
        down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentallpos[0][0]+1<map.length){
                    currentallpos[0][0]+=1;
                paint(-180);
            }
                else{
                HeroView.setText("Нельзя двигаться на север");
            }
            }
        });
        left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentallpos[1][0]-1>=0) {
                    currentallpos[1][0]-=1;
                    paint(-90);
                }
                else{
                    HeroView.setText("Нельзя двигаться на запад");
                }
            }
        });
        right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentallpos[1][0]+1<map.length) {
                    currentallpos[1][0] += 1;
                    paint(90);
                }
                else{
                    HeroView.setText("Нельзя двигаться на восток");
                }
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                cl.close();
            }
        });

    }
    public void paintMap(Integer i,Integer j,Integer k,int angle){
                if(map[i][j]!=roomcount+1) {
                    try {
                        img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Mini_map\\"+k+".png"));
                        //img = img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
                        img = ChangeImage(img,angle,25,25,0.1,0.1);
                        icon = new ImageIcon(img);
                        minimap[j][i].setIcon(icon);
                    }
                    catch (IOException e){
                        System.out.println(e);
                    }

                }
    }


    public void paint(int angle){//////////ФИКСАНУТЬ ЧЕРЕП В 1 0
        String temp="";
        cl.getallpospos();
        System.out.println("Poluchil "+currentallpos[0][0]+" "+currentallpos[1][0]+" "+currentallpos[2][0]);
        msg="Client_posit"+currentallpos[0][0]+"@"+currentallpos[1][0]+"@"+currentallpos[2][0];
        paintMap(currentallpos[0][0],currentallpos[1][0],currentallpos[2][0],angle);
        try(Reader reader= new FileReader("src\\main\\resources\\Database\\bd.csv")){
            while((c=reader.read())!=-1){
                  temp+=Character.toString(c);
                if(c=='\n'){
                    temp="";
                }
                //System.out.println(temp);
                if(temp.equals(Integer.toString(currentallpos[2][0]))) {
                    while((c=reader.read())!='\n') {
                        status+=Character.toString(c);
                        System.out.print((char) c);
                    }
                    break;
                }
            }
            //reader.close();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        HeroView.setText(status);
        status="";
        try {
            ////img = img.getScaledInstance(800, 600,  java.awt.Image.SCALE_SMOOTH);
            img = ImageIO.read(new File("src\\main\\resources\\Drawable\\Rooms\\"+ currentallpos[2][0] +".png"));
            img = ChangeImage(img,0,800,600,0.5,0.5);
            icon = new ImageIcon(img);
            room.setIcon(icon);
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("Отправил="+msg);
        try {
            cl.sendMsg(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public BufferedImage ChangeImage(BufferedImage buffImage, double angle,int width1,int height1, double sx, double sy) {
        BufferedImage Image = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = Image.createGraphics();
        g.scale(sx,sy);
        g.drawImage(buffImage, 0, 0,null);

        double radian = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(radian));
        double cos = Math.abs(Math.cos(radian));
        int width  = Image.getWidth();
        int height = Image.getHeight();
        int nWidth = (int) Math.floor((double) width * cos + (double) height * sin);
        int nHeight = (int) Math.floor((double) height * cos + (double) width * sin);
        BufferedImage ChangeImage = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = ChangeImage.createGraphics();
        graphics.setColor(Color.WHITE);

        //graphics.fillRect(0, 0, nWidth, nHeight);
       // graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.translate((nWidth - width) / 2, (nHeight - height) / 2);
        graphics.rotate(radian, (double) (width / 2), (double) (height / 2));
        graphics.drawImage(Image, 0, 0,null);
        graphics.dispose();
        return ChangeImage;
    }

}

