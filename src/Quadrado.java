
import java.awt.Color;
import java.util.Random;


public class Quadrado {
    int x,y,largura,altura;
    Color cor;

    public Quadrado(int largura, int altura, Color cor) {
        this.largura = largura;
        this.altura = altura;
        this.cor = cor;
    }

    //Função que verifica a posição de um quadro seja para validar se a cobra está na mesma posição do corpo ou a colisão com a maçã
    public boolean samePosition(int x,int y){
        if(this.x<=x && this.x>=x){
            if(this.y<=y && this.y>=y) {
                return true;
            }else return false;
        }else  return false;
    }

    // Função para gerar aleatoriamente a posição do quadrado (maçã)
    public void randomPosition(){
        Random random = new Random();
        int x = random.nextInt(44)*10;
        if(x == 0){
            x = random.nextInt(44)*10;
        }
        int y = random.nextInt(35)*10;
        if(y == 0){
            y = random.nextInt(35)*10;
        }
        this.setY(y);
        this.setX(x);
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

}
