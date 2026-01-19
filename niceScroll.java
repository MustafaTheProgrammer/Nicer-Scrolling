import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import java.util.ArrayList;

class niceScroll extends JFrame implements KeyListener{
    public static int width = 36;
    public static int height = 24;
    public static String[] pixels = new String[width * height];
    public static String white = "\u001B[100m  ";
    public static String black = "\u001B[47m  ";
    public static String green = "\u001B[102m  ";
    public static boolean rightKey;
    public static boolean leftKey;
    public static boolean upKey;
    public static boolean downKey;
    public static float playerx = 8;
    public static float playery = -10;
    public static float playerxv = 0;
    public static float playeryv = 0;
    public static float camx;
    public static float camy;
    public static ArrayList<Float> tiles = new ArrayList<Float>();

    public static int[][] level = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//not so important
                                   {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                                   {1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
                                   {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                                   {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                                   {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
                                   {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                                   {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                                   {1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                                   {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                                   {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
    
    public niceScroll(){
        this.setTitle("Nicer scrolling");
        this.setSize(100, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
        gameLoop();
    }

    static void gameLoop(){
        createLevel(level);
        while (1==1){
            System.out.print("\033[H\033[2J");
            System.out.flush();

            for (int i = 0; i < pixels.length; i++){
                pixels[i] = white;
            }
            
            //player x vel setting
            if (rightKey){
                playerxv += 0.5;
                if (playerxv > 2){
                    playerxv = 2;
                }
            }
            else{
                if (playerxv > 0){
                    playerxv -= 0.5;
                }
            }
            
            if (leftKey){
                playerxv -= 0.5;
                if (playerxv < -2){
                    playerxv = -2;
                }
            }
            else{
                if (playerxv < 0){
                    playerxv += 0.5;
                }
            }
            
            //horizontally move player or camera
            if (playerx + camx <= 17 || playerx + camx >= (level[0].length * 6)-17){
                playerx += playerxv;
            }
            else{
                playerx = 17;
                camx += playerxv;
            }
            
            if (camx < 0){
                camx = 0;
            }
            if (camx > level[0].length * 6){
                camx = level[0].length * 6;
            }
            
            //player y vel setting
            if (upKey){
                playeryv += 0.5;
                if (playeryv > 2){
                    playeryv = 2;
                }
            }
            else{
                if (playeryv > 0){
                    playeryv -= 0.5;
                }
            }
            
            if (downKey){
                playeryv -= 0.5;
                if (playeryv < -2){
                    playeryv = -2;
                }
            }
            else{
                if (playeryv < 0){
                    playeryv += 0.5;
                }
            }
            
            //vertically move player or camera
            if (playery + camy >= -11 || playery + camy <= -((level.length * 6) - 6)+11){
                playery += playeryv;
            }
            else{
                playery = -11;
                camy += playeryv;
            }
            
            if (camy > 0){
                camy = 0;
            }
            if (camy < -(level.length * 6)){
                camy = -(level.length * 6);
            }

            //render everything

            for (int i = 0; i < tiles.size(); i += 2){
                rect(tiles.get(i) - camx, tiles.get(i+1) - camy, 6, 6, green);
            }
            
            rect(playerx, playery, 3, 3, black);

            for (int i = 0; i < height; i++){
                for (int j = 0; j < width; j++){
                    System.out.print(pixels[i * width + j]);
                }
                System.out.println("\u001B[0m");
            }

            try{
                Thread.sleep(50);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    static public void main(String[] args){
        new niceScroll();
    }

    static void setPixel(float x, float y, String color){
        if (x >= 0 && x <= width-1 && y <= 0 && y >= -1*(height-1)){
            pixels[Math.abs(Math.round(y)) * width + Math.round(x)] = color;
        }
    }

    static void rect(float x, float y, int w, int h, String color){
        for (float i = x; i <= x + w; i++){
            for (float j = y; j <= y + h; j++){
                setPixel(i, j, color);
            }
        }
    }

    static void addTile(float x, float y){
        tiles.add(x);
        tiles.add(y);
    }

    static void createLevel(int[][] level){
        for (int i = 0; i < level.length; i++){
            for (int j = 0; j < level[0].length; j++){
                switch (level[i][j]){
                        case 1: addTile(j*6, (i*-6)+1);
                        break;
                }
            }
        }
    }
    
    public void keyTyped(KeyEvent e){

    }
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
                case 37: leftKey = true;
                break;
                case 38: upKey = true;
                break;
                case 39: rightKey = true;
                break;
                case 40: downKey = true;
                break;
        }
    }
    public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()){
                case 37: leftKey = false;
                break;
                case 38: upKey = false;
                break;
                case 39: rightKey = false;
                break;
                case 40: downKey = false;
                break;
        }
    }
}