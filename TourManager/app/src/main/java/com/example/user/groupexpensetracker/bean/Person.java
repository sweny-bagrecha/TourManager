package com.example.user.groupexpensetracker.bean;

/**
 * Created by Tanuja on 28-09-2016.
 */
    public class Person {
        private String name;

        public Person() {
      /*Blank default constructor essential for Firebase*/
        }

        //Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }


