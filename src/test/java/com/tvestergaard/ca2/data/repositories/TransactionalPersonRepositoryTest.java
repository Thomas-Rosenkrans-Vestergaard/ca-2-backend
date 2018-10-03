package com.tvestergaard.ca2.data.repositories;

import com.tvestergaard.ca2.TestConnection;
import com.tvestergaard.ca2.data.entities.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static junit.framework.TestCase.*;

public class TransactionalPersonRepositoryTest
{

    private static final EntityManagerFactory          emf = TestConnection.create();
    private              TransactionalPersonRepository instance;

    @Before
    public void setUp() throws Exception
    {
        instance = new TransactionalPersonRepository(emf);
        instance.begin();
    }

    @After
    public void tearDown() throws Exception
    {
        instance.rollback();
        instance.getEntityManager().close();
    }

    @Test
    public void create()
    {
        Person person = instance.create("a", "b", "c");

        assertNotNull(person.getId());
        assertEquals("a", person.getFirstName());
        assertEquals("b", person.getLastName());
        assertEquals("c", person.getEmail());
        assertEquals(0, person.getPhoneNumbers().size());
        assertEquals(0, person.getHobbies().size());
    }

    @Test
    public void update()
    {
        Person person = instance.create("a", "b", "c");

        Person updated = instance.update(person.getId(), "d", "e", "f");

        assertEquals(person, updated);

        assertEquals("d", updated.getFirstName());
        assertEquals("e", updated.getLastName());
        assertEquals("f", updated.getEmail());
    }

    @Test
    public void withPhoneNumber()
    {
        Person person = instance.withPhoneNumber("number2");

        assertNotNull(person);
        assertEquals("first2", person.getFirstName());
    }

    @Test
    public void withPhoneNumberReturnsNull()
    {
        assertNull(instance.withPhoneNumber(""));
    }

    @Test
    public void withHobbyId()
    {
        List<Person> persons = instance.withHobby(3);
        assertEquals(1, persons.size());
        assertEquals("first3", persons.get(0).getFirstName());
    }

    @Test
    public void withHobbyName()
    {
        List<Person> persons = instance.withHobby("name3");
        assertEquals(1, persons.size());
        assertEquals("first3", persons.get(0).getFirstName());
    }

    @Test
    public void countWithHobbyId()
    {
        assertEquals(0, instance.countWithHobby(-1));
        assertEquals(1, instance.countWithHobby(1));
        assertEquals(2, instance.countWithHobby(4));
    }

    @Test
    public void countWithHobbyName()
    {
        assertEquals(0, instance.countWithHobby(""));
        assertEquals(1, instance.countWithHobby("name1"));
        assertEquals(2, instance.countWithHobby("name4"));
    }

    @Test
    public void inCityName()
    {
        List<Person> persons = instance.inCity("city3");

        assertEquals(1, persons.size());
        assertEquals("first3", persons.get(0).getFirstName());
        assertEquals("last3", persons.get(0).getLastName());
    }

    @Test
    public void inCityId()
    {
        List<Person> persons = instance.inCity(3);

        assertEquals(1, persons.size());
        assertEquals("first3", persons.get(0).getFirstName());
        assertEquals("last3", persons.get(0).getLastName());
    }


    @Test
    public void inZipCode()
    {
        List<Person> persons = instance.inZipCode("zip2");

        assertEquals(1, persons.size());
        assertEquals("first2", persons.get(0).getFirstName());
        assertEquals("last2", persons.get(0).getLastName());
    }

    @Test
    public void get()
    {
        List<Person> persons = instance.get();

        assertEquals(5, persons.size());
        assertEquals("first1", persons.get(0).getFirstName());
        assertEquals("first5", persons.get(4).getFirstName());
    }

    @Test
    public void getById()
    {
        Person person = instance.get(3);

        assertNotNull(person);
        assertEquals("first3", person.getFirstName());
    }

    @Test
    public void getByIdReturnsNull()
    {
        assertNull(instance.get(-1));
    }

    @Test
    public void persist()
    {
        Person person = new Person("f", "l", "e");

        Person persisted = instance.persist(person);
        assertEquals(persisted, persisted);

        assertEquals("f", persisted.getFirstName());
        assertEquals("l", persisted.getLastName());
        assertEquals("e", persisted.getEmail());
    }

    @Test
    public void updateEntity()
    {
        Person person = instance.create("a", "b", "c");

        person.setFirstName("d");
        person.setLastName("e");
        person.setEmail("f");

        Person updated = instance.update(person);

        assertEquals(person, updated);

        assertEquals("d", updated.getFirstName());
        assertEquals("e", updated.getLastName());
        assertEquals("f", updated.getEmail());
    }

    @Test
    public void delete()
    {
        Person persisted = instance.persist(new Person("a", "b", "c"));
        int    id        = persisted.getId();

        assertEquals(persisted, instance.get(id));
        assertEquals(persisted, instance.delete(id));
        assertNull(instance.get(id));
    }

    @Test
    public void deleteReturnsNull()
    {
        assertNull(instance.delete(-1));
    }
}