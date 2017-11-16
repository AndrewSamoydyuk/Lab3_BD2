import org.neo4j.driver.v1.*;
import java.util.ArrayList;
import java.util.List;
import static org.neo4j.driver.v1.Values.parameters;

public class Lab3_main
{
    Driver driver;

    public Lab3_main(String uri, String user, String password)
    {
        driver = GraphDatabase.driver(uri,AuthTokens.basic(user, password));
    }

    private void addPerson(String name, int Age, int Id , String sex, String[] posts)
    {
        try (Session session = driver.session())
        {
            try (Transaction tx = session.beginTransaction())
            {
                tx.run(
                        "MERGE (" +
                                "a:Person {name: {x}, age : {y}, id : {z}, sex : {c}, posts: {d} }" +
                                ")",
                        parameters(
                                "x", name,
                                "y",Age,
                                "z", Id,
                                "c",sex,
                                "d", posts
                        ));
                tx.success();
            }
        }
    }

    private void addFriendRelation(int firstID, int secondID)
    {
        try (Session session = driver.session())
        {
            try (Transaction tx = session.beginTransaction())
            {
                tx.run(
                        "match (a{id: {x} }),(b{id: {y} }) Merge (a)-[r:FRIEND]->(b)",
                        parameters(
                                "x", firstID,
                                "y",secondID

                        ));
                tx.success();
            }
        }
    }

    private void addGroupRelation(int GroupID, int personID)
    {
        try (Session session = driver.session())
        {
            try (Transaction tx = session.beginTransaction())
            {
                tx.run(
                        "match (a{id: {y} }),(b{id: {x} }) Merge (a)-[r: SUBSCRIBER]->(b)",
                        parameters(
                                "x", GroupID,
                                "y",personID

                        ));
                tx.success();
            }
        }
    }

    private void addGroup(String name, int Id)
    {
        try (Session session = driver.session())
        {
            try (Transaction tx = session.beginTransaction())
            {
                tx.run(
                        "MERGE (" +
                                "a:Group {name: {x}, id : {y}}" +
                                ")",
                        parameters(
                                "x", name,
                                "y", Id
                        ));
                tx.success();
            }
        }
    }

    public String task_3_a ()
    {
        List<String> res= new ArrayList<String>();

        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (n:Person) return n.name order by n.name"
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }

