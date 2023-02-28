public class Matrix {
    int n;
    int[][] m;

    Matrix(int _n) {
        n = _n;
        m = new int[n][n];
    }

    void setToCycleGraph() {
        for (int i = 0; i < n - 1; i++) {
            addEdge(i, i + 1);
        }
        addEdge(0, n - 1);
    }

    void setToIdentityMatrix() {
        for (int i = 0; i < n; i++)
            m[i][i] = 1;
    }

    @Override
    public Matrix clone() {
        Matrix x = new Matrix(n);
        for (int i = 0; i < n; i++)
            System.arraycopy(m[i], 0, x.m[i], 0, n);
        return x;
    }

    void addEdge(int i, int j) {
        assert (i >= 0 && i < n);
        assert (j >= 0 && j < n);
        m[i][j] = m[j][i] = 1;
    }

    void printWithoutSpaces() {
        for (int i = 0; i < n; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < n; j++) {
                s.append(m[i][j]);
            }
            System.out.println(s);
        }
    }

    Matrix multiply(Matrix A) {
        assert (A.n == n);
        Matrix res = new Matrix(n);
        for (int i = 0; i < n; i++)
            for (int k = 0; k < n; k++)
                for (int j = 0; j < n; j++) {
                    res.m[i][j] += m[i][k] * A.m[k][j];
                }
        return res;
    }

    Matrix pow(int y) {
        Matrix x = this.clone();
        Matrix p = new Matrix(n);
        p.setToIdentityMatrix();
        while (y != 0) {
            if (y % 2 == 1) {
                p = p.multiply(x);
            }
            x = x.multiply(x);
            y /= 2;
        }
        return p;
    }

    void print() {
        for (int i = 0; i < n; i++) {
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < n; j++) {
                line.append(m[i][j]);
                line.append(' ');
            }
            System.out.println(line);
        }
    }
}
