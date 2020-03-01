import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class MyTest {

    @Test
    public void testRoute() throws Exception{
        int[] routers = new int[]{50001,50002,50003,50004,50005};
        Router r1 = new Router(50001,new int[]{50002,50004},routers);
        Router r2 = new Router(50002,new int[]{50001,50003},routers);
        Router r3 = new Router(50003,new int[]{50002,50005},routers);
        Router r4 = new Router(50004,new int[]{50001,50005},routers);
        Router r5 = new Router(50005,new int[]{50004,50003},routers);
//        int[] routers = new int[]{50001,50002,50003};
//        Router r1 = new Router(50001,new int[]{50002},routers);
//        Router r2 = new Router(50002,new int[]{50001,50003},routers);
//        Router r3 = new Router(50003,new int[]{50002},routers);
        User u = new User("Router 50001 info " +
                "Router 50002 info " +
                "Router 50003 info " +
                "Router 50004 info " +
                "Router 50005 info " +
                "Endpoint 50001 50003");
        List<Callable<Void>> tasks = new ArrayList<>();
        tasks.add(new c(u));
        tasks.add(new c(r1));
        tasks.add(new c(r2));
        tasks.add(new c(r3));
        tasks.add(new c(r4));
        tasks.add(new c(r5));
        int threadCount = tasks.size();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        executorService.invokeAll(tasks);

        assertEquals(1,1);
    }

    class c implements Callable<Void> {//TODO name
        Node n;
        c(Node n) {
            this.n = n;
        }
        public Void call() throws Exception {
            n.start();
            return null;
        }
    }
}
