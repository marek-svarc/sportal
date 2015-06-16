package com.clubeek.dao.impl.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.impl.ownframework.ClubTeamDaoImpl;
import com.clubeek.model.ClubTeam;

/**
 * Simple performace test for different @link {@link ClubTeamDao} implementations.
 *
 * @author vitfo
 */
public class ClubTeamDaoImplPerformanceTest {
    private static int numOfObjects = 50;
    private static int numOfObjectsForSelectTest = 500;
    private static int numOfRounds = 10;
    private static int numOfRoundsForSelectTest = 10;
    private static Random gen = new Random();

    public static void main(String[] args) {
        
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/performance/java/com/clubeek/dao/impl/performance/test-config.xml");
        ClubTeamDao springClubTeamDao = ctx.getBean("clubTeamDao", ClubTeamDao.class);
        ClubTeamDao ownClubTeamDao = new ClubTeamDaoImpl();
        
        long springInsert = 0L;
        long ownInsert = 0L;
        long springDelete = 0L;
        long ownDelete = 0L;
        long springSelect = 0L;
        long ownSelect = 0L;
        
        long sInsert = 0L;
        long oInsert = 0L;
        
        // SPRING
        System.out.println("Deleting table content");
        testDelete(springClubTeamDao);
        
        System.out.println("Spring test start");
        for (int i = 0; i < numOfRounds; i++) {
            System.out.println("Round: " + (i + 1));
            springInsert += testInsert(springClubTeamDao);
            springDelete += testDelete(springClubTeamDao);
        }
        
        System.out.println("Inserting " + numOfObjectsForSelectTest + " objects for select testing");
        sInsert = insertObjects(springClubTeamDao, numOfObjectsForSelectTest);
        System.out.println("Inserting finished");
        
        List<Integer> sIds = getObjectIds(springClubTeamDao);
        for (int i = 0; i < numOfRounds; i++) {
            springSelect += testSelect(springClubTeamDao, sIds);
        }
        
        // OWN
        System.out.println("Deleting table content");
        testDelete(springClubTeamDao);  // I use spring template because it is much faster.
        
        System.out.println("Own test start");
        for (int i = 0; i < numOfRounds; i++) {
            System.out.println("Round: " + (i + 1));
            ownInsert += testInsert(ownClubTeamDao);
            ownDelete += testDelete(ownClubTeamDao);
        }

        System.out.println("Inserting " + numOfObjectsForSelectTest + " objects for select testing");
        oInsert = insertObjects(ownClubTeamDao, numOfObjectsForSelectTest);
        System.out.println("Inserting finished");
        
        List<Integer> oIds = getObjectIds(ownClubTeamDao);
        for (int i = 0; i < numOfRounds; i++) {
            ownSelect += testSelect(ownClubTeamDao, oIds);
        }
            
        System.out.println("Tests end");
        
        
        // RESULTS
        System.out.println("\n");
        System.out.println("Test results: ");
        System.out.println("-------------");
        System.out.println("Number of inserting/deleting objects: " + numOfObjects);
        System.out.println("Number of selecting objects: " + numOfObjectsForSelectTest);
        System.out.println("Number of test rounds: " +  numOfRounds);
        System.out.println();
        System.out.println("Spring insert time: " + springInsert);
        System.out.println("Own insert time:    " + ownInsert);
        System.out.println("Spring delete time: " + springDelete);
        System.out.println("Own delete time:    " + ownDelete);
        System.out.println("Spring select time: " + springSelect);
        System.out.println("Own select time:    " + ownSelect);
        System.out.println();
        System.out.println("Spring insert time of " + numOfObjectsForSelectTest + " objects: " + sInsert);
        System.out.println("Own insert time of " + numOfObjectsForSelectTest + " objects   : " + oInsert);
        System.out.println();
        System.out.println("Spring insert time - own insert time: " +  (springInsert - ownInsert));
        System.out.println("Spring delete time - own delete time: " +  (springDelete - ownDelete));
        System.out.println("Spring select time - own select time: " +  (springSelect- ownSelect));
        System.out.println("\n");
    }
    
    private static List<ClubTeam> createListOfObjects(int num) {
        List<ClubTeam> list = new ArrayList<ClubTeam>();
        for (int i = 0; i < num; i++) {
            ClubTeam ct = new ClubTeam();
            ct.setActive((gen.nextInt(2) == 0 ? false : true));
            ct.setName(UUID.randomUUID().toString());
            ct.setSorting(gen.nextInt(10));
            ct.setCategoryId(4);
            list.add(ct);
        }
        return list;
    }
    
    private static long insertObjects(ClubTeamDao ctd, int num) {
        List<ClubTeam> list = createListOfObjects(num);
        
        long start = System.nanoTime();
        for (ClubTeam ct : list) {
            ctd.insertRow(ct);
        }
        return System.nanoTime() - start;
    }
    
    private static long testInsert(ClubTeamDao ctd) {
        long start, end;
        List<ClubTeam> list = createListOfObjects(numOfObjects);
        
        // test
        start = System.nanoTime();
        
        for (ClubTeam ct : list) {
            ctd.insertRow(ct);
        }
        
        end = System.nanoTime();
        
        return end - start;
    }
    
    private static long testDelete(ClubTeamDao ctd) {
        long start;
        start = System.nanoTime();
        for (ClubTeam ct : ctd.getAllClubTeams()) {
            ctd.deleteRow(ct.getId());
        }
        return System.nanoTime() - start;
    }
    
    private static long testSelect(ClubTeamDao ctd, List<Integer> ids) {
        long start = System.nanoTime();
        for (int i = 0; i < numOfRoundsForSelectTest; i++) {
            ctd.getClubTeamById(ids.get(gen.nextInt(ids.size())));
        }
        return System.nanoTime() - start;
    }
    
    private static List<Integer> getObjectIds(ClubTeamDao ctd) {
        List<Integer> ids = new ArrayList<Integer>();
        for (ClubTeam ct : ctd.getAllClubTeams()) {
            ids.add(ct.getId());
        }
        return ids;
    }
}
