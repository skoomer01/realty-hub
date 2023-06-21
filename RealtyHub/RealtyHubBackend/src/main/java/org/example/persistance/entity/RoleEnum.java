package org.example.persistance.entity;

public enum RoleEnum {
    CUSTOMER(0),
    REALTOR(1),
    ADMIN(2),
    INACTIVE(3);

    private int num;
    RoleEnum(int num){this.num = num;}
    public int getNum(){return num;}

}
