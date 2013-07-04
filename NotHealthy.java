/**
 * A program that simulates a cigarette manufacturing system. 
 * There are three smoker threads and one agent thread. Each smoker continuously rolls 
 * a cigarette and smokes it. To roll and smoke a cigarette, the smoker needs three
 * ingredients: tobacco, paper, and matches. One of the smoker threads has paper, another 
 * has tobacco, and the third has matches. The agent has an infinite supply of all three 
 * materials. The agent places two of the ingredients on the table, then goes to sleep 
 * for a while (1 sec). The smoker who has the remaining ingredient(s) then makes 
 * and smokes a cigarette, then goes to sleep for a while (1 sec). The agent puts 
 * another two of the three ingredients on the table, and the cycle repeats. 
 */

import java.util.Random ;
import java.util.List ;
import java.util.ArrayList ;

enum cigarette {
    tobacco, paper, matches
}

class Agent extends Thread
{
    private cigarette [] 
        resource = { cigarette.tobacco, cigarette.paper, cigarette.matches } ;

    public static List< cigarette > table ;

    public Agent( List< cigarette > t )
    {
        table = t ;
    }

    public void run()
    {
        while( true )
        {
            synchronized( table )
            {
                int fitem = 0, sitem = 0 ;
                try 
                {
                    fitem = new Random().nextInt( 3 ) ;
                    sitem = new Random().nextInt( 3 ) ;
                }
                catch( IllegalArgumentException iaex ) {
                    System.err.println("You provided a negative integer to random number generator.") ;
                }
                table.add( resource[ fitem ] ) ;
                table.add( resource[ sitem ] ) ;
                System.out.println("Agent has put "+ resource[ fitem ].toString() +
                        " and "+ resource[ sitem ].toString() +" on the table.") ;
                table.notifyAll() ;
                try {
                    table.wait( 1000 ) ; 
                } catch( InterruptedException iex ) {
                    System.err.println("The thread was interrupted.") ;
                }
            }
        }
    }
}

class Smoker extends Thread
{
    private cigarette cig1, cig2, cig3 ;
    private String name ;
    public static List< cigarette > table ;

    public Smoker( String name, cigarette cig, List< cigarette > t )
    {
        this.name  = name ;
        this.cig1  = cig ;
        this.cig2  = null ;
        this.cig3  = null ;
        table = t ;
    }

    // Once done with smoking, set all the resources to null.
    private void smoke()
    {
        assert cig1 != null && cig2 != null && cig3 != null ;
        assert cig1 != cig2 && cig2 != cig3 && cig1 != cig3 ;
        // Just made sure that when I start to smoke, that I have
        // all the contents and none of them are same.
        cig1 = null ;
        cig2 = null ;
        cig3 = null ;
        // Done with smoking.
        System.out.println( name +" is smoking." ) ;
        table.notifyAll() ;
        try {
            table.wait( 1000 ) ;
        } catch( InterruptedException iex ) {
            System.err.println("The thread was interrupted.") ;
        }
    }

    public void run()
    {
        while( true )
        {
            synchronized( table )
            {
                int i = -1 ; 
                if( cig1 == null && table.size() > 0 ) {
                    cig1 = table.remove( 0 ) ;
                } else {
                    threadWait() ;
                }
                if( cig2 == null && table.size() > 0 )
                {
                    for( Enum e : table ) 
                    {
                        if( e != cig1 ) {
                            i = table.indexOf( e ) ;
                        }
                    }
                    if( i != -1 ) {
                        cig2 = table.remove( i ) ;
                    }
                }
                else {
                    threadWait() ;
                }
                if( cig2 != null && table.size() > 0 )
                {
                    i = -1 ;
                    for( Enum e : table ) 
                    {
                        if( e != cig1 && e != cig2 ) {
                            i = table.indexOf( e ) ;
                        }
                    }
                    if( i != -1 )
                    {
                        cig3 = table.remove( i ) ;
                        smoke() ;
                    }
                }
                else {
                    threadWait() ;
                }
            }
        }
    }

    public void threadWait()
    {
        try 
        {
            table.wait() ;
            table.notifyAll() ;
        } catch( InterruptedException iex ) {
            iex.printStackTrace() ;
        }
    }
}

public class NotHealthy
{
    public static List< cigarette > table ;

    public NotHealthy() {}

    public Smoker[] initialiseSmoker( List< cigarette > t )
    {
        Smoker [] s = { new Smoker( "Smoker 1", cigarette.tobacco, t ),
            new Smoker( "Smoker 2", cigarette.paper, t ),
            new Smoker( "Smoker 3", cigarette.matches, t ) } ;
        return s;
    }

    public static void main( String [] args )
    {
        NotHealthy n = new NotHealthy() ;
        table = new ArrayList< cigarette >() ;
        Smoker [] s = n.initialiseSmoker( table ) ;
        Agent a = new Agent( table ) ;
        a.start() ;
        for( Smoker smoker : s ) {
            smoker.start() ;
        }
    }
}
