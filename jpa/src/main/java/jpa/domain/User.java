package jpa.domain;

/**
 * Created by shanmin.zhang
 * Date: 18/5/29
 * Time: 下午3:22
 */


import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "user_test")
@NamedQuery(name = "User.findByName", query = "select name,address from User u where u.name=?1")
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "name")
    String name;
    @Column(name = "address")
    String address;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

}