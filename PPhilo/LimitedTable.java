// CSD Mar 2013 Juansa Sendra
public class LimitedTable extends RegularTable { //max 4 in dinning-room
    int philoIn;
    public LimitedTable(Log log) {
        super(log);
        philoIn = 0;
    }
    
    public synchronized void enter(int id) {
        //illegal("RegularTable.enter");
        while(philoIn == 4){
            log.wenter(id);
            waiting();
        }
        
        philoIn++;
        log.enter(id);
      
    }
    
    public synchronized void exit(int id)  {
        //illegal("RegularTable.exit");
        philoIn--;
        log.exit(id);
          notifyAll();
    }
}
