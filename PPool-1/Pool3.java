
//CSD feb 2015 Juansa Sendra
public class Pool3 extends Pool0 {    // max capacity

    // To be completed
    private int contador_xiquets    = 0;
    private int contador_instructor = 0;
    private int capacidad           = 0;
    public synchronized long kidRests(int id) {
        if (contador_xiquets > 0) {
            contador_xiquets--;
            capacidad--;

        }
        notifyAll();
        return log.rests(id);
    }
    public synchronized long instructorRests(int id) {

        while (((contador_instructor == 1) && (contador_xiquets > 0))
                || ((contador_xiquets + 1 / (contador_instructor)) >= log.maxKI())) {    // ||  ((maxKI / contador_instructor) <  contador_instructor   )
            try {
               log.leaveWait(id);

                // log.enterWait(id);
                wait();
            } catch (Exception e) {}
        }

        if (contador_instructor > 0) {
            contador_instructor--;
            capacidad--;
        }

        notifyAll();

        // log.enterWait(id);
        return log.rests(id);
    }

    public synchronized long instructorSwims(int id) {
        while (capacidad > 4) {
            try {
              //  log.leaveWait(id);
                 log.enterWait(id);
                wait();
            } catch (Exception e) {}
        }
        contador_instructor++;
        capacidad++;
        notifyAll();

        return log.swims(id);
    }



    public synchronized long kidSwims(int id) {
        while ((contador_instructor == 0)
                || (((contador_xiquets + 1) / contador_instructor) >= log.maxKI()) ||  (capacidad > 4   ) ) {    // ||  ((maxKI / contador_instructor) <  contador_instructor   )
            try {
               // log.enterWait(id);
                log.enterWait(id);
                wait();
            } catch (Exception e) {
                // e.message();
            }
        }
        contador_xiquets++;
        capacidad++;
        // }
        notifyAll();
       //  log.leaveWait(id);
        return log.swims(id);
    }

    public synchronized void make(Log log1) {
        log = log1;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
