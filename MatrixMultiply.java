// So you want to multiply the matrix.
// Lessons learnt, Java is more about concurrency rather than parallelism.
// The class holds the key to parallelism, and every time you want to start
// a thread, you have to initialise a new object of that class for it to 
// make that information and give you that thread. 
// Since the method run cannot be passed any parameter, the class that has
// the run method will have to make sure that it has all the information to
// implement the function it wants.
// One thing that did not get test here was the fact that, if the matrix
// was too big, then would calling display before the threads had finished,
// would that have been disastrous? Have to get the idea as to how would
// it been affected the display called, or would the threads have finished.

class Multiply implements Runnable {

    static int [][] mat1;
    static int [][] mat2;
    static int [][] mat3;
    int row, col;

    public Multiply () {
        mat1 = new int [][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        mat2 = new int [][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        mat3 = new int [3][3];
    }

    public Multiply (int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void run () {
        for (int i = 0; i < 3; i++) {
            mat3[ row ][ col ] += mat1[ row ][i] * mat2[i][ col ];
        }
    }
    
    public void display () {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print (String.valueOf (mat3 [i][j]) + " " );
            }
            System.out.println();
        }
    }
}

public class MatrixMultiply {

    // Calculate each cell of matrix individually.
    public void multiply() {
        Multiply M = new Multiply();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                new Thread(new Multiply(i, j)).start();
            }
        }
        M.display();
    }

    public static void main (String [] args) {
        MatrixMultiply M = new MatrixMultiply();
        M.multiply();
    }
}
