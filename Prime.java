/**
 * Program to compute the given numbers on the command line as if 
 * they are prime or not via, the command line.
 * A separate thread for each of the command line argument number 
 * given is ran.
 * Usage: java Prime 1000000000000187 1000000000000037 1000000000000091 1000000000000159 4
 */

public class Prime extends Thread
{
    private long number;
    private int  counter;

    public Prime( long number, int counter )
    {
        this.number  = number ;
        this.counter = counter ;
    }

    public boolean isPrime( long number )
    {
        if( number <= 1 ) {
            return false;
        }
        if( number == 2 ) {
            return true;
        }
        if( number % 2 == 0 ) {
            return false;
        }
        int sqrt = ( int )Math.sqrt( number );
        for( int i = 3 ; i <= sqrt ; i += 2 ) // Skip over the even numbers in loop.
        {
            if( number % i == 0 ) {
                return false;
            }
        }
        return true;
    }

    public void run()
    {
        long start_time = System.currentTimeMillis();
        boolean is_prime = isPrime( this.number );
        long end_time = System.currentTimeMillis();
        if( is_prime ) {
            System.out.println("i = " + counter + " prime "+ (end_time - start_time) +" msec");
        } else {
            System.out.println("i = " + counter + " not prime "+ (end_time - start_time) +" msec");
        }
    }

    public static void main( String [] args )
    {
        int counter = 0;
        for ( String s : args )
        {
            try
            {
                Prime P = new Prime( Long.parseLong( s ), counter );
                P.start();
                counter++ ;
            }
            catch ( NumberFormatException nex ) {}  // Just let it go.
        }
    }
}
