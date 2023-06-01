package com.helpinghands.repo;

import com.helpinghands.domain.IEntity;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Session {

    private static SessionFactory sessionFactory = null;
    private static org.hibernate.Session session = null;
    private static void buildSession() {
        try{
            sessionFactory = HibernateUtil.getSessionFactory();
            System.out.println("Session created");

        }catch(Exception e){
            System.out.println("Exception occured. "+e.getMessage());
            e.printStackTrace();
            closeFactory();
        }
    }

    public static void closeFactory(){
        if(sessionFactory!=null && !sessionFactory.isClosed()){
            System.out.println("Closing SessionFactory");
            sessionFactory.close();
        }
    }

    public static org.hibernate.Session getInstance(){
        if(sessionFactory==null)
            buildSession();
        session = sessionFactory.openSession();//.getCurrentSession();
        return session;
    }

    public static void doTransaction(TransactionProcessor op) {
        if(sessionFactory==null)
            buildSession();
        var sess = sessionFactory.openSession(); //getInstance();
        var tran = sess.beginTransaction();
        op.execute(sess, tran);
        sess.close();
    }

    public interface TransactionProcessor {
        void execute(org.hibernate.Session session, Transaction transaction);
    }

}
