
//CSD feb 2013 Juansa Sendra
public class Pool4 extends Pool0 {    // kids cannot enter if there are instructors waiting to exit

    // To be completed
    private int     contador_xiquets      = 0;
    private int     contador_instructor   = 0;
    private int     capacidad             = 0;
    private int instructores_saliendo = 0;

    public synchronized long instructorRests(int id) {
        while (((contador_instructor == 1) && (contador_xiquets > 0))
                || (contador_xiquets  > (contador_instructor - 1) * log.maxKI())) {    // ||  ((maxKI / contador_instructor) <  contador_instructor   )
            instructores_saliendo ++;
            log.leaveWait(id);
            try {
                wait();
            } catch (Exception e) {}
        }
        if (contador_instructor > 0) {
            contador_instructor--;
            capacidad--;

        }
        if (instructores_saliendo > 0){ instructores_saliendo--;}

        notifyAll();
        return log.rests(id);
    }

    public synchronized long instructorSwims(int id) {
        while (capacidad >= log.capacity()) {
            try {
                log.enterWait(id);
                wait();
            } catch (Exception e) {}
        }

        contador_instructor++;
        capacidad++;
        notifyAll();
        return log.swims(id);
    }

    public synchronized long kidRests(int id) {
        if (contador_xiquets > 0) {
            contador_xiquets--;
            capacidad--;
        }
        notifyAll();
        return log.rests(id);
    }

    public synchronized long kidSwims(int id) {
        while (((contador_instructor == 0) || (instructores_saliendo > 0))
                || (contador_xiquets  >= contador_instructor * log.maxKI())
                || (capacidad >= log.capacity())) {
            log.enterWait(id);
            try {
                wait();
            } catch (Exception e) {
            }
        }
        contador_xiquets++;
        capacidad++;
        notifyAll();
        return log.swims(id);
    }
    public synchronized void make(Log log1) {
        log = log1;
    }
}