        return res.toString();
    }

    public String task_3_b (String sex)
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (p:Person) where p.sex= {x} return p.name, p.age order by p.age desc",
                    parameters("x",sex)
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_3_c (String name)
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (node:Person)<-[:FRIEND]-(n) where node.name = {x} return n.name order by n.name",
                    parameters("x",name)
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_3_d (String name)
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (node:Person)<-[:FRIEND]-(n)<-[:FRIEND]-(f) where node.name = {x} return f.name order by f.name",
                    parameters("x",name)
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_3_e ()
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "MATCH (n:Person)<-[:FRIEND]-(f) RETURN n.name, count(f) as counter order by n.name"
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_3_f ()
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (n: Group) return n.name order by n.name"
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_3_g (String name)
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (g:Group)<-[:SUBSCRIBER]-(p:Person) where p.name = {x} return g.name order by g.name",
                    parameters("x",name)
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_3_h ()
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (g:Group)<-[:SUBSCRIBER]-(p:Person) return g.name, count(p) order by count(p) desc"
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
}

    public String task_3_i ()
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (g:Group)<-[:SUBSCRIBER]-(p:Person) return p.name, count(g) order by count(g) desc"
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_3_j (String name)
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (p:Person)<-[:FRIEND]-(f:Person)<-[:FRIEND]-(ff:Person)-[:SUBSCRIBER]->(g:Group) where p.name={x} return count(g)",
                    parameters("x",name)
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_4_a (String name)
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (n) where n.name= {x} return n.posts",
                    parameters("x",name)
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_4_b (int length)
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (n:Person) return filter(row in n.posts where length(row)> {x} )",
                    parameters("x",length)
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_4_c ()
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (n:Person) return n.name, size(n.posts) order by size(n.posts) desc"
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_4_d (String name)
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (n:Person)<-[:FRIEND]-(f:Person)<-[:FRIEND]-(ff:Person) where n.name={x} return ff.name, ff.posts",
                    parameters("x",name)
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public String task_4_i ()
    {
        List<String> res= new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(
                    "match (p:Person) with (reduce(total = 0, row in p.posts | total + length(row)))/size(p.posts) as num, p.name as name return name, num order by num desc"
            );
            while (result.hasNext())
            {
                Record record = result.next();
                res.add(record.values().toString());
            }
        }
        return res.toString();
    }

    public void close()
    {
        driver.close();
    }

    public static void main(String... args)
    {
        Lab3_main example = new Lab3_main("bolt://localhost:7687", "neo4j", "neo4j");

        //System.out.printf(example.task_3_a());
        //System.out.printf(example.task_3_b("male"));
        //System.out.printf(example.task_3_c("Ada"));
        //System.out.printf(example.task_3_d("Ada"));
        //System.out.printf(example.task_3_e());
        //System.out.printf(example.task_3_f());
        //System.out.printf(example.task_3_g("Grishka"));
        //System.out.printf(example.task_3_h());
        //System.out.printf(example.task_3_i());
        //System.out.printf(example.task_3_j("Misha"));
        //System.out.printf(example.task_4_a("Bob"));
        //System.out.printf(example.task_4_b(15));
        //System.out.printf(example.task_4_c());
        //System.out.printf(example.task_4_d("Dima"));
        //System.out.printf(example.task_4_i());

        /*
        example.addPerson("Andrew", 19, 1, "male", new String[]{"Andrew First post","Andrew second post","Andrew third post"});
        example.addPerson("Bob", 20, 2,"male", new String[]{"Bob First post","Bob second post","Bob third post", "Bob fourth post"});
        example.addPerson("Ada", 18, 3,"Female", new String[]{"Ada First post","Ada second post"});
        example.addPerson("Grishka", 17, 4,"male", new String[]{"Grishka First post","Grishka second post","Grishka third post"});
        example.addPerson("Taras", 21, 5,"male", new String[]{"Taras First post","Taras second post"});
        example.addPerson("Nazar", 22, 6,"male", new String[]{"Nazar First post","Nazar second post","Nazar third post", "Grishka fourth post"});
        example.addPerson("Dima", 23, 7,"male", new String[]{"Dima First post","Dima second post","Dima third post"});
        example.addPerson("Misha", 16, 8,"male", new String[]{"Misha First post","Misha second post"});
        example.addPerson("Liza", 17, 9,"Female", new String[]{"Liza First post","Liza second post","Liza third post"});



        example.addGroup("KPI",101);
        example.addGroup("NAU",102);



        example.addFriendRelation(1,3);
        example.addFriendRelation(1,5);
        example.addFriendRelation(2,3);
        example.addFriendRelation(6,7);
        example.addFriendRelation(4,2);
        example.addFriendRelation(3,8);
        example.addFriendRelation(5,9);
        example.addFriendRelation(3,1);
        example.addFriendRelation(5,1);
        example.addFriendRelation(3,2);
        example.addFriendRelation(7,6);
        example.addFriendRelation(2,4);
        example.addFriendRelation(8,3);
        example.addFriendRelation(9,5);



        example.addGroupRelation(101,2);
        example.addGroupRelation(101,5);
        example.addGroupRelation(101,7);
        example.addGroupRelation(101,8);
        example.addGroupRelation(102,1);
        example.addGroupRelation(102,2);
        example.addGroupRelation(102,4);
        example.addGroupRelation(102,6);
        example.addGroupRelation(102,9);
        */



        example.close();
    }

}