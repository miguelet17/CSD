// CSD feb 2015 Juansa Sendra

 

public class Pool2 extends Pool0{ //max kids/instructor

    // To be completed
    private int contador_xiquets     = 0;
    private int contador_instructor = 0;
    // Log         log;

    public synchronized long instructorRests(int id) {
        while (   (contador_instructor == 1 && contador_xiquets > 0) || ((contador_xiquets +1  / (contador_instructor )) >= log.maxKI()) ) { //||  ((maxKI / contador_instructor) <  contador_instructor   )
                try {
                   log.leaveWait(id);
                    //log.enterWait(id);
                    wait();
                } catch (Exception e) {
                }
            }
            if (contador_instructor > 0) {
                contador_instructor--;
            }


            notifyAll();
       // log.enterWait(id);
            return log.rests(id);

    }

    public synchronized long instructorSwims(int id) {
        contador_instructor++;
        notifyAll();
        return log.swims(id);
    }

    public synchronized long kidRests(int id) {
        if (contador_xiquets > 0) {
            contador_xiquets--;
        }
        notifyAll();
        return log.rests(id);
    }

    public synchronized long kidSwims(int id) {
        while (contador_instructor == 0 ||  (( (contador_xiquets+1) / contador_instructor  )  >=  log.maxKI()   )  ) { //||  ((maxKI / contador_instructor) <  contador_instructor   )
            try {
                log.enterWait(id);
                wait();

            } catch (Exception e) {

                // e.message();
            }
        }
        contador_xiquets++;
        // }
        notifyAll();
      //  log.leaveWait(id);
        return log.swims(id);
    }

    public synchronized void make(Log log1) {
        log = log1;
    }
}