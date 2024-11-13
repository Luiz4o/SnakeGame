public class ListaCircular {
    NodeDuplo inicio;
    int tamanho;

    public ListaCircular(){
        tamanho=0;
        inicio=null;
    }
    public void adicionar(Quadrado info){
        //Se inicio e fim forem null a lista está vazia.
        if(inicio==null){
            //Adiciona na primeira posição
            NodeDuplo novoNo = new NodeDuplo(info, null, null);
            inicio=novoNo;
            inicio.setAnterior(inicio);
            tamanho++;
        }
        else{//Adiciona na ultima posição
            //Cria o novo Nó
            NodeDuplo novoNo = new NodeDuplo(info,inicio,inicio.getAnterior());
            //Altera o ponteiro do ultimo Nó para o NovoNó
            inicio.getAnterior().setProximo(novoNo);
            //Seta ProximoNo do ultimo Nó para o Novo Nó
            inicio.setAnterior(novoNo);
            tamanho++;
        }

    }

}