import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tabuleiro extends JFrame {

    private JPanel painel;
    private JPanel menu;
    private JButton iniciarButton;
    private JButton resetButton;
    private JButton modeButton;
    private JButton pauseButton;
    private JTextField placarField;
    private double x, y;
    private String direcao = "direita";
    private long tempoAtualizacao = 150;
    private double incremento = 10;
    private Quadrado obstaculo, cobra, parteCorpo;
    private int larguraTabuleiro, alturaTabuleiro;
    private int placar=0;
    private ListaCircular corpo= new ListaCircular();
    private boolean validate=true;
    private boolean mode=true;
    private int eyeLeftX,eyeRightX,eyeLeftY,eyeRightY;

    public Tabuleiro() {
        larguraTabuleiro = 460;
        alturaTabuleiro = 400;

        cobra = new Quadrado(10, 10, new Color(41, 120, 14));
        cobra.x = larguraTabuleiro / 2+10;
        cobra.y = alturaTabuleiro / 2;
        corpo.adicionar(cobra);

        eyeLeftX=2;
        eyeRightX=5;

        obstaculo = new Quadrado(10, 10, Color.red);
        obstaculo.randomPosition();

        setTitle("Jogo da Cobrinha");
        setSize(larguraTabuleiro,alturaTabuleiro+ 30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menu = new JPanel();
        menu.setLayout(new FlowLayout());

        iniciarButton = new JButton("Iniciar");
        resetButton = new JButton("Reiniciar");
        pauseButton = new JButton("Pausar");
        modeButton = new JButton("Modo de Jogo");
        placarField = new JTextField("Placar: 0");
        placarField.setEditable(false);

        menu.add(iniciarButton);
        menu.add(resetButton);
        menu.add(pauseButton);
        menu.add(modeButton);
        menu.add(placarField);

        //Imagem de fundo do jogo
        ImageIcon backgroundImage = new ImageIcon("C:/Users/henri/Documents/luiz_H/snakeG/src/bg_snake22.jpg");


        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);

                NodeDuplo aux = corpo.inicio;

                //Perccore a Lista do corpo iterando sobre ela pintando cada parte do corpo
                for (int i = 1; i < corpo.tamanho; i++) {
                    g.setColor(aux.getInfo().cor);
                    g.fillRect(aux.getInfo().x, aux.getInfo().y, aux.getInfo().largura, aux.getInfo().altura);
                    aux = aux.getProximo();

                }

                g.setColor(cobra.cor);
                g.fillRect(cobra.x, cobra.y, cobra.largura, cobra.altura);

                // Cor dos Olhos da cobra
                g.setColor(new Color(255, 255, 255));
                g.fillOval(cobra.x + eyeLeftX, cobra.y +eyeLeftY, 3, 3);
                g.fillOval(cobra.x + eyeRightX, cobra.y+eyeRightY, 3, 3);

                g.setColor(obstaculo.cor);
                g.fillRect(obstaculo.x, obstaculo.y, obstaculo.largura, obstaculo.altura);
            }
        };

        add(menu, BorderLayout.NORTH);
        add(painel, BorderLayout.CENTER);

        setVisible(true);

        // ActionListener para o botão Iniciar
        iniciarButton.addActionListener(e -> {
            Iniciar();
            painel.requestFocusInWindow(); // Devolve o foco para o painel
        });

        // ActionListener para o botão Reset
        resetButton.addActionListener(e -> {
            Reiniciar();

        });

        //ActionListaner para o botão que altera o modo
        modeButton.addActionListener(e -> {
            validate=false;
            int i = JOptionPane.showConfirmDialog(null, "Quer mesmo Trocar de modo?", "Modo de Jogo", JOptionPane.OK_CANCEL_OPTION);
            if(i == JOptionPane.YES_OPTION) {
                Reiniciar();
                mode=!mode;


            }
            else{
                Iniciar();
                painel.requestFocusInWindow();
            }

        });

        // ActionListener para o botão Pausar
        pauseButton.addActionListener(e -> {
            Pausar();

        });

        painel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (!direcao.equals("direita")) {
                            direcao = "esquerda";
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!direcao.equals("esquerda")) {
                            direcao = "direita";
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (!direcao.equals("baixo")) {
                            direcao = "cima";
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!direcao.equals("cima")) {
                            direcao = "baixo";
                        }
                        break;
                }
            }
        });

        painel.setFocusable(true);
        painel.requestFocusInWindow();
    }


    private void Iniciar() {

        new Thread(() -> {
            validate=true;
            double velocidade=1;
            while (validate) {
                try {

                    Thread.sleep(tempoAtualizacao);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switch (direcao) {
                    case "esquerda":
                        cobra.x -= incremento;
                        eyeLeftX=0;
                        eyeRightX=0;
                        eyeLeftY=5;
                        eyeRightY=2;
                        break;
                    case "direita":
                        cobra.x += incremento;
                        eyeLeftX=8;
                        eyeRightX=8;
                        eyeLeftY=2;
                        eyeRightY=5;
                        break;
                    case "cima":
                        cobra.y -= incremento;
                        eyeLeftX=2;
                        eyeRightX=5;
                        eyeLeftY=0;
                        eyeRightY=0;
                        break;
                    case "baixo":
                        cobra.y += incremento;
                        eyeLeftX=2;
                        eyeRightX=5;
                        eyeLeftY=8;
                        eyeRightY=8;
                        break;
                }
                // Função que verifica se o obstaculo (maça) está na mesma posição que a cobra caso esteja ela joga a mça em uma posição aleatória
                // e adiciona mais uma parte no corpo da cobrae aumenta o placa
                if(obstaculo.samePosition(cobra.x, cobra.y)){
                    obstaculo.randomPosition();

                    // Tempo de atualização é quem manda na velocidade, logo que a taxa de atualização for menor ele anda de forma mais rápida, ou seja, a cada maçã progressivamente
                    // vai aumentando a velocidade
                    tempoAtualizacao--;

                    placarField.setText("Placar: " + placar++);
                    if(placar==100){
                        Win();
                    }
                    // Lógica para adicionar partes ao corpo, primeiro ele preenche a lista ate a terceira posição pois ele segue uma lógica de mover o corpo de trás
                    // para frente sendo assim a primeira posição tenho que adicionar mais de uma parte, na sequência ele vai adicionando de uma em uma na última posição
                    if(corpo.tamanho==1){
                        for (int i = 0; i < 3; i++) {
                            if (corpo.tamanho % 2 == 0) {
                                Quadrado novaParte = new Quadrado(10, 10, new Color(0, 0, 0));
                                corpo.adicionar(novaParte);
                            }else{
                                Quadrado novaParte = new Quadrado(10, 10, new Color(79, 152, 52));
                                corpo.adicionar(novaParte);
                            }
                        }
                    }else if (corpo.tamanho % 2 == 0) {
                        Quadrado novaParte = new Quadrado(10, 10, new Color(0, 0, 0));
                        corpo.adicionar(novaParte);
                    }else{
                        Quadrado novaParte = new Quadrado(10, 10, new Color(79, 152, 52));
                        corpo.adicionar(novaParte);
                    }

                }

                //Atualiza a posição da cabeça da cobra, que é o no apontado no inicio
                NodeDuplo aux = corpo.inicio;
                aux.getInfo().x = cobra.x;
                aux.getInfo().y = cobra.y;

                // Percorre a lista circular de trás para frente mas não pasando pelo inicio que seria a cabeça que ja foi atualizada
                for (int i = corpo.tamanho - 1; i > 0; i--) {
                    if(aux.getAnterior().getInfo().samePosition(obstaculo.x, obstaculo.y)){
                        obstaculo.randomPosition();
                    }
                    aux.getAnterior().getInfo().x = aux.getAnterior().getAnterior().getInfo().x;
                    aux.getAnterior().getInfo().y = aux.getAnterior().getAnterior().getInfo().y;
                    aux= aux.getAnterior();
                    // Sistema de colisão no próprio corpo
                    // O motivo de eu ter escolhido este número foi uma questão de tentativa e erro pois a questão do reposicionamento do corpo
                    //acontece que em alguns instantes uma sequencia de partes estão na mesma posição da cabeça
                    if(i>4){
                        if(corpo.inicio.getInfo().samePosition(aux.getInfo().x, aux.getInfo().y)){
                            JOptionPane.showMessageDialog(this, "A cobra atingiu seu próprio corpo!", "Você PERDEU!", JOptionPane.INFORMATION_MESSAGE);
                            validate= false;
                            break;
                        }
                    }
                }

                // Lógica que define quando a cabeça ultrapassa atinge algum ponto da extremidade ela aparece no lado inverso, alterando seu X e Y conforme necessita
                if(mode) {
                    if (corpo.inicio.getInfo().x >= 440) {
                        corpo.inicio.getInfo().x = 0;
                    } else if (corpo.inicio.getInfo().y >= 350) {
                        corpo.inicio.getInfo().y = 0;
                    } else if (corpo.inicio.getInfo().x <= 0) {
                        corpo.inicio.getInfo().x = 440;
                    } else if (corpo.inicio.getInfo().y <= 0) {
                        corpo.inicio.getInfo().y = 350;
                    }
                }else{
                    // Sistema de parada caso a cabeça atinja alguma extremidade do tabuleiro
                    if(corpo.inicio.getInfo().x>=450 || corpo.inicio.getInfo().y>=360 || corpo.inicio.getInfo().x<=-10 || corpo.inicio.getInfo().y<=-10){
                        JOptionPane.showMessageDialog(this, "Você atingiu a borda do mapa!", "Você PERDEU!", JOptionPane.INFORMATION_MESSAGE);
                        validate= false;
                    }
                }
                painel.repaint();
            }
        }).start();
    }

    // Função para ganhar o jogo, necessita atingir a pontuação de 100 pontos
    private void Win(){
        JOptionPane.showMessageDialog(this, "Parabés você Ganhou!", "YOU WIN!!", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // Função que reinicia o Jogo, necessita fazer a confirmação, caso negue ele irá voltar o jogo aonde estava, caso aceite
    // zera o placar zera a lista do corpo e adiciona a cabeça novamente e inicia sua posição no centro do tabuleiro e inicia o jogo
    private void Reiniciar(){
        validate=false;
        int i = JOptionPane.showConfirmDialog(null, "Quer mesmo Reiniciar?", "Reinicar", JOptionPane.OK_CANCEL_OPTION);
        if(i == JOptionPane.YES_OPTION) {
            placar=0;
            placarField.setText("Placar: " + placar);
            corpo= new ListaCircular();
            corpo.adicionar(cobra);
            corpo.inicio.getInfo().x = larguraTabuleiro / 2+10;
            corpo.inicio.getInfo().y = alturaTabuleiro / 2;
            Iniciar();
            painel.requestFocusInWindow();
        }
        else{
            Iniciar();
            painel.requestFocusInWindow();


        }
    }

    // Função de pause, para o movimento de iniciar que é incremental, e caso aperte novamente chama iniciar e continua aonde parou.
    private void Pausar(){
        if(validate){
            validate=false;
            JOptionPane.showMessageDialog(this, "Jogo Pausado! \n Aperte pause novamente para despausar o jogo",
                    "Pause", JOptionPane.INFORMATION_MESSAGE);

        }else{
            Iniciar();
            painel.requestFocusInWindow();
        }

    }
    public static void main(String[] args) {
        new Tabuleiro();
    }
}
