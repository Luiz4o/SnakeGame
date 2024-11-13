public class NodeDuplo {
    private Quadrado info;
    private NodeDuplo proximo, anterior;

    public NodeDuplo(Quadrado info, NodeDuplo proximo, NodeDuplo anterior) {
        this.info = info;
        this.proximo = proximo;
        this.anterior = anterior;
    }

    public void setInfo(Quadrado info) {
        this.info = info;
    }

    public void setProximo(NodeDuplo proximo) {
        this.proximo = proximo;
    }

    public void setAnterior(NodeDuplo anterior) {
        this.anterior = anterior;
    }

    public Quadrado getInfo() {
        return info;
    }

    public NodeDuplo getProximo() {
        return proximo;
    }

    public NodeDuplo getAnterior() {
        return anterior;
    }


}
