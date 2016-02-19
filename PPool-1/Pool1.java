// CSD feb 2015 Juansa Sendra

public class Pool1 extends Pool0{//there cannot be kids alone
    // To be completed
    private int contador = 0;
    public synchronized long  kidSwims(int id) {
        while (contador == 0){
            try	{	
                log.enterWait(id);	
                wait();
            }	catch	(Exception	e)	{
            //    e.message();
            }   
        }
        log.leaveWait(id);		
        contador++;
        notifyAll();
        return log.swims(id);
    }
}