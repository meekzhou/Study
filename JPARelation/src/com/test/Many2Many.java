package com.test;

import com.bean.Student;
import com.bean.Teacher;
import com.util.Util;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Created by zhouweitao on 16/6/20.
 */
public class Many2Many {
    @Test
    public void initDB() {
        Util.getManager();
        Util.close();
    }

    /**
     * 保存老师和学生
     * */
    @Test
    public void save() {
        EntityTransaction transaction = Util.getManager().getTransaction();
        transaction.begin();

        Teacher teacher = new Teacher("李老师");
//        Student student = new Student("周学生");
        Util.getManager().persist(teacher);
//        Util.getManager().persist(student);
        transaction.commit();
        Util.getManager().close();
    }


    /**
     * 建立老师和学生的关系
     * */
    @Test
    public void saveRelation() {
        EntityTransaction transaction = Util.getManager().getTransaction();
        EntityManager em = Util.getManager();
        transaction.begin();
        Student student = em.find(Student.class, 1);
        student.addTeacher(em.find(Teacher.class,2));
        em.persist(student);
        transaction.commit();
        Util.getManager().close();
    }

    /**
     * 删除老师和学生的关系
     * */
    @Test
    public void clearRelation() {
        EntityTransaction transaction = Util.getManager().getTransaction();
        EntityManager em = Util.getManager();
        transaction.begin();
        Student student = em.find(Student.class, 1);
        student.reomveTeacher(em.getReference(Teacher.class,1)); //应为不需要进行实体的装载,是用托管状态的实体有助于提高性能(getReference()为延迟加载方法)
        em.persist(student);
        transaction.commit();
        Util.getManager().close();
    }

    /**
     * 老师和学生存在关系,单独删除老师
     * */
    @Test
    public void deleteTeacher() {
        EntityTransaction transaction = Util.getManager().getTransaction();
        EntityManager em = Util.getManager();
        transaction.begin();
        Student student = em.find(Student.class, 1);
        student.reomveTeacher(em.getReference(Teacher.class,1));//解除老师和学生之间的关系
        em.remove(em.getReference(Teacher.class,1));            //应为老师为关系被维护端,所以删除老师需要先解除老师和学生的关系
        transaction.commit();
        Util.getManager().close();
    }


    /**
     * 老师和学生存在关系,单独删除学生
     * */
    @Test
    public void deleteStudent() {
        EntityTransaction transaction = Util.getManager().getTransaction();
        EntityManager em = Util.getManager();
        transaction.begin();
        em.remove(em.getReference(Student.class,1)); //学生为关系的维护端,删除学生不需要先解除学生和老师之间的关系
        transaction.commit();
        Util.getManager().close();
    }
}