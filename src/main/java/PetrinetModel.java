import java.util.Arrays;

public class PetrinetModel {
    int T;
    String color[];
    int[] TP;
    String[] M;
    String[] G;
    String[] E;
    String[] V;

    public PetrinetModel(int t, String[] color, int[] tp, String[] m, String[] g, String[] e, String[] v) {
        T = t;
        this.color = color;
        this.TP = TP;
        M = m;
        G = g;
        E = e;
        V = v;
    }

    public PetrinetModel(){

    }

    public int getT() {
        return T;
    }

    @Override
    public String toString() {
        return "PetrinetModel{" +
                "T=" + T +
                ", color=" + Arrays.toString(color) +
                ", TP=" + Arrays.toString(TP) +
                ", M=" + Arrays.toString(M) +
                ", G=" + Arrays.toString(G) +
                ", E=" + Arrays.toString(E) +
                ", V=" + Arrays.toString(V) +
                '}';
    }

    public void setT(int t) {
        T = t;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public int[] getTP() {
        return TP;
    }

    public void setTP(int[] TP) {
        this.TP = TP;
    }

    public String[] getM() {
        return M;
    }

    public void setM(String[] m) {
        M = m;
    }

    public String[] getG() {
        return G;
    }

    public void setG(String[] g) {
        G = g;
    }

    public String[] getE() {
        return E;
    }

    public void setE(String[] e) {
        E = e;
    }

    public String[] getV() {
        return V;
    }

    public void setV(String[] v) {
        V = v;
    }
}
